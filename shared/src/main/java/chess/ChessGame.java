package chess;

import java.util.*;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {
        setBoard(new ChessBoard());
        board.resetBoard();
        setTeamTurn(TeamColor.WHITE);
    }

    private TeamColor curr_turn;
    private ChessBoard board;
    public void setBoard(ChessBoard board) {
        this.board = board;
    }
    public ChessBoard getBoard() {
        return board;
    }
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
        Collection v = piece.pieceMoves(getBoard(), startPosition);
        List<ChessMove> valid = new ArrayList<>(v);
        List<ChessMove> rms = new ArrayList<>();
        for (ChessMove i:valid) {
            ChessBoard tempBoard = tempBoardHelper(getBoard(), i);
            if (checkHelper(tempBoard, piece.getTeamColor())){
                rms.add(i);
            }
        }
        valid.removeAll(rms);
        return valid;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition())==null) {
            throw new InvalidMoveException("There isn't a Piece there!");
        }
        if (board.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("Wait your Turn");
        }
        Collection<ChessMove> valids = validMoves(move.getStartPosition());
        if(valids.contains(move)){
            setBoard(tempBoardHelper(board, move));
        } else {
            throw new InvalidMoveException("Invalid Move Entered. Please enter a valid Move");
        }
        //ugly but it works
        if (getTeamTurn() == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    public boolean isInCheck(TeamColor teamColor) {
        return checkHelper(getBoard(), teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> valids = new ArrayList<>(validMoves(findKing(getBoard(), teamColor)));
        return valids.isEmpty() && isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> valids = new ArrayList<>(validMoves(findKing(getBoard(), teamColor)));
        return valids.isEmpty() && !isInCheck(teamColor);
    }




    // ------ helpers -----

    private ChessBoard tempBoardHelper(ChessBoard board, ChessMove testMove){
        ChessBoard testBoard = new ChessBoard();
        for (int i = 1; i<9; i++){
            for (int j = 1; j<9; j++){
                ChessPosition copyPosition = new ChessPosition(i,j);
                ChessPiece copyPiece = board.getPiece(copyPosition);
                testBoard.addPiece(copyPosition, copyPiece);
            }
        }
        ChessPiece movePiece = board.getPiece(testMove.getStartPosition());

        if (movePiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            testBoard.addPiece(testMove.getEndPosition(), new ChessPiece(movePiece.getTeamColor(),testMove.getPromotionPiece()));
        } else {
            testBoard.addPiece(testMove.getEndPosition(), movePiece);
        }

        testBoard.addPiece(testMove.getStartPosition(), null);
        return testBoard;
    }

    private boolean checkHelper(ChessBoard board, TeamColor color) {
        ChessPosition king = findKing(board, color);
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece occupant = board.getPiece(new ChessPosition(i, j));
                if (occupant == null) {
                    continue;
                }
                Collection<ChessMove> moves = occupant.pieceMoves(board, new ChessPosition(i, j));
                for (ChessMove move : moves) {
                    ChessPosition end = move.getEndPosition();
                    //noinspection DataFlowIssue
                    if (end.getRow() == king.getRow() && end.getColumn() == king.getColumn()) {
                        return true;
                    }
                }
            }
        }
        return false;
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
