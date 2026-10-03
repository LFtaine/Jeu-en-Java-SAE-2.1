package tests;

import org.junit.jupiter.api.Test;
import iut.gon.model.Team;
import iut.gon.model.tokens.Token;
import iut.gon.model.tokens.Pawn;
import iut.gon.model.tokens.Ring;

import static org.junit.jupiter.api.Assertions.*;

public class TokenTest {

    @Test
    void pawn_shouldHaveCorrectTeamAfterConstruction() {
        Pawn pawn = new Pawn(Team.BLACK);
        assertEquals(Team.BLACK, pawn.getTeam());
    }

    @Test
    void pawn_shouldHaveCorrectTeamWhite() {
        Pawn pawn = new Pawn(Team.WHITE);
        assertEquals(Team.WHITE, pawn.getTeam());
    }

    @Test
    void pawn_changeTeam_shouldSwitchToOppositeTeam() {
        Pawn pawn = new Pawn(Team.BLACK);
        pawn.changeTeam();
        assertEquals(Team.WHITE, pawn.getTeam());
    }

    @Test
    void pawn_changeTeam_shouldSwitchFromWhiteToBlack() {
        Pawn pawn = new Pawn(Team.WHITE);
        pawn.changeTeam();
        assertEquals(Team.BLACK, pawn.getTeam());
    }

    @Test
    void pawn_setTeam_shouldUpdateTeam() {
        Pawn pawn = new Pawn(Team.BLACK);
        pawn.setTeam(Team.WHITE);
        assertEquals(Team.WHITE, pawn.getTeam());
    }

    @Test
    void pawn_charRepr_shouldReturnDotForWhite() {
        Pawn pawn = new Pawn(Team.WHITE);
        String repr = pawn.charRepr();
        assertEquals(".", repr);
    }

    @Test
    void pawn_charRepr_shouldReturnXForBlack() {
        Pawn pawn = new Pawn(Team.BLACK);
        String repr = pawn.charRepr();
        assertEquals("x", repr);
    }

    @Test
    void pawn_clone_shouldReturnDifferentInstance() {
        Pawn pawn = new Pawn(Team.BLACK);
        Token cloned = pawn.clone();
        assertNotSame(pawn, cloned);
    }

    @Test
    void pawn_clone_shouldReturnSameTeam() {
        Pawn pawn = new Pawn(Team.BLACK);
        Token cloned = pawn.clone();
        assertEquals(pawn.getTeam(), cloned.getTeam());
    }

    @Test
    void pawn_clone_shouldBePawn() {
        Pawn pawn = new Pawn(Team.WHITE);
        Token cloned = pawn.clone();
        assertInstanceOf(Pawn.class, cloned);
    }


    @Test
    void ring_shouldHaveCorrectTeamAfterConstruction() {
        Ring ring = new Ring(Team.BLACK);
        assertEquals(Team.BLACK, ring.getTeam());
    }

    @Test
    void ring_shouldHaveCorrectTeamWhite() {
        Ring ring = new Ring(Team.WHITE);
        assertEquals(Team.WHITE, ring.getTeam());
    }

    @Test
    void ring_charRepr_shouldReturnLowercaseOForWhite() {
        Ring ring = new Ring(Team.WHITE);
        String repr = ring.charRepr();
        assertEquals("o", repr);
    }

    @Test
    void ring_charRepr_shouldReturnUppercaseOForBlack() {
        Ring ring = new Ring(Team.BLACK);
        String repr = ring.charRepr();
        assertEquals("O", repr);
    }

    @Test
    void ring_clone_shouldReturnDifferentInstance() {
        Ring ring = new Ring(Team.BLACK);
        Token cloned = ring.clone();
        assertNotSame(ring, cloned);
    }

    @Test
    void ring_clone_shouldReturnSameTeam() {
        Ring ring = new Ring(Team.WHITE);
        Token cloned = ring.clone();
        assertEquals(ring.getTeam(), cloned.getTeam());
    }

    @Test
    void ring_clone_shouldBeRing() {
        Ring ring = new Ring(Team.BLACK);
        Token cloned = ring.clone();
        assertInstanceOf(Ring.class, cloned);
    }

    @Test
    void ring_setTeam_shouldUpdateTeam() {
        Ring ring = new Ring(Team.BLACK);
        ring.setTeam(Team.WHITE);
        assertEquals(Team.WHITE, ring.getTeam());
    }


    @Test
    void team_other_blackShouldReturnWhite() {
        assertEquals(Team.WHITE, Team.BLACK.other());
    }

    @Test
    void team_other_whiteShouldReturnBlack() {
        assertEquals(Team.BLACK, Team.WHITE.other());
    }

    @Test
    void team_getColor_shouldReturnNonNull() {
        assertNotNull(Team.BLACK.getColor());
        assertNotNull(Team.WHITE.getColor());
    }

    @Test
    void team_colorsAreDistinct() {
        assertNotEquals(Team.BLACK.getColor(), Team.WHITE.getColor());
    }
}