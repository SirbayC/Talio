package client.components.dialog;

import client.components.cells.MyTaskCell;
import client.components.cells.TagCellComboBox;
import client.components.cells.TagCellListView;
import client.observable.ObservableBoard;
import client.observable.listeners.TagComboBoxModifyListener;
import client.observable.listeners.TagListViewListener;
import client.utils.CustomizationUtils;
import commons.dto.CardDto;
import commons.dto.CardTagDto;
import commons.dto.TaskDto;
import commons.entities.Card;
import commons.entities.Tag;
import commons.entities.Task;
import commons.mappers.CardMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class EditCard extends AbstractDialog<CardDto> {

    private final Card card;
    private final CardDto dto;
    private final ObservableBoard board;
    private final Runnable unregister;
    private Runnable unregister2;
    private Runnable unregister3;

    @FXML
    private TextField cardTitleField;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextArea description;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Text emptyInputError;

    @FXML
    private ListView<Task> taskListView;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private ListView<Tag> activeTags;

    @FXML
    private ComboBox<Tag> allTagComboBox;

    @FXML
    private Button addTag;

    private final CustomizationUtils customizationUtils;

    /**
     * Get edit card dialog with title and dto
     *
     * @param title title
     * @param card  card
     * @param board observable board
     */
    public EditCard(String title, Card card, ObservableBoard board) {
        super(title);
        this.card = card;
        this.dto = CardMapper.INSTANCE.toDto(card).setCardListId(card.getCardList().getId());
        this.board = board;

        customizationUtils = new CustomizationUtils();

        unregister = board.listenToTasks((task, changeType) -> {
            if(task == null || !card.equals(task.getCard()))
                return;

            switch(changeType) {
                case ADD -> taskListView.getItems().add(task);
                case UPDATE -> {
                    List<Task> list = new ArrayList<>(taskListView.getItems());
                    list.remove(task);
                    list.add(task.getIndexInCard(), task);
                    taskListView.getItems().setAll(list);
                }
                case DELETE -> taskListView.getItems().remove(task);
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * @return path to text dialog
     */
    @Override
    public String getFxmlPath() {
        return "/client/components/dialogs/EditCard.fxml";
    }

    /**
     * {@inheritDoc}
     *
     * @return always true
     */
    @Override
    public boolean verifyResult() {
        if(!cardTitleField.getText().isBlank())
            return true;

        emptyInputError.setVisible(true);
        PauseTransition showMark = new PauseTransition(Duration.seconds(5));
        showMark.setOnFinished(event1 -> emptyInputError.setVisible(false));
        showMark.play();
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @return card dto with the changes to the card
     */
    @Override
    public CardDto gatherResult() {
        CardDto changes = new CardDto().setTitle(cardTitleField.getText())
            .setDescription(description.getText())
            .setDate(datePicker.getValue())
            .setColor(customizationUtils.toString(colorPicker.getValue()))
            .diff(dto);// Only return the actual difference

        // Removed tags from card
        dto.getTags()
            .stream()
            .filter(dto -> activeTags.getItems().stream().noneMatch(tag -> tag.getId() == dto.getId()))
            .forEach(dto -> board.send(
                "/boards/{boardId}/linkCardTag",
                new CardTagDto().setTagId(dto.getId())
                    .setCardId(card.getId())
                    .setLink(false)
            ));

        // Added tags to card
        activeTags.getItems()
            .stream()
            .filter(tag -> dto.getTags().stream().noneMatch(tagDto -> tag.getId() == tagDto.getId()))
            .forEach(tag -> board.send(
                "/boards/{boardId}/linkCardTag",
                new CardTagDto().setTagId(tag.getId())
                    .setCardId(card.getId())
                    .setLink(true)
            ));

        if(datePicker.getValue() == null)
            changes.setDate(LocalDate.of(2000, 1, 1));

        return changes;
    }

    /**
     * Show edit card dialog
     */
    @Override
    public void show() {
        super.show();

        allTagComboBox.setButtonCell(new TagCellComboBox());
        allTagComboBox.setCellFactory(param -> new TagCellComboBox());
        allTagComboBox.setItems(FXCollections.observableArrayList(board.getTags().getMap().values().stream().toList()));
        unregister2 = board.listenToTags(new TagComboBoxModifyListener(this.allTagComboBox));

        activeTags.setCellFactory(param -> new TagCellListView(board, activeTags));

        if(dto == null)
            return;

        cardTitleField.setText(dto.getTitle());
        datePicker.setValue(dto.getDate());
        description.setText(dto.getDescription());

        taskListView.setCellFactory(param -> new MyTaskCell(board));
        taskListView.setItems(FXCollections.observableArrayList(card.getTaskList()));

        if(dto.getColor() != null)
            colorPicker.setValue(Color.web(dto.getColor()));

        activeTags.setItems(FXCollections.observableArrayList(card.getTags()));
        unregister3 = board.listenToTags(new TagListViewListener(this.activeTags));
    }

    /**
     * adds the selected card to the card's list of tags
     */
    @SuppressWarnings("unused")
    public void confirmTag() {
        Tag tag = allTagComboBox.getValue();
        if(tag != null && !activeTags.getItems().contains(tag) && (card == null || !card.getTags().contains(tag))) {
            activeTags.getItems().add(tag);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param event action event
     */
    @Override
    protected void close(ActionEvent event) {
        Stream.of(unregister, unregister2, unregister3)
            .forEach(ForkJoinPool.commonPool()::execute);

        super.close(event);
    }

    /**
     * Sets the hover effects of the button
     */
    public void setHoverEffects() {
        List.of(closeButton, colorPicker, addTag, addTaskButton)
            .forEach(this::setHoverEffects);
    }

    /**
     * Prompts the window to add a new task, then adds it to the task list
     */
    @SuppressWarnings("unused")
    public void addTask() {
        TextDialog.show(
            "Add Task",
            "Enter task description",
            ""
        ).thenAccept(str -> board.send(
            "/boards/{boardId}/tasks",
            new TaskDto().setCardId(dto.getId()).setTitle(str)
        ));
    }

    /**
     * Show edit card dialog with provided information
     *
     * @param title title of dialog
     * @param card  card instance
     * @param board observable board
     * @return future
     */
    public static CompletableFuture<CardDto> show(String title, Card card, ObservableBoard board) {
        EditCard dialog = new EditCard(title, card, board);
        dialog.show();
        return dialog.getFuture();
    }

}
