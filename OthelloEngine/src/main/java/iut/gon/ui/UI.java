package iut.gon.ui;

import iut.gon.ai.AI;
import iut.gon.hexagonal_coordinates.Coordinate;
import iut.gon.hexagonal_coordinates.CoordinateDoubled;
import iut.gon.hexagonal_coordinates.Point;
import iut.gon.model.Model;
import iut.gon.model.Player;
import iut.gon.model.Team;
import iut.gon.model.state.IState;
import iut.gon.model.tokens.Pawn;
import iut.gon.model.tokens.Ring;
import iut.gon.model.tokens.Token;

import java.util.*;

/**
 * Bibliothèque de méthodes statiques pour l'affichage
 */
public class UI {

    private static final Scanner sc = new Scanner(System.in);

    public static boolean chooseMode() {
        while (true) {
            System.out.print("Voulez-vous jouer contre le bot ? [Y/n] ");
            String input = sc.next().trim();
            sc.nextLine();

            if (input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("O")) {
                return true;
            } else if (input.equalsIgnoreCase("N")) {
                return false;
            } else {
                System.out.println("Veuillez entrer Y, O ou N.");
            }
        }
    }

    public static String chooseUsername() {
        System.out.print("Veuillez entrer votre nom d'utilisateur : ");

        String username = sc.nextLine().trim();

        while (username.isEmpty()) {
            System.out.print("Le nom d'utilisateur ne peut pas être vide. Veuillez réessayer : ");
            username = sc.nextLine().trim();
        }

        return username;
    }

    public static void displayBoard(IState state) {
        String[][] grille = new String[11][19];
        for (String[] row : grille) Arrays.fill(row, " ");

        for (Map.Entry<Coordinate, Token> entry : state.board().entrySet()) {
            Point p = entry.getKey().to2DCoordinate();
            String symbole = getString(entry);

            if (p.y() >= 0 && p.y() < 11 && p.x() >= 0 && p.x() < 19) {
                grille[p.y()][p.x()] = symbole;
            }
        }

        // Dizaines (vide pour 0-9)
        System.out.print("   ");
        for (int x = 0; x < 19; x++)
            System.out.printf("%2s", x >= 10 ? x / 10 : " ");
        System.out.println();

        // Unités
        System.out.print("   ");
        for (int x = 0; x < 19; x++)
            System.out.printf("%2d", x % 10);
        System.out.println();

        // Séparateur
        System.out.print("   ");
        System.out.println("--".repeat(19));

        // Lignes
        for (int y = 0; y < 11; y++) {
            System.out.printf("%2d|", y);
            for (int x = 0; x < 19; x++)
                System.out.printf("%2s", grille[y][x]);
            System.out.println();
        }
    }

    private static String getString(Map.Entry<Coordinate, Token> entry) {
        Token token = entry.getValue();

        String symbole;
        if (token == null) {
            symbole = "_";
        } else if (token instanceof Ring && token.getTeam() == Team.WHITE) {
            symbole = "o";
        } else if (token instanceof Ring && token.getTeam() == Team.BLACK) {
            symbole = "O";
        } else if (token instanceof Pawn && token.getTeam() == Team.WHITE) {
            symbole = ".";
        } else {
            symbole = "x";
        }
        return symbole;
    }

    public static CoordinateDoubled getCoordinate(String message) {
        System.out.println(message);
        while (true) {
            try {
                System.out.print("Ligne : ");
                int y = sc.nextInt();
                System.out.print("Colonne : ");
                int x = sc.nextInt();
                sc.nextLine();
                return new CoordinateDoubled(y, x);
            } catch (InputMismatchException e) {
                System.out.println("Entrée invalide. Veuillez entrer des nombres entiers.");
                sc.nextLine();
            }
        }
    }

    public static void placeRing(Model model, Player player, int ringNumber) {
        System.out.println(player.getUsername() + " place son anneau " + ringNumber + "/5");
        Coordinate coord;

        if (player instanceof AI ai) {
            coord = ai.chooseRingPlacement(model.getCurrentState());
            if (coord != null) {
                model.setCurrentState(model.getCurrentState().toggleToken(coord, new Ring(player.getTeam()), player.getTeam()));
            }
        } else {
            while (true) {
                coord = getCoordinate("");
                if (!model.isInField(coord)) {
                    System.out.println("Coordonnée en dehors du plateau");
                    continue;
                }
                if (model.getTokenAt(coord) != null) {
                    System.out.println("Case non libre");
                    continue;
                }

                model.setCurrentState(model.getCurrentState().toggleToken(coord, new Ring(player.getTeam()), player.getTeam()));
                break;
            }
        }
        System.out.println(player.getUsername() + " a placé son anneau en " + coord);
    }

    public static void playRound(Model model) {
        System.out.println("Tour de " + model.getTurn());
        while (true) {
            CoordinateDoubled from = getCoordinate("Anneau à déplacer :");
            CoordinateDoubled to = getCoordinate("Destination :");

            try {
                model.moveRing(from, to);
                break;
            } catch (Exception e) {
                System.out.println("Coup invalide : " + e.getMessage() + ". Réessaie.");
            }
        }
    }

    public static void removeLine(Model model) {
        System.out.println("Une ligne a été créée ! Tu dois retirer une ligne et un anneau.");

        List<Set<Coordinate>> lignes = model.getCurrentState().lines();
        for (int i = 0; i < lignes.size(); i++) {
            System.out.println(i + " → " + lignes.get(i));
        }

        int indexLigne = -1;
        while (indexLigne < 0 || indexLigne >= lignes.size()) {
            System.out.print("Quelle ligne retirer ? (numéro) : ");
            try {
                indexLigne = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Veuillez entrer un nombre valide.");
                sc.nextLine();
            }
        }

        while (true) {
            CoordinateDoubled ring = getCoordinate("Anneau à retirer :");
            try {
                Set<Coordinate> line = lignes.get(indexLigne);
                model.removeLine(line, ring);
                break;
            } catch (Exception e) {
                System.out.println("Suppression invalide : " + e.getMessage() + ". Réessaie.");
            }
        }
    }
}
