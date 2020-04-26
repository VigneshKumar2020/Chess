package com.chess.engine;

import com.chess.engine.board.Tile;
import com.chess.engine.piece.Piece;
import com.chess.engine.player.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/*
 * Created by Vignesh Kumar on 08 April 2020
 */
public final class Moves {

    private Moves() {
        throw new RuntimeException("Moves is non-instantiable");
    }

    public static void filter(Piece movedPiece,
                              Collection<Tile> destinationCoordinates,
                              Player player) {
        Objects.requireNonNull(movedPiece);
        if (destinationCoordinates == null) destinationCoordinates = Collections.emptyList();
        Tile currentTile = movedPiece.getCurrentTile();
        for (Tile i : destinationCoordinates) {
            movedPiece.setCurrentTile(i);
            if (player.isInCheck())
                destinationCoordinates.remove(i);
        }
        movedPiece.setCurrentTile(currentTile);
    }
}
