package com.chess.engine.piece;

public enum PieceType {

    ROOK(5), KNIGHT(3), BISHOP(3), KING(0), QUEEN(9), PAWN(1);

    private final int points;

    PieceType(final int points) {
        this.points = points;
    }

    public double getPoints() {
        return this.points;
    }
}