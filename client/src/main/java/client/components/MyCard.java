package client.components;

import client.components.dialog.ConfirmDialog;
import client.components.dialog.EditCard;
import client.observable.ObservableBoard;
import client.scenes.ButtonCtrl;
import client.utils.CustomizationUtils;
import commons.dto.CardDto;
import commons.entities.Card;
import commons.entities.CardList;
import commons.entities.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import lombok.Getter;

@Getter
public class MyCard extends VBox implements ButtonCtrl {

    @FXML
    private Text title;

    @FXML
    private Label taskProgress;

    @FXML
    private Button removeCardButton;

    @FXML
    @Getter
    private VBox cardBox;

    @FXML
    private Button moveCardButton;

    @FXML
    private SVGPath descIcon;

    @FXML
    private SVGPath dateIcon;

    @FXML
    private HBox tagCircles;

    private final ListView<Card> cards;

    private Card card;

    private final CustomizationUtils customizationUtils;

    private final ObservableBoard observableBoard;

    /**
     * @param observableBoard observable board
     * @param cards           reference to the ListView that holds this card
     */
    public MyCard(ObservableBoard observableBoard, ListView<Card> cards) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/components/MyCard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        customizationUtils = new CustomizationUtils();

        try {
            fxmlLoader.load();
            setHoverEffects();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.observableBoard = observableBoard;
        this.cards = cards;

        moveCardButton.setOnDragDetected(event -> {
            if(card == null) {
                return;
            }
            setOpacity(0.4);
            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("c" + cards.getItems().indexOf(card));
            dragboard.setContent(content);
            event.consume();
        });

        setDragOver();
        setDragEnter();
        setDragExit();
        setDragDropped();
        setDragDone();
    }

    /**
     * Sets the hover effects of the buttons
     */
    public void setHoverEffects() {
        List.of(removeCardButton, moveCardButton)
            .forEach(this::setHoverEffects);
    }

    private void setDragDropped() {
        this.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if(dragboard.hasString() && dragboard.getString().startsWith("c")) {
                MyCard source = (MyCard) event.getGestureSource();
                if(this.getCard().getCardList() != source.getCard().getCardList()) {
                    switchCardsDifferentLists(event);
                } else {
                    switchCardsSameList(event);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void switchCardsDifferentLists(DragEvent event) {
        Card originCard = ((MyCard) event.getGestureSource()).getCard();
        Card destinationCard = this.getCard();
        CardList originCardList = originCard.getCardList();
        CardList destinationCardList = destinationCard.getCardList();

        //make sure that the drag and drop effect ends properly
        originCard.setCardList(destinationCardList);

        List<Card> list = new ArrayList<>(destinationCardList.getCards());
        list.add(destinationCard.getIndexInCardList(), originCard);
        originCard.setIndexInCardList(-1);// Make sure it gets triggered
        updateIndexes(destinationCardList, list);

        list = new ArrayList<>(originCardList.getCards());
        list.remove(originCard);
        updateIndexes(originCardList, list);
    }

    private void switchCardsSameList(DragEvent event) {
        ObservableList<Card> items = cards.getItems();

        int originIdx = Integer.parseInt(event.getDragboard().getString().substring(1));
        int destinationIdx = items.indexOf(this.getCard());

        Card originCard = items.get(originIdx);

        List<Card> list = new ArrayList<>(cards.getItems());
        list.remove(originCard);
        list.add(destinationIdx, originCard);
        updateIndexes(card.getCardList(), list);
    }

    /**
     * Update indexes of cards in list
     *
     * @param destinationCardList destination card list
     * @param list                list of cards to update
     */
    private void updateIndexes(CardList destinationCardList, List<Card> list) {
        for(int i = 0; i < list.size(); i++) {
            Card card = list.get(i);
            if(card.getIndexInCardList() == i)
                continue;

            observableBoard.send(
                "/boards/{boardId}/cards",
                new CardDto().setId(card.getId())
                    .setCardListId(destinationCardList.getId())
                    .setIndexInCardList(i)
            );
        }
    }

    private void setDragExit() {
        this.setOnDragExited(event -> {
            Dragboard dragboard = event.getDragboard();
            MyCard source = (MyCard) event.getGestureSource();
            if(source != this && dragboard.hasString() && dragboard.getString().startsWith("c")) {
                if(!source.getCard().getCardList().equals(this.getCard().getCardList()))
                    this.cards.setOpacity(0.4);
                cardBox.setStyle(cardBox.getStyle()
                    + "-fx-border-color: black; -fx-padding: 5px; "
                    + "-fx-border-width: 1px; -fx-border-style: solid;");
            }
            event.consume();
        });
    }

    private void setDragEnter() {
        this.setOnDragEntered(event -> {
            Dragboard dragboard = event.getDragboard();
            Object source = event.getGestureSource();
            if(source != this && dragboard.hasString() && dragboard.getString().startsWith("c")) {
                this.cards.setOpacity(1);
                cardBox.setStyle(cardBox.getStyle()
                    + "-fx-border-color: black; -fx-padding: 5px; "
                    + "-fx-border-width: 3px; -fx-border-style: dashed;");
            }
            event.consume();
        });
    }

    private void setDragOver() {
        this.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();
            Object source = event.getGestureSource();
            if(source != this && dragboard.hasString() && dragboard.getString().startsWith("c")) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }

    private void setDragDone() {
        this.setOnDragDone(event -> {
            ((MyCard) event.getGestureSource()).setOpacity(1);
            ((MyCard) event.getGestureSource()).cards.setOpacity(1);
        });
    }

    /**
     * Sets the card's fields
     *
     * @param card card instance
     */
    public void setCard(Card card) {
        this.card = card;
        if(card.getColor() == null)
            card.setColor(customizationUtils.getDefaultCardColor());
        Color contrastingColor = customizationUtils.getContrastingColor(card.getColor());
        title.setText(card.getTitle());
        descIcon.setVisible(card.hasDescription());
        descIcon.setFill(contrastingColor);
        dateIcon.setVisible(card.hasDate());
        int done = 0;
        for(int i = 0; i < card.getTaskList().size(); i++) {
            if(card.getTaskList().get(i).isCompleted())
                done++;
        }
        taskProgress.setText(String.format("%d/%d", done, card.getTaskList().size()));
        taskProgress.setTextFill(contrastingColor);
        if(card.hasDescription() && card.hasDate()) {
            dateIcon.setTranslateX(22);
        } else {
            dateIcon.setTranslateX(0);
        }
        if(card.getColor() != null) {
            cardBox.setStyle(cardBox.getStyle() + ";" + "-fx-background-color: " +
                card.getColor() + ";");
        } else {
            cardBox.setStyle(cardBox.getStyle() + ";" + "-fx-background-color: " +
                customizationUtils.getDefaultCardColor() + ";");
        }
        title.setFill(contrastingColor);

        moveCardButton.setStyle(moveCardButton.getStyle() + "-fx-background-color: " +
            (contrastingColor == Color.WHITE ? "white" : "black") + ";");

        removeCardButton.setStyle(removeCardButton.getStyle() + "-fx-background-color: " +
            (contrastingColor == Color.WHITE ? "white" : "black") + ";");

        tagCircles.getChildren().setAll();
        card.getTags().stream().map(Tag::getColour).limit(6).forEach(s -> {
            Circle circle = new Circle();
            circle.setFill(Paint.valueOf(s));
            circle.setRadius(5);
            circle.setStrokeWidth(1);
            circle.setStrokeMiterLimit(10);
            circle.setStrokeType(StrokeType.INSIDE);
            circle.setStroke(Color.BLACK);
            tagCircles.getChildren().add(circle);
        });

        borderAdaptation(card);
    }

    /**
     * Adapts the border of the card
     *
     * @param card card instance
     */
    public void borderAdaptation(Card card) {

        if(card.getColor() != null && card.getCardList().getBackgroundColor() != null
            && card.getColor().equals(card.getCardList().getBackgroundColor())) {
            cardBox.setStyle(cardBox.getStyle() + "-fx-border-color: " +
                (customizationUtils.getContrastingColor(card.getCardList().getBackgroundColor()) == Color.WHITE
                    ? "white" : "black") + "; -fx-border-width: 1px;");
        } else if(card.getCardList().getBackgroundColor() == null && card.getColor() != null
            && card.getColor().equals(customizationUtils.getDefaultCardListColor())) {
            cardBox.setStyle(cardBox.getStyle() + "-fx-border-color: " +
                (customizationUtils.getContrastingColor(card.getColor()) == Color.WHITE
                    ? "white" : "black") + "; -fx-border-width: 1px;");
        }
    }

    /**
     * Shows the edit card scene Called by javaFX when clicking on the card's title
     */
    public void edit() {
        EditCard.show(
            "Edit Card",
            this.card,
            this.getObservableBoard()
        ).thenAccept(dto -> observableBoard.send(
            "/boards/{boardId}/cards",
            dto
        ));
    }

    /**
     * Creates an underline on the cards when the user hovers over them
     */
    @SuppressWarnings("unused")
    public void mouseEnter() {
        title.setUnderline(true);
    }

    /**
     * Removes the underline on the cards when the user stops hovering
     */
    @SuppressWarnings("unused")
    public void mouseExit() {
        title.setUnderline(false);
    }

    /**
     * Gets called when delete button is clicked
     */
    @SuppressWarnings("unused")
    public void remove() {
        ConfirmDialog.show(
            "Delete card",
            "Are you sure you wish to delete this card?"
        ).thenAccept(v -> observableBoard.send(
            "/boards/{boardId}/cards/delete",
            card.getId()
        )).thenAccept(str -> {
            cards.getItems().remove(card);
            cards.getItems().forEach(c -> {
                observableBoard.send(
                    "/boards/{boardId}/cards",
                    new CardDto()
                        .setId(c.getId())
                        .setIndexInCardList(cards.getItems().indexOf(c))
                );
            });
        });
    }

    /**
     * Displays context buttons when mouse enters card
     */
    @SuppressWarnings("unused")
    public void cardBoxOnMouseEnter() {
        removeCardButton.setVisible(true);
        moveCardButton.setVisible(true);
    }

    /**
     * Hides context buttons when mouse leaves card
     */
    @SuppressWarnings("unused")
    public void cardBoxOnMouseExit() {
        removeCardButton.setVisible(false);
        moveCardButton.setVisible(false);
    }

}