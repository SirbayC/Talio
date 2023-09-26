package client.observable.listeners;

import client.observable.ObservableBoard;
import client.observable.ObservableValueMap;
import client.observable.enums.ChangeType;
import commons.entities.Card;
import commons.entities.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TaskModifyListenerTest {
    private ObservableBoard board;
    private TaskModifyListener listener;

    @BeforeEach
    public void setup() {
        Card card = new Card();
        card.setTitle("test card");

        board = new ObservableBoard();
        board.setCards(new ObservableValueMap<>(new HashMap<>()));
        board.getCards().getMap().put(0L, card);

        listener = new TaskModifyListener(board);
    }

    @Test
    void testListenDelete() {
        Task task = new Task()
            .setTitle("test task")
            .setCard(board.getCards().get(0L))
            .setIndexInCard(0);

        board.getCards().get(0L).getTaskList().add(task);

        listener.listen(task, ChangeType.DELETE);

        assertEquals(0, board.getCards().get(0L).getTaskList().size());
    }

    @Test
    void testListenUpdate() {
        Card card = new Card().setId(100);
        Task task = new Task().setCard(card);
        listener.listen(task, ChangeType.ADD);

        Task updateTask = new Task().setTitle("test task")
            .setCard(board.getCards().get(0L))
            .setIndexInCard(0);

        board.getCards().get(0L).getTaskList().add(updateTask);
        updateTask.setIndexInCard(1);

        listener.listen(updateTask, ChangeType.UPDATE);

        assertEquals(1, board.getCards().get(0L).getTaskList().size());
        assertEquals(updateTask, board.getCards().get(0L).getTaskList().get(0));
        assertEquals(1, board.getCards().get(0L).getTaskList().get(0).getIndexInCard());
    }

}