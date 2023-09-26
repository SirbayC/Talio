package commons.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.entities.Board;
import commons.entities.Card;
import commons.entities.Task;

public class TaskTest {

    private Task task;
    private Board board;
    private Card card;

    @BeforeEach
    void setUp() {
        board = new Board().setId(1L).setName("Board");
        card = new Card().setId(1L).setBoard(board).setTitle("Card");
        task = new Task().setId(1L).setCard(card).setTitle("Task").setIndexInCard(1).setCompleted(false);
    }

    @Test
    void testSetCard() {
        Card newCard = new Card().setId(2L).setBoard(board).setTitle("New card");
        task.setCard(newCard);
        assertEquals(newCard, task.getCard());
        assertEquals(board, task.getBoard());
        assertEquals(task, task.setCard(null));
    }

    @Test
    void testSetBoard() {
        Board newBoard = new Board().setId(2L).setName("New board");
        task.setBoard(newBoard);
        assertEquals(newBoard, task.getBoard());
    }

    @Test
    void testEquals() {
        Task task1 = new Task().setId(1L);
        Task task2 = new Task().setId(1L);
        Task task3 = new Task().setId(2L).setCompleted(true);

        assertTrue(task1.equals(task2));
        assertFalse(task1.equals(task3));
        assertFalse(task1.equals(null));
        assertFalse(task1.equals(new Object()));
    }

    @Test
    void testHashCode() {
        Task task1 = new Task().setId(1L);
        Task task2 = new Task().setId(1L);

        assertEquals(task1.hashCode(), task2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Task", task.toString());
    }

}
