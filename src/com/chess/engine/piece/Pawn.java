package com.chess.engine.piece;

import com.chess.engine.Alliance;
import com.chess.engine.Position;
import com.chess.engine.board.Board;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

import static com.chess.engine.Position.POSITION_CACHE;

/*
 * Created by Vignesh Kumar on 10 April 2020
 */
public final class Pawn extends Piece {

    private final int one = getAllianceNumber(1);
    private final int two = getAllianceNumber(2);

    Pawn(final Alliance alliance, final Tile currentTile, final Board board) {
        super(PieceType.PAWN, alliance, 1, currentTile, board);
    }

    @Override
    public final List<Position> initCandidateMoveCoordinates() {
        return ImmutableList.copyOf(calculateCandidateMoveCoordinates());
    }
    @SuppressWarnings("MethodComplexity")
    public final List<Position> calculateCandidateMoveCoordinates() {
        List<Position> CANDIDATE_MOVE_COORDINATES = new ArrayList<>();
        Position rightDiagonal;
        Position leftDiagonal;
        int xCoordinate = piecePosition.getxCoordinate();
        int yCoordinate = piecePosition.getyCoordinate();

        if (board.getTile(POSITION_CACHE.get(String.format("%d%d", xCoordinate + one, yCoordinate))).isEmpty())
            CANDIDATE_MOVE_COORDINATES.add(POSITION_CACHE.get(String.format("%d%d", one, 0)));

        if (hasNotMovedYet() && board.getTile(POSITION_CACHE.get(String.format("%d%d",xCoordinate + two, yCoordinate))).isEmpty())
            CANDIDATE_MOVE_COORDINATES.add(POSITION_CACHE.get(String.format("%d%d",two, 0)));

        rightDiagonal = POSITION_CACHE.get(String.format("%d%d", xCoordinate + one, yCoordinate + 1));
        //yCoordinate remains the same for both the alliances
        leftDiagonal = POSITION_CACHE.get(String.format("%d%d", xCoordinate + one, yCoordinate - 1));
        //yCoordinate +1 and -1 ensure that an opponent can be on either side
        if (board.contains(rightDiagonal) && !board.getTile(rightDiagonal).isEmpty() &&
                this.isOpponentOf(board.getTile(rightDiagonal).getPieceOnTile()))
            CANDIDATE_MOVE_COORDINATES.add(POSITION_CACHE.get(String.format("%d%d", one, 1)));

        if (leftDiagonal.getxCoordinate() == 3 && leftDiagonal.getyCoordinate() == 2)
            System.out.println(leftDiagonal);

        if (board.contains(leftDiagonal) && !board.getTile(leftDiagonal).isEmpty() &&
                this.isOpponentOf(board.getTile(leftDiagonal).getPieceOnTile()))
            CANDIDATE_MOVE_COORDINATES.add(POSITION_CACHE.get(String.format("%d%d", one, -1)));
        Tile toTheRightDiagonal = board.getTile(xCoordinate + one, yCoordinate + 1);
        Tile toTheLeftDiagonal = board.getTile(xCoordinate + one, yCoordinate - 1);
        //todo
        if (canEnPassant(toTheRightDiagonal, toTheLeftDiagonal)) {
            Tile rightTile = board.getTile(xCoordinate, yCoordinate + 1);
            if ((!board.getTile(xCoordinate, yCoordinate + 1).isEmpty() && rightTile.getPieceOnTile().isPawn()) &&
                    rightTile.getPieceOnTile().hasMovedOnlyOnce)
                CANDIDATE_MOVE_COORDINATES.add(POSITION_CACHE.get(String.format("%d%d", one, 1)));
            else
                CANDIDATE_MOVE_COORDINATES.add(POSITION_CACHE.get(String.format("%d%d",one, -1)));
        }
        /*
            unlike other pieces, pawn does not set its candidate move coordinates in the constructor, as
            it needs to be calculated each time there's a move on the board, hence whenever it calculates the legal moves,
            it sets it in its super class
        */
        super.setCandidateMoveCoordinates(CANDIDATE_MOVE_COORDINATES);
        return CANDIDATE_MOVE_COORDINATES;
    }
    @Override
    public final boolean isPawn() {
        return true;
    }
    private boolean canEnPassant(Tile toTheRight, Tile toTheLeft) {
        return this.currentTile.equals(board.getTile(initialTile.getPosition().getxCoordinate() + getAllianceNumber(3), initialTile.getPosition().getyCoordinate()))
                && (((!toTheLeft.isEmpty() && toTheLeft.getPieceOnTile().isPawn()) && toTheLeft.getPieceOnTile().hasMovedOnlyOnce) ||
                (!toTheRight.isEmpty() && toTheRight.getPieceOnTile().isPawn() && toTheRight.getPieceOnTile().hasMovedOnlyOnce));
    }

    private int getAllianceNumber(int number) {
        return this.is(Alliance.WHITE) ? -number : number;
    }
}
