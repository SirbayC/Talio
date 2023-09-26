package commons.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    public void checkConstructor() {
        Tag tag = new Tag();
        assertNotNull(tag);

        tag.setName("s");
        Card c = new Card().setTitle("a");
        tag.getCards().add(c);

        assertEquals("s", tag.getName());
        assertEquals(c, tag.getCards().get(0));
    }

    @Test
    public void boardTest() {
        Board b = new Board();
        assertEquals(0, b.getTags().size());

        Tag tag = new Tag().setBoard(b);
        assertEquals(0, b.getTags().size());

        tag.setBoard(null);
        assertEquals(b, tag.getBoard());
    }

    @Test
    void testEqualsHashCode() {
        Tag tag1 = new Tag(1, "TestName", "TestColor", "Calibri", null, null);
        Tag tag2 = new Tag(1, "TestName", "TestColor", "Arial", new ArrayList<>(), null);
        assertEquals(tag1, tag2);
        assertNotEquals(tag1, null);
        assertNotEquals(tag1, new Card());
        assertNotEquals(tag1.getFont(), tag2.getFont());
    }

    @Test
    void testToString() {
        var actual = new CardList().setTitle("a").toString();
        assertTrue(actual.contains(CardList.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("a"));
    }

}