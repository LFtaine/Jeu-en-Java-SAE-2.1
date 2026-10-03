package iut.gon.ai;

import iut.gon.hexagonal_coordinates.Direction;
import iut.gon.hexagonal_coordinates.Mode;
import iut.gon.hexagonal_coordinates.Coordinate;
import iut.gon.model.Team;
import iut.gon.model.Player;
import iut.gon.model.actions.Action;
import iut.gon.model.actions.Move;
import iut.gon.model.actions.RemoveLine;
import iut.gon.model.state.IState;
import iut.gon.model.tokens.Pawn;
import iut.gon.model.tokens.Token;

import java.util.*;

public class MinimaxAI extends Player implements AI {
    private static final String username = "MinimaxAI";

    private final int maxDepth;
    private Team aiTeam;

    public MinimaxAI(int maxDepth) {
        super(username, Team.BLACK);
        this.maxDepth = maxDepth;
    }


    @Override
    public Action chooseMove(IState state) {
        this.aiTeam = state.turn();

        Node root = new Node(state, null, null);
        double bestScore = Double.NEGATIVE_INFINITY;
        Action bestAction = null;

        for (Action action : getPossibleActions(state)) {
            IState childState = simulateAction(state, action);
            if (childState == null) continue;

            Node childNode = new Node(childState, root, action);
            double score = minimax(childNode, maxDepth - 1,
                    Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);

            if (score > bestScore) {
                bestScore = score;
                bestAction = action;
            }
        }
        return bestAction;
    }

    @Override
    public Coordinate chooseRingPlacement(IState state) {
        List<Coordinate> available = new ArrayList<>();
        for (Map.Entry<Coordinate, Token> entry : state.board().entrySet()) {
            if (entry.getValue() == null) {
                available.add(entry.getKey());
            }
        }

        if (available.isEmpty()) return null;

        Random random = new Random();
        return available.get(random.nextInt(available.size()));
    }

    private double minimax(Node node, int depth, double alpha, double beta, boolean isMax) {
        IState currentState = node.state();

        if (depth == 0 || isGameOver(currentState)) {
            return evaluate(currentState);
        }

        List<Action> possibleActions = getPossibleActions(currentState);

        if (possibleActions.isEmpty()) return 0;

        if (isMax) {
            double maxEval = Double.NEGATIVE_INFINITY;
            for (Action action : possibleActions) {
                IState childState = simulateAction(currentState, action);
                if (childState == null) continue;
                Node childNode = new Node(childState, node, action);
                double eval = minimax(childNode, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha   = Math.max(alpha, eval);
                if (alpha >= beta) break;
            }
            return maxEval;
        } else {
            double minEval = Double.POSITIVE_INFINITY;
            for (Action action : possibleActions) {
                IState childState = simulateAction(currentState, action);
                if (childState == null) continue;
                Node childNode = new Node(childState, node, action);
                double eval = minimax(childNode, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta    = Math.min(beta, eval);
                if (alpha >= beta) break;
            }
            return minEval;
        }
    }


    public List<Action> getPossibleActions(IState state) {
        List<Action> actions = new ArrayList<>();
        List<Set<Coordinate>> lines = state.lines();

        if (!lines.isEmpty()) {
            Team current = state.turn();
            List<Coordinate> currentRings = state.rings().get(current);

            for (Set<Coordinate> line : lines) {
                if (!isLineOfTeam(state, line, current)) continue;
                for (Coordinate ring : currentRings) {
                    actions.add(new RemoveLine(line, ring));
                }
            }

            if (actions.isEmpty()) {
                Team opponent = current.other();
                List<Coordinate> oppRings = state.rings().get(opponent);
                for (Set<Coordinate> line : lines) {
                    for (Coordinate ring : oppRings) {
                        actions.add(new RemoveLine(line, ring));
                    }
                }
            }
        } else {
            Team current = state.turn();
            List<Coordinate> rings = state.rings().get(current);
            for (Coordinate ring : rings) {
                for (Coordinate dest : state.availableMoves(ring)) {
                    actions.add(new Move(ring, dest));
                }
            }
        }

        return actions;
    }


    private IState simulateAction(IState state, Action action) {
        try {
            if (action instanceof Move move) {
                return state.move(move);
            } else if (action instanceof RemoveLine rl) {
                return state.removeLine(rl);
            }
        } catch (Exception e) {
            // Action illégale : on ignore
        }
        return null;
    }


    public boolean isGameOver(IState state) {
        return state.winner() != null || state.isDraw();
    }


    private double evaluate(IState state) {
        if (state.isDraw()) return 0;
        Team winner = state.winner();
        if (winner == aiTeam) return  100000;
        if (winner != null)   return -100000;

        double score = 0;

        // Anneaux retirés : 5 au départ, winner() se déclenche à ≤ 2 restants
        int aiRings  = state.rings().get(aiTeam).size();
        int oppRings = state.rings().get(aiTeam.other()).size();
        int aiRemoved  = 5 - aiRings;
        int oppRemoved = 5 - oppRings;
        score += (aiRemoved - oppRemoved) * 1000;

        // Lignes de 4 pions
        int aiLines4  = countLinesOf4(state, aiTeam);
        int oppLines4 = countLinesOf4(state, aiTeam.other());
        score += (aiLines4 - oppLines4) * 50;

        // Mobilité
        int aiMobility  = calculateMobility(state, aiTeam);
        int oppMobility = calculateMobility(state, aiTeam.other());
        score += (aiMobility - oppMobility) * 0.5;

        return score;
    }


    private int countLinesOf4(IState state, Team team) {
        Map<Coordinate, Token> board = state.board();
        Direction[] halfAxes = {Direction.N, Direction.NE, Direction.NO};
        int count = 0;

        for (Map.Entry<Coordinate, Token> entry : board.entrySet()) {
            Token token = entry.getValue();
            if (!(token instanceof Pawn pawn) || pawn.getTeam() != team) continue;

            Coordinate coord = entry.getKey();
            for (Direction dir : halfAxes) {
                if (countStreak(board, coord, dir, team) == 4) count++;
            }
        }
        return count;
    }


    private int countStreak(Map<Coordinate, Token> board, Coordinate start,
                             Direction dir, Team team) {
        int count = 1;
        Coordinate cur = start;
        while (true) {
            try {
                cur = cur.toDir(Mode.FLAT, dir);
            } catch (Exception e) {
                break;
            }
            Token t = board.get(cur);
            if (!(t instanceof Pawn p) || p.getTeam() != team) break;
            count++;
        }
        return count;
    }


    private int calculateMobility(IState state, Team team) {
        int total = 0;
        for (Coordinate ring : state.rings().get(team)) {
            total += state.availableMoves(ring).size();
        }
        return total;
    }


    private boolean isLineOfTeam(IState state, Set<Coordinate> line, Team team) {
        Map<Coordinate, Token> board = state.board();
        for (Coordinate c : line) {
            Token t = board.get(c);
            if (!(t instanceof Pawn p) || p.getTeam() != team) return false;
        }
        return true;
    }
}