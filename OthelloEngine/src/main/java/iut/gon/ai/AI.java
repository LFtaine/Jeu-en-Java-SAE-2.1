package iut.gon.ai;

import iut.gon.model.actions.Action;
import iut.gon.model.state.IState;
import iut.gon.hexagonal_coordinates.Coordinate;

public interface AI {
    Action chooseMove(IState state);
    Coordinate chooseRingPlacement(IState state);
}
