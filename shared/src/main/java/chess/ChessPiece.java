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


        return validMoves;
    }


    private List<ChessMove> knightMove(List<ChessMove> validMoves) {
        //todo index checks!!!!
        int row = position.getRow();
        int col = position.getColumn();

        if(col-2>0) { //move down
            if (row-1>0) {validMoves.add([row-1, col-2]);}
            if (row+1<8) {validMoves.add([row+1, col-2]);}
        }
        if(col+2<8) { //move up
            if(row-1>0) {validMoves.add([row-1, col+2]);}
            if(row+1<8) {validMoves.add([row+1, col+2]);}
        }
        if(row-2>0) { //move left
            if(col-1>0){validMoves.add([row-2, col-1])}
            if(col+1<8){validMoves.add([row-2, col+1])}
        }
        if(row+2<8) { //move right
            if(col-1>0){validMoves.add([row+2, col-1])}
            if(col+1<8){validMoves.add([row+2, col+1])}
        }
    return validMoves;
    }

}
