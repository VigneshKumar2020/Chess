package com.chess.engine.board;

import com.chess.engine.Position;
import com.chess.engine.piece.Piece;

/*
 * Created by Vignesh Kumar on 08 April 2020
 */
public final class Tile implements Comparable<Tile> {

    private final Position pos;
    private Piece pieceOnTile;
    private final String cachedEmptyTileToString;

    public Tile(final Position pos) {
        this.pos = pos;
        cachedEmptyTileToString = String.format("%s%s%c", "| ", pos.getKey(), '|');
    }
    public final boolean isEmpty() {
        return pieceOnTile == null;
    }

    public final Piece getPieceOnTile() {
        if (isEmpty())
            throw new RuntimeException("This tile doesn't have a piece on it \n. Please check whether a tile is not empty and then get its piece");
        return pieceOnTile;
    }

    public final void setPieceOnTile(Piece pieceOnTile) {
        this.pieceOnTile = pieceOnTile;
    }

    public final Position getPosition() {
        return this.pos;
    }

    public int getxCoordinate() {
        return pos.getxCoordinate();
    }

    public int getyCoordinate() {
        return this.pos.getyCoordinate();
    }

    @Override
    public String toString() {
        return this.isEmpty() ? cachedEmptyTileToString : pieceOnTile.toString();
    }

    @Override
    public final int compareTo(Tile other) {
        return (10 * pos.getxCoordinate() + this.pos.getyCoordinate()) -
                (10 * other.getPosition().getxCoordinate() + other.getPosition().getyCoordinate());
    }
}