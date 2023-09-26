package commons.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class BoardDtoTest {

    @Test
    void ConstructorTest() {
        BoardDto boardDto = new BoardDto(1, "name", new ArrayList<>(), "black", "red", "black", "white", "Arial", new ArrayList<>());
        assertEquals(1, boardDto.getId());
        assertEquals("name", boardDto.getName());
        assertEquals(0, boardDto.getCardLists().size());
        assertEquals("black", boardDto.getOuterColor());
        assertEquals("red", boardDto.getInnerColor());
        assertEquals("Arial", boardDto.getFont());
        assertEquals("white", boardDto.getCardListBackgroundColor());
        assertEquals("black", boardDto.getCardListBorderColor());
    }
}