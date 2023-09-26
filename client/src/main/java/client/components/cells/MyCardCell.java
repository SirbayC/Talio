package client.components.cells;

import client.components.MyCard;
import client.observable.ObservableBoard;
import client.observable.enums.ChangeType;
import commons.entities.Card;
import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import lombok.Getter;

public class MyCardCell extends ListCell<Card> {

    @Getter
    private final MyCard myCard;

    /**
     * Card Cell
     *
     * @param observableBoard observable board
     * @param cards ref
     */
    public MyCardCell(ObservableBoard observableBoard, ListView<Card> cards) {
        setStyle("-fx-padding: 10 0 0 0; -fx-background-color: none;");
        myCard = new MyCard(observableBoard, cards);
        Runnable unregister = observableBoard.listenToCards(((value, changeType) -> {
            if(changeType == ChangeType.DELETE)
                return;

            if(myCard.getCard() == null)
                return;

            if(!myCard.getCard().equals(value))
                return;

            myCard.setCard(value);
        }));

        // Runnable clean-up to prevent memory-leaks
        itemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null)
                return;

            Platform.runLater(unregister);
        });
    }

    @Override
    protected void updateItem(Card card, boolean empty) {
        super.updateItem(card, empty);
        if(empty || card == null) {
            setGraphic(null);
            return;
        }

        myCard.setCard(card);
        setGraphic(myCard);
    }

}
