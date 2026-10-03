import iut.gon.hexagonal_coordinates.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    

    @Test
    void testConstructorAndFields() {
        Point p = new Point(3, 7);
        assertEquals(3, p.x());
        assertEquals(7, p.y());
    }

    @Test
    void testNegativeValues() {
        Point p = new Point(-1, -5);
        assertEquals(-1, p.x());
        assertEquals(-5, p.y());
    }

    @Test
    void testZeroValues() {
        Point p = new Point(0, 0);
        assertEquals(0, p.x());
        assertEquals(0, p.y());
    }


    @Test
    void testEqualsSameValues() {
        Point p1 = new Point(4, 6);
        Point p2 = new Point(4, 6);
        assertEquals(p1, p2);
    }

    @Test
    void testEqualsDifferentX() {
        Point p1 = new Point(4, 6);
        Point p2 = new Point(5, 6);
        assertNotEquals(p1, p2);
    }

    @Test
    void testEqualsDifferentY() {
        Point p1 = new Point(4, 6);
        Point p2 = new Point(4, 7);
        assertNotEquals(p1, p2);
    }

    @Test
    void testEqualsNull() {
        Point p = new Point(4, 6);
        assertNotEquals(null, p);
    }

    @Test
    void testEqualsOtherType() {
        Point p = new Point(4, 6);
        assertNotEquals("texte", p);
    }


    @Test
    void testHashCodeConsistency() {
        Point p = new Point(4, 6);
        assertEquals(p.hashCode(), p.hashCode());
    }

    @Test
    void testHashCodeEqualObjects() {
        Point p1 = new Point(4, 6);
        Point p2 = new Point(4, 6);
        assertEquals(p1.hashCode(), p2.hashCode());
    }


    @Test
    void testToString() {
        Point p = new Point(3, 7);
        String s = p.toString();
        // Doit contenir les deux valeurs
        assertTrue(s.contains("3"));
        assertTrue(s.contains("7"));
    }
}