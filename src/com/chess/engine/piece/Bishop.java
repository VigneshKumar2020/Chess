package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.List;

/*
 * Created by Vignesh Kumar on 08 April 2020
 */
public final class Bishop extends Piece {
    //positionList
    Bishop(final Alliance alliance, final Tile currentTile, final Board board) {
        super(PieceType.BISHOP, alliance, 100, currentTile, board);
        super.setCandidateMoveCoordinates(this.initCandidateMoveCoordinates());
    }

    @Override
    public final List<Position> initCandidateMoveCoordinates() {
        return ImmutableList.copyOf(Position.getPositions("11", "-1-1", "1-1", "-11"));
    }
}
