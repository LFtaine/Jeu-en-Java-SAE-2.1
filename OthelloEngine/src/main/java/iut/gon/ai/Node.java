package iut.gon.ai;

import iut.gon.model.actions.Action;
import iut.gon.model.state.IState;

public record Node(IState state, Node parent, Action action) {}