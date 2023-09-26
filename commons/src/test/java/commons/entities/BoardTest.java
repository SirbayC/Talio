package commons.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    public void checkConstructor() {
        Board b = new Board();
        assertNotNull(b);

        CardList cl = new CardList().setBoard(b);
        b.getCardLists().add(cl);
        assertEquals(cl, b.getCardLists().get(0));

        Card c = new Card().setCardList(cl);
        b.getCards().add(c);
        assertEquals(c, b.getCards().get(0));
    }

    @Test
    void testEquals() {
        Board b = new Board();
        assertNotEquals(b, null);
        assertNotEquals(b, new Card());
    }

    @Test
    void testEqualsHashCode() {
        Board b1 = new Board().setId(1);
        Board b2 = new Board().setId(1);

        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    void testNotEqualsHashCode() {
        Board b1 = new Board().setId(1);
        Board b2 = new Board().setId(2);

        assertNotEquals(b1, b2);
        assertNotEquals(b1.hashCode(), b2.hashCode());
    }

    @Test
    void testToString() {
        var actual = new Board().toString();
        assertTrue(actual.contains(Board.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("a"));
    }

    @Test
    void testIdName() {
        Board b = new Board();
        b.setName("a").setId(1);
        assertEquals("a", b.getName());
        assertEquals(1, b.getId());
    }

    @Test
    void testColor() {
        Board board1 = new Board().setOuterColor("white");
        Board board2 = new Board().setOuterColor(null);
        assertEquals("white", board1.getOuterColor());
        assertNull(board2.getOuterColor());
    }

    @Test
    void testFont() {
        Board board = new Board().setFont("Arial");
        Board board1 = new Board();
        assertEquals("Arial", board.getFont());
        assertNull(board1.getFont());
    }

}