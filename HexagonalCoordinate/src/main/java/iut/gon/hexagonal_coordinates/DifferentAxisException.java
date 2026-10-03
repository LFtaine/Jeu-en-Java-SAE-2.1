package iut.gon.hexagonal_coordinates;

import java.util.*;

/**
 * Exception quand deux cases ne sont pas sur le même axe
 */
public class DifferentAxisException extends Exception{

    /**
     * Default constructor
     */
    public DifferentAxisException() {
    }

    /**
     * @param s Le message
     */
    public DifferentAxisException(String s) {
        // TODO implement here
            super(s);
        }

}