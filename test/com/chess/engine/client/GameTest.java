package com.chess.engine.client;

import com.chess.engine.Game;
import com.chess.engine.board.Board;
import com.chess.engine.exception.IllegalMoveException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {


    Board board = Game.startGame();

    @Test
    public void setUp() {

        System.out.println(board);

    }
    @Test
    public void initialWhitePawnMovetest() {

        board = Game.moveRequest(board.getWhitePlayer(),
                6, 1,5,1);
        System.out.println(board);
    }

    @Test
    public void pawnCaptureMoveTest() {
        Game.moveRequest(board.getWhitePlayer(), 6, 3, 4, 3);
        Game.moveRequest(board.getBlackPlayer(), 1, 2, 3, 2);
        assertTrue(board.pieceAt(3, 2).isOpponentOf(board.pieceAt(4, 3)));
        assertTrue(board.pieceAt(4, 3).isOpponentOf(board.pieceAt(3, 2)));
        assertTrue(board.contains(3, 2) && !board.getTile(3, 2).isEmpty());
    }

    @Test
    public void checkTest() {
        board.getTile(6, 3).getPieceOnTile().selfDestruct();
        assertTrue(board.getTile(6, 3).isEmpty());
        board.getTile(1, 3).getPieceOnTile().selfDestruct();
        Game.moveRequest(board.getWhitePlayer(), 7, 4, 6, 3);
        board.getTile(6, 3).getPieceOnTile().calculateLegalMoves();
        assertTrue(board.getTile(6, 3).getPieceOnTile().getLegalMoves().contains(board.getTile(0, 3)));
        assertTrue(board.isTileUnderAttack(0, 3));
        assertTrue(board.isTileUnderAttack(board.getBlackPlayer().getKing().getCurrentTile()));
        assertTrue(board.getBlackPlayer().isInCheck());
    }

    @Test
    public void initialBlackPawnMoveTest() {
        board = Game.moveRequest(board.getBlackPlayer(),
                1, 7, 3, 7);
    }

    @Test
    public void secondBlackPawnMoveTest() {

        initialWhitePawnMovetest();

        board = Game.moveRequest(board.getBlackPlayer(),
                1, 0,2,0);

    }
    @Test
    public void initialWhitePawnMoveNegativetest() {

        Throwable exception = assertThrows(
                IllegalMoveException.class, () -> Game.moveRequest(board.getWhitePlayer(),
                        6, 1,8,1)
        );

        assertEquals("Illegal Move", exception.getMessage());
    }
    @Test
    public void RookAttackNegativetest() {

        Throwable exception = assertThrows(
                IllegalMoveException.class, () -> Game.moveRequest(board.getWhitePlayer(),
                        7, 0,6,0)
        );

        assertEquals("Illegal Move", exception.getMessage());
    }

    @Test
    public void calculatePointsTest() {
        Game.moveRequest(board.getWhitePlayer(), 7, 6, 5, 5);
        Game.moveRequest(board.getBlackPlayer(), 0, 1, 2, 2);
        Game.moveRequest(board.getWhitePlayer(), 5, 5, 4, 3);
        Game.moveRequest(board.getBlackPlayer(), 2, 2, 4, 3);
        board.getBlackPlayer().calculatePoints();
        System.out.println(board.getBlackPlayer().getPoints());
//        System.out.println();
    }

    @Test
    public void KnightMoveTest() {
        Game.moveRequest(board.getWhitePlayer(), 7, 1, 5, 0);
        System.out.println(board);
    }

}