package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {

    }
    private TeamColor curr_turn;
    private ChessBoard board;


    public TeamColor getTeamTurn() {
        return curr_turn;
    }

    public void setTeamTurn(TeamColor team) {
        curr_turn = team;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = getBoard().getPiece(startPosition);
        return piece.pieceMoves(getBoard(), startPosition);
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece agent = board.getPiece(move.getStartPosition());
        Collection<ChessMove> valid = agent.pieceMoves(getBoard(), move.getStartPosition());

    }

    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king = findKing(getBoard(), teamColor);
        for (int i=1; i<9; i++) {
            for (int j = 1; j<9; j++){
                ChessPiece occupant = board.getPiece(new ChessPosition(i,j));
                if (occupant==null) {continue;}
                Collection<ChessMove> moves = occupant.pieceMoves(getBoard(), new ChessPosition(i,j));
                for (ChessMove move : moves) {
                    ChessPosition end = move.getEndPosition();
                    if (end.getRow() == king.getRow() && end.getColumn() == king.getColumn()) {return true;}
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    private ChessPosition findKing(ChessBoard board, TeamColor color) {
        for (int i=1; i<9; i++) {
            for (int j = 1; j<9; j++){
                ChessPiece occupant = board.getPiece(new ChessPosition(i,j));
                if (occupant == null) {continue;}
                if (occupant.getPieceType() == ChessPiece.PieceType.KING
                && occupant.getTeamColor() == color){
                    return new ChessPosition(i,j);
                }
            }
        }
        return null;
    }


    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return curr_turn == chessGame.curr_turn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(curr_turn, board);
    }
}
