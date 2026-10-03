package tests;

import iut.gon.hexagonal_coordinates.Coordinate;
import iut.gon.model.Model;
import iut.gon.model.Team;
import iut.gon.model.factory.FactoryCube;
import iut.gon.model.state.IState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    private Model model;
    private IState initialState;

    @BeforeEach
    void setUp() {
        initialState = new FactoryCube().testState();
        model = new Model(initialState);
    }

    @Test
    void constructor_shouldStoreInitialState() {
        assertNotNull(model.getCurrentState());
    }

    @Test
    void getCurrentState_shouldReturnInitialState() {
        assertEquals(initialState, model.getCurrentState());
    }

    @Test
    void setCurrentState_shouldUpdateState() {
        IState newState = new FactoryCube().emptyState();
        model.setCurrentState(newState);
        assertEquals(newState, model.getCurrentState());
    }

    @Test
    void getTurn_shouldNotBeNull() {
        assertNotNull(model.getTurn());
    }

    @Test
    void getTurn_shouldMatchCurrentStateTurn() {
        assertEquals(initialState.turn(), model.getTurn());
    }

    @Test
    void getRings_black_shouldReturnFiveRings() {
        assertEquals(5, model.getRings(Team.BLACK).size());
    }

    @Test
    void getRings_white_shouldReturnFiveRings() {
        assertEquals(5, model.getRings(Team.WHITE).size());
    }

    @Test
    void getRings_shouldNotBeNull() {
        assertNotNull(model.getRings(Team.BLACK));
        assertNotNull(model.getRings(Team.WHITE));
    }

    @Test
    void getPawns_shouldNotBeNull() {
        assertNotNull(model.getPawn(Team.BLACK));
        assertNotNull(model.getPawn(Team.WHITE));
    }

    @Test
    void movesFrom_validRingCoordinate_shouldReturnNonNull() {
        Coordinate ringCoord = model.getRings(model.getTurn()).get(0);
        assertNotNull(model.movesFrom(ringCoord));
    }

    @Test
    void movesFrom_shouldReturnSetOfCoordinates() {
        Coordinate ringCoord = model.getRings(model.getTurn()).get(0);
        Set<Coordinate> moves = model.movesFrom(ringCoord);
        assertNotNull(moves);
    }

    @Test
    void moveRing_validMove_shouldUpdateState() {
        Coordinate ringCoord = model.getRings(model.getTurn()).get(0);
        Set<Coordinate> moves = model.movesFrom(ringCoord);
        if (!moves.isEmpty()) {
            Coordinate dest = moves.iterator().next();
            IState before = model.getCurrentState();
            model.moveRing(ringCoord, dest);
            assertNotEquals(before, model.getCurrentState());
        } else {
            assertTrue(true, "Aucun mouvement disponible");
        }
    }

    @Test
    void moveRing_shouldNotThrowOnValidMove() {
        Coordinate ringCoord = model.getRings(model.getTurn()).get(0);
        Set<Coordinate> moves = model.movesFrom(ringCoord);
        if (!moves.isEmpty()) {
            Coordinate dest = moves.iterator().next();
            assertDoesNotThrow(() -> model.moveRing(ringCoord, dest));
        }
    }

    @Test
    void getPawnsLines_initialState_shouldReturnNonNull() {
        assertNotNull(model.getPawnsLines());
    }

    @Test
    void removeToken_shouldNotThrowOnValidCoordinate() {
        Coordinate ringCoord = model.getRings(model.getTurn()).get(0);
        assertDoesNotThrow(() -> model.removeToken(ringCoord));
    }

    @Test
    void removeToken_shouldUpdateState() {
        Coordinate ringCoord = model.getRings(model.getTurn()).get(0);
        IState before = model.getCurrentState();
        model.removeToken(ringCoord);
        assertNotEquals(before, model.getCurrentState());
    }

    @Test
    void removeLine_whenNoLine_shouldThrowRuntimeException() {
        Coordinate ring = model.getRings(model.getTurn()).get(0);
        Set<Coordinate> fakeLine = new HashSet<>();
        assertThrows(RuntimeException.class, () -> model.removeLine(fakeLine, ring));
    }
}