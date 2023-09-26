package client.components.cells;

import client.components.MyCard;
import client.components.MyCardList;
import client.observable.ObservableBoard;
import client.observable.enums.ChangeType;
import commons.dto.CardDto;
import commons.dto.CardListDto;
import commons.entities.Card;
import commons.entities.CardList;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class MyCardListCell extends ListCell<CardList> {

    private final MyCardList myCardList;

    /**
     * Card List Cell
     *
     * @param observableBoard observable board
     */
    public MyCardListCell(ObservableBoard observableBoard) {
        super();
        this.myCardList = new MyCardList(observableBoard);

        setStyle("-fx-padding: 0 15 1 0; -fx-background-color: none;");

        setDragOver();
        setDragEnter();
        setDragExit();
        setDragDropped();
        setDragDone();

        Runnable unregister = observableBoard.listenToCardLists(((value, changeType) -> {
            if(changeType == ChangeType.DELETE)
                return;

            if(myCardList.getCardList() == null)
                return;

            if(!myCardList.getCardList().equals(value))
                return;

            myCardList.setCardList(value);
        }));

        // Runnable clean-up to prevent memory-leaks
        itemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null)
                return;

            Platform.runLater(unregister);
        });
    }

    private void setDragDropped() {
        this.setOnDragDropped(event -> {
            if(getItem() == null)
                return;

            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if(dragboard.hasString()
                && dragboard.getString().startsWith("l")) {

                switchCardLists(event);
                success = true;

            } else if(dragboard.hasString()
                && dragboard.getString().startsWith("c")
                && !((MyCard) event.getGestureSource()).getCard().getCardList().equals(this.getItem())) {

                addNewCardFromAnotherList(event);
                success = true;

            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void switchCardLists(DragEvent event) {
        int originIdx = Integer.parseInt(event.getDragboard().getString().substring(1));
        CardList originCardList = getListView().getItems().get(originIdx);

        int destinationIdx = getListView().getItems().indexOf(getItem());

        List<CardList> list = new ArrayList<>(getListView().getItems());
        list.remove(originCardList);
        list.add(destinationIdx, originCardList);
        originCardList.setIndexInBoard(-1);// Make sure it gets triggered
        for(int i = 0; i < list.size(); i++) {
            CardList cardList = list.get(i);
            if(cardList.getIndexInBoard() == i)
                continue;

            myCardList.getObservableBoard().send(
                "/boards/{boardId}/cardlists",
                new CardListDto().setId(cardList.getId()).setIndexInBoard(i)
            );
        }
    }

    private void addNewCardFromAnotherList(DragEvent event) {
        MyCard originCard = (MyCard) event.getGestureSource();
        CardList originCardList = originCard.getCard().getCardList();

        List<Card> list = new ArrayList<>(getItem().getCards());
        list.add(originCard.getCard());
        originCard.getCard().setIndexInCardList(-1);// Make sure it gets triggered
        for(int i = 0; i < list.size(); i++) {
            Card card = list.get(i);
            if(card.getIndexInCardList() == i)
                continue;

            myCardList.getObservableBoard().send(
                "/boards/{boardId}/cards",
                new CardDto().setId(card.getId()).setCardListId(getItem().getId()).setIndexInCardList(i)
            );
        }

        list = new ArrayList<>(originCardList.getCards());
        list.remove(originCard.getCard());
        for(int i = 0; i < list.size(); i++) {
            Card card = list.get(i);
            if(card.getIndexInCardList() == i)
                continue;

            myCardList.getObservableBoard().send(
                "/boards/{boardId}/cards",
                new CardDto().setId(card.getId()).setCardListId(originCardList.getId()).setIndexInCardList(i)
            );
        }
    }

    private void setDragExit() {
        this.setOnDragExited(event -> {
            Dragboard dragboard = event.getDragboard();
            Object source = event.getGestureSource();
            if(dragboard.hasString()) {
                if(dragboard.getString().startsWith("l")
                    && !((MyCardList) source).getCardList().equals(this.getItem())) {
                    setOpacity(1);
                } else if(dragboard.getString().startsWith("c")
                    && !((MyCard) source).getCard().getCardList().equals(this.getItem())) {
                    this.myCardList.getCards().setOpacity(1);
                }
            }
            event.consume();
        });
    }

    private void setDragEnter() {
        this.setOnDragEntered(event -> {
            Dragboard dragboard = event.getDragboard();
            Object source = event.getGestureSource();
            if(dragboard.hasString()) {
                if(dragboard.getString().startsWith("l")
                    && !((MyCardList) source).getCardList().equals(this.getItem())) {
                    setOpacity(0.4);
                } else if(dragboard.getString().startsWith("c")
                    && !((MyCard) source).getCard().getCardList().equals(this.getItem())) {
                    this.myCardList.getCards().setOpacity(0.4);
                }
            }
            event.consume();
        });
    }

    private void setDragOver() {
        this.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();
            Object source = event.getGestureSource();
            if(dragboard.hasString()) {
                if(dragboard.getString().startsWith("l")
                    && !((MyCardList) source).getCardList().equals(this.getItem())) {
                    event.acceptTransferModes(TransferMode.MOVE);
                } else if(dragboard.getString().startsWith("c")
                    && !((MyCard) source).getCard().getCardList().equals(this.getItem())) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            }
            event.consume();
        });
    }

    private void setDragDone() {
        this.setOnDragDone(event -> {
            myCardList.getCards().setOpacity(1);
        });
    }

    @Override
    protected void updateItem(CardList cardList, boolean empty) {
        super.updateItem(cardList, empty);
        if(empty || cardList == null) {
            setGraphic(null);
            return;
        }

        setStyle("-fx-padding: 0 15 1 0; -fx-background-color: none;");
        myCardList.setCardList(cardList);
        myCardList.setLists(getListView());
        setGraphic(myCardList);
    }

}
