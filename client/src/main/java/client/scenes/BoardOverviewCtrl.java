package client.scenes;

import client.components.TagList;
import client.components.cells.MyCardListCell;
import client.components.dialog.ConfirmDialog;
import client.components.dialog.TextDialog;
import client.observable.ObservableBoard;
import client.observable.listeners.BoardListener;
import client.observable.listeners.CardListModifyListener;
import client.observable.listeners.CardListOrderListener;
import client.observable.listeners.CardModifyListener;
import client.observable.listeners.CardOrderListener;
import client.observable.listeners.TaskModifyListener;
import client.observable.listeners.TaskOrderListener;
import client.utils.CustomizationUtils;
import client.utils.TaskWrapper;
import com.google.inject.Inject;
import commons.dto.BoardDto;
import commons.dto.CardListDto;
import commons.entities.Board;
import commons.entities.CardList;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

public class BoardOverviewCtrl implements ButtonCtrl {

    @FXML
    private Button backButton;

    @FXML
    @Getter
    @Setter
    private ListView<CardList> lists;

    @FXML
    private Text boardName;

    @FXML
    private Button addListButton;

    @FXML
    private StackPane manageBoardPopUp;

    @FXML
    private TextField boardNameField;

    @FXML
    private Text boardId;

    @FXML
    private Button saveButton;

    @FXML
    private Button closeBoardPopUpButton;

    @FXML
    private Button updateConfMarkButton;

    @FXML
    private Button showBoardManagementButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button leaveButton;

    @FXML
    private Button tagButton;

    @FXML
    private Text emptyInputError;

    @FXML
    private Button saveOuterColor;

    @FXML
    private Button resetOuterColor;

    @FXML
    private Button resetInnerColor;

    @FXML
    private ColorPicker outerColorPicker;
    @FXML
    private ColorPicker innerColorPicker;

    @FXML
    private AnchorPane boardBackground;

    @FXML
    private ComboBox<String> fontPicker;

    @FXML
    private ColorPicker cardListBorderColorPicker;

    @FXML
    private ColorPicker cardListBackgroundColorPicker;

    @FXML
    private Button resetBorderColor;

    @FXML
    private Button resetBackgroundColor;

    @Getter
    private Board board;

    private final MainCtrl mainCtrl;

    private final ObservableBoard observableBoard;


    private final CustomizationUtils customizationUtils;

    /**
     * Constructor for the BoardOverviewController using the main controller provided
     *
     * @param mainCtrl the main controller provided on initialization
     */
    @Inject
    public BoardOverviewCtrl(MainCtrl mainCtrl) {
        this.customizationUtils = new CustomizationUtils();
        this.mainCtrl = mainCtrl;
        this.observableBoard = new ObservableBoard();
    }

    /**
     * Sets the hover effects for the buttons in the scene
     */
    public void setHoverEffects() {
        List.of(addListButton, backButton, saveButton, closeBoardPopUpButton, deleteButton,
                showBoardManagementButton, leaveButton, tagButton, saveOuterColor,
                outerColorPicker, resetOuterColor, resetInnerColor,
                innerColorPicker, fontPicker, cardListBorderColorPicker,
                cardListBackgroundColorPicker, resetBorderColor, resetBackgroundColor)
            .forEach(this::setHoverEffects);
    }

    /**
     * Populates the board overview by converting each provided CardList to a MyCardList (restores lost references)
     *
     * @param board board instance
     */
    public void setBoard(Board board) {
        // Init Board
        this.board = board;

        // Init list
        lists.setCellFactory(param -> new MyCardListCell(observableBoard));
        lists.setItems(FXCollections.observableArrayList());
        lists.getItems().setAll(board.getCardLists());

        // Listen to changes to the board
        observableBoard.setBoard(mainCtrl.getServer(), board);
        observableBoard.listenToBoard(new BoardListener(board, boardName,
            boardBackground, showBoardManagementButton, lists, backButton));
        observableBoard.listenToBoard(b -> {
            if(b != null)
                return;

            mainCtrl.showOpenCreateScene();
        });

        // Listen to changes to card lists
        observableBoard.listenToCardLists(new CardListModifyListener(lists.getItems()));
        observableBoard.listenToCardLists(new CardListOrderListener(lists.getItems()));

        // Listen to changes to cards
        observableBoard.listenToCards(new CardModifyListener(observableBoard));
        observableBoard.listenToCards(new CardOrderListener(observableBoard));

        //Listen to changes to tasks
        observableBoard.listenToTasks(new TaskModifyListener(observableBoard));
        observableBoard.listenToTasks(new TaskOrderListener(observableBoard));

        leaveButton.setDisable(mainCtrl.isAdmin());
    }

    /**
     * Opens the prompt window for creating a list with a new name
     */
    public void goToAddList() {
        TextDialog.show(
            "Add List",
            "Enter new list title",
            ""
        ).thenAccept(str -> observableBoard.send(
            "/boards/{boardId}/cardlists",
            new CardListDto()
                .setTitle(str)
                .setBorderColor(board.getCardListBorderColor())
                .setBackgroundColor(board.getCardListBackgroundColor())
        ));
    }

    /**
     * Opens the prompt window for creating a list with a new name
     */
    public void goToShowTags() {
        TagList.show(this.observableBoard);
    }

    /**
     * Go back to overview
     */
    public void backToOverview() {
        mainCtrl.showOpenCreateScene();
    }

    /**
     * shows the described pop-up
     */
    public void showBoardManagementPopUp() {
        manageBoardPopUp.setVisible(true);
        boardNameField.setText(board.getName());
        boardId.setText(Long.toString(board.getId()));

        outerColorPicker.setValue(Color.web(board.getOuterColor()));
        innerColorPicker.setValue(Color.web(board.getInnerColor()));

        fontPicker.setItems(FXCollections.observableList(customizationUtils.getFonts()));
        fontPicker.setValue(board.getFont());

        cardListBackgroundColorPicker.setValue(Color.web(board.getCardListBackgroundColor()));
        cardListBorderColorPicker.setValue(Color.web(board.getCardListBorderColor()));
    }

    /**
     * hides the described pop-up
     */
    public void closeBoardManagementPopUp() {
        manageBoardPopUp.setVisible(false);
    }

    /**
     * saves the inputted board name in the database
     */
    public void applyBoardNameChange() {
        if(boardNameField.getText().isBlank()) {
            emptyInputError.setVisible(true);
            PauseTransition showMark = new PauseTransition(Duration.seconds(1));
            showMark.setOnFinished(event -> emptyInputError.setVisible(false));
            showMark.play();
            return;
        }

        observableBoard.send(
            "/boards/" + board.getId(),
            new BoardDto().setId(board.getId()).setName(boardNameField.getText())
        );

        updateConfMarkButton.setVisible(true);
        PauseTransition showMark = new PauseTransition(Duration.seconds(1));
        showMark.setOnFinished(event -> updateConfMarkButton.setVisible(false));
        showMark.play();
    }

    /**
     * deletes the board (from the database, for all users)
     */
    public void deleteBoard() {
        ConfirmDialog.show(
            "Delete board",
            "Are you sure you want to delete this board?"
        ).thenAccept(v -> {
            manageBoardPopUp.setVisible(false);
            observableBoard.send("/boards/" + board.getId() + "/delete", board.getId());
        });
    }

    /**
     * removes the board from client's list (marks it as unvisited)
     */
    public void leaveBoard() {
        TaskWrapper.wrap(() ->
                mainCtrl.getClientBoardManager().remove(board))
            .thenAccept(completed -> {
                if(!completed)
                    return;
                manageBoardPopUp.setVisible(false);
                mainCtrl.showOpenCreateScene();
            });
    }

    /**
     * Saves the colors and font
     */
    public void save() {
        // Board colors
        String outerColor = customizationUtils.toString(outerColorPicker.getValue());
        String innerColor = customizationUtils.toString(innerColorPicker.getValue());

        // CardList colors
        String cardListBorderColor = customizationUtils.toString(cardListBorderColorPicker.getValue());
        String cardListBackgroundColor = customizationUtils.toString(cardListBackgroundColorPicker.getValue());

        String font = fontPicker.getValue();

        observableBoard.send(
            "/boards/" + board.getId(),
            new BoardDto().setId(board.getId())
                .setOuterColor(outerColor)
                .setInnerColor(innerColor)
                .setCardListBackgroundColor(cardListBackgroundColor)
                .setCardListBorderColor(cardListBorderColor)
                .setFont(font)
        );

        for(CardList cardList : observableBoard.getCardLists().values())
            observableBoard.send(
                "/boards/{boardId}/cardlists",
                new CardListDto().setId(cardList.getId())
                    .setBackgroundColor(cardListBackgroundColor)
                    .setBorderColor(cardListBorderColor)
            );
    }

    /**
     * Resets the color to its original color
     */
    public void resetOuterColor() {
        outerColorPicker.setValue(Color.web(customizationUtils.getDefaultBoardOuterColor()));
    }


    /**
     * Resets the color to its original color
     */
    public void resetInnerColor() {
        innerColorPicker.setValue(Color.web(customizationUtils.getDefaultBoardInnerColor()));
    }

    /**
     * Resets the background color for card lists
     */
    public void resetBackgroundColor() {
        cardListBackgroundColorPicker.setValue(Color.web(customizationUtils.getDefaultCardListColor()));
    }

    /**
     * Resets the background color for card lists
     */
    public void resetBorderColor() {
        cardListBorderColorPicker.setValue(Color.web(customizationUtils.getDefaultCardListBorderColor()));
    }

}
