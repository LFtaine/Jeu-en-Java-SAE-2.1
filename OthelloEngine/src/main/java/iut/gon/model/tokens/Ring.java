package iut.gon.model.tokens;

import iut.gon.model.Team;

public class Ring extends Token {

    /**
     * Default constructor
     */
    public Ring(Team color) {
        super(color);
    }

    /**
     * Représentation textuelle de l'anneau pour la console
     */
    @Override
    public String charRepr() {
        return this.team == Team.WHITE ? "o" : "O";
    }

}