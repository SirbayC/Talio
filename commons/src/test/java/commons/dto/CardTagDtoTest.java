package commons.dto;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardTagDtoTest {

    @Test
    public void testGettersAndSetters() {
        CardTagDto cardTag = new CardTagDto()
            .setCardId(12345L)
            .setTagId(67890L)
            .setLink(true);
        CardTagDto cardTagDto = new CardTagDto(1L,2L,false);
        assertEquals(12345L, cardTag.getCardId());
        assertEquals(67890L, cardTag.getTagId());
        assertTrue(cardTag.isLink());
        cardTag.setCardId(98765L);
        cardTag.setTagId(43210L);
        cardTag.setLink(false);
        assertEquals(98765L, cardTag.getCardId());
        assertEquals(43210L, cardTag.getTagId());
        assertFalse(cardTag.isLink());
    }

    @Test
    public void testEqualsAndHashCode() {
        CardTagDto cardTag1 = new CardTagDto()
            .setCardId(12345L)
            .setTagId(67890L)
            .setLink(true);
        CardTagDto cardTag2 = new CardTagDto()
            .setCardId(12345L)
            .setTagId(67890L)
            .setLink(true);
        assertEquals(cardTag1, cardTag2);
        assertEquals(cardTag1.hashCode(), cardTag2.hashCode());
        CardTagDto cardTag3 = new CardTagDto()
            .setCardId(54321L)
            .setTagId(98760L)
            .setLink(false);
        assertNotEquals(cardTag1, cardTag3);
        assertNotEquals(cardTag1.hashCode(), cardTag3.hashCode());
    }
}
