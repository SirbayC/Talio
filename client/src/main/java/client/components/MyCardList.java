package client.components;

import client.components.cells.MyCardCell;
import client.components.dialog.ConfirmDialog;
import client.components.dialog.EditList;
import client.components.dialog.TextDialog;
import client.observable.ObservableBoard;
import client.scenes.ButtonCtrl;
import client.utils.AnimationUtils;
import client.utils.CustomizationUtils;
import commons.dto.CardDto;
import commons.dto.CardListDto;
import commons.entities.Card;
import commons.entities.CardList;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MyCardList extends VBox implements ButtonCtrl {

    @FXML
    private Text title;

    @FXML
    private ListView<Card> cards;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addCardButton;

    @FXML
    private Button moveButton;

    @FXML
    private Pane topPane;

    @FXML
    private VBox cardListBox;

    @FXML
    private ScrollPane scrollPane;

    private CardList cardList;

    @Setter
    private ListView<CardList> lists;

    private final ObservableBoard observableBoard;

    private final AnimationUtils animationUtils;

    private final CustomizationUtils customizationUtils;

    /**
     * @param observableBoard observable board
     */
    public MyCardList(ObservableBoard observableBoard) {
        animationUtils = new AnimationUtils(TranslateTransition::new);
        customizationUtils = new CustomizationUtils();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/components/MyCardList.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            setHoverEffects();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.observableBoard = observableBoard;
        this.cards.setCellFactory(param -> new MyCardCell(observableBoard, cards));
        this.cards.setItems(FXCollections.observableArrayList());

        moveButton.setOnDragDetected(event -> {
            if(cardList == null)
                return;

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("l" + lists.getItems().indexOf(cardList));
            dragboard.setContent(content);
            event.consume();
        });
    }

    /**
     * Sets the hover effects
     */
    public void setHoverEffects() {
        List.of(editButton, deleteButton, moveButton, addCardButton)
            .forEach(this::setHoverEffects);
    }

    /**
     * Sets the card list by converting each provided Card to a MyCard (restores lost references)
     *
     * @param cardList card list instance
     */
    public void setCardList(CardList cardList) {
        this.cardList = cardList;
        title.setText(cardList.getTitle());
        topPane.setMinWidth(Math.max(100, cardList.getTitle().length() * 15.0));
        String contrastingColor;
        if(cardList.getBackgroundColor() != null) {
            contrastingColor = (customizationUtils.getContrastingColor(cardList.getBackgroundColor()) == Color.WHITE ?
                "white" : "black");
        } else {
            contrastingColor = "black";
        }

        title.setFill((cardList.getBackgroundColor() == null ?
            customizationUtils.getContrastingColor(customizationUtils.getDefaultCardListColor())
            : customizationUtils.getContrastingColor(cardList.getBackgroundColor())));

        moveButton.setStyle(moveButton.getStyle() + "-fx-background-color: " +
            contrastingColor + ";");

        editButton.setStyle(editButton.getStyle() + "-fx-background-color: " +
            contrastingColor + ";");

        deleteButton.setStyle(deleteButton.getStyle() + "-fx-background-color: " +
            contrastingColor + ";");

        topPane.setStyle(topPane.getStyle() + "-fx-border-color: " + (cardList.getBackgroundColor() == null ?
            customizationUtils.getDefaultCardListBorderColor() : cardList.getBorderColor()) + ";");

        addCardButton.setStyle(addCardButton.getStyle() + "-fx-background-color: " + (cardList.getBackgroundColor() == null ?
            customizationUtils.getDefaultCardListBorderColor() : cardList.getBorderColor()) + ";" +
            "-fx-text-fill: " + (cardList.getBorderColor() == null ? "black" :
            (customizationUtils.getContrastingColor(cardList.getBorderColor()) == Color.WHITE ? "white" : "black")) + ";");

        cardListBackground();

        cardList.getCards().sort(Comparator.comparing(Card::getIndexInCardList));
        cards.getItems().setAll(cardList.getCards());

        borderAdaptation(cardList, contrastingColor);
    }

    /**
     * Adapts the border of the addCard button and the card list
     *
     * @param cardList         an instance of a cardList
     * @param contrastingColor the contrasting color to the background color of the cardList
     */
    public void borderAdaptation(CardList cardList, String contrastingColor) {
        cardListBox.setStyle(cardListBox.getStyle() + "-fx-border-color: " + (cardList.getBackgroundColor() == null ?
            customizationUtils.getDefaultCardListBorderColor() : cardList.getBorderColor()) + ";");

        if(cardList.getBorderColor() != null && cardList.getBorderColor().equals(cardList.getBackgroundColor())) {
            addCardButton.setStyle(addCardButton.getStyle() + "-fx-border-color: " + contrastingColor + ";" +
                "-fx-border-width: 0.5;");
        } else {
            addCardButton.setStyle(addCardButton.getStyle() + "-fx-border-width: 0px;");
        }

        if(cardList.getBorderColor() != null && cardList.getBoard().getInnerColor() != null
            && cardList.getBorderColor().equals(cardList.getBoard().getInnerColor())
            && cardList.getBackgroundColor().equals(cardList.getBorderColor())) {
            cardListBox.setStyle(cardListBox.getStyle() + "-fx-border-color: " +
                (customizationUtils.getContrastingColor(cardList.getBackgroundColor()) == Color.WHITE
                    ? "white" : "black") + ";");
        } else if(cardList.getBorderColor() == null && cardList.getBoard().getInnerColor() != null
            && cardList.getBoard().getInnerColor().equals(customizationUtils.getDefaultCardListBorderColor())
            && cardList.getBackgroundColor().equals(cardList.getBorderColor())) {
            cardListBox.setStyle(cardListBox.getStyle() + "-fx-border-color: " +
                (customizationUtils.getContrastingColor(cardList.getBoard().getInnerColor()) == Color.WHITE
                    ? "white" : "black") + ";");
        }
    }

    /**
     * Sets the background colors of the card list
     */
    public void cardListBackground() {
        cardListBox.setStyle(cardListBox.getStyle() + "-fx-background-color: " +
            (cardList.getBackgroundColor() == null ? customizationUtils.getDefaultCardListColor() :
                cardList.getBackgroundColor()) + ";");

        scrollPane.setStyle(scrollPane.getStyle() + "-fx-background-color: " +
            (cardList.getBackgroundColor() == null ? customizationUtils.getDefaultCardListColor() :
                cardList.getBackgroundColor()) + ";");

        cards.setStyle(cards.getStyle() + "-fx-background-color: " +
            (cardList.getBackgroundColor() == null ? customizationUtils.getDefaultCardListColor() :
                cardList.getBackgroundColor()) + ";");
    }

    /**
     * Opens dialog to change the card list name
     */
    @SuppressWarnings("unused")
    public void editCardList() {
        EditList.show(
            "Edit list",
            this.cardList
        ).thenAccept(cardListDto -> observableBoard.send(
            "/boards/{boardId}/cardlists",
            cardListDto
        ));
    }

    /**
     * Opens dialog to change the card list name
     */
    @SuppressWarnings("unused")
    public void deleteCardList() {
        ConfirmDialog.show(
            "Delete list",
            "Are you sure you want to remove the list with title: " + cardList.getTitle() + " ?"
        ).thenAccept(v -> observableBoard.send(
            "/boards/{boardId}/cardlists/delete",
            cardList.getId()
        )).thenAccept(str -> {
            lists.getItems().remove(cardList);
            lists.getItems().forEach(cardList1 -> observableBoard.send(
                "/boards/{boardId}/cardlists",
                new CardListDto()
                    .setId(cardList1.getId())
                    .setIndexInBoard(lists.getItems().indexOf(cardList1))
            ));
        });
    }

    /**
     * Displays context buttons when mouse enters top area
     */
    @SuppressWarnings("unused")
    public void topPaneOnMouseEnter() {
        animationUtils.moveTextAnimation(title, -30);
        editButton.setVisible(true);
        deleteButton.setVisible(true);
        moveButton.setVisible(true);
    }

    /**
     * Hides context buttons when mouse leaves top area
     */
    @SuppressWarnings("unused")
    public void topPaneOnMouseExit() {
        animationUtils.positionBackAnimation(title);
        editButton.setVisible(false);
        deleteButton.setVisible(false);
        moveButton.setVisible(false);
    }

    /**
     * Opens the prompt window for creating a card with a new name
     */
    @SuppressWarnings("unused")
    public void goToAddCard() {
        TextDialog.show(
            "Add Card",
            "Enter the card title",
            ""
        ).thenAccept(str -> observableBoard.send(
            "/boards/{boardId}/cards",
            new CardDto().setCardListId(cardList.getId())
                .setTitle(str)
        ));
    }

}