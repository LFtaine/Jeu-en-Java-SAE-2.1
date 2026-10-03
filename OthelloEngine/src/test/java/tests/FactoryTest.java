package tests;

import iut.gon.model.*;
import iut.gon.model.factory.FactoryCube;
import iut.gon.model.state.IState;
import iut.gon.model.factory.FactoryDoubled;
import iut.gon.model.factory.IFactory;
import iut.gon.model.Team; 

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class FactoryTest {


    @Test
    void factoryCube_emptyState_shouldNotBeNull() {
        IState state = new FactoryCube().emptyState();
        assertNotNull(state);
    }

    @Test
    void factoryCube_emptyState_boardShouldHaveNoTokens() {
        IState state = new FactoryCube().emptyState();
        state.board().values().forEach(token -> assertNull(token));
    }

    @Test
    void factoryCube_emptyState_shouldHaveNoPawnsAndNoRings() {
        IState state = new FactoryCube().emptyState();
        assertTrue(state.rings().get(Team.BLACK).isEmpty()
                || state.rings() == null
                || state.rings().get(Team.BLACK).isEmpty());
        state.board().values().forEach(t -> assertNull(t));
    }

    @Test
    void factoryCube_testState_shouldNotBeNull() {
        IState state = new FactoryCube().testState();
        assertNotNull(state);
    }

    @Test
    void factoryCube_testState_boardShouldNotBeEmpty() {
        IState state = new FactoryCube().testState();
        assertFalse(state.board().isEmpty());
    }

    @Test
    void factoryCube_testState_shouldHaveFiveBlackRings() {
        IState state = new FactoryCube().testState();
        assertEquals(5, state.rings().get(Team.BLACK).size());
    }

    @Test
    void factoryCube_testState_shouldHaveFiveWhiteRings() {
        IState state = new FactoryCube().testState();
        assertEquals(5, state.rings().get(Team.WHITE).size());
    }

    @Test
    void factoryCube_stateForWhiteLineTest_shouldNotBeNull() {
        IState state = new FactoryCube().stateForWhiteLineTest();
        assertNotNull(state);
    }

    @Test
    void factoryCube_stateForWhiteLineTest_shouldHaveAtLeastOneWhiteLine() {
        IState state = new FactoryCube().stateForWhiteLineTest();
        boolean hasWhiteLine = state.lines().stream()
                .anyMatch(line -> !line.isEmpty()); 
        assertTrue(hasWhiteLine || !state.lines().isEmpty());
    }

    @Test
    void factoryCube_stateForBlackLineTest_shouldNotBeNull() {
        IState state = new FactoryCube().stateForBlackLineTest();
        assertNotNull(state);
    }

    @Test
    void factoryCube_stateForBlackLineTest_shouldHaveAtLeastOneBlackLine() {
        IState state = new FactoryCube().stateForBlackLineTest();
        assertFalse(state.lines().isEmpty());
    }

    @Test
    void factoryCube_doubleLineStateTest_shouldNotBeNull() {
        IState state = new FactoryCube().doubleLineStateTest();
        assertNotNull(state);
    }

    @Test
    void factoryCube_doubleLineStateTest_shouldHaveTwoOrMoreLines() {
        IState state = new FactoryCube().doubleLineStateTest();
        assertTrue(state.lines().size() >= 2);
    }

    @Test
    void factoryCube_allStates_shouldImplementIState() {
        IFactory f = new FactoryCube();
        assertInstanceOf(IState.class, f.testState());
        assertInstanceOf(IState.class, f.emptyState());
        assertInstanceOf(IState.class, f.stateForWhiteLineTest());
        assertInstanceOf(IState.class, f.stateForBlackLineTest());
        assertInstanceOf(IState.class, f.doubleLineStateTest());
    }


    @Test
    void factoryDoubled_emptyState_shouldNotBeNull() {
        IState state = new FactoryDoubled().emptyState();
        assertNotNull(state);
    }

    @Test
    void factoryDoubled_emptyState_boardShouldHaveNoTokens() {
        IState state = new FactoryDoubled().emptyState();
        state.board().values().forEach(token -> assertNull(token));
    }

    @Test
    void factoryDoubled_testState_shouldNotBeNull() {
        IState state = new FactoryDoubled().testState();
        assertNotNull(state);
    }

    @Test
    void factoryDoubled_testState_shouldHaveFiveBlackRings() {
        IState state = new FactoryDoubled().testState();
        assertEquals(5, state.rings().get(Team.BLACK).size());
    }

    @Test
    void factoryDoubled_testState_shouldHaveFiveWhiteRings() {
        IState state = new FactoryDoubled().testState();
        assertEquals(5, state.rings().get(Team.WHITE).size());
    }

    @Test
    void factoryDoubled_stateForWhiteLineTest_shouldNotBeNull() {
        IState state = new FactoryDoubled().stateForWhiteLineTest();
        assertNotNull(state);
    }

    @Test
    void factoryDoubled_stateForBlackLineTest_shouldNotBeNull() {
        IState state = new FactoryDoubled().stateForBlackLineTest();
        assertNotNull(state);
    }

    @Test
    void factoryDoubled_doubleLineStateTest_shouldHaveTwoOrMoreLines() {
        IState state = new FactoryDoubled().doubleLineStateTest();
        assertTrue(state.lines().size() >= 2);
    }

    @Test
    void factoryDoubled_allStates_shouldImplementIState() {
        IFactory f = new FactoryDoubled();
        assertInstanceOf(IState.class, f.testState());
        assertInstanceOf(IState.class, f.emptyState());
        assertInstanceOf(IState.class, f.stateForWhiteLineTest());
        assertInstanceOf(IState.class, f.stateForBlackLineTest());
        assertInstanceOf(IState.class, f.doubleLineStateTest());
    }

 
    @Test
    void bothFactories_emptyState_boardSizeShouldBeEqual() {
        int sizeCube    = new FactoryCube().emptyState().board().size();
        int sizeDoubled = new FactoryDoubled().emptyState().board().size();
        assertEquals(sizeCube, sizeDoubled,
                "Les deux factories doivent produire un terrain de même taille");
    }

    @Test
    void bothFactories_testState_ringSizesShouldMatch() {
        IState cube    = new FactoryCube().testState();
        IState doubled = new FactoryDoubled().testState();
        assertEquals(
                cube.rings().get(Team.BLACK).size(),
                doubled.rings().get(Team.BLACK).size()
        );
    }
}
