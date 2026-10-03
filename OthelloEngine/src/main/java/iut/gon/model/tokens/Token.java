package iut.gon.model.tokens;

import iut.gon.model.Team;

public abstract class Token implements Cloneable {

    /**
     * Default constructor
     */
    public Token(Team team) {
        this.team = team;
    }

    protected Team team;

    /**
     * @return
     */
    public Team getTeam() {
        return this.team;
    }

    /**
     * @param team 
     * @return
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * @return
     */
    public abstract String charRepr();

    /**
     * @return
     */
    @Override
    public Token clone() {
        try {
            return (Token) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Erreur lors du clonage du Token", e);
        }
    }

}