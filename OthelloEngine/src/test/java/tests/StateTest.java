package tests;

import iut.gon.hexagonal_coordinates.Coordinate;
import iut.gon.model.Team;
import iut.gon.model.actions.Move;
import iut.gon.model.actions.RemoveLine;
import iut.gon.model.factory.FactoryCube;
import iut.gon.model.state.IState;
import iut.gon.model.tokens.Pawn;
import iut.gon.model.tokens.Ring;
import iut.gon.model.tokens.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    private IState testState;
    private IState whiteLineState;
    private IState blackLineState;
    private IState doubleLineState;
    private IState emptyState;

    @BeforeEach
    void setUp() {
        FactoryCube factory = new FactoryCube();
        testState       = factory.testState();
        whiteLineState  = factory.stateForWhiteLineTest();
        blackLineState  = factory.stateForBlackLineTest();
        doubleLineState = factory.doubleLineStateTest();
        emptyState      = factory.emptyState();
    }


    @Test
    void board_shouldNotBeNull() {
        assertNotNull(testState.board());
    }

    @Test
    void board_shouldContainAllTerrainCoordinates() {
        assertFalse(testState.board().isEmpty());
    }

    @Test
    void emptyBoard_shouldHaveNullTokensEverywhere() {
        emptyState.board().values().forEach(token -> assertNull(token));
    }


    @Test
    void rings_shouldReturnRingsForBlack() {
        List<Coordinate> rings = testState.rings().get(Team.BLACK);
        assertNotNull(rings);
    }

    @Test
    void rings_shouldReturnRingsForWhite() {
        List<Coordinate> rings = testState.rings().get(Team.WHITE);
        assertNotNull(rings);
    }

    @Test
    void rings_initialStateShouldHaveFiveRingsPerTeam() {
        assertEquals(5, testState.rings().get(Team.BLACK).size());
        assertEquals(5, testState.rings().get(Team.WHITE).size());
    }

    @Test
    void turn_shouldNotBeNull() {
        assertNotNull(testState.turn());
    }

    @Test
    void turn_shouldBeBlackOrWhite() {
        Team t = testState.turn();
        assertTrue(t == Team.BLACK || t == Team.WHITE);
    }


    @Test
    void winner_shouldReturnNullWhenNoWinner() {
        assertNull(testState.winner());
    }

    @Test
    void lines_emptyState_shouldReturnEmptyList() {
        assertTrue(emptyState.lines().isEmpty());
    }

    @Test
    void lines_whiteLineState_shouldDetectOneLine() {
        assertFalse(whiteLineState.lines().isEmpty());
    }

    @Test
    void lines_whiteLineState_eachLineShouldHaveFiveCoordinates() {
        for (Set<Coordinate> line : whiteLineState.lines()) {
            assertEquals(5, line.size());
        }
    }

    @Test
    void lines_blackLineState_shouldDetectOneLine() {
        assertFalse(blackLineState.lines().isEmpty());
    }

    @Test
    void lines_doubleLineState_shouldDetectTwoOrMoreLines() {
        assertTrue(doubleLineState.lines().size() >= 2);
    }

 
    @Test
    void availableMoves_shouldNotBeNull() {
        Coordinate ringCoord = testState.rings().get(Team.BLACK).get(0);
        assertNotNull(testState.availableMoves(ringCoord));
    }

    @Test
    void getTokenAt_emptyCell_shouldReturnNull() {
        Optional<Coordinate> emptyCoord = testState.board().entrySet().stream()
                .filter(e -> e.getValue() == null)
                .map(Map.Entry::getKey)
                .findFirst();
        assertTrue(emptyCoord.isPresent(), "testState should have at least one empty cell");
        assertNull(testState.board().get(emptyCoord.get()));
    }

    @Test
    void getTokenAt_ringCell_shouldReturnRing() {
        Coordinate ringCoord = testState.rings().get(Team.BLACK).get(0);
        Token t = testState.board().get(ringCoord);
        assertInstanceOf(Ring.class, t);
    }


    @Test
    void move_withValidRingAndDestination_shouldReturnNewState() {
        Coordinate ringCoord = testState.rings().get(Team.BLACK).get(0);
        Set<Coordinate> moves = testState.availableMoves(ringCoord);
        assertFalse(moves.isEmpty(), "BLACK ring should have at least one available move");
        Coordinate dest = moves.iterator().next();
        IState newState = testState.move(new Move(ringCoord, dest));
        assertNotNull(newState);
        assertNotSame(testState, newState);
    }

    @Test
    void move_withWrongTeamRing_shouldThrowIllegalArgumentException() {
        assertEquals(Team.BLACK, testState.turn(),
                "testState turn should be BLACK (as set up by FactoryCube)");

        Coordinate whiteRingCoord = testState.rings().get(Team.WHITE).get(0);
        Set<Coordinate> moves = testState.availableMoves(whiteRingCoord);
        assertFalse(moves.isEmpty(), "WHITE ring should have at least one reachable cell");

        Coordinate dest = moves.iterator().next();
        assertThrows(IllegalArgumentException.class,
                () -> testState.move(new Move(whiteRingCoord, dest)));
    }

    @Test
    void move_whenLineExists_shouldThrowRuntimeException() {
        Team currentTeam = whiteLineState.turn();
        Coordinate ringCoord = whiteLineState.rings().get(currentTeam).get(0);
        Set<Coordinate> moves = whiteLineState.availableMoves(ringCoord);
        assertFalse(moves.isEmpty(), "Current team ring should have at least one available move");

        Coordinate dest = moves.iterator().next();
        
        assertThrows(RuntimeException.class,
                () -> whiteLineState.move(new Move(ringCoord, dest)));
    }

    @Test
    void move_shouldPlacePawnAtOrigin() {
        Coordinate ringCoord = testState.rings().get(Team.BLACK).get(0);
        Set<Coordinate> moves = testState.availableMoves(ringCoord);
        assertFalse(moves.isEmpty(), "BLACK ring should have at least one available move");

        Coordinate dest = moves.iterator().next();
        IState newState = testState.move(new Move(ringCoord, dest));
        Token tokenAtOrigin = newState.board().get(ringCoord);
        assertInstanceOf(Pawn.class, tokenAtOrigin);
    }

    @Test
    void move_shouldBeImmutable_originalStateUnchanged() {
        Coordinate ringCoord = testState.rings().get(Team.BLACK).get(0);
        Set<Coordinate> moves = testState.availableMoves(ringCoord);
        assertFalse(moves.isEmpty(), "BLACK ring should have at least one available move");

        Map<Coordinate, Token> boardBefore = new HashMap<>(testState.board());
        Coordinate dest = moves.iterator().next();
        testState.move(new Move(ringCoord, dest));
        assertEquals(boardBefore, testState.board());
    }


    @Test
    void removeLine_whenNoLineExists_shouldThrowRuntimeException() {
        assertTrue(testState.lines().isEmpty(),
                "testState should have no lines to ensure removeLine throws");
        Set<Coordinate> fakeLine = new HashSet<>();
        assertThrows(RuntimeException.class, () -> testState.removeLine(
                new RemoveLine(fakeLine, testState.rings().get(testState.turn()).get(0))
        ));
    }

    @Test
    void removeLine_withValidLine_shouldRemoveRing() {
        assertFalse(whiteLineState.lines().isEmpty(), "whiteLineState should have at least one line");

        Set<Coordinate> line = whiteLineState.lines().get(0);

        Team currentTeam = whiteLineState.turn();
        List<Coordinate> teamRings = whiteLineState.rings().get(currentTeam);
        assertFalse(teamRings.isEmpty(), "Current team should have at least one ring");
        Coordinate ring = teamRings.get(0);

        int ringsBefore = whiteLineState.rings().get(currentTeam).size();
        IState newState = whiteLineState.removeLine(new RemoveLine(line, ring));
        int ringsAfter = newState.rings().get(currentTeam).size();

        assertEquals(ringsBefore - 1, ringsAfter);
    }

    @Test
    void removeLine_withInvalidLine_shouldThrowRuntimeException() {
        assertFalse(whiteLineState.lines().isEmpty(), "whiteLineState should have at least one line");

        Set<Coordinate> badLine = new HashSet<>();
        assertThrows(RuntimeException.class, () ->
                whiteLineState.removeLine(new RemoveLine(badLine,
                        whiteLineState.rings().get(whiteLineState.turn()).get(0)))
        );
    }

    @Test
    void state_equalsSameContent_shouldBeEqual() {
        FactoryCube f = new FactoryCube();
        IState s1 = f.testState();
        IState s2 = f.testState();
        assertEquals(s1, s2);
    }

    @Test
    void state_hashCode_sameContentSameHash() {
        FactoryCube f = new FactoryCube();
        IState s1 = f.testState();
        IState s2 = f.testState();
        assertEquals(s1.hashCode(), s2.hashCode());
    }
}