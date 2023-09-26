package commons.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class TagDtoTest {

    @Test
    void ConstructorTest() {
        TagDto tagDto = new TagDto(1, "title", "red", new ArrayList<>(), "Arial");
        assertNotNull(tagDto);
        assertEquals(tagDto.getId(), 1);
        assertEquals("title", tagDto.getName());
        assertEquals(0, tagDto.getCardIds().size());
        assertEquals("Arial", tagDto.getFont());
    }

    @Test
    void diff(){
        TagDto tagDto = new TagDto(1L, "title", "black", new ArrayList<>(), "Arial");
        assertEquals(tagDto, tagDto.diff(null));
        TagDto tagDto2 = new TagDto(1L, "title", "black", new ArrayList<>(), "Arial");
        TagDto diff = tagDto2.diff(tagDto);
        assertEquals(tagDto2.getId(), diff.getId());
        assertNull(diff.getName());
        assertNull(diff.getColour());
        assertNull(diff.getCardIds());
        assertNull(diff.getFont());
    }
}