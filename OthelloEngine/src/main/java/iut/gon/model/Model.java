package iut.gon.model;

import iut.gon.hexagonal_coordinates.Coordinate;
import iut.gon.model.state.IState;
import iut.gon.model.tokens.Token;
import iut.gon.model.tokens.Ring;
import iut.gon.model.tokens.Pawn;
import iut.gon.model.actions.Move;
import iut.gon.model.actions.RemoveLine;

import java.util.*;

public class Model {

    private IState currentState;

    /**
     * @param state L'état du jeu
     */
    public Model(IState state) {
        this.currentState = state;
    }

    /**
     * @param state  L'état du jeu
     */
    public void setCurrentState(IState state) {
        this.currentState = state;
    }

    /**
     * @param from Les coordonnées pour lesquelles on recherche les mouvements
     * @return Les mouvements possibles
     */
    public Set<Coordinate> movesFrom(Coordinate from) {
        if (currentState == null) return new HashSet<>();
        return currentState.availableMoves(from);
    }

    /**
     * @param from Source
     * @param to Destination
     */
    public void moveRing(Coordinate from, Coordinate to) {
        if (currentState != null) {
            currentState = currentState.move(new Move(from, to));
        }
    }

    public void removeToken(Coordinate c) {
        if (currentState != null) {
            currentState = currentState.removeToken(c);
        }
    }
    
    /**
     * @return
     */
    public List<Set<Coordinate>> getPawnsLines() {
        if (currentState == null) return new ArrayList<>();
        return currentState.lines();
    }

    /**
     * @param line 
     * @param ring 
     */
    public void removeLine(Set<Coordinate> line, Coordinate ring) {
        if (currentState != null) {
            currentState = currentState.removeLine(new RemoveLine(line, ring));
        }
    }

//    /**
//     * @return
//     */
//    public Map<Coordinate, Token> getBoard() {
//        if (currentState == null) return new HashMap<>();
//        return currentState.board();
//    }

    /**
     * @param c 
     * @return
     */
    public Token getTokenAt(Coordinate c) {
        if (currentState == null || currentState.board() == null) return null;
        return currentState.board().get(c);
    }

    /**
     * @param c 
     * @return
     */
    public boolean isInField(Coordinate c) {
        if (currentState == null) return false;
        return currentState.isInField(c);
    }

    /**
     * @param team 
     * @return
     */
    public List<Coordinate> getRings(Team team) {
        List<Coordinate> rings = new ArrayList<>();
        if (currentState == null || currentState.board() == null) return rings;
        
        for (Map.Entry<Coordinate, Token> entry : currentState.board().entrySet()) {
            Token t = entry.getValue();
            if (t instanceof Ring && t.getTeam() == team) {
                rings.add(entry.getKey());
            }
        }
        return rings;
    }

    /**
     * @param team 
     * @return
     */
    public List<Coordinate> getPawn(Team team) {
        List<Coordinate> pawns = new ArrayList<>();
        if (currentState == null || currentState.board() == null) return pawns;
        
        for (Map.Entry<Coordinate, Token> entry : currentState.board().entrySet()) {
            Token t = entry.getValue();
            if (t instanceof Pawn && t.getTeam() == team) {
                pawns.add(entry.getKey());
            }
        }
        return pawns;
    }

    /**
     * @return
     */
    public Team getTurn() {
        if (currentState == null) return null;
        return currentState.turn();
    }

    /**
     * @return
     */
    public IState getCurrentState() {
        return this.currentState;
    }

}