package iut.gon.hexagonal_coordinates;

/**
 * Représente un point avec une abcisse et une ordonnée
 * @param x L'abcisse (colonne)
 * @param y L'ordonnée (ligne)
 */
public record Point(int x, int y) {
    /**
     * @return Une représentation du point en String
     */
    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}