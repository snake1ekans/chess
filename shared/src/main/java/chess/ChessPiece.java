package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    final ChessGame.TeamColor team;
    final ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.team = pieceColor;
        this.type = type;
    }

    private ChessPosition position;
    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }
    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return team;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<ChessMove>();
            ChessPiece piece = board.getPiece(myPosition);
            if (piece.getPieceType() == PieceType.KNIGHT) {return knightMove(validMoves, board);}
            if (piece.getPieceType() == PieceType.KING) {return kingMove(validMoves, board);}

        return validMoves;
    }

    private List<ChessMove> kingMove(List<ChessMove> validMoves, ChessBoard board) {
        int row = position.getRow();
        int col = position.getColumn();

        if (col-1>0 && row-1>0 && board.getPiece(new ChessPosition(row-1, col-1)).team != this.team) {
            validMoves.add(new ChessMove(position, new ChessPosition(row-1, col-1), null));}

        if (col-1>0 && board.getPiece(new ChessPosition(row, col-1)).team != this.team) {
            validMoves.add(new ChessMove(position, new ChessPosition(row, col-1), null));}

        if (col-1>0 && row+1 <=8 && board.getPiece(new ChessPosition(row+1, col-1)).team != this.team) {
            validMoves.add(new ChessMove(position, new ChessPosition(row+1, col-1), null));}

        if (col+1<=8 && row-1>0 && board.getPiece(new ChessPosition(row-1, col+1)).team != this.team) {
            validMoves.add(new ChessMove(position, new ChessPosition(row-1, col+1), null));}

        if (col+1<=8 && board.getPiece(new ChessPosition(row, col+1)).team != this.team) {
            validMoves.add(new ChessMove(position, new ChessPosition(row, col+1), null));}

        if (col+1<=8 && row+1 <=8 && board.getPiece(new ChessPosition(row+1, col+1)).team != this.team) {
            validMoves.add(new ChessMove(position, new ChessPosition(row+1, col+1), null));}

        if (row+1 <=8 && board.getPiece(new ChessPosition(row+1, col)).team != this.team) {
            validMoves.add(new ChessMove(position, new ChessPosition(row+1, col), null));}

        if (row-1>0 && board.getPiece(new ChessPosition(row-1, col)).team != this.team) {
            validMoves.add(new ChessMove(position, new ChessPosition(row-1, col), null));}
        return validMoves;
    }

    private List<ChessMove> knightMove(List<ChessMove> validMoves, ChessBoard board) {
        //todo same-team collisions
        int row = position.getRow();
        int col = position.getColumn();

        if(col-2>0) { //move down

            if (row-1>0 && board.getPiece(new ChessPosition(row-1, col-2)).team != this.team) {
                validMoves.add(new ChessMove(position, new ChessPosition(row-1, col-2), null));}
            if (row+1<=8 && board.getPiece(new ChessPosition(row+1, col-2)).team != this.team) {
                validMoves.add(new ChessMove(position, new ChessPosition(row+1, col-2), null));}
        }
        if(col+2<8) { //move up
            if (row-1>0 && board.getPiece(new ChessPosition(row-1, col-2)).team != this.team) {
                validMoves.add(new ChessMove(position, new ChessPosition(row-1, col-2), null));}
            if (row+1<=8 && board.getPiece(new ChessPosition(row+1, col-2)).team != this.team) {
                validMoves.add(new ChessMove(position, new ChessPosition(row+1, col-2), null));}
        }
        if(row-2>0) { //move left
            if(col-1>0 && board.getPiece(new ChessPosition(row-2, col-1)).team != this.team){
                validMoves.add(new ChessMove(position, new ChessPosition(row-2, col-1), null));}
            if(col+1<=8 && board.getPiece(new ChessPosition(row-2, col+1)).team != this.team){
                validMoves.add(new ChessMove(position, new ChessPosition(row-2, col+1), null));}
        }
        if(row+2<8) { //move right
            if(col-1>0 && board.getPiece(new ChessPosition(row+2, col-1)).team != this.team){
                validMoves.add(new ChessMove(position, new ChessPosition(row-2, col-1), null));}
            if(col+1<=8 && board.getPiece(new ChessPosition(row+2, col+1)).team != this.team){
                validMoves.add(new ChessMove(position, new ChessPosition(row-2, col+1), null));}
        }
    return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return team == that.team && type == that.type && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, type, position);
    }
}
