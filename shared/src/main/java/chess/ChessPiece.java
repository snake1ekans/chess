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
            if (piece.getPieceType() == PieceType.KING) {return kingMove(validMoves, board, myPosition);}

        return validMoves;
    }

    private List<ChessMove> kingMove(List<ChessMove> validMoves, ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();

        ChessPosition downLeft = new ChessPosition(row-1, col-1);
        if (col-1>0 && row-1>0 && (board.getPiece(downLeft) == null || board.getPiece(downLeft).team != this.team)) {
            validMoves.add(new ChessMove(position, downLeft, null));}

        ChessPosition down =new ChessPosition(row, col-1);
        if (col-1>0 && (board.getPiece(down) == null || board.getPiece(down).team != this.team)) {
            validMoves.add(new ChessMove(position, down, null));}

        ChessPosition downRight = new ChessPosition(row+1, col-1);
        if (col-1>0 && row+1 <=8 && (board.getPiece(downRight) == null || board.getPiece(downRight).team != this.team)) {
            validMoves.add(new ChessMove(position, downRight, null));}

        ChessPosition upLeft = new ChessPosition(row-1, col+1);
        if (col+1<=8 && row-1>0 && (board.getPiece(upLeft) == null || board.getPiece(upLeft).team != this.team)) {
            validMoves.add(new ChessMove(position, upLeft, null));}

        ChessPosition up =new ChessPosition(row, col+1);
        if (col+1<=8 && (board.getPiece(up) == null || board.getPiece(up).team != this.team)) {
            validMoves.add(new ChessMove(position, up, null));}

        ChessPosition upRight =new ChessPosition(row+1, col+1);
        if (col+1<=8 && row+1 <=8 && (board.getPiece(upRight) == null || board.getPiece(upRight).team != this.team)) {
            validMoves.add(new ChessMove(position, upRight, null));}

        ChessPosition right = new ChessPosition(row+1, col);
        if (row+1 <=8 && (board.getPiece(right) == null || board.getPiece(right).team != this.team)) {
            validMoves.add(new ChessMove(position, right, null));}

        ChessPosition left = new ChessPosition(row-1, col);
        if (row-1>0 && (board.getPiece(left) == null || board.getPiece(left).team != this.team)) {
            validMoves.add(new ChessMove(position, left, null));}
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
