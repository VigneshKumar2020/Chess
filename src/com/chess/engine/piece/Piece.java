package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Tile;
import com.chess.engine.exception.IllegalMoveException;
import com.chess.engine.player.Player;
import com.sun.istack.internal.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.chess.engine.Alliance.WHITE;

/*
 * Created by Vignesh Kumar on 08 April 2020
 */
public abstract class Piece {

    @Nullable
    Tile currentTile;
    @Nullable
    Position piecePosition;
    final Tile initialTile;
    Player player;
    private final PieceType pieceType;
    final Alliance alliance;
    private final int limit; //used in calculation of legal moves
    final Board board;
    Collection<Position> CANDIDATE_MOVE_COORDINATES;
    Collection<Tile> legalMoves = new HashSet<>();
    boolean hasMovedOnlyOnce;
    String cachedToString;

    Piece(PieceType pieceType, Alliance alliance,
          int limit, Tile currentTile, Board board) {
        this.pieceType = pieceType;
        this.board = board;
        this.alliance = alliance;
        this.currentTile = currentTile;
        this.piecePosition = currentTile.getPosition();
        this.limit = limit;
        initialTile = currentTile;
        CANDIDATE_MOVE_COORDINATES = this.initCandidateMoveCoordinates();
        computeToString(piecePosition);
    }

    public boolean isLegalMove(Tile tile) {
        return legalMoves.contains(tile);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    void computeToString(Position piecePosition) {
        String str = String.format("%c%c%s%c", '|', pieceType.name().charAt(0), piecePosition.getKey(), '|');
        cachedToString = this.is(WHITE) ? str : str.toLowerCase();
    }

    public final Position getPiecePosition() {
        return piecePosition;
    }

    public final boolean is(Alliance alliance) {
        return this.alliance.equals(alliance);
    }

    final boolean hasNotMovedYet() {
        return currentTile.equals(initialTile);
    }

    public final Collection<Tile> getLegalMoves() {
        return legalMoves;
    }

    public final void calculateLegalMoves() {
        legalMoves.clear();
        for (Position i : CANDIDATE_MOVE_COORDINATES)
            framePath(i);
        if (isCapableOfCastling(piecePosition.getxCoordinate(), piecePosition.getyCoordinate()))
            legalMoves.add(board.getTile(7, 6)); //CastleMoves
//        Moves.filter(this, legalMoves, player);
//        board.setGameOver(legalMoves.isEmpty());
//        Collections.sort(legalMoves);
    }

    private void framePath(Position candidatePosition) {
        int candidateXCoordinate = candidatePosition.getxCoordinate();
        int currentXCoordinate = piecePosition.getxCoordinate();
        int candidateYCoordinate = candidatePosition.getyCoordinate();
        int currentYCoordinate = piecePosition.getyCoordinate();
        Tile destinationTile = board.getTile(candidateXCoordinate + currentXCoordinate,
                candidateYCoordinate + currentYCoordinate);

        for (int j = 0; board.contains(destinationTile) && j < limit; j++) {
            if (destinationTile.isEmpty()) {
                legalMoves.add(destinationTile);
                destinationTile = board.getTile(destinationTile.getxCoordinate() + candidatePosition.getxCoordinate(),
                        destinationTile.getyCoordinate() + candidatePosition.getyCoordinate());
            } else {
                if (this.isOpponentOf(destinationTile.getPieceOnTile()))
                    legalMoves.add(destinationTile);
                break;
            }
        }
    }

    boolean isCapableOfCastling(int xCoordinate, int yCoordinate) {
        return false;
    }

    final void setCandidateMoveCoordinates(Collection<Position> CANDIDATE_MOVE_COORDINATES) {
        this.CANDIDATE_MOVE_COORDINATES = CANDIDATE_MOVE_COORDINATES;
    }

    public boolean isPawn() {
        return false;
    }

    public boolean isKing() {
        return false;
    }

    public final boolean isOpponentOf(final Piece other) {
        return other != null && !this.is(other.alliance);
    }

    public final void setCurrentTile(final Tile tile) {
        if (!tile.equals(currentTile) && board.contains(tile)) {
            currentTile.setPieceOnTile(null);
            tile.setPieceOnTile(this);
            this.currentTile = tile;
            this.piecePosition = currentTile.getPosition();
            computeToString(piecePosition);
        }
    }

    public final void selfDestruct() {
        currentTile.setPieceOnTile(null);
        currentTile = null;
        piecePosition = null;
        legalMoves = Collections.emptyList();
        CANDIDATE_MOVE_COORDINATES = Collections.emptyList();
    }

    public final Tile getCurrentTile() {
        return this.currentTile;
    }

    public final Alliance getAlliance() {
        return this.alliance;
    }

    public final PieceType getPieceType() {
        return pieceType;
    }

    //TODO refactor, rethink about moving move() elsewhere!!!!
    @SuppressWarnings("MethodComplexity")
    public final Board moveTo(final Tile destinationTile, boolean recursiveFlag) throws IllegalMoveException {
        Player opponent = this.player.getOpponent();
        if (board.getGameOver()) {
            opponent.setIsInCheckmate(opponent.isInCheck());
            board.setMatchIsDrawn(!opponent.isInCheck());
        }
        for (Piece i : board.calculateActivePieces()) {
            if (i.isPawn())
                i.initCandidateMoveCoordinates();
            i.calculateLegalMoves();
        }

        if (isLegalMove(destinationTile) && !board.getGameOver()) {
            System.out.println(player + "'s turn");
            hasMovedOnlyOnce = hasNotMovedYet();
            if (!destinationTile.isEmpty()) {
                destinationTile.getPieceOnTile().removeFromBoard();
                opponent.calculateActivePieces();
                player.calculatePoints();
            }
            setCurrentTile(destinationTile);

            if (this.isKing() && this.hasMovedOnlyOnce && destinationTile.equals(board.getTile(7, 6)))
                board.getTile(7, 7).getPieceOnTile().setCurrentTile(board.getTile(7, 5));

            organize(opponent, recursiveFlag);
        } else {
            if (recursiveFlag)
                player.move();
            else
                throw new IllegalMoveException("Illegal Move");
        }
        return board;
    }

    private void organize(Player opponent, boolean recursiveFlag) {
        if (this.isPawn())
            board.pawnPromotion(this);
        if (opponent.isInCheckmate())
            System.out.println(this.player + " has won!");
        else if (board.getMatchIsDrawn())
            System.out.println("It's a stalemate");
        else if (recursiveFlag)
            opponent.move();
    }

    @Override
    public String toString() {
        return cachedToString;
    }

    public abstract List<Position> initCandidateMoveCoordinates();

    private void removeFromBoard() {
        board.deadPieces.add(this);
        this.selfDestruct(); //putting it out of board
    }

    public static Piece createPiece(PieceType pieceType, Alliance alliance,
                                    Tile currentTile, Board board) {
        switch (pieceType) {
            case ROOK:
                return new Rook(alliance, currentTile, board);
            case KNIGHT:
                return new Knight(alliance, currentTile, board);
            case BISHOP:
                return new Bishop(alliance, currentTile, board);
            case KING:
                return new King(alliance, currentTile, board);
            case QUEEN:
                return new Queen(alliance, currentTile, board);
            case PAWN:
                return new Pawn(alliance, currentTile, board);
            default:
                throw new IllegalArgumentException("PieceType has no match");
        }
    }
}