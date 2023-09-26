package client.observable.listeners;

import client.observable.ObservableBoard;
import client.observable.ObservableValueMap;
import client.observable.enums.ChangeType;
import commons.entities.Card;
import commons.entities.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardModifyListenerTest {

    private ObservableBoard board;
    private CardModifyListener listener;

    @BeforeEach
    public void setUp() {
        CardList cardList = new CardList();
        cardList.setTitle("Test Card List");

        board = new ObservableBoard();
        board.setCardLists(new ObservableValueMap<>(new HashMap<>()));
        board.getCardLists().getMap().put(0L, cardList);

        listener = new CardModifyListener(board);
    }

    @Test
    public void testListen_Add() {
        Card card = new Card();
        card.setTitle("Test Card");
        card.setCardList(board.getCardLists().get(0L));
        card.setIndexInCardList(0);

        listener.listen(card, ChangeType.ADD);

        assertEquals(1, board.getCardLists().get(0L).getCards().size());
        assertEquals(card, board.getCardLists().get(0L).getCards().get(0));
    }

    @Test
    public void testListen_Delete() {
        Card card = new Card();
        card.setTitle("Test Card");
        card.setCardList(board.getCardLists().get(0L));
        card.setIndexInCardList(0);

        board.getCardLists().get(0L).getCards().add(card);

        listener.listen(card, ChangeType.DELETE);

        assertEquals(0, board.getCardLists().get(0L).getCards().size());
    }

    @Test
    public void testListen_Update() {
        CardList cl = new CardList();
        cl.setId(100);
        Card c = new Card().setCardList(cl);
        listener.listen(c, ChangeType.ADD);

        Card card = new Card();
        card.setTitle("Test Card");
        card.setCardList(board.getCardLists().get(0L));
        card.setIndexInCardList(0);

        board.getCardLists().get(0L).getCards().add(card);

        card.setIndexInCardList(1);

        listener.listen(card, ChangeType.UPDATE);

        assertEquals(1, board.getCardLists().get(0L).getCards().size());
        assertEquals(card, board.getCardLists().get(0L).getCards().get(0));
        assertEquals(1, board.getCardLists().get(0L).getCards().get(0).getIndexInCardList()); //doesn't update indexes here
    }

}
