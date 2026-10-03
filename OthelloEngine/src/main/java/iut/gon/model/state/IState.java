package iut.gon.model.state;

import iut.gon.hexagonal_coordinates.Coordinate;
import iut.gon.model.Team;
import iut.gon.model.actions.Move;
import iut.gon.model.actions.RemoveLine;
import iut.gon.model.tokens.Token;

import java.util.*;

public interface IState {

    /**
     * @param move 
     * @return
     */
    IState move(Move move);

    /**
     * @param removeLine 
     * @return
     */
    IState removeLine(RemoveLine removeLine);


    /**
     * @param from 
     * @return
     */
    Set<Coordinate> availableMoves(Coordinate from);

    /**
     * @return
     */
    List<Set<Coordinate>> lines();

    /**
     * @return
     */
    Team turn();

    /**
     * @param c
     * @return
     */
    IState removeToken(Coordinate c);

    /**
     * @return
     */
    Map<Coordinate, Token> board();

    /**
     * @return
     */
    Map<Team,List<Coordinate>> rings();
    Team winner();
    
    IState toggleToken(Coordinate c, Token t, Team eq);

	boolean isInField(Coordinate c);

    boolean isDraw();
}