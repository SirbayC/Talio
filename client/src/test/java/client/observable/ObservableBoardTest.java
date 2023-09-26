package client.observable;

import client.observable.enums.ChangeType;
import client.utils.ServerUtils;
import commons.dto.BoardDto;
import commons.dto.CardDto;
import commons.dto.CardListDto;
import commons.dto.TaskDto;
import commons.entities.Board;
import commons.entities.Card;
import commons.entities.CardList;
import commons.entities.Task;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObservableBoardTest {

    private ObservableBoard board;

    private Board b;
    private CardList l1, l2, l3;
    private Card c1, c2, c3;
    private Task t1;

    @BeforeEach
    void setUp() {
        b = new Board().setId(1).setName("Default board");

        l1 = new CardList().setId(1).setBoard(b).setTitle("Todo").setIndexInBoard(0);
        l2 = new CardList().setId(2).setBoard(b).setTitle("In Process").setIndexInBoard(1);
        l3 = new CardList().setId(3).setBoard(b).setTitle("Done").setIndexInBoard(2);
        b.getCardLists().addAll(List.of(l1, l2, l3));

        c1 = new Card().setId(1).setCardList(l1).setTitle("Tidy bedroom").setIndexInCardList(1);
        c2 = new Card().setId(2).setCardList(l1).setTitle("Wash dishes").setIndexInCardList(0);
        c3 = new Card().setId(3).setCardList(l2).setTitle("Clean bathroom").setIndexInCardList(0);
        l1.getCards().addAll(List.of(c1, c2));
        l2.getCards().add(c3);
        b.getCards().addAll(List.of(c1, c2, c3));

        t1 = new Task().setId(1).setCard(c1).setBoard(b).setTitle("Make bed").setIndexInCard(0);
        Task t2 = new Task().setId(2).setCard(c1).setBoard(b).setTitle("Change sheets").setIndexInCard(1);
        Task t3 = new Task().setId(3).setCard(c2).setBoard(b).setTitle("Empty dishwasher").setIndexInCard(1);
        c1.getTaskList().add(t1);
        c1.getTaskList().add(t2);
        c2.getTaskList().add(t3);

        board = new ObservableBoard();
        board.setBoard(new ServerUtils(""), b);
    }

    @Test
    void gettersAndSetter() {
        board.setCardLists(null);
        Assertions.assertNull(board.getCardLists());
        board.setCards(null);
        Assertions.assertNull(board.getCards());
    }

    @Test
    void listenToBoard() {
        CountDownLatch latch = new CountDownLatch(1);
        board.listenToBoard(b -> latch.countDown());

        board.updateBoard(new BoardDto().setId(1));
        Assertions.assertEquals(0, latch.getCount());
    }

    @Test
    void updateBoard() {
        board.listenToBoard(board -> Assertions.assertEquals(
            "Test Board",
            board.getName()
        ));

        board.updateBoard(new BoardDto().setId(b.getId()).setName("Test Board"));
    }

    @Test
    void listenToCardLists() {
        CountDownLatch latch = new CountDownLatch(2);
        board.listenToCardLists((cardList, changeType) -> latch.countDown());

        board.updateCardList(new CardListDto().setId(l1.getId()));
        board.updateCardList(new CardListDto().setId(l2.getId()));
        Assertions.assertEquals(0, latch.getCount());
    }

    @Test
    void listenToCardListNone() {
        CountDownLatch latch = new CountDownLatch(2);
        board.listenToCardList(l3.getId(), (cardList, changeType) -> latch.countDown());

        board.updateCardList(new CardListDto().setId(l1.getId()));
        board.updateCardList(new CardListDto().setId(l2.getId()));
        Assertions.assertEquals(2, latch.getCount());
    }

    @Test
    void listenToCardListOne() {
        CountDownLatch latch = new CountDownLatch(2);
        board.listenToCardList(l2.getId(), (cardList, changeType) -> latch.countDown());

        board.updateCardList(new CardListDto().setId(l1.getId()));
        board.updateCardList(new CardListDto().setId(l2.getId()));
        Assertions.assertEquals(1, latch.getCount());
    }

    @Test
    void updateCardList() {
        board.listenToCardLists((cardList, changeType) -> {
            Assertions.assertEquals(ChangeType.UPDATE, changeType);
            Assertions.assertEquals("Test List", l1.getTitle());
        });

        board.updateCardList(new CardListDto().setId(l1.getId()).setTitle("Test List"));
    }

    @Test
    void removeCardList() {
        board.listenToCardLists((cardList, changeType) -> {
            Assertions.assertEquals(ChangeType.DELETE, changeType);
            Assertions.assertEquals(l1, cardList);
        });

        board.removeCardList(l1.getId());
        Assertions.assertFalse(b.getCardLists().contains(l1));
        l1.getCards().forEach(c -> Assertions.assertFalse(b.getCards().contains(c)));
    }

    @Test
    void listenToCards() {
        CountDownLatch latch = new CountDownLatch(2);
        board.listenToCards((card, changeType) -> latch.countDown());

        board.updateCard(new CardDto().setId(c1.getId()).setCardListId(1L));
        board.updateCard(new CardDto().setId(c2.getId()).setCardListId(1L));
        Assertions.assertEquals(0, latch.getCount());
    }

    @Test
    void listenToCardNone() {
        CountDownLatch latch = new CountDownLatch(2);
        board.listenToCard(c3.getId(), (card, changeType) -> latch.countDown());

        board.updateCard(new CardDto().setId(c1.getId()).setCardListId(1L));
        board.updateCard(new CardDto().setId(c2.getId()).setCardListId(1L));
        Assertions.assertEquals(2, latch.getCount());
    }

    @Test
    void listenToCardOne() {
        CountDownLatch latch = new CountDownLatch(2);
        board.listenToCard(c2.getId(), (card, changeType) -> latch.countDown());

        board.updateCard(new CardDto().setId(c1.getId()).setCardListId(1L));
        board.updateCard(new CardDto().setId(c2.getId()).setCardListId(1L));
        Assertions.assertEquals(1, latch.getCount());
    }

    @Test
    void updateCard() {
        board.listenToCards((cardList, changeType) -> {
            Assertions.assertEquals(ChangeType.UPDATE, changeType);
            Assertions.assertEquals("Test List", c1.getTitle());
        });

        board.updateCard(new CardDto().setId(c1.getId()).setTitle("Test List").setCardListId(1L));
    }

    @Test
    void removeCard() {
        board.listenToCards((card, changeType) -> {
            Assertions.assertEquals(ChangeType.DELETE, changeType);
            Assertions.assertEquals(c1, card);
        });

        board.removeCard(c1.getId());
        Assertions.assertFalse(b.getCards().contains(c1));
        Assertions.assertFalse(c1.getCardList().getCards().contains(c1));
    }

    @Test
    void removeTask() {
        board.listenToTasks((task, changeType) -> {
            Assertions.assertEquals(ChangeType.DELETE, changeType);
            Assertions.assertEquals(t1, task);
        });

        board.removeTask(t1.getId());
        Assertions.assertFalse(t1.getCard().getTaskList().contains(t1));
    }

    @Test
    void updateTask() {
        board.listenToTasks((task, changeType) -> {
            Assertions.assertEquals(ChangeType.UPDATE, changeType);
            Assertions.assertEquals("Test title2", t1.getTitle());
        });

        board.updateTask(new TaskDto().setId(t1.getId()).setTitle("Test title2"));

    }
}