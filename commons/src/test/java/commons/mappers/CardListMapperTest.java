package commons.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import commons.dto.CardListDto;
import commons.entities.Card;
import commons.entities.CardList;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CardListMapperTest {

    @Test
    void nullValues() {
        assertNull(CardListMapper.INSTANCE.toEntity(null));
        assertNull(CardListMapper.INSTANCE.toDto(null));
        assertNull(CardListMapper.INSTANCE.partialUpdate(null, null));
        CardList cardList = new CardList().setCards(null);
        CardListDto cardListDto = CardListMapper.INSTANCE.toDto(cardList);
        assertNull(cardListDto.getCards());
    }

    @Test
    void PartialUpdate() {
        Card card1 = new Card().setDescription("MyDesc");
        Card card2 = new Card();
        CardList cardList = new CardList().setCards(new ArrayList<>(List.of(card1, card2))).setIndexInBoard(0);
        CardListDto cardListDto = new CardListDto().setIndexInBoard(1).setCards(new ArrayList<>(List.of(CardMapper.INSTANCE.toDto(card1))));
        CardListMapper.INSTANCE.partialUpdate(cardListDto, cardList);
        assertEquals(cardList.getCards().size(), 1);
        cardList.setCards(null);
        cardListDto.setTitle("title");
        CardListMapper.INSTANCE.partialUpdate(cardListDto, cardList);
        assertEquals(cardList.getCards().size(), 1);
    }

    @Test
    void linkCards() {
        CardListDto cardListDto = new CardListDto().setCards(null);
        CardList cardList = CardListMapper.INSTANCE.toEntity(cardListDto);
        assertNotNull(cardList.getCards());
    }
}