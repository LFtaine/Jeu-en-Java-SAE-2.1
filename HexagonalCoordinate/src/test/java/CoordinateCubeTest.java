import iut.gon.hexagonal_coordinates.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CoordinateCubeTest {


    @Test
    void testConstructorValidCoord() {
        assertDoesNotThrow(() -> new CoordinateCube(0, 0, 0));
        assertDoesNotThrow(() -> new CoordinateCube(1, -1, 0));
    }

    @Test
    void testConstructorInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CoordinateCube(1, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> new CoordinateCube(0, 0, 1));
    }


    @Test
    void testToDirNE() {
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertEquals(new CoordinateCube(1, -1, 0), c.toDir(Mode.FLAT, Direction.NE));
    }

    @Test
    void testToDirN() {
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertEquals(new CoordinateCube(0, -1, 1), c.toDir(Mode.FLAT, Direction.N));
    }


    @Test
    void testGetNeighborsAlwaysSix() {
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        assertEquals(6, c.getNeighbors(Mode.FLAT).size());
    }

    // ── between ──────────────────────────────────────────────────────────

    @Test
    void testBetweenSameAxis() throws DifferentAxisException {
        CoordinateCube a = new CoordinateCube(-2, 0, 2);
        CoordinateCube b = new CoordinateCube( 2, 0, -2);
        List<Coordinate> between = a.between(Mode.FLAT, b);
        assertEquals(3, between.size());
        assertTrue(between.contains(new CoordinateCube(-1, 0, 1)));
        assertTrue(between.contains(new CoordinateCube( 0, 0, 0)));
        assertTrue(between.contains(new CoordinateCube( 1, 0, -1)));
    }

    @Test
    void testBetweenDifferentAxisThrows() {
        CoordinateCube a = new CoordinateCube(0, 0, 0);
        CoordinateCube b = new CoordinateCube(1, 1, -2); // aucun axe commun
        assertThrows(DifferentAxisException.class, () -> a.between(Mode.FLAT, b));
    }

    @Test
    void testBetweenAdjacentIsEmpty() throws DifferentAxisException {
        CoordinateCube a = new CoordinateCube(0, 0, 0);
        CoordinateCube b = new CoordinateCube(1, -1, 0);
        assertTrue(a.between(Mode.FLAT, b).isEmpty());
    }


    @Test
    void testCenterMapsToDoubledCenter() {
        CoordinateCube c = new CoordinateCube(0, 0, 0);
        Point p = c.to2DCoordinate();
        assertEquals(9, p.x()); // col
        assertEquals(5, p.y()); // row
    }

    @Test
    void testNeighborMapsCorrectly() {
        // NE de [0,0,0] = [1,-1,0] → doubled [4,10]
        CoordinateCube c = new CoordinateCube(1, -1, 0);
        assertEquals(new Point(10, 4), c.to2DCoordinate());
    }
}
