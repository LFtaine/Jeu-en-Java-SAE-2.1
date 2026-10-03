package iut.gon.hexagonal_coordinates;

/**
 * Les différentes directions possibles
 */
public enum Direction {
    NO, N, NE, E, SE, S, SO, O;

    public Direction opposite() {
        return switch (this) {
            case N  -> S;
            case S  -> N;
            case NE -> SO;
            case SO -> NE;
            case NO -> SE;
            case SE -> NO;
            case E  -> O;
            case O  -> E;
        };
    }
}