package iut.gon.hexagonal_coordinates;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Représente des coordonnées
 */
public abstract class Coordinate {

    /**
     * @return Un point correspondant aux coordonnées
     */
    public abstract Point to2DCoordinate();

    /**
     * @param mode Le mode d'hexagones actuel
     * @param direction la direction vers laquelle on veut aller
     * @return Les nouvelles coordonnées
     */
    public abstract Coordinate toDir(Mode mode, Direction direction);

    /**
     * @param mode Le mode d'hexagones actuel
     * @return tous les voisins de la coordonnée
     */
    public List<Coordinate> getNeighbors(Mode mode) {
        List<Coordinate> neighbors = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            try {
                neighbors.add(toDir(mode, direction));
            } catch (InvalidParameterException e) {
                // On fait rien on skip juste
            }
        }
        return neighbors;
    }

    /**
     * @param mode Le mode d'hexagones actuel
     * @param to Les coordonnées de la destination
     * @return liste des coordonnées entre le point et la destination
     */
    public abstract List<Coordinate> between(Mode mode, Coordinate to) throws DifferentAxisException;

    /**
     * @param mode Le mode d'hexagones actuel
     * @return Les nouvelles coordonnées
     */
    public Coordinate NO(Mode mode) {
        return toDir(mode, Direction.NO);
    }

    /**
     * @param mode Le mode d'hexagones actuel
     * @return Les nouvelles coordonnées
     */
    public Coordinate NE(Mode mode) {
        return toDir(mode, Direction.NE);
    }

    /**
     * @param mode Le mode d'hexagones actuel
     * @return Les nouvelles coordonnées
     */
    public Coordinate E(Mode mode) {
        return toDir(mode, Direction.E);
    }

    /**
     * @param mode Le mode d'hexagones actuel
     * @return Les nouvelles coordonnées
     */
    public Coordinate O(Mode mode) {
        return toDir(mode, Direction.O);
    }

    /**
     * @param mode Le mode d'hexagones actuel
     * @return Les nouvelles coordonnées
     */
    public Coordinate N(Mode mode) {
        return toDir(mode, Direction.N);
    }

    /**
     * @param mode Le mode d'hexagones actuel
     * @return Les nouvelles coordonnées
     */
    public Coordinate SO(Mode mode) {
        return toDir(mode, Direction.SO);
    }

    public Coordinate S(Mode mode){
        return toDir(mode, Direction.S);
    }

    /**
     * @param mode Le mode d'hexagones actuel
     * @return Les nouvelles coordonnées
     */
    public Coordinate SE(Mode mode) {
        return toDir(mode, Direction.SE);
    }


}