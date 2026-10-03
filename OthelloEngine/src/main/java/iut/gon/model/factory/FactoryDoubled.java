package iut.gon.model.factory;

import iut.gon.hexagonal_coordinates.CoordinateDoubled;
import iut.gon.hexagonal_coordinates.Coordinate;
import iut.gon.model.Team;
import iut.gon.model.state.IState;
import iut.gon.model.state.State;
import iut.gon.model.tokens.Pawn;
import iut.gon.model.tokens.Ring;
import iut.gon.model.tokens.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FactoryDoubled implements IFactory {

    public FactoryDoubled() {
    }

 
    private Map<Coordinate, Token> buildEmptyBoard() {
        Map<Coordinate, Token> board = new HashMap<>();

    
        int radius = 5;
        for (int q = -radius; q <= radius; q++) {
            int r1 = Math.max(-radius, -q - radius);
            int r2 = Math.min( radius, -q + radius);
            for (int r = r1; r <= r2; r++) {
                int row = r + 5;
                int col = 2 * q + r + 9;
                board.put(new CoordinateDoubled(row, col), null);
            }
        }
        return board;
    }

 
    private void placeRing(Map<Coordinate, Token> board, int row, int col, Team team) {
        board.put(new CoordinateDoubled(row, col), new Ring(team));
    }

    private void placePawn(Map<Coordinate, Token> board, int row, int col, Team team) {
        board.put(new CoordinateDoubled(row, col), new Pawn(team));
    }

  
    private void placeBaseTokens(Map<Coordinate, Token> board) {
        // Anneaux noirs
        placeRing(board, 5,  9, Team.BLACK);
        placeRing(board, 5, 11, Team.BLACK);
        placeRing(board, 5, 13, Team.BLACK);
        placeRing(board, 4,  6, Team.BLACK);

        // Anneaux blancs
        placeRing(board, 3,  9, Team.WHITE);
        placeRing(board, 3, 11, Team.WHITE);
        placeRing(board, 5,  7, Team.WHITE);
        placeRing(board, 6,  6, Team.WHITE);
        placeRing(board, 8,  8, Team.WHITE);

        // Pions noirs
        placePawn(board, 1,  9, Team.BLACK);
        placePawn(board, 1, 11, Team.BLACK);
        placePawn(board, 2,  8, Team.BLACK);
        placePawn(board, 2, 12, Team.BLACK);
        placePawn(board, 3,  7, Team.BLACK);
        placePawn(board, 4,  8, Team.BLACK);
        placePawn(board, 4, 10, Team.BLACK);
        placePawn(board, 6,  8, Team.BLACK);
        placePawn(board, 7,  5, Team.BLACK);
        placePawn(board, 7,  7, Team.BLACK);
        placePawn(board, 8,  4, Team.BLACK);

        // Pions blancs
        placePawn(board, 2,  6, Team.WHITE);
        placePawn(board, 2, 10, Team.WHITE);
        placePawn(board, 3, 13, Team.WHITE);
        placePawn(board, 4, 12, Team.WHITE);
        placePawn(board, 5,  5, Team.WHITE);
        placePawn(board, 6, 10, Team.WHITE);
        placePawn(board, 7,  9, Team.WHITE);
        placePawn(board, 8,  6, Team.WHITE);
        placePawn(board, 9,  7, Team.WHITE);
    }

  
    @Override
    public IState testState() {
        Map<Coordinate, Token> board = buildEmptyBoard();
        placeBaseTokens(board);
        placeRing(board, 6, 2, Team.BLACK);
        return new State(board, Team.BLACK, new ArrayList<>());
    }


    @Override
    public IState stateForBlackLineTest() {
        Map<Coordinate, Token> board = buildEmptyBoard();
        placeBaseTokens(board);
        placeRing(board, 6, 2, Team.BLACK);


        placePawn(board, 5, 1, Team.BLACK);
        placePawn(board, 5, 3, Team.BLACK);
        placePawn(board, 5, 5, Team.BLACK);
        placePawn(board, 5, 7, Team.BLACK);
        placePawn(board, 5, 9, Team.BLACK);

        List<java.util.Set<Coordinate>> lines = State.getPawnsLines(board, iut.gon.hexagonal_coordinates.Mode.POINTY);
        return new State(board, Team.BLACK, lines);
    }

    
    @Override
    public IState stateForWhiteLineTest() {
        Map<Coordinate, Token> board = buildEmptyBoard();
        placeBaseTokens(board);
        placeRing(board, 6, 2, Team.BLACK);

        
        placePawn(board, 3, 5, Team.WHITE);
        placePawn(board, 3, 7, Team.WHITE);
        placePawn(board, 3, 9, Team.WHITE);
        placePawn(board, 3, 11, Team.WHITE);
        placePawn(board, 3, 13, Team.WHITE);

        List<java.util.Set<Coordinate>> lines = State.getPawnsLines(board, iut.gon.hexagonal_coordinates.Mode.POINTY);
        return new State(board, Team.WHITE, lines);
    }

    
    @Override
    public IState emptyState() {
        Map<Coordinate, Token> board = buildEmptyBoard();
        return new State(board, Team.BLACK, new ArrayList<>());
    }

    
    @Override
    public IState doubleLineStateTest() {
        Map<Coordinate, Token> board = buildEmptyBoard();
        placeBaseTokens(board);
        placeRing(board, 6, 2, Team.BLACK);

        // Ligne noire (rangée 5, cols 1..9)
        placePawn(board, 5, 1, Team.BLACK);
        placePawn(board, 5, 3, Team.BLACK);
        placePawn(board, 5, 5, Team.BLACK);
        placePawn(board, 5, 7, Team.BLACK);
        placePawn(board, 5, 9, Team.BLACK);

        // Ligne blanche (rangée 3, cols 5..13)
        placePawn(board, 3, 5, Team.WHITE);
        placePawn(board, 3, 7, Team.WHITE);
        placePawn(board, 3, 9, Team.WHITE);
        placePawn(board, 3, 11, Team.WHITE);
        placePawn(board, 3, 13, Team.WHITE);

        List<java.util.Set<Coordinate>> lines = State.getPawnsLines(board, iut.gon.hexagonal_coordinates.Mode.POINTY);
        return new State(board, Team.BLACK, lines);
    }
}