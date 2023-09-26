package commons.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import commons.dto.BoardDto;
import commons.dto.CardListDto;
import commons.entities.Board;
import commons.entities.Card;
import commons.entities.CardList;
import commons.entities.Tag;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class BoardMapperTest {

    @Test
    void nullValues() {
        assertNull(BoardMapper.INSTANCE.toEntity(null));
        assertNull(BoardMapper.INSTANCE.toDto(null));
        assertNull(BoardMapper.INSTANCE.partialUpdate(null, null));
        Board board = new Board().setCardLists(null);
        BoardDto boardDto = BoardMapper.INSTANCE.toDto(board);
        assertNull(boardDto.getCardLists());
    }

    @Test
    void PartialUpdate() {
        CardList cardList = new CardList();
        CardListDto cardListDto = CardListMapper.INSTANCE.toDto(cardList);
        BoardDto boardDto = new BoardDto().setCardLists(List.of(cardListDto)).setName("Name");
        Board board = new Board().setCardLists(null);
        BoardMapper.INSTANCE.partialUpdate(boardDto, board);
        assertEquals(List.of(cardList), board.getCardLists());
        assertEquals(board.getName(), "Name");

        board = new Board();
        BoardMapper.INSTANCE.partialUpdate(boardDto, board);
        assertEquals(1, board.getCardLists().size());
    }

    @Test
    void linkCardLists() {
        BoardDto boardDto = new BoardDto().setId(1).setName("p").setCardLists(null);
        Board board1 = BoardMapper.INSTANCE.toEntity(boardDto);
        assertNotNull(board1.getCardLists());
    }

    @Test
    void linkCards() {
        Card card = new Card();
        CardList cl1 = new CardList().setCards(List.of(card));
        BoardDto boardDto = new BoardDto().setId(1).setName("p").setCardLists(new ArrayList<>(List.of(CardListMapper.INSTANCE.toDto(cl1))));
        Board board1 = BoardMapper.INSTANCE.toEntity(boardDto);
        assertEquals(board1, board1.getCardLists().get(0).getBoard());
    }

    @Test
    void toDtoNotEmptyCardList() {
        CardList cl1 = new CardList().setCards(List.of(new Card()));
        Board board = new Board().setCardLists(List.of(cl1));
        BoardDto boardDto = BoardMapper.INSTANCE.toDto(board);
        assertEquals(CardListMapper.INSTANCE.toDto(cl1), boardDto.getCardLists().get(0));
    }

    @Test
    void linkTags() {
        Tag tag = new Tag();
        BoardDto boardDto = new BoardDto().setId(1).setName("p").setTags(new ArrayList<>(List.of(TagMapper.INSTANCE.toDto(tag))));
        Board board1 = BoardMapper.INSTANCE.toEntity(boardDto);
        assertEquals(board1, board1.getTags().get(0).getBoard());
    }

    @Test
    void linkTagsToCards() {
        Tag tag = new Tag().setId(1).setName("TestTag");
        Card c1 = new Card().setId(0).setTags(List.of(tag));
        Card c2 = new Card().setId(1).setTags(List.of(tag));
        CardList cardList = new CardList().setId(1).setCards(List.of(c1, c2));
        tag.setCards(List.of(c1, c2));
        BoardDto boardDto = new BoardDto()
            .setId(1)
            .setName("Board")
            .setTags(new ArrayList<>(List.of(TagMapper.INSTANCE.toDto(tag))))
            .setCardLists(List.of(CardListMapper.INSTANCE.toDto(cardList)));
        Board board1 = BoardMapper.INSTANCE.toEntity(boardDto);
        assertEquals(board1, board1.getTags().get(0).getBoard());
        assertEquals(board1.getTags(), List.of(tag));

    }

    @Test
    void linkTagsNull() {
        Card c1 = new Card().setId(0).setTags(null);
        Card c2 = new Card().setId(1).setTags(null);
        CardList cardList = new CardList().setId(1).setCards(List.of(c1, c2));
        BoardDto boardDto = new BoardDto()
            .setId(1)
            .setName("Board")
            .setCardLists(List.of(CardListMapper.INSTANCE.toDto(cardList)));
        Board board1 = BoardMapper.INSTANCE.toEntity(boardDto);
        assertEquals(board1.getTags(), List.of());

    }

    @Test
    void linkTagsToCardsNull() {
        CardList cardList = new CardList().setId(1);
        BoardDto boardDto = new BoardDto()
            .setId(1)
            .setName("Board")
            .setCardLists(List.of(CardListMapper.INSTANCE.toDto(cardList)));
        Board board1 = BoardMapper.INSTANCE.toEntity(boardDto.setTags(null));
        assertNotNull(board1.getTags());
    }

}