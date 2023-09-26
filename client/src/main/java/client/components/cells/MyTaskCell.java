package client.components.cells;

import client.components.MyTask;
import client.observable.ObservableBoard;
import client.observable.enums.ChangeType;
import commons.entities.Task;
import javafx.application.Platform;
import javafx.scene.control.ListCell;
import lombok.Getter;

public class MyTaskCell extends ListCell<Task> {

    @Getter
    private final MyTask myTask;

    /**
     * Task cell constructor
     *
     * @param observableBoard observable board
     */
    public MyTaskCell(ObservableBoard observableBoard) {
        setStyle("-fx-padding: 0 15 1 0; -fx-background-color: none;");
        myTask = new MyTask(observableBoard);
        Runnable unregister = observableBoard.listenToTasks(((value, changeType) -> {
            if(changeType == ChangeType.DELETE)
                return;

            if(myTask.getTask() == null)
                return;

            if(!myTask.getTask().equals(value))
                return;

            myTask.setTask(value);
        }));

        // Runnable clean-up to prevent memory-leaks
        itemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null)
                return;

            Platform.runLater(unregister);
        });
    }


    @Override
    protected void updateItem(Task task, boolean empty) {

        super.updateItem(task, empty);
        if(empty || task == null) {
            setGraphic(null);
            return;
        }

        myTask.setTask(task);
        myTask.setTaskListView(getListView());
        setGraphic(myTask);
    }
}
