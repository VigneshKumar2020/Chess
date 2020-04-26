package com.chess.engine.client;

import com.chess.engine.Alliance;
import com.chess.engine.Game;
import com.chess.engine.board.Board;

public class CLI {


    public static void main(String[] args) {
        Board board = Game.startGame();
        Game.moveRequestForCLI(board, Alliance.WHITE);
    }
}

