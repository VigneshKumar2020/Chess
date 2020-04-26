package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.List;

/*
 * Created by Vignesh Kumar on 09 April 2020
 */
public final class Rook extends Piece
{
    Rook(Alliance alliance, Tile currentTile, Board board)
    {
        super(PieceType.ROOK, alliance, 100, currentTile, board);
        super.setCandidateMoveCoordinates(this.initCandidateMoveCoordinates());
    }

    @Override
    public final List<Position> initCandidateMoveCoordinates() {
        return ImmutableList.copyOf(Position.getPositions("10", "01", "-10", "0-1"));
    }
}
