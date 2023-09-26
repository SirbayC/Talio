package client.observable.listeners;

import client.observable.enums.ChangeType;
import commons.entities.CardList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardListOrderListenerTest {

    @Test
    public void testListenNoUpdate() {
        ObservableList<CardList> list = FXCollections.observableArrayList();
        CardListOrderListener listener = new CardListOrderListener(list);
        CardList cardList = new CardList().setTitle("Title");
        listener.listen(cardList, ChangeType.ADD);
        assertEquals(0, list.size());
    }

    @Test
    public void testListenWithNullIndex() {
        ObservableList<CardList> list = FXCollections.observableArrayList();
        CardListOrderListener listener = new CardListOrderListener(list);
        CardList cardList = new CardList().setTitle("Title");
        listener.listen(cardList, ChangeType.UPDATE);
        assertEquals(0, list.size());
        list.add(cardList);
        listener.listen(cardList, ChangeType.UPDATE);
        assertEquals(1, list.size());
        assertEquals(list.get(0), cardList);
    }

    @Test
    public void testListenIndexUnchanged() {
        ObservableList<CardList> list = FXCollections.observableArrayList();
        CardListOrderListener listener = new CardListOrderListener(list);
        CardList cardList = new CardList().setTitle("Title");
        cardList.setIndexInBoard(0);
        list.add(cardList);
        listener.listen(cardList, ChangeType.UPDATE);
        assertEquals(1, list.size());
        assertEquals(list.get(0), cardList);
    }

    @Test
    public void testListenIndexChanged() {
        ObservableList<CardList> list = FXCollections.observableArrayList();
        CardListOrderListener listener = new CardListOrderListener(list);
        CardList cardList1 = new CardList().setTitle("Title");
        CardList cardList2 = new CardList().setTitle("Title");
        CardList cardList3 = new CardList().setTitle("Title");
        list.addAll(cardList1, cardList2, cardList3);
        cardList2.setIndexInBoard(2);
        listener.listen(cardList2, ChangeType.UPDATE);
        assertEquals(3, list.size());
        assertEquals(list.get(0), cardList1);
        assertEquals(list.get(1), cardList3);
        assertEquals(list.get(2), cardList2);
    }

}
