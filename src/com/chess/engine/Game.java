package com.chess.engine;

import com.chess.engine.board.Board;
import com.chess.engine.exception.IllegalMoveException;
import com.chess.engine.player.Player;

public class Game {

    public static Board startGame() {
        return Board.createBoardRequest();
    }

    public static Board moveRequest(Player player,
                             int currentXCoordinate, int currentYCoordinate,
                             int xCoordinate, int yCoordinate) throws IllegalMoveException{

        return player.move(currentXCoordinate, currentYCoordinate,
                xCoordinate, yCoordinate, false);
    }

    public static Board moveRequestForCLI(Board board, Alliance alliance) throws IllegalMoveException {
        return board.getPlayerOf(alliance).move();
    }


}
