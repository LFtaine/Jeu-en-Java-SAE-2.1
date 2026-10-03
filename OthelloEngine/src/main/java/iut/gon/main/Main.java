package iut.gon.main;

import iut.gon.ai.AI;
import iut.gon.ai.MinimaxAI;
import iut.gon.model.Model;
import iut.gon.model.Player;
import iut.gon.model.Team;
import iut.gon.model.actions.Action;
import iut.gon.model.actions.Move;
import iut.gon.model.actions.RemoveLine;
import iut.gon.model.factory.FactoryDoubled;
import iut.gon.ui.UI;

/**
 * Classe principale
 */
public class Main {

    /**
     * Premier joueur de la partie.
     */
    public static Player j1;
    /**
     * Second joueur de la partie (humain ou IA).
     */
    public static Player j2;

    /**
     * Initialise les joueurs en demandant le mode de jeu et les noms d'utilisateur.
     */
    private static void initPlayers() {
        boolean isPvE = UI.chooseMode();

        System.out.println("Joueur 1:");
        j1 = new Player(UI.chooseUsername(), Team.WHITE);
        if (!isPvE) System.out.println("Joueur 2:");
        j2 = isPvE ? new MinimaxAI(3) : new Player(UI.chooseUsername(), Team.BLACK);
    }

    /**
     * Gère la phase initiale de placement des anneaux par les joueurs.
     * @param model Le modèle du jeu.
     */
    private static void initRings(Model model) {
        for (int i = 1; i <= 5; i++) {
            for (Player p : new Player[]{j1, j2}) {
                UI.displayBoard(model.getCurrentState());
                UI.placeRing(model, p, i);
            }
        }
    }

    /**
     * Point d'entrée principal du programme.
     * @param args Arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        // Initialiser le jeu et les joueurs
        Model model = new Model(new FactoryDoubled().emptyState());

        initPlayers();
        initRings(model);

        boucleDeJeu(model, j1, j2);
    }

    /**
     * Boucle principale du jeu qui gère l'alternance des tours.
     * @param model Le modèle du jeu.
     * @param j1 Le premier joueur.
     * @param j2 Le second joueur.
     */
    private static void boucleDeJeu(Model model, Player j1, Player j2) {
        while (model.getCurrentState().winner() == null && !model.getCurrentState().isDraw()) {
            UI.displayBoard(model.getCurrentState());
            Team currentTeam = model.getTurn();
            Player currentPlayer = (j1.getTeam() == currentTeam) ? j1 : j2;

            if (currentPlayer instanceof AI ai) {
                System.out.println("L'IA réfléchit...");
                Action action = ai.chooseMove(model.getCurrentState());
                if (action instanceof Move move) {
                    model.moveRing(move.getFrom(), move.getTo());
                } else if (action instanceof RemoveLine removeLine) {
                    model.removeLine(removeLine.getLine(), removeLine.getRing());
                }
            } else {
                if (!model.getPawnsLines().isEmpty()) {
                    UI.removeLine(model);
                } else {
                    UI.playRound(model);
                }
            }
        }

        UI.displayBoard(model.getCurrentState());
        if (model.getCurrentState().isDraw()) {
            System.out.println("Match nul !");
        } else {
            System.out.println("Victoire de " + (model.getCurrentState().winner() == Team.WHITE ? j1 : j2) + " !");
        }
    }
}
