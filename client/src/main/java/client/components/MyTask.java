package client.components;

import client.components.dialog.ConfirmDialog;
import client.components.dialog.TextDialog;
import client.observable.ObservableBoard;
import client.scenes.ButtonCtrl;
import commons.dto.TaskDto;
import commons.entities.Card;
import commons.entities.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyTask extends HBox implements ButtonCtrl {

    @FXML
    private Label text;

    @FXML
    private Button removeTaskButton;

    @FXML
    private Button moveTaskButton;

    @FXML
    private CheckBox checkBox;

    private ListView<Task> taskListView;

    private Task task;

    @FXML
    @Getter
    private HBox taskBox;

    private final ObservableBoard observableBoard;

    /**
     * Constructor for MyTask
     *
     * @param observableBoard main controller
     */
    public MyTask(ObservableBoard observableBoard) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/components/MyTask.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            setHoverEffects();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.observableBoard = observableBoard;

        moveTaskButton.setOnDragDetected(event -> {
            if(task == null) {
                return;
            }
            setOpacity(0.4);
            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("t" + taskListView.getItems().indexOf(task));
            dragboard.setContent(content);
            event.consume();
        });

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> observableBoard.send(
            "/boards/{boardId}/tasks",
            new TaskDto().setId(task.getId())
                .setCardId(task.getCard().getId())
                .setCompleted(newValue)
        ));

        setDragOver();
        setDragDropped();
        setDragEnter();
        setDragDone();
        setDragExit();
    }

    private void setDragEnter() {
        this.setOnDragEntered(event -> {
            Dragboard dragboard = event.getDragboard();
            Object source = event.getGestureSource();
            if(source != this && dragboard.hasString() && dragboard.getString().startsWith("t")) {
                this.taskListView.setOpacity(1);
                taskBox.setStyle("-fx-background-color: #fff; "
                    + "-fx-border-color: black;"
                    + "-fx-border-width: 3px; -fx-border-style: dashed");
            }
            event.consume();
        });
    }

    private void setDragDone() {
        this.setOnDragDone(event -> {
            ((MyTask) event.getGestureSource()).setOpacity(1);
            ((MyTask) event.getGestureSource()).taskListView.setOpacity(1);
        });
    }

    private void setDragExit() {
        this.setOnDragExited(event -> {
            Dragboard dragboard = event.getDragboard();
            MyTask source = (MyTask) event.getGestureSource();
            if(source != this && dragboard.hasString() && dragboard.getString().startsWith("t")) {
                this.taskListView.setOpacity(0.4);
                taskBox.setStyle("-fx-background-color: #fff; "
                    + "-fx-border-color: black;"
                    + "-fx-border-width: 0px; -fx-border-style: none");
            }
            event.consume();
        });
    }

    /**
     * drag and drop
     */
    private void setDragDropped() {
        this.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if(dragboard.hasString() && dragboard.getString().startsWith("t")) {
                switchTasks(event);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * drag and drop helper
     *
     * @param event drag and drop
     */
    private void switchTasks(DragEvent event) {
        ObservableList<Task> taskItems = taskListView.getItems();

        int originIdx = Integer.parseInt(event.getDragboard().getString().substring(1));
        int destinationIdx = taskItems.indexOf(this.getTask());

        Task originTask = taskItems.get(originIdx);

        List<Task> list = new ArrayList<>(taskListView.getItems());
        list.remove(originTask);
        list.add(destinationIdx, originTask);
        updateIndexes(task.getCard(), list);

    }

    /**
     * Update indexes of tasks in a card
     *
     * @param destinationCard destination card
     * @param list            list of tasks
     */
    private void updateIndexes(Card destinationCard, List<Task> list) {
        for(int i = 0; i < list.size(); i++) {
            Task task = list.get(i);
            if(task.getIndexInCard() == i)
                continue;

            observableBoard.send(
                "/boards/{boardId}/tasks",
                new TaskDto().setId(task.getId())
                    .setCardId(destinationCard.getId())
                    .setIndexInCard(i)
            );
        }
    }

    /**
     * set drag over
     */
    private void setDragOver() {
        this.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();
            Object source = event.getGestureSource();
            if(source != this && dragboard.hasString() && dragboard.getString().startsWith("t")) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }


    /**
     * Edit task's description
     *
     */
    @SuppressWarnings("unused")
    public void changeTaskTitle() {
        TextDialog.show(
            "Edit description",
            "Add new description",
            task.getTitle()
        ).thenAccept(str -> observableBoard.send(
            "/boards/{boardId}/tasks",
            new TaskDto().setId(task.getId())
                .setCardId(task.getCard().getId())
                .setTitle(str)
        ));
    }

    /**
     * Removes task
     *
     */
    @SuppressWarnings("unused")
    public void removeTask() {
        ConfirmDialog.show(
            "Delete card",
            "Are you sure you wish to delete this task?"
        ).thenAccept(v -> observableBoard.send(
            "/boards/{boardId}/tasks/delete",
            task.getId()
        )).thenAccept(str -> {
            taskListView.getItems().remove(task);
            taskListView.getItems().forEach(task1 -> observableBoard.send(
                "/boards/{boardId}/tasks",
                new TaskDto()
                    .setId(task1.getId())
                    .setCardId(task1.getCard().getId())
                    .setIndexInCard(taskListView.getItems().indexOf(task1))
            ));
        });
    }

    /**
     * sets card fields
     *
     * @param task task instance
     */
    public void setTask(Task task) {
        this.task = task;
        text.setText(task.getTitle());
        checkBox.setSelected(task.isCompleted());
    }

    /**
     * sets hover effects
     */
    public void setHoverEffects() {
        Stream.of(removeTaskButton, moveTaskButton)
            .forEach(this::setHoverEffects);
    }

    /**
     * Creates an underline on the cards when the user hovers over them
     */
    @SuppressWarnings("unused")
    public void mouseEnter() {
        text.setUnderline(true);
    }

    /**
     * Removes the underline on the cards when the user stops hovering
     */
    @SuppressWarnings("unused")
    public void mouseExit() {
        text.setUnderline(false);
    }
}



