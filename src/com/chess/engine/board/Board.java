package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.piece.Piece;
import com.chess.engine.player.Player;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.stream.IntStream;

import static com.chess.engine.Alliance.BLACK;
import static com.chess.engine.Alliance.WHITE;
import static com.chess.engine.Position.POSITION_CACHE;
import static com.chess.engine.piece.PieceType.*;

/*
 * Created by Vignesh Kumar on 11 April 2020
 */
public final class Board {
    private final int NUM_TILES_PER_ROW = 8;
    private final Map<Alliance, Player> allianceMap;
    private final Map<Position, Tile> BOARD_MAP = initBoardMap();
    final List<Piece> whitePieces;
    final List<Piece> blackPieces;
    private boolean matchIsDrawn;
    private int counter;
    public final List<Piece> deadPieces = new ArrayList<>();
    private boolean gameOver;

    //TODO manage move makers and moves
    private Board() {
        createStandardBoard();
        allianceMap = initAllianceMap();
        allianceMap.get(WHITE).calculateActivePieces();
        allianceMap.get(BLACK).calculateActivePieces();
        whitePieces = calculateActivePieces(WHITE);
        blackPieces = calculateActivePieces(BLACK);
        for (Piece i : whitePieces) i.setPlayer(getWhitePlayer());
        for (Piece i : blackPieces) i.setPlayer(getBlackPlayer());
    }

    private Map<Alliance, Player> initAllianceMap() {
        final Map<Alliance, Player> allianceMap = new HashMap<>();
        allianceMap.put(WHITE, new Player(WHITE, this));
        allianceMap.put(BLACK, new Player(BLACK, this));
        return ImmutableMap.copyOf(allianceMap);
    }

    public boolean contains(final Position position) {
        return this.BOARD_MAP.containsKey(position);
    }

    public boolean contains(final Tile tile) {
        return this.BOARD_MAP.containsValue(tile);
    }

    public boolean contains(int xCoordinate, int yCoordinate) {
        return this.contains(POSITION_CACHE.get(String.format("%d%d", xCoordinate, yCoordinate)));
    }

    public Player getWhitePlayer() {
        return allianceMap.get(WHITE);
    }

    public Player getBlackPlayer() {
        return allianceMap.get(BLACK);
    }

    public Player getPlayerOf(Alliance alliance) {
        if (alliance.equals(WHITE) || alliance.equals(BLACK))
            return allianceMap.get(alliance);
        else throw new NoSuchElementException("The given alliance doesn't fit in");
    }

    public final List<Piece> calculateActivePieces() {
        counter++;
        List<Piece> activePieces = new ArrayList<>();
        for (int i = 0; i < NUM_TILES_PER_ROW; i++){
            for (int j = 0; j < NUM_TILES_PER_ROW; j++){
                Tile tile = getTile(i, j);
                if (!tile.isEmpty()) {
                    activePieces.add(tile.getPieceOnTile());
                    if (counter == 1) tile.getPieceOnTile().setCurrentTile(tile);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    public final List<Piece> calculateActivePieces(Alliance alliance) {
        List<Piece> pieces = new ArrayList<>();
        for (Piece i : calculateActivePieces()) {
            if (i.is(alliance))
                pieces.add(i);
        }
        return ImmutableList.copyOf(pieces);
    }

    private Map<Position, Tile> initBoardMap() {
        Map<Position, Tile> tileMap = new HashMap<>();
        for (int i = 0; i < NUM_TILES_PER_ROW; i++) {
            String rowAddress = String.format("%d", i);
            for (int j = 0; j < NUM_TILES_PER_ROW; j++) {
                String positionAddress = String.format("%s%d", rowAddress, j);
                Position e = POSITION_CACHE.get(positionAddress);
                tileMap.put(e, new Tile(e));
            }
        }
        return ImmutableMap.copyOf(tileMap);
    }

    private void createStandardBoard() {
        //Major pieces of the board
        for (Alliance alliance : Alliance.values()) {
            final int xCoordinate = alliance.equals(BLACK) ? 0 : 7;
            final int alliancexCoordinate = alliance.equals(WHITE) ? 6 : 1;

            IntStream.range(0, 5).forEach(i -> this.getTile(xCoordinate, i).setPieceOnTile(Piece.createPiece(values()[i], alliance,
                    this.getTile(xCoordinate, i), this)));
            IntStream.range(0, 3).forEach(i -> this.getTile(xCoordinate, 5 + i).setPieceOnTile(Piece.createPiece(values()[2-i], alliance,
                    this.getTile(xCoordinate, 5 + i), this)));
            //Pawns
            IntStream.range(0, 8).forEach(yCoordinate -> this.getTile(alliancexCoordinate, yCoordinate).setPieceOnTile(Piece.createPiece(PAWN,
                    alliance, this.getTile(alliancexCoordinate, yCoordinate), this)));
        }
    }


    public final Piece pieceAt(Tile tile) {
        return Objects.requireNonNull(tile.getPieceOnTile());
    }

    public final Piece pieceAt(Position position) {
        return Objects.requireNonNull(this.pieceAt(this.getTile(position)));
    }

    public final Piece pieceAt(int xCoordinate, int yCoordinate) {
        return Objects.requireNonNull(this.pieceAt(POSITION_CACHE.get(String.format("%d%d", xCoordinate, yCoordinate))));
    }

    public final Tile getTile(int xCoordinate, int yCoordinate) {
        return getTile(POSITION_CACHE.get(String.format("%d%d", xCoordinate, yCoordinate)));
    }

    public Tile getTile(Position position) {
        return BOARD_MAP.get(position);
    }

    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < NUM_TILES_PER_ROW; i++) {
            for (int j = 0; j < NUM_TILES_PER_ROW; j++)
                builder.append(this.getTile(i, j));
            builder.append("\n");
        }
        return builder.toString();
    }

    public final void setMatchIsDrawn(final boolean matchIsDrawn) {
        this.matchIsDrawn = matchIsDrawn;
    }

    public final boolean getMatchIsDrawn() {
        return matchIsDrawn;
    }

    public final boolean isTileUnderAttack(Tile tile) {
        for (Piece i : calculateActivePieces()) {
            if (i.getLegalMoves().contains(tile))
                return true;
        }
        return false;
    }

    public final boolean isTileUnderAttack(int xCoordinate, int yCoordinate) {
        return isTileUnderAttack(getTile(xCoordinate, yCoordinate));
    }

    public final void pawnPromotion(final Piece pawn) {
        final int xCoordinate = pawn.getPiecePosition().getxCoordinate();
        if (pawn.isPawn() && (pawn.is(BLACK) ? xCoordinate == 7 : xCoordinate == 0)) {
            pawn.getCurrentTile().setPieceOnTile(Piece.createPiece(QUEEN, pawn.getAlliance(),
                    pawn.getCurrentTile(), this));//TODO allow the player to choose
            pawn.selfDestruct();
        }
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public static Board createBoardRequest() {
        return new Board();
    }

    public boolean getGameOver() {
        return gameOver;
    }
}