package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        final ChessGame.TeamColor team = pieceColor;
        final ChessPiece.PieceType p_type = type;
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
        //return ChessGame.TeamColor.team;
        return ChessGame.TeamColor.BLACK; //temp
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        //return p_type;
        return PieceType.PAWN; //temp
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
            if (piece.getPieceType() == PieceType.KNIGHT) {return knightMove(validMoves);}
//todo write the rest of the piece logic
        return validMoves;
    }


    private List<ChessMove> knightMove(List<ChessMove> validMoves) {
        //todo index checks!!!!
        int row = position.getRow();
        int col = position.getColumn();

        if(col-2>0) { //move down
            if (row-1>0 && ) {validMoves.add(new ChessMove(position, new ChessPosition(row-1, col-2), PieceType.KNIGHT));}
            if (row+1<=8) {validMoves.add(new ChessMove(position, new ChessPosition(row+1, col-2), PieceType.KNIGHT));}
        }
        if(col+2<8) { //move up
            if(row-1>0) {validMoves.add(new ChessMove(position, new ChessPosition(row-1, col+2), PieceType.KNIGHT));}
            if(row+1<=8) {validMoves.add(new ChessMove(position, new ChessPosition(row+1, col+2), PieceType.KNIGHT));}
        }
        if(row-2>0) { //move left
            if(col-1>0){validMoves.add(new ChessMove(position, new ChessPosition(row-2, col-1), PieceType.KNIGHT));}
            if(col+1<=8){validMoves.add(new ChessMove(position, new ChessPosition(row-2, col+1), PieceType.KNIGHT));}
        }
        if(row+2<8) { //move right
            if(col-1>0){validMoves.add(new ChessMove(position, new ChessPosition(row+2, col-1), PieceType.KNIGHT));}
            if(col+1<=8){validMoves.add(new ChessMove(position, new ChessPosition(row+2, col+1), PieceType.KNIGHT));}
        }
    return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(position);
    }
}
