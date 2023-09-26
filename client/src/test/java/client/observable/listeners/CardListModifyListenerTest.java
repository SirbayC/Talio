package client.observable.listeners;

import client.observable.enums.ChangeType;
import commons.entities.CardList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardListModifyListenerTest {

    @Test
    public void testAddCardList() {
        ObservableList<CardList> list = FXCollections.observableArrayList();
        CardListModifyListener listener = new CardListModifyListener(list);
        CardList cardList = new CardList().setTitle("Title");
        cardList.setIndexInBoard(0);
        listener.listen(cardList, ChangeType.ADD);
        assertTrue(list.contains(cardList));
        assertEquals(1, list.size());
    }

    @Test
    public void testDeleteCardList() {
        ObservableList<CardList> list = FXCollections.observableArrayList();
        CardListModifyListener listener = new CardListModifyListener(list);
        CardList cardList = new CardList().setTitle("Title");
        cardList.setIndexInBoard(0);
        list.add(cardList);
        listener.listen(cardList, ChangeType.DELETE);
        assertFalse(list.contains(cardList));
        assertEquals(0, list.size());
    }

    @Test
    public void testUpdateCardList() {
        ObservableList<CardList> list = FXCollections.observableArrayList();
        CardListModifyListener listener = new CardListModifyListener(list);
        CardList cardList = new CardList().setTitle("Title");
        listener.listen(cardList, ChangeType.UPDATE);
        assertFalse(list.contains(cardList));
        assertEquals(0, list.size());
    }

    @Test
    public void testAddCardListWithNullIndex() {
        ObservableList<CardList> list = FXCollections.observableArrayList();
        CardListModifyListener listener = new CardListModifyListener(list);
        CardList cardList = new CardList().setTitle("Title");
        listener.listen(cardList, ChangeType.ADD);
        assertTrue(list.contains(cardList));
        assertEquals(1, list.size());
        assertEquals(list.get(list.size()-1), cardList);
    }

}
