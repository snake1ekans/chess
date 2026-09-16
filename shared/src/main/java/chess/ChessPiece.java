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

            if (piece.getPieceType() == PieceType.KNIGHT) {return knightMove(validMoves, board, myPosition);}
            if (piece.getPieceType() == PieceType.KING) {return kingMove(validMoves, board, myPosition);}
            if (piece.getPieceType() == PieceType.BISHOP) {return bishopMove(validMoves, board, myPosition);}
            if (piece.getPieceType() == PieceType.ROOK) {return rookMove(validMoves, board, myPosition);}
            if (piece.getPieceType() == PieceType.QUEEN) {return queenMove(validMoves, board, myPosition);}

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

    private List<ChessMove> knightMove(List<ChessMove> validMoves, ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();

        if(col-2>0) { //move down
            ChessPosition downLeft = new ChessPosition(row-1, col-2);
            ChessPosition downRight = new ChessPosition(row+1, col-2);
            if (row-1>0 && (board.getPiece(downLeft) == null || board.getPiece(downLeft).team != this.team)) {
                validMoves.add(new ChessMove(position, downLeft, null));}
            if (row+1<=8 && (board.getPiece(downRight) == null || board.getPiece(downRight).team != this.team)) {
                validMoves.add(new ChessMove(position, downRight, null));}
        }

        if(col+2<8) { //move up
            ChessPosition upRight = new ChessPosition(row+1, col+2);
            ChessPosition upLeft = new ChessPosition(row-1, col+2);
            if (row-1>0 && (board.getPiece(upLeft) == null || board.getPiece(upLeft).team != this.team)) {
                validMoves.add(new ChessMove(position, upLeft, null));}
            if (row+1<=8 && (board.getPiece(upRight) == null || board.getPiece(upRight).team != this.team)) {
                validMoves.add(new ChessMove(position, upRight, null));}
        }
        if(row-2>0) { //move left
            ChessPosition leftDown = new ChessPosition(row-2, col-1);
            ChessPosition leftUp = new ChessPosition(row-2, col+1);
            if(col-1>0 && (board.getPiece(leftDown) == null || board.getPiece(leftDown).team != this.team)){
                validMoves.add(new ChessMove(position, leftDown, null));}
            if(col+1<=8 && (board.getPiece(leftUp) == null || board.getPiece(leftUp).team != this.team)) {
                validMoves.add(new ChessMove(position, leftUp, null));}
        }
        if(row+2<8) { //move right
            ChessPosition rightDown = new ChessPosition(row+2, col-1);
            ChessPosition rightUp = new ChessPosition(row+2, col+1);
            if(col-1>0 && (board.getPiece(rightDown) == null || board.getPiece(rightDown).team != this.team)){
                validMoves.add(new ChessMove(position, rightDown, null));}
            if(col+1<=8 &&(board.getPiece(rightUp) == null || board.getPiece(rightUp).team != this.team)){
                validMoves.add(new ChessMove(position, rightUp, null));}
        }
    return validMoves;
    }

    private List<ChessMove> bishopMove(List<ChessMove> validMoves, ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        int i=0;
        //left down
        while(i<8) {
            i++;
            ChessPosition tryPiece = new ChessPosition(row-i, col-i);
            if (row-i<=0 || col-i<=0 || (board.getPiece(tryPiece) != null && board.getPiece(tryPiece).team == this.team)) {
                break;
            }

            validMoves.add(new ChessMove(position, new ChessPosition(row-i, col-i), null));
            if (board.getPiece(tryPiece)!=null && board.getPiece(tryPiece).team != this.team) {break;}
        }
        //left up
        i=0;
        while(i<8) {
            i++;
            ChessPosition tryPiece = new ChessPosition(row-i, col+i);
            if (row-i<=0 || col+i>8|| (board.getPiece(tryPiece) != null && board.getPiece(tryPiece).team == this.team)) {
                break;
            }
            validMoves.add(new ChessMove(position, new ChessPosition(row-i, col+i), null));
            if (board.getPiece(tryPiece)!=null && board.getPiece(tryPiece).team != this.team) {break;}

        }

        //right down
        i=0;
        while(i<8) {
            i++;
            ChessPosition tryPiece = new ChessPosition(row+i, col-i);
            if (row+i>8|| col-i<=0|| (board.getPiece(tryPiece) != null && board.getPiece(tryPiece).team == this.team)) {
                break;
            }
            validMoves.add(new ChessMove(position, new ChessPosition(row+i, col-i), null));
            if (board.getPiece(tryPiece)!=null && board.getPiece(tryPiece).team != this.team) {break;}

        }
        i=0;
        //right up
        while(i<8) {
            i++;
            ChessPosition tryPiece = new ChessPosition(row+i, col+i);
            if (row+i>8 || col+i>8|| (board.getPiece(tryPiece) != null && board.getPiece(tryPiece).team == this.team)) {
                break;
            }
            validMoves.add(new ChessMove(position, new ChessPosition(row+i, col+i), null));
            if (board.getPiece(tryPiece)!=null && board.getPiece(tryPiece).team != this.team) {break;}

        }
        return validMoves;
    }

    private List<ChessMove> rookMove(List<ChessMove> validMoves, ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        int i = 0;

        //left
        while(i<8) {
            i++;
            ChessPosition tryPiece = new ChessPosition(row-i, col);
            if (row-i<=0 || (board.getPiece(tryPiece) != null && board.getPiece(tryPiece).team == this.team)) {
                break;
            }
            validMoves.add(new ChessMove(position, new ChessPosition(row-i, col), null));
            if (board.getPiece(tryPiece)!=null && board.getPiece(tryPiece).team != this.team) {break;}
        } i=0;

        //right
        while(i<8) {
            i++;
            ChessPosition tryPiece = new ChessPosition(row+i, col);
            if (row+i>8 || (board.getPiece(tryPiece) != null && board.getPiece(tryPiece).team == this.team)) {
                break;
            }
            validMoves.add(new ChessMove(position, new ChessPosition(row+i, col), null));
            if (board.getPiece(tryPiece)!=null && board.getPiece(tryPiece).team != this.team) {break;}
        } i=0;

        //down
        while(i<8) {
            i++;
            ChessPosition tryPiece = new ChessPosition(row, col-i);
            if (col-i<=0 || (board.getPiece(tryPiece) != null && board.getPiece(tryPiece).team == this.team)) {
                break;
            }
            validMoves.add(new ChessMove(position, new ChessPosition(row, col-i), null));
            if (board.getPiece(tryPiece)!=null && board.getPiece(tryPiece).team != this.team) {break;}
        } i=0;

        //up
        while(i<8) {
            i++;
            ChessPosition tryPiece = new ChessPosition(row, col+i);
            if (col+i>8|| (board.getPiece(tryPiece) != null && board.getPiece(tryPiece).team == this.team)) {
                break;
            }
            validMoves.add(new ChessMove(position, new ChessPosition(row, col+i), null));
            if (board.getPiece(tryPiece)!=null && board.getPiece(tryPiece).team != this.team) {break;}
        } i=0;
    return validMoves;
    }

    private List<ChessMove> queenMove(List<ChessMove> validMoves, ChessBoard board, ChessPosition position) {
        rookMove(validMoves, board, position);
        bishopMove(validMoves, board, position);
        return validMoves;

    }

    


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return team == that.team && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, type);
    }
}
