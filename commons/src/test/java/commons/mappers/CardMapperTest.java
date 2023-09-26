package commons.mappers;

import commons.dto.CardDto;
import commons.entities.Card;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CardMapperTest {
    @Test
    void nullValues(){
        assertNull(CardMapper.INSTANCE.toEntity(null));
        assertNull(CardMapper.INSTANCE.toDto(null));
        assertNull(CardMapper.INSTANCE.partialUpdate(null, null));
    }

    @Test
    void partialUpdate(){
        Card card = new Card();
        CardDto cardDto = new CardDto().setTitle("title").setDescription("desc").setDate(LocalDate.of(2000,12,1)).setIndexInCardList(1);
        CardMapper.INSTANCE.partialUpdate(cardDto, card);
        assertEquals(card.getTitle(), "title");
        assertEquals(card.getDescription(), "desc");
    }
}