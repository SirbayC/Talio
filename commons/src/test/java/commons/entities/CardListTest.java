package commons.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class CardListTest {

    @Test
    public void checkConstructor() {
        CardList cl = new CardList();
        assertNotNull(cl);

        cl.setTitle("f");
        cl.setBackgroundColor("white");
        cl.setBorderColor("black");
        Card c = new Card().setTitle("a").setCardList(cl);
        cl.getCards().add(c);

        assertEquals("f", cl.getTitle());
        assertEquals(c, cl.getCards().get(0));
        assertEquals("white", cl.getBackgroundColor());
        assertEquals("black", cl.getBorderColor());
    }

    @Test
    public void boardTest() {
        Board b = new Board();
        assertEquals(0, b.getCardLists().size());

        CardList cardList = new CardList().setBoard(b);
        assertEquals(0, b.getCardLists().size());

        cardList.setBoard(null);
        assertEquals(b, cardList.getBoard());
    }

    @Test
    void testEqualsHashCode() {
        CardList cl1 = new CardList().setId(1);
        CardList cl2 = new CardList().setId(1);
        assertFalse(cl1.equals(1));
        assertEquals(cl1, cl2);
        assertEquals(cl1.hashCode(), cl2.hashCode());
    }

    @Test
    void testNotEqualsHashCode() {
        CardList cl1 = new CardList().setId(1);
        CardList cl2 = new CardList().setId(2);

        assertNotEquals(cl1, null);
        assertNotEquals(cl1, new CardList());
        assertNotEquals(cl1, cl2);
        assertNotEquals(cl1.hashCode(), cl2.hashCode());
    }

    @Test
    void testToString() {
        var actual = new CardList().setTitle("a").toString();
        assertTrue(actual.contains(CardList.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("a"));
    }

}