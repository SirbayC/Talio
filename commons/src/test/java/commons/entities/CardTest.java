package commons.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    public void checkConstructor() {
        Card c = new Card();
        assertNotNull(c);

        c.setTitle("f");
        assertEquals("f", c.getTitle());

        c.setId(1);
        assertEquals(1, c.getId());

        c.setDescription("Desc");
        assertEquals("Desc", c.getDescription());
        assertTrue(c.hasDescription());

        c.setDate(LocalDate.of(2000, 10, 1));
        assertEquals(LocalDate.of(2000, 10, 1), c.getDate());

        c.setIndexInCardList(1);
        assertEquals(1, c.getIndexInCardList());

        c.setColor("white");
        assertEquals("white", c.getColor());
    }

    @Test
    public void boardTest() {
        Board b = new Board();
        assertEquals(0, b.getCards().size());

        Card card = new Card().setBoard(b);
        assertEquals(0, b.getCards().size());
        assertEquals(b, card.getBoard());
    }

    @Test
    public void cardListTest() {
        CardList cl = new CardList();
        assertEquals(0, cl.getCards().size());

        Card card = new Card().setCardList(cl);
        assertEquals(0, cl.getCards().size());
        assertEquals(cl, card.getCardList());

        card.setCardList(null);
        assertEquals(cl, card.getCardList());
    }

    @Test
    void testEqualsHashCode() {
        Card c1 = new Card().setId(1);
        Card c2 = new Card().setId(1);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testNotEqualsHashCodeHashCode() {
        Card c1 = new Card().setId(1);
        Card c2 = new Card().setId(2);

        assertNotEquals(c1, c2);
        assertNotEquals(c1, null);
        assertNotEquals(c1, new Board());
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString() {
        var actual = new Card().setTitle("a").toString();
        assertTrue(actual.contains(Card.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("a"));
    }

}