package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.Vector;

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

    private ChessPosition position;
    private boolean doubleMoved = false;


    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return ChessPiece.team;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return p_type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Vector<Vector<Integer>> validMoves;

        throw new RuntimeException("Not implemented");
    }

    private Vector<Vector<Integer>> pawnMove(Vector<Vector<Integer>> validMoves) { //there's gotta be an easier way to do this team differentiation
        return validMoves;
    }

    private Vector<Vector<Integer>>  queenMove(Vector<Vector<Integer>> validMoves) {
        rookMove(validMoves);

        return validMoves;
    }

    private Vector<Vector<Integer>> rookMove(Vector<Vector<Integer>> validMoves) {
        //todo fix pseudocode for capture logic, check loops for internal indexing

        // valid moving column toward 0
        for (int i=position.getColumn(); i>0; i--) { 
            validMoves.add([position.getRow(),i]);
            if (piece in way) {break;}
        }
        //valid moving column toward 8
        for (int i=position.getColumn(); i<8; i++) { 
            validMoves.add([position.getRow(),i]);
            if (piece in way) {break;}
        }
        //valid moving row toward 0
        for (int i=position.getRow(); i>0; i--) { 
            validMoves.add([i, position.getColumn()]);
            if (piece in way) {break;}
        }
        //valid moving row toward 8
        for (int i=position.getRow(); i<8; i++) { 
            validMoves.add([i, position.getColumn()]);
            if (piece in way) {break;}
        }
        return validMoves;
    }

    private Vector<Vector<Integer>> bishopMove(Vector<Vector<Integer>> validMoves) {
        
        

        return validMoves;
    }

    private Vector<Vector<Integer>> knightMove(Vector<Vector<Integer>> validMoves) {
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

    }

}
