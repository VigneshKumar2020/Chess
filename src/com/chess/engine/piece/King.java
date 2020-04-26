package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;

/*
 * Created by Vignesh Kumar on 08 April 2020
 */
public final class King extends Piece {

    final Tile rookTile = this.is(Alliance.WHITE) ? board.getTile(7, 7) : board.getTile(0, 7);

    King(Alliance alliance, Tile currentTile, Board board) {
        super(PieceType.KING, alliance, 1, currentTile, board);
        super.setCandidateMoveCoordinates(this.initCandidateMoveCoordinates());
    }

    @Override
    public final List<Position> initCandidateMoveCoordinates() {
        return ImmutableList.copyOf(Position.getPositions("-10", "-1-1", "-11", "10", "11", "1-1", "01", "0-1"));
        //Castling can be taken care in the calculateLegalMoves method of the piece
    }

    @Override
    public final boolean isKing() {
        return true;
    }

    @SuppressWarnings("MethodComplexity") //All the checks should pass to perform castling
    @Override
    public final boolean isCapableOfCastling(int xCoordinate, int yCoordinate) {
        return this.hasNotMovedYet() && !player.isInCheck() &&
                Objects.requireNonNull(board.getTile(xCoordinate, yCoordinate + 1)).isEmpty() &&
                board.getTile(xCoordinate, yCoordinate + 2).isEmpty() &&
                !rookTile.isEmpty() && rookTile.getPieceOnTile().hasNotMovedYet() &&
                !board.isTileUnderAttack(rookTile) &&
                !board.isTileUnderAttack(xCoordinate, yCoordinate + 2) &&
                !board.isTileUnderAttack(xCoordinate, yCoordinate + 1);
    }
}