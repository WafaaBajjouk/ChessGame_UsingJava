package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    protected PieceType pieceType;
    protected Alliance pieceAlliance;
    protected int piecePosition;
    protected boolean isFirstMove;
    

  
    protected Piece(PieceType type, Alliance alliance, int piecePosition, boolean isFirstMove) {
        this.pieceType = type;
        this.piecePosition = piecePosition;
        this.pieceAlliance = alliance;
        this.isFirstMove = isFirstMove;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    /**
     * Piece value is used in TakenPiecesPanel for sorting them by piece value
     * @return The value of each piece
     */
    public int getPieceValue() {
        return this.pieceType.getPieceValue();
    }

    public abstract Piece movePiece(Move move);

    public abstract Collection<Move> calculateLegalMoves(Board board);

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }
        Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.piecePosition && pieceType == otherPiece.pieceType &&
                pieceAlliance == otherPiece.pieceAlliance && isFirstMove == otherPiece.isFirstMove;
    }

    @Override
    public int hashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }
}