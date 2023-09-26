package commons.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CardDtoTest {

    CardDto cardDto;

    @BeforeEach
    void setup() {
        cardDto = new CardDto(1, 1L, "title", "desc", LocalDate.of(2022, 1, 1), 1, new ArrayList<>(), new ArrayList<>(), "white");
    }

    @Test
    void constructorTest() {
        assertEquals(1, cardDto.getId());
        assertEquals("title", cardDto.getTitle());
    }

    @Test
    void diffTestNoDiff() {
        CardDto diffCard = cardDto.diff(cardDto);
        assertNull(diffCard.getTitle());
        assertNull(diffCard.getDescription());
        assertNull(diffCard.getDate());
        assertNull(diffCard.getIndexInCardList());
    }

    @Test
    void diffTest1Diff() {
        CardDto cardDto2 = new CardDto(1, 1L, "title2", "desc", LocalDate.of(2022, 1, 1), 1, new ArrayList<>(), new ArrayList<>(), "black");
        CardDto diffCard = cardDto.diff(cardDto2);
        assertNotNull(diffCard.getTitle());
        assertNull(diffCard.getDescription());
        assertNull(diffCard.getDate());
        assertNull(diffCard.getIndexInCardList());
        assertNotNull(diffCard.getColor());
    }
}