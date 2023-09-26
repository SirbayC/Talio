package server.controllers;

import commons.dto.TaskDto;
import commons.entities.Board;
import commons.entities.Card;
import commons.entities.Task;
import commons.mappers.TaskMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.repositories.TestBoardRepository;
import server.repositories.TestCardRepository;
import server.repositories.TestTaskRepository;
import server.services.TaskService;


class TaskControllerTest {
    private TestBoardRepository boardRepository;

    private TestCardRepository cardRepository;

    private TestTaskRepository taskRepository;

    private TaskController sut;

    @BeforeEach
    public void setup() {
        boardRepository = new TestBoardRepository();
        cardRepository = new TestCardRepository();
        taskRepository = new TestTaskRepository();
        TaskService taskService = new TaskService(cardRepository, taskRepository, TaskMapper.INSTANCE);
        sut = new TaskController(taskService);
    }

    @Test
    public void failedUpdateNoBoard() {
        Assertions.assertEquals(0, boardRepository.count());
        Assertions.assertNull(
            sut.sendUpdate(0, new TaskDto().setId(0).setCardId(1L).setTitle("New Title"))
        );
    }

    @Test
    public void failedUpdateNoCard() {
        //Add and save board
        Board board = new Board();
        boardRepository.save(board);

        //Verify board exists but card doesn't
        Assertions.assertEquals(1, boardRepository.count());
        Assertions.assertEquals(0, cardRepository.count());
        Assertions.assertNull(
            sut.sendUpdate(board.getId(), new TaskDto().setId(0).setCardId(0L).setTitle("New Title"))
        );
    }

    @Test
    public void successfulUpdate() {
        //Prepare
        Board board = new Board();
        boardRepository.save(board);

        Card card = new Card().setBoard(board);
        cardRepository.save(card);

        Task task = new Task().setCard(card).setBoard(board);
        taskRepository.save(task);

        //Verify entity existance of entities
        Assertions.assertEquals(1, boardRepository.count());
        Assertions.assertEquals(1, cardRepository.count());
        Assertions.assertEquals(1, taskRepository.count());

        Assertions.assertNotNull(
            sut.sendUpdate(board.getId(), new TaskDto()
                .setId(task.getId())
                .setTitle("New Title")
                .setCardId(card.getId())
            )
        );
    }

    @Test
    public void failedTaskCreate() {
        Board board = new Board();
        boardRepository.save(board);

        Card card = new Card().setBoard(board);
        board.getCards().add(card);
        cardRepository.save(card);

        Assertions.assertNull(
            sut.sendUpdate(board.getId() +1, new TaskDto().setTitle("New Title").setCardId(card.getId()))
        );
    }

    @Test
    public void successfulTaskCreate() {
        //Prepare
        Board board = new Board();
        boardRepository.save(board);

        Card card = new Card().setBoard(board);
        board.getCards().add(card);
        cardRepository.save(card);

        Assertions.assertNotNull(sut.sendUpdate(board.getId(), new TaskDto()
            .setTitle("New Title")
            .setCardId(card.getId())
        ));
        board.getCards().get(0).getTaskList().add(taskRepository.getById(1L));

        Assertions.assertEquals(1, taskRepository.count());
        Assertions.assertEquals("New Title", board.getCards().get(0).getTaskList().get(0).getTitle());

    }

    @Test
    public void failedTaskDelete() {
        //Prepare
        Board board = new Board();
        boardRepository.save(board);

        Card card = new Card().setBoard(board);
        cardRepository.save(card);

        Task task = new Task().setCard(card);
        taskRepository.save(task);

        Assertions.assertEquals(-1, sut.sendUpdate(task.getBoard().getId(), task.getId()+1));
    }

    @Test
    public void successfulTaskDelete() {
        //Prepare
        Board board = new Board();
        boardRepository.save(board);

        Card card = new Card().setBoard(board);
        cardRepository.save(card);

        Task task = new Task().setCard(card);
        taskRepository.save(task);

        Assertions.assertEquals(task.getId(), sut.sendUpdate(board.getId(), task.getId()));
    }

    @Test
    public void databaseUse() {
        //Prepare
        Board board = new Board();
        boardRepository.save(board);

        Card card = new Card().setBoard(board);
        cardRepository.save(card);

        Task task = new Task().setCard(card).setBoard(board);
        taskRepository.save(task);

        //Call method and verify database usage
        sut.sendUpdate(board.getId(), new TaskDto().setId(task.getId()).setCardId(card.getId()).setTitle("New Title"));
        Assertions.assertTrue(taskRepository.calledMethods.contains("findTaskByCardIdAndId"));
    }
}

