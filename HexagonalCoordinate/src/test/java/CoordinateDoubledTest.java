import iut.gon.hexagonal_coordinates.*;


import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CoordinateDoubledTest {


    @Test
    void testToDirPOINTY() {
        CoordinateDoubled c = new CoordinateDoubled(5, 9); // centre
        assertEquals(new CoordinateDoubled(5, 11), c.toDir(Mode.POINTY, Direction.E));
        assertEquals(new CoordinateDoubled(5,  7), c.toDir(Mode.POINTY, Direction.O));
        assertEquals(new CoordinateDoubled(4, 10), c.toDir(Mode.POINTY, Direction.NE));
        assertEquals(new CoordinateDoubled(4,  8), c.toDir(Mode.POINTY, Direction.NO));
        assertEquals(new CoordinateDoubled(6, 10), c.toDir(Mode.POINTY, Direction.SE));
        assertEquals(new CoordinateDoubled(6,  8), c.toDir(Mode.POINTY, Direction.SO));
    }

    @Test
    void testToDirFLAT() {
        CoordinateDoubled c = new CoordinateDoubled(5, 9);
        assertEquals(new CoordinateDoubled(3, 9), c.toDir(Mode.FLAT, Direction.N));
        assertEquals(new CoordinateDoubled(7, 9), c.toDir(Mode.FLAT, Direction.S));
    }


    @Test
    void testGetNeighborsAlwaysSix() {
        CoordinateDoubled c = new CoordinateDoubled(5, 9);
        assertEquals(6, c.getNeighbors(Mode.POINTY).size());
        assertEquals(6, c.getNeighbors(Mode.FLAT).size());
    }

    @Test
    void testGetNeighborsPOINTY() {
        CoordinateDoubled c = new CoordinateDoubled(5, 9);
        List<Coordinate> neighbors = c.getNeighbors(Mode.POINTY);
        assertTrue(neighbors.contains(new CoordinateDoubled(5, 11)));
        assertTrue(neighbors.contains(new CoordinateDoubled(5,  7)));
        assertTrue(neighbors.contains(new CoordinateDoubled(4, 10)));
        assertTrue(neighbors.contains(new CoordinateDoubled(4,  8)));
        assertTrue(neighbors.contains(new CoordinateDoubled(6, 10)));
        assertTrue(neighbors.contains(new CoordinateDoubled(6,  8)));
    }


    @Test
    void testBetweenSameAxisPOINTY() throws DifferentAxisException {
        CoordinateDoubled a = new CoordinateDoubled(5, 7);
        CoordinateDoubled b = new CoordinateDoubled(5, 13);
        List<Coordinate> between = a.between(Mode.POINTY, b);
        assertEquals(2, between.size());
        assertTrue(between.contains(new CoordinateDoubled(5, 9)));
        assertTrue(between.contains(new CoordinateDoubled(5, 11)));
    }

    @Test
    void testBetweenAdjacentIsEmpty() throws DifferentAxisException {
        CoordinateDoubled a = new CoordinateDoubled(5, 9);
        CoordinateDoubled b = new CoordinateDoubled(5, 11);
        assertTrue(a.between(Mode.POINTY, b).isEmpty());
    }

    @Test
    void testBetweenDifferentAxisThrows() {
        CoordinateDoubled a = new CoordinateDoubled(5, 9);
        CoordinateDoubled b = new CoordinateDoubled(6, 11); // pas le même axe
        assertThrows(DifferentAxisException.class, () -> a.between(Mode.POINTY, b));
    }


    @Test
    void testTo2DCoordinate() {
        CoordinateDoubled c = new CoordinateDoubled(5, 9);
        Point p = c.to2DCoordinate();
        assertEquals(9, p.x());
        assertEquals(5, p.y());
    }


    @Test
    void testNInvalidForPOINTY() {
        CoordinateDoubled c = new CoordinateDoubled(5, 9);
        assertThrows(InvalidParameterException.class, () -> c.N(Mode.POINTY));
    }

    @Test
    void testEInvalidForFLAT() {
        CoordinateDoubled c = new CoordinateDoubled(5, 9);
        assertThrows(InvalidParameterException.class, () -> c.E(Mode.FLAT));
    }
}
