package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
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
    private ChessGame.TeamColor team = pieceColor;
    private PieceType type = type; //getting this figured out
    private bool doubleMoved = false;


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
        private vector<vector<int>> validMoves;

        throw new RuntimeException("Not implemented");
    }

    private pawnMove(validMoves) { //there's gotta be an easier way to do this team differentiation

        if (this.team == WHITE) {
            if (position.getrow()==2) { //todo check index, could be 1, don't remember if internally indexed as 1 or 0
                validMoves.add([position.getColumn()])
            } 
        
                

        }
        if (team == BLACK) {


        }
    }
    private queenMove(validMoves) {
        rookMove(validMoves);

    }

    private rookMove(validMoves) {
        //todos: fix pseudocode for capture logic, check loops for internal indexing 
        // valid moving column toward 0
        for (int i=position.getColumn(); i>0; i--) { 
            validMoves.add([position.getRow(),i])
            if (piece in way) {break;}
        }
        //valid moving column toward 8
        for (int i=position.getColumn(); i<8; i++) { 
            validMoves.add([position.getRow(),i])
            if (piece in way) {break;}
        }
        //valid moving row toward 0
        for (int i=position.getRow(); i>0; i--) { 
            validMoves.add([i, position.getColumn()])
            if (piece in way) {break;}
        }
        //valid moving row toward 8
        for (int i=position.getRow(); i<8; i++) { 
            validMoves.add([i, position.getColumn()])
            if (piece in way) {break;}
        }
    }

    private bishopMove(validMoves) {
        
        


    }

    private knightMove(validMoves) { 
        //todo index checks!!!!
        row = position.getRow();
        col = position.getColumn();

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


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return Objects.equals(position, that.position) && team == that.team && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, team, type);
    }
}
