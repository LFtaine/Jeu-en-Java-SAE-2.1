package iut.gon.model.state;
import iut.gon.hexagonal_coordinates.Coordinate;
import iut.gon.hexagonal_coordinates.Direction;

import iut.gon.hexagonal_coordinates.Mode;
import iut.gon.model.Team;
import iut.gon.model.actions.Move;
import iut.gon.model.actions.RemoveLine;
import iut.gon.model.tokens.Pawn;
import iut.gon.model.tokens.Ring;
import iut.gon.model.tokens.Token;

import java.util.*;

/**
 * 
 */
public class State implements IState {

    /**
     * Default constructor
     */
    public State(Map<Coordinate, Token> board, Team turn, List<Set<Coordinate>> lines) {
        this.board = board;
        this.turn = turn;
        this.lines = lines;
    }

    private final Map<Coordinate,Token> board;
    /**
     * 
     */
    private final Team turn;
    

    /**
     * 
     */
    public List<Set<Coordinate>> lines;

    /**
     * @param c 
     * @return
     */
    public boolean isInField(Coordinate c) {
        return board != null && board.containsKey(c);
    }

    @Override
    public boolean isDraw() {
        if (!lines.isEmpty()) return false;
        Team current = turn();
        for (Map.Entry<Coordinate, Token> entry : board.entrySet()) {
            if (entry.getValue() instanceof Ring r && r.getTeam() == current) {
                if (!availableMoves(entry.getKey()).isEmpty()) return false;
            }
        }
        return true;
    }

    /**
     * @return
     */
    public Team winner() {
        long blackRings = board().values().stream().filter(t -> t instanceof Ring && t.getTeam() == Team.BLACK).count();
        long whiteRings = board().values().stream().filter(t -> t instanceof Ring && t.getTeam() == Team.WHITE).count();
        if (blackRings <= 2) return Team.BLACK;
        if (whiteRings <= 2) return Team.WHITE;
        return null;
    }


    private static boolean tokensEqual(Token a, Token b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.getClass().equals(b.getClass()) && a.getTeam() == b.getTeam();
    }

    /**
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State other)) return false;
        if (turn != other.turn) return false;
        if (board == null && other.board == null) return true;
        if (board == null || other.board == null) return false;
        if (!board.keySet().equals(other.board.keySet())) return false;
        for (Coordinate c : board.keySet()) {
            if (!tokensEqual(board.get(c), other.board.get(c))) return false;
        }
        return true;
    }

    /**
     * @return
     */
    public int hashCode() {
        if (board == null) return java.util.Objects.hash(0, turn);
        long blackRings = board.values().stream().filter(t -> t instanceof Ring && t.getTeam() == Team.BLACK).count();
        long whiteRings = board.values().stream().filter(t -> t instanceof Ring && t.getTeam() == Team.WHITE).count();
        long blackPawns = board.values().stream().filter(t -> t instanceof Pawn && t.getTeam() == Team.BLACK).count();
        long whitePawns = board.values().stream().filter(t -> t instanceof Pawn && t.getTeam() == Team.WHITE).count();
        return java.util.Objects.hash(board.size(), blackRings, whiteRings, blackPawns, whitePawns, turn);
    }

    /**
     * @param move 
     * @return
     */
    
    @Override
    public IState move(Move move) {

        if (!board().containsKey(move.getFrom()) || !board().containsKey(move.getTo())) {
            throw new IndexOutOfBoundsException("Coordonnée hors du plateau");
        }

        if (!lines().isEmpty()) {
            throw new RuntimeException("Une ligne doit être supprimée d'abord");
        }

        Token tokenFrom = board().get(move.getFrom());
        if (!(tokenFrom instanceof Ring)
                || !tokenFrom.getTeam().equals(turn())
                || !availableMoves(move.getFrom()).contains(move.getTo())) {
            throw new IllegalArgumentException("Ce coup n'est pas possible");
        }

        Map<Coordinate, Token> newBoard = new HashMap<>(board());

        List<Coordinate> between;
        
        try {
            between = move.getFrom().between(Mode.FLAT, move.getTo());
        } catch (iut.gon.hexagonal_coordinates.DifferentAxisException e) {
            throw new IllegalArgumentException("Le mouvement doit s'effectuer sur un axe aligné.", e);
        }

        for (Coordinate c : between) {
            Token t = newBoard.get(c);
            if (t instanceof Pawn) {
                newBoard.put(c, new Pawn(t.getTeam().other()));
            }
        }

        newBoard.put(move.getTo(), tokenFrom);
        newBoard.put(move.getFrom(), new Pawn(turn()));

        List<Set<Coordinate>> newLines = getPawnsLines(newBoard, Mode.FLAT);

        newLines.sort((a, b) -> {
            boolean aHasCurrent = a.stream()
                .anyMatch(c -> newBoard.get(c) != null && newBoard.get(c).getTeam().equals(turn()));
            boolean bHasCurrent = b.stream()
                .anyMatch(c -> newBoard.get(c) != null && newBoard.get(c).getTeam().equals(turn()));
            
            if (aHasCurrent && !bHasCurrent) return -1;
            if (!aHasCurrent && bHasCurrent) return 1;
            return 0;
        });

        return new State(newBoard, turn().other(), newLines);
    }

    /**
     * @param removeLine la ligne à retirer
     * @return le nouvel état
     */
    @Override
    public IState removeLine(RemoveLine removeLine) {

        if (lines().isEmpty()) {
            throw new RuntimeException("Aucune ligne à supprimer");
        }

        Set<Coordinate> line = removeLine.getLine();

        if (line == null || line.size() != 5) {
            throw new RuntimeException("Ligne invalide");
        }

        
        boolean exists = lines().stream()
                .anyMatch(l -> l.equals(line));

        if (!exists) {
            throw new RuntimeException("Cette ligne n'existe pas");
        }

        
        Team lineTeam = null;

        for (Coordinate c : line) {

            Token t = board().get(c);

            if (!(t instanceof Pawn pawn)) {
                throw new RuntimeException("La ligne doit contenir uniquement des pions");
            }

            if (lineTeam == null) {
                lineTeam = pawn.getTeam();
            }
            else if (lineTeam != pawn.getTeam()) {
                throw new RuntimeException("Les pions ne sont pas de la même équipe");
            }
        }

        
        Coordinate ringCoordinate = removeLine.getRing();

        Token ring = board().get(ringCoordinate);

        if (!(ring instanceof Ring) || ring.getTeam() != lineTeam) {
            throw new RuntimeException("Anneau invalide");
        }

        
        Map<Coordinate, Token> newBoard = new HashMap<>(board());

        
        for (Coordinate c : line) {
            newBoard.put(c, null);
        }

        
        newBoard.put(ringCoordinate, null);

        List<Set<Coordinate>> newLines =
                getPawnsLines(newBoard, Mode.FLAT);

        boolean stillHasLine = false;

        for (Set<Coordinate> l : newLines) {

            Coordinate c = l.iterator().next();
            Token t = newBoard.get(c);

            if (t instanceof Pawn pawn && pawn.getTeam() == lineTeam) {
                stillHasLine = true;
                break;
            }
        }

        Team nextTurn;
        if (stillHasLine) {
            nextTurn = lineTeam;
        } else {
            assert lineTeam != null;
            nextTurn = lineTeam.other();
        }

        return new State(newBoard, nextTurn, newLines);

    }

    /**
     * @param from
     * @return
     */
    
    public Set<Coordinate> availableMoves(Coordinate from) {
        Set<Coordinate> listAvailable = new HashSet<>();

        Token ring = board().get(from);
        if (!(ring instanceof Ring)) {
            return listAvailable;
        }

        Mode mode = Mode.FLAT;
        Direction[] flatDirs = {Direction.N, Direction.NE, Direction.NO, Direction.S, Direction.SE, Direction.SO};

        for (Direction dir : flatDirs) {
            Coordinate current = from.toDir(mode, dir);
            boolean jumpedOverPawns = false;

            while (board().containsKey(current)) {
                Token token = board().get(current);

                if (token == null) {
                    listAvailable.add(current);
                    if (!jumpedOverPawns) {
                        current = current.toDir(mode, dir);
                    } 
                    else {
                        break;
                    }
                } 
                else if (token instanceof Pawn) {
                    jumpedOverPawns = true;
                    current = current.toDir(mode, dir);
                } 
                else if (token instanceof Ring) {
                    break;
                }
            }
        }

        return listAvailable;
    }

    /**
     * @return
     */
    public List<Set<Coordinate>> lines() {
        // TODO implement here
    	return this.lines;
    }

    /**
     * @return
     */
    public Team turn() {
        return this.turn;
    }

    /**
     */

    private static final Direction[] FLAT_AXES   = {Direction.N,  Direction.NE, Direction.NO};
    private static final Direction[] POINTY_AXES = {Direction.E,  Direction.NE, Direction.SE};

    public static List<Set<Coordinate>> getPawnsLines(Map<Coordinate, Token> board, Mode mode) {
        // TODO implement here
        List<Set<Coordinate>> result = new ArrayList<>();
        Set<Set<Coordinate>> seen = new HashSet<>();

        Direction[] halfAxes = (mode == Mode.FLAT) ? FLAT_AXES : POINTY_AXES;

        for (Map.Entry<Coordinate, Token> entry : board.entrySet()) {
            if (!(entry.getValue() instanceof Pawn pawn)) continue;

            Coordinate coord = entry.getKey();

            for (Direction dir : halfAxes){
                List<Coordinate> backward = new ArrayList<>();
                Coordinate cur = coord.toDir(mode, dir.opposite());
                while (board.containsKey(cur) && board.get(cur) instanceof Pawn p && p.getTeam() == pawn.getTeam()) {
                    backward.add(cur);
                    cur = cur.toDir(mode, dir.opposite());
                }
                Collections.reverse(backward);

                List<Coordinate> chain = new ArrayList<>(backward);
                chain.add(coord);

                cur = coord.toDir(mode, dir);
                while(board.containsKey(cur) && board.get(cur) instanceof Pawn p && p.getTeam() == pawn.getTeam()){
                    chain.add(cur);
                    cur = cur.toDir(mode, dir);
                }

                for (int i = 0; i <= chain.size() - 5; i++){
                    Set<Coordinate> line = new HashSet<>(chain.subList(i, i + 5));
                    if (seen.add(line)) result.add(line);
                }
            }
        }
        return result;
    }

    /**
     * @return
     */
    public Map<Coordinate, Token> board() {
        return this.board;
    }

    
    @Override
    public IState removeToken(Coordinate c) {
        Map<Coordinate, Token> newBoard = new HashMap<>(board());
        newBoard.put(c, null);
        return new State(newBoard, turn(), lines());
    }
    
    /**
     * @return
     */
    public Map<Team, List<Coordinate>> rings() {
        Map<Team, List<Coordinate>> result = new HashMap<>();
        result.put(Team.BLACK, new ArrayList<>());
        result.put(Team.WHITE, new ArrayList<>());
        if (board == null) return result;
        for (Map.Entry<Coordinate, Token> entry : board.entrySet()) {
            if (entry.getValue() instanceof Ring) {
                result.get(entry.getValue().getTeam()).add(entry.getKey());
            }
        }
        return result;
    }

    @Override
    public IState toggleToken(Coordinate coordinate, Token t, Team team) {
        Map<Coordinate, Token> newBoard = new HashMap<>(board());
        
        try {
            java.lang.reflect.Constructor<?> constructor = t.getClass().getConstructors()[0];
            Token newToken = (Token) constructor.newInstance(team);
            Token currentToken = newBoard.get(coordinate);
            
            if (currentToken != null 
                    && currentToken.getClass().equals(t.getClass()) 
                    && currentToken.getTeam().equals(team)) {
                newBoard.put(coordinate, null);
            } else {
                newBoard.put(coordinate, newToken);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'instanciation dynamique du Token via Reflection", e);
        }
        
        List<Set<Coordinate>> newLines = getPawnsLines(newBoard, Mode.FLAT);
        
        newLines.sort((a, b) -> {
            boolean aHasCurrent = a.stream().anyMatch(c -> newBoard.get(c) != null && newBoard.get(c).getTeam().equals(turn()));
            boolean bHasCurrent = b.stream().anyMatch(c -> newBoard.get(c) != null && newBoard.get(c).getTeam().equals(turn()));
            if (aHasCurrent && !bHasCurrent) return -1;
            if (!aHasCurrent && bHasCurrent) return 1;
            return 0;
        });

        return new State(newBoard, turn(), newLines);
    }

	

}