package iut.gon.model.actions;

import iut.gon.hexagonal_coordinates.Coordinate;

/**
 * 
 */
public class Move extends Action {

    /**
     * 
     */
    private Coordinate from;

    /**
     * 
     */
    private Coordinate to;

    /**
     * @param from 
     * @param to
     */
    public Move(Coordinate from, Coordinate to) {
        this.from = from;
        this.to = to;
    }

    /**
     * @return
     */
    public Coordinate getFrom() {
        
        return from;
    }

    /**
     * @param from 
     * @return
     */
    public void setFrom(Coordinate from) {
        this.from=from;
    }

    /**
     * @return
     */
    public Coordinate getTo() {
        return to;
    }

    /**
     * @param to 
     * @return
     */
    public void setTo(Coordinate to) {
        this.to=to;
    }

}