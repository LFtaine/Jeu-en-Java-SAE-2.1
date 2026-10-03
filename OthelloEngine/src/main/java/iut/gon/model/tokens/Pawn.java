package iut.gon.model.tokens;

import iut.gon.model.Team;

public class Pawn extends Token {

    /**
     * Default constructor
     */
    public Pawn(Team color) {
        super(color);
    }

    /**
     * Inverse l'équipe du pion
     */
    public void changeTeam() {
        this.team = this.team.other();
    }

    /**
     * Représentation textuelle du pion pour la console
     */
    @Override
    public String charRepr() {
        return this.team == Team.WHITE ? "." : "x";
    }

    
    
}