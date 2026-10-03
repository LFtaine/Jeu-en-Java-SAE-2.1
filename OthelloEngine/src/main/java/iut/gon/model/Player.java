package iut.gon.model;

public class Player {
    private final String username;
    private final Team team;

    public Player(String username, Team team) {
        this.username = username;
        this.team = team;
    }

    public String getUsername() {
        return username;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "Joueur " + (team.ordinal() + 1) + " (" + username + ")";
    }
}
