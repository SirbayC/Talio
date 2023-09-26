package client.observable.listeners;

import client.observable.ObservableBoard;
import client.observable.ObservableValueMap;
import client.observable.enums.ChangeType;
import commons.entities.Card;
import commons.entities.CardList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CardOrderListenerTest {

    private ObservableBoard board;
    private CardOrderListener cardOrderListener;

    @BeforeEach
    void setUp() {
        board = new ObservableBoard();
        board.setCardLists(new ObservableValueMap<>(new HashMap<>()));
        cardOrderListener = new CardOrderListener(board);
    }

    @Test
    void listenToNotUpdateType() {
        Card card = new Card();
        List<Card> cards = new ArrayList<>();
        cards.add(card);
        CardList cardList = new CardList();
        cardList.setCards(List.of(card, new Card()));
        board.getCardLists().getMap().put(cardList.getId(), cardList);
        cardOrderListener.listen(card, ChangeType.DELETE);
        Assertions.assertNotEquals(cards, cardList.getCards());
    }

    @Test
    void changeCardSameList() {
        Card card1 = new Card();
        card1.setIndexInCardList(1);

        Card card2 = new Card();
        card2.setIndexInCardList(0);

        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);

        CardList cardList = new CardList();
        cardList.setId(1L);
        cardList.setCards(cards);
        card1.setCardList(cardList);
        card2.setCardList(cardList);

        board.getCardLists().getMap().put(cardList.getId(), cardList);

        cardOrderListener.listen(card1, ChangeType.UPDATE);

        Assertions.assertEquals(cards, cardList.getCards());
        Assertions.assertEquals(cardList.getCards().get(0), card2);
        Assertions.assertEquals(cardList.getCards().get(1), card1);
    }

    @Test
    void changeCardDiffLists() {
        Card card = new Card().setId(1L);
        card.setIndexInCardList(1);

        List<Card> cards1 = new ArrayList<>();
        cards1.add(new Card().setIndexInCardList(0).setId(2L));
        cards1.add(card);

        List<Card> cards2 = new ArrayList<>();
        cards2.add(new Card().setIndexInCardList(0).setId(3L));

        CardList cardList1 = new CardList();
        cardList1.setId(3L);
        cardList1.setCards(cards1);

        CardList cardList2 = new CardList();
        cardList2.setId(4L);
        cardList2.setCards(cards2);
        card.setCardList(cardList2);

        board.getCardLists().getMap().put(cardList1.getId(), cardList1);
        board.getCardLists().getMap().put(cardList2.getId(), cardList2);

        cardOrderListener.listen(card, ChangeType.UPDATE);

        Assertions.assertEquals(cardList2, card.getCardList());
        Assertions.assertTrue(cardList2.getCards().contains(card));
        Assertions.assertEquals(1, card.getIndexInCardList());
    }

}
