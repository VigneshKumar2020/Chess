package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Tile;
import com.chess.engine.exception.IllegalMoveException;
import com.chess.engine.piece.Piece;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static com.chess.engine.Alliance.WHITE;

/*
 * Created by Vignesh Kumar on 08 April 2020
 */
public final class Player {

    final Scanner sc = new Scanner(System.in);
    final RuntimeException exception = new IllegalMoveException("Illegal Move");
    private int points;
    private final Piece king;
    private List<Piece> activePieces;
    private final Board board;
    private final Alliance alliance;
    final String cachedToString;
    private boolean isInCheckmate;

    public Player(Alliance alliance, Board board) {
        this.alliance = Objects.requireNonNull(alliance);
        this.board = Objects.requireNonNull(board);
        activePieces = board.calculateActivePieces(alliance);
        this.king = establishKing();
        this.cachedToString = String.format("%s%s", this.alliance.toString().toLowerCase(), " player");
    }

    public final Player getOpponent() {
        return alliance.equals(WHITE) ? board.getBlackPlayer() : board.getWhitePlayer();
    }

    public Piece establishKing() {
        for (Piece i : activePieces) {
            if (i.isKing())
                return i;
        }
        throw new RuntimeException("Board doesn't have a king. It has only " + activePieces + " pieces\n" + (16 - activePieces.size()) +
                " pieces are not on the board");
    }

    public final void setIsInCheckmate(final boolean inCheckmate) {
        isInCheckmate = inCheckmate;
    }

    public final void calculateActivePieces() {
        this.activePieces = this.board.calculateActivePieces(this.alliance);
    }

    public final boolean isInCheck() {
        return board.isTileUnderAttack(king.getCurrentTile());
    }

    public final boolean isInCheckmate() {
        return isInCheckmate;
    }

    public final Alliance getAlliance() {
        return this.alliance;
    }

    public final List<Piece> getActivePieces() {
        return ImmutableList.copyOf(this.activePieces);
    }

    public final void calculatePoints() {
        List<Piece> opponentActivePieces = getOpponent().getActivePieces();
        calculateActivePieces();
        for (Piece i : activePieces)
            for (Piece j : opponentActivePieces)
                points += i.getPieceType().getPoints() - j.getPieceType().getPoints();
    }

    public final Piece getKing() {
        return king;
    }

    public final int getPoints() {
        return points;
    }

    @Override
    public final String toString() {
        return cachedToString;
    }

    public Board move() throws IllegalMoveException{
        System.out.println(board);
        System.out.println("Current row no ?");
        int row = sc.nextInt();
        System.out.println("Current column no?");
        int column = sc.nextInt();
        System.out.println("Row no.?");
        int xCoordinate = sc.nextInt();
        System.out.println("Column no.?");
        int yCoordinate = sc.nextInt();
        return move(row, column, xCoordinate, yCoordinate, true);
    }

    //TODO fix
    public Board move(int currentXCoordinate,int currentYCoordinate,
                      int xCoordinate, int yCoordinate, boolean recursiveFlag) {
        Tile tile = board.getTile(currentXCoordinate, currentYCoordinate);
        System.out.println(this);
        if (!tile.isEmpty() && tile.getPieceOnTile().is(this.alliance)) {
            return tile.getPieceOnTile().moveTo(board.getTile(xCoordinate, yCoordinate), recursiveFlag);
        }
        throw exception;
    }


}