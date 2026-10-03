package iut.gon.model.factory;

import iut.gon.model.state.IState;

/**
 * 
 */
public interface IFactory {

    /**
     * @return
     */
    IState testState();

    /**
     * @return
     */
    IState stateForBlackLineTest();

    /**
     * @return
     */
    IState stateForWhiteLineTest();

    /**
     * @return
     */
    IState emptyState();

    /**
     * @return
     */
    IState doubleLineStateTest();
}