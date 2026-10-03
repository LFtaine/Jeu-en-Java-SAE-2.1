package iut.gon.model.actions;

import iut.gon.hexagonal_coordinates.Coordinate;
import java.util.*;

public class RemoveLine extends Action {

    private Set<Coordinate> line;
    private Coordinate ring;

    /**
     * Constructeur par défaut
     */
    public RemoveLine() {
    }

    /**
     * Constructeur paramétré
     * @param line  
     * @param ring  
     */
    public RemoveLine(Set<Coordinate> line, Coordinate ring) {
        this.line = line;
        this.ring = ring;
    }

    public Set<Coordinate> getLine() {
        return line;
    }

    public void setLine(Set<Coordinate> line) {
        this.line = line;
    }

    public Coordinate getRing() {
        return ring;
    }

    public void setRing(Coordinate ring) {
        this.ring = ring;
    }
}