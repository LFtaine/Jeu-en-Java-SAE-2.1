package iut.gon.hexagonal_coordinates;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Coordonnées cubiques
 */
public class CoordinateCube extends Coordinate {

    /**
     * axe horizontal
     */
    private final int q;

    /**
     * axe diagonal bas gauche -> haut droite
     */
    private final int r;

    /**
     * axe diagonal haut gauche -> bas droite
     */
    private final int s;

    /**
     * @param q L'axe horizontal
     * @param r L'axe diagonal bas gauche -> haut droite
     * @param s L'axe diagonal haut gauche -> bas droite
     */
    public CoordinateCube(int q, int r, int s) {
        if(q + r + s != 0) throw new IllegalArgumentException("Coordonnées cubiques non valides: q + r + s doit être égal à O (reçu :" + (q+r+s) + ")");
        this.q = q;
        this.r = r;
        this.s = s;
    }

    /**
     * @return L'axe horizontal
     */
    public int getQ() { return q; }

    /**
     * @return L'axe diagonal bas gauche -> haut droite
     */
    public int getR() { return r; }

    /**
     * @return L'axe diagonal haut gauche -> bas droite
     */
    public int getS() { return s; }

    @Override
    public Point to2DCoordinate() {
        int col = 2 * q + r + 9; // x
        int row = r + 5;         // y
        return new Point(col, row);
    }

    @Override
    public Coordinate toDir(Mode mode, Direction direction) {
        int[] delta = switch (mode) {
            case FLAT -> switch (direction) {
                case N  -> new int[]{ 0, -1, +1};
                case S  -> new int[]{ 0, +1, -1};
                case NE -> new int[]{+1, -1,  0};
                case SO -> new int[]{-1, +1,  0};
                case NO -> new int[]{-1,  0, +1};
                case SE -> new int[]{+1,  0, -1};
                default -> throw new InvalidParameterException("Direction invalide pour FLAT : " + direction);
            };
            case POINTY -> switch (direction) {
                case E  -> new int[]{+1,  0, -1};
                case O  -> new int[]{-1,  0, +1};
                case NE -> new int[]{+1, -1,  0};
                case SO -> new int[]{-1, +1,  0};
                case NO -> new int[]{ 0, -1, +1};
                case SE -> new int[]{ 0, +1, -1};
                default -> throw new InvalidParameterException("Direction invalide pour POINTY : " + direction);
            };
        };
        return new CoordinateCube(q + delta[0], r + delta[1], s + delta[2]);
    }

    @Override
    public List<Coordinate> between(Mode mode, Coordinate to) throws DifferentAxisException {
        CoordinateCube other = (CoordinateCube) to;

        if (this.q != other.q && this.r != other.r && this.s != other.s)
            throw new DifferentAxisException("Les coordonnées ne sont pas sur le même axe");

        Direction dir;
        if (this.s == other.s) dir = other.q > this.q ? Direction.NE : Direction.SO;
        else if (this.r == other.r) dir = other.q > this.q ? Direction.SE : Direction.NO;
        else dir = other.r > this.r ? Direction.S  : Direction.N;
        
        List<Coordinate> result = new ArrayList<>();
        CoordinateCube cur = (CoordinateCube) this.toDir(mode, dir);
        while (!cur.equals(other)) {
            result.add(cur);
            cur = (CoordinateCube) cur.toDir(mode, dir);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CoordinateCube c)) return false;
        return this.q == c.q && this.r == c.r && this.s == c.s;
    }

    @Override
    public int hashCode() {
        return 31 * (31 * q + r) + s;
    }

    @Override
    public String toString() { return "[" + q + ", " + r + ", " + s + "]"; }

    @Override
    public List<Coordinate> getNeighbors(Mode mode) {
        Direction[] validDirs = mode == Mode.FLAT
                ? new Direction[]{Direction.N, Direction.NE, Direction.SE, Direction.S, Direction.SO, Direction.NO}
                : new Direction[]{Direction.E, Direction.NE, Direction.SE, Direction.O, Direction.SO, Direction.NO};

        List<Coordinate> neighbors = new ArrayList<>();
        for (Direction dir : validDirs) {
            neighbors.add(toDir(mode, dir));
        }
        return neighbors;
    }

}