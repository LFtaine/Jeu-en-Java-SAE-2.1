package tests;

import iut.gon.model.actions.Action;
import iut.gon.model.actions.Move;
import iut.gon.model.actions.RemoveLine;
import iut.gon.hexagonal_coordinates.CoordinateDoubled;
import iut.gon.hexagonal_coordinates.Coordinate;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {

    private CoordinateDoubled coord(int y, int x) {
        return new CoordinateDoubled(y, x);
    }

    private Set<Coordinate> fiveCoords() {
        Set<Coordinate> line = new HashSet<>();
        for (int i = 0; i < 5; i++) line.add(coord(4, i * 2));
        return line;
    }

    @Test
    void move_shouldStoreFromCoordinate() {
        CoordinateDoubled from = coord(0, 0);
        CoordinateDoubled to   = coord(1, 1);
        Move move = new Move(from, to);
        assertNotNull(move.getFrom());
    }

    @Test
    void move_shouldStoreToCoordinate() {
        CoordinateDoubled from = coord(0, 0);
        CoordinateDoubled to   = coord(2, 2);
        Move move = new Move(from, to);
        assertNotNull(move.getTo());
    }

    @Test
    void move_getFrom_shouldReturnCorrectValue() {
        CoordinateDoubled from = coord(3, 5);
        CoordinateDoubled to   = coord(7, 9);
        Move move = new Move(from, to);
        assertEquals(from, move.getFrom());
    }

    @Test
    void move_getTo_shouldReturnCorrectValue() {
        CoordinateDoubled from = coord(3, 5);
        CoordinateDoubled to   = coord(7, 9);
        Move move = new Move(from, to);
        assertEquals(to, move.getTo());
    }

    @Test
    void move_setFrom_shouldUpdateValue() {
        CoordinateDoubled from    = coord(0, 0);
        CoordinateDoubled to      = coord(2, 2);
        CoordinateDoubled newFrom = coord(4, 4);
        Move move = new Move(from, to);
        move.setFrom(newFrom);
        assertEquals(newFrom, move.getFrom());
    }

    @Test
    void move_setTo_shouldUpdateValue() {
        CoordinateDoubled from  = coord(0, 0);
        CoordinateDoubled to    = coord(2, 2);
        CoordinateDoubled newTo = coord(6, 8);
        Move move = new Move(from, to);
        move.setTo(newTo);
        assertEquals(newTo, move.getTo());
    }

    @Test
    void move_shouldExtendAction() {
        Move move = new Move(coord(0, 0), coord(2, 2));
        assertInstanceOf(Action.class, move);
    }

    @Test
    void removeLine_shouldStoreLine() {
        RemoveLine rl = new RemoveLine(fiveCoords(), coord(9, 9));
        assertNotNull(rl.getLine());
    }

    @Test
    void removeLine_shouldStoreRing() {
        RemoveLine rl = new RemoveLine(fiveCoords(), coord(9, 9));
        assertNotNull(rl.getRing());
    }

    @Test
    void removeLine_getLine_shouldReturnFiveCoordinates() {
        RemoveLine rl = new RemoveLine(fiveCoords(), coord(9, 9));
        assertEquals(5, rl.getLine().size());
    }

    @Test
    void removeLine_getRing_shouldReturnCorrectCoordinate() {
        CoordinateDoubled ring = coord(8, 6);
        RemoveLine rl = new RemoveLine(fiveCoords(), ring);
        assertEquals(ring, rl.getRing());
    }

    @Test
    void removeLine_setLine_shouldUpdateLine() {
        RemoveLine rl = new RemoveLine(fiveCoords(), coord(9, 9));
        Set<Coordinate> newLine = new HashSet<>();
        for (int i = 0; i < 5; i++) newLine.add(coord(6, i * 2));
        rl.setLine(newLine);
        assertEquals(newLine, rl.getLine());
    }

    @Test
    void removeLine_setRing_shouldUpdateRing() {
        RemoveLine rl = new RemoveLine(fiveCoords(), coord(9, 9));
        CoordinateDoubled newRing = coord(3, 7);
        rl.setRing(newRing);
        assertEquals(newRing, rl.getRing());
    }

    @Test
    void removeLine_shouldExtendAction() {
        RemoveLine rl = new RemoveLine();
        assertInstanceOf(Action.class, rl);
    }



    @Test
    void action_moveIsSubtypeOfAction() {
        Action action = new Move(coord(0, 0), coord(2, 2));
        assertInstanceOf(Move.class, action);
    }

    @Test
    void action_removeLineIsSubtypeOfAction() {
        Action action = new RemoveLine();
        assertInstanceOf(RemoveLine.class, action);
    }
}