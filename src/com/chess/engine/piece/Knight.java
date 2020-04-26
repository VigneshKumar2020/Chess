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
public final class Knight extends Piece {

    String cachedToString;

    Knight(final Alliance alliance, final Tile currentTile, final Board board) {
        super(PieceType.KNIGHT, alliance, 1, currentTile, board);
        super.setCandidateMoveCoordinates(this.initCandidateMoveCoordinates());
    }

    @Override
    void computeToString(Position piecePosition) {
        cachedToString = String.format("%cs%c%s%c", '|', this.is(Alliance.WHITE) ? 'N' : 'n', piecePosition.getKey(), '|');
    }

    @Override
    public String toString() {
        return cachedToString;
    }

    @Override
    public final List<Position> initCandidateMoveCoordinates() {
        return ImmutableList.copyOf(Position.getPositions(
                "-2-1", "-1-2", "1-2", "2-1", "21", "12", "-12", "-21"));
    }
}