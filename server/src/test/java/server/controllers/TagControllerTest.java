package server.controllers;

import commons.dto.TagDto;
import commons.entities.Board;
import commons.entities.Tag;
import commons.mappers.TagMapper;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.repositories.TestBoardRepository;
import server.repositories.TestTagRepository;
import server.services.TagService;

class TagControllerTest {

    private TestBoardRepository boardRepository;
    private TestTagRepository tagRepository;
    private TagController sut;

    @BeforeEach
    public void setup() {
        boardRepository = new TestBoardRepository();
        tagRepository = new TestTagRepository();
        TagService service = new TagService(tagRepository, boardRepository, TagMapper.INSTANCE);
        sut = new TagController(service);
    }

    @Test
    public void updateTagNoBoard() {
        Tag l1 = new Tag().setName("Todo");
        tagRepository.save(l1);

        Assertions.assertNull(
            sut.sendUpdate(
                0,
                new TagDto().setId(l1.getId()).setName("ToDo2")
            )
        );
    }

    @Test
    public void createTag() {
        Board b = new Board();
        boardRepository.save(b);

        Tag l1 = new Tag().setBoard(b).setName("Todo");
        tagRepository.save(l1);

        Assertions.assertNotNull(
            sut.sendUpdate(
                b.getId(),
                new TagDto().setName("ToDo2")
            )
        );
        Assertions.assertEquals(2, tagRepository.tags.size());
    }

    @Test
    public void updateTagSuccess() {
        Board b = new Board();
        boardRepository.save(b);

        Tag l1 = new Tag().setBoard(b).setName("Todo");
        Tag l2 = new Tag().setBoard(b).setName("In Process");
        tagRepository.saveAll(List.of(l1, l2));

        Assertions.assertNotNull(
            sut.sendUpdate(
                b.getId(),
                new TagDto().setId(l1.getId()).setName("ToDo2")
            )
        );
        Assertions.assertEquals(
            tagRepository.findById(l1.getId()).get().getName(),
            "ToDo2"
        );
    }

    @Test
    public void databaseUse() {
        Board board = new Board();
        boardRepository.save(board);
        sut.sendUpdate(board.getId(), new TagDto().setName("Test List"));

        Assertions.assertTrue(tagRepository.calledMethods.contains("save"));
    }

    @Test
    public void deleteTagNoTag() {
        Board b = new Board();
        boardRepository.save(b);
        sut.sendUpdate(
            boardRepository.findAll().get(0).getId(),
            0
        );

        Assertions.assertTrue(tagRepository.calledMethods.contains("findTagByBoard_IdAndId"));
        Assertions.assertFalse(tagRepository.calledMethods.contains("delete"));
    }

    @Test
    public void deleteTagSuccess() {
        Board b = new Board();
        boardRepository.save(b);
        Tag l1 = new Tag().setBoard(b).setName("Todo");
        tagRepository.save(l1);
        Tag l2 = new Tag().setBoard(b).setName("In Process");
        tagRepository.save(l2);

        sut.sendUpdate(
            boardRepository.findAll().get(0).getId(),
            tagRepository.findAll().get(0).getId()
        );
        Assertions.assertTrue(tagRepository.calledMethods.contains("delete"));
        Assertions.assertEquals(
            tagRepository.findAll(),
            List.of(l2)
        );
    }
}

