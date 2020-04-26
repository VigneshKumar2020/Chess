package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.List;

/*
 * Created by Vignesh Kumar on 10 April 2020
 */
public final class Queen extends Piece {

    Queen(Alliance alliance, Tile currentTile, Board board) {
        super(PieceType.QUEEN, alliance,100, currentTile, board);
        super.setCandidateMoveCoordinates(this.initCandidateMoveCoordinates());
    }
    @Override
    public final List<Position> initCandidateMoveCoordinates() {
        return ImmutableList.copyOf(Position.getPositions("10", "01", "-10", "0-1", "11", "-1-1", "1-1", "-11"));
    }
}