package iut.gon.hexagonal_coordinates;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.ArrayList;

import java.util.*;

/**
 * 
 */
public class CoordinateDoubled extends Coordinate {

    /**
     * Chardonnay (ligne)
     */
    private final int y;

    /**
     * Abscissa (colonne)
     */
    private final int x;

    /**
     * @param y Chardonnay
     * @param x Abscissa
     */
    public CoordinateDoubled(int y, int x) {
        // TODO implement here
        this.y = y;
        this.x = x;
    }

    private int toCubeQ() { return (x - y - 4) / 2; }
    private int toCubeR() { return y - 5; }
    private int toCubeS() { return -toCubeQ() - toCubeR(); }

    @Override
    public Point to2DCoordinate() {
        return new Point(x, y);
    }

    @Override
    public Coordinate toDir(Mode mode, Direction direction) {
        if (mode == Mode.POINTY && (direction == Direction.N || direction == Direction.S))
            throw new InvalidParameterException("POINTY n'accepte pas N et S.");
        if (mode == Mode.FLAT && (direction == Direction.E || direction == Direction.O))
            throw new InvalidParameterException("FLAT n'accepte pas E et O.");

        return switch (direction) {
            case N  -> new CoordinateDoubled(y - 2, x);
            case S  -> new CoordinateDoubled(y + 2, x);
            case E  -> new CoordinateDoubled(y,x + 2);
            case O  -> new CoordinateDoubled(y,x - 2);
            case NE -> new CoordinateDoubled(y - 1,x + 1);
            case NO -> new CoordinateDoubled(y - 1,x - 1);
            case SE -> new CoordinateDoubled(y + 1,x + 1);
            case SO -> new CoordinateDoubled(y + 1,x - 1);
        };
    }

    @Override
    public List<Coordinate> between(Mode mode, Coordinate to) throws DifferentAxisException {
        CoordinateDoubled other = (CoordinateDoubled) to;
        int dy = other.y - this.y;
        int dx = other.x - this.x;

        // Vérifie l'axe EN PREMIER =lève l'exception si pas le même axe
        Direction dir = findAxis(mode, dy, dx);

        // seulement si même axe = on marche
        List<Coordinate> result = new ArrayList<>();
        CoordinateDoubled cur = (CoordinateDoubled) this.toDir(mode, dir);
        while (!cur.equals(other)) {
            result.add(cur);
            cur = (CoordinateDoubled) cur.toDir(mode, dir);
        }
        return result;
    }

    private Direction findAxis(Mode mode, int dy, int dx) throws DifferentAxisException {
        if (mode == Mode.FLAT  && dx == 0 && dy != 0) return dy < 0 ? Direction.N  : Direction.S;
        if (mode == Mode.POINTY && dy == 0 && dx != 0) return dx > 0 ? Direction.E  : Direction.O;
        if (dy == -dx && dy != 0) return dy < 0 ? Direction.NE : Direction.SO;
        if (dy ==  dx && dy != 0) return dy < 0 ? Direction.NO : Direction.SE;

        // Aucun axe trouvé = exception
        throw new DifferentAxisException("Coordonnées pas sur le même axe");
    }

    // car sans equals, neighbors.contains(new CoordinateDoubled(5, 11))
    // renvoie tourjours false même si un voisin a les mêmes coordinates.
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CoordinateDoubled c)) return false;
        return this.y == c.y && this.x == c.x;
    }

    @Override
    public int hashCode() {
        return 31 * y + x;
    }

    @Override
    public String toString(){
        return "[" + y + ", " + x + "]";
    }
}