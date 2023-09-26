package commons.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class CardListDtoTest {

    @Test
    void ConstructorTest() {
        CardListDto cardListDto = new CardListDto(1, "title", 1, new ArrayList<>(), "white", "black");
        assertEquals(cardListDto.getId(), 1);
    }

    @Test
    void ColorsTest() {
        CardListDto cardListDto = new CardListDto(1, "title", 1, new ArrayList<>(), "white", "black");
        assertEquals(cardListDto.getBorderColor(), "black");
        assertEquals(cardListDto.getBackgroundColor(), "white");
    }

    @Test
    void diff(){
        CardListDto cardListDto = new CardListDto(1, "title", 1, new ArrayList<>(), "white", "black");
        assertEquals(cardListDto, cardListDto.diff(null));
        CardListDto cardListDto2 = new CardListDto(1, "title", 1, new ArrayList<>(), "white", "black");
        CardListDto diff = cardListDto2.diff(cardListDto);
        assertEquals(cardListDto.getId(), diff.getId());
        assertNull(diff.getTitle());
        assertNull(diff.getCards());
        assertNull(diff.getIndexInBoard());
        assertNull(diff.getBackgroundColor());
        assertNull(diff.getBorderColor());
    }
}