package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ClientMain;
import model.GameData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Renderer {

    static final String EMPTY_WHITE = EscapeSequences.SET_BG_COLOR_WHITE + " " + EscapeSequences.EMPTY + " ";
    static final String EMPTY_BLACK = EscapeSequences.SET_BG_COLOR_BLACK + " " + EscapeSequences.EMPTY + " ";
    static final String OPEN_WHITE = EscapeSequences.SET_BG_COLOR_WHITE + " ";
    static final String OPEN_BLACK = EscapeSequences.SET_BG_COLOR_BLACK + " ";

    public void introString() {
        System.out.println("Welcome to this glorious mess. Please Log In, Register, or Type 'Help' for assistance.");
    }

    public void invalidCommand() {
        System.out.println("Invalid command. Please enter a valid command or type 'help' for a list of commands.");
    }

    //todo: alter the input to recognize the different states in clientMain
    public void help(ClientMain.States state) {
        //universal commands
        System.out.println("Here are the commands:");
        System.out.println("\t- help (h) - prints out the help menu with available commands");
        System.out.println("\t- quit (q) - quits the game, logs you out, and quits the program");

        //specific to no_login
        if (state == ClientMain.States.NO_LOGIN) {
            System.out.println("\t- register [username] [password] [email] - registers a new user");
            System.out.println("\t- login [username] [password] - logs in an existing user");

            // for a logged-in user
        } else if (state == ClientMain.States.LOGGED_IN) {
            System.out.println("\t- logout - logs you out");
            System.out.println("\t- create game [Game Name] - creates a new game");
            System.out.println("\t- list games - lists all games, players, and IDs");
            System.out.println("\t- play game [Game ID] [WHITE | BLACK] - Joins the game with the given Game ID and chosen position");
            System.out.println("\t- observe game [Game ID] - allows you to spectate the game with the given Game ID");
        }
    }

    public void list(ArrayList<GameData> games) {
        //todo: potential for status
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.SET_TEXT_BOLD);
        System.out.print("|  ID  |  NAME  |  WHITE  |  BLACK  |  STATUS  |" +
                EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");
        System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);

        for (GameData game : games) {
            String whiteRender = game.whiteUsername() == null ? EscapeSequences.SET_TEXT_COLOR_GREEN + "OPEN"
                    : EscapeSequences.SET_TEXT_COLOR_RED + game.whiteUsername();
            String blackRender = game.blackUsername() == null ? EscapeSequences.SET_TEXT_COLOR_GREEN + "OPEN" + " "
                    : EscapeSequences.SET_TEXT_COLOR_RED + game.blackUsername() + " ";

            boolean open = game.whiteUsername() == null || game.blackUsername() == null;
            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + "| ");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + game.gameID());
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + " | ");
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + game.gameName());
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + " |");

            //White player
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.SET_TEXT_ITALIC +
                    EscapeSequences.WHITE_QUEEN + whiteRender);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.RESET_TEXT_ITALIC + " | ");
            //Black Player
            System.out.print(EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_ITALIC +
                    EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.BLACK_QUEEN + blackRender);
            System.out.print(EscapeSequences.RESET_TEXT_ITALIC + EscapeSequences.SET_BG_COLOR_WHITE +
                    EscapeSequences.SET_TEXT_COLOR_BLACK + " | ");
            //Vacancy status
            if (open) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN + EscapeSequences.SET_TEXT_ITALIC + "OPEN");
            } else {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.SET_TEXT_ITALIC + "FULL");
            }
            System.out.print(EscapeSequences.RESET_TEXT_ITALIC + EscapeSequences.SET_TEXT_COLOR_BLACK + " | "
                    + EscapeSequences.RESET_BG_COLOR + "\n" + EscapeSequences.RESET_TEXT_COLOR);
        }
    }

    public void renderBoardWhite(GameData game) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        ChessBoard board = game.game().getBoard();
        System.out.print(EMPTY_BLACK  + "   a    b    c    d    e    f    g    h  "
                + EMPTY_BLACK + EscapeSequences.RESET_BG_COLOR + "\n");
        for (int i=8; i>0; i--){ // iterate column-wise

            String rowString = OPEN_BLACK + "  " + i + "  ";
            for (int j=1; j<9; j++){ // iterate row-wise
                rowString = rowString.concat(renderSquare(board, i, j));
            }
            rowString = rowString.concat(OPEN_BLACK + " " + i + "  ");
            rowString = rowString.concat(EscapeSequences.RESET_BG_COLOR+"\n");
            System.out.print(rowString);
        }
        System.out.print(EMPTY_BLACK  + "   a    b    c    d    e    f    g    h  "
                + EMPTY_BLACK + EscapeSequences.RESET_BG_COLOR + "\n");
    }

    public void renderBoardBlack(GameData game) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        ChessBoard board = game.game().getBoard();
        System.out.print(EMPTY_BLACK  + "   h    g    f    e    d    c    b    a  "
                + EMPTY_BLACK + EscapeSequences.RESET_BG_COLOR + "\n");
        for (Integer i=1; i<9; i++){ // iterate column-wise
            String rowString = OPEN_BLACK + "  " + i + "  ";
            for (Integer j=8; j>0; j--){ // iterate row-wise
                rowString = rowString.concat(renderSquare(board, i, j));
            }
            rowString = rowString.concat(OPEN_BLACK + " " + i + "  ");
            rowString = rowString.concat(EscapeSequences.RESET_BG_COLOR+"\n");
            System.out.print(rowString);
        }
        System.out.print(EMPTY_BLACK + EscapeSequences.SET_TEXT_COLOR_WHITE + "   h    g    f    e    d    c    b    a  "
                + EMPTY_BLACK + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n");
    }


    // ----------- Helpers ----------
    private String renderSquare(ChessBoard board, Integer i, Integer j){
        String rowString = "";
        ChessPiece piece = board.getPiece(new ChessPosition(i,j));
        boolean blackSquareCheck = (i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1);
        if (piece == null) {
            rowString = rowString.concat(blackSquareCheck ? EMPTY_BLACK : EMPTY_WHITE);
            return rowString;
        }

        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        switch (piece.getPieceType()) {
            case ChessPiece.PieceType.BISHOP:
                rowString = rowString.concat(blackSquareCheck ? OPEN_BLACK : OPEN_WHITE);
                rowString = rowString.concat(pieceColor == ChessGame.TeamColor.BLACK ?
                        EscapeSequences.BLACK_BISHOP : EscapeSequences.WHITE_BISHOP);
                rowString = rowString.concat(" ");
                break;
            case ChessPiece.PieceType.PAWN:
                rowString = rowString.concat(blackSquareCheck ? OPEN_BLACK : OPEN_WHITE);
                rowString = rowString.concat(pieceColor == ChessGame.TeamColor.BLACK ?
                        EscapeSequences.BLACK_PAWN : EscapeSequences.WHITE_PAWN);
                rowString = rowString.concat(" ");
                break;
            case ChessPiece.PieceType.KING:
                rowString = rowString.concat(blackSquareCheck ? OPEN_BLACK : OPEN_WHITE);
                rowString = rowString.concat(pieceColor == ChessGame.TeamColor.BLACK ?
                        EscapeSequences.BLACK_KING : EscapeSequences.WHITE_KING);
                rowString = rowString.concat(" ");
                break;
            case ChessPiece.PieceType.QUEEN:
                rowString = rowString.concat(blackSquareCheck ? OPEN_BLACK : OPEN_WHITE);
                rowString = rowString.concat(pieceColor == ChessGame.TeamColor.BLACK ?
                        EscapeSequences.BLACK_QUEEN : EscapeSequences.WHITE_QUEEN);
                rowString = rowString.concat(" ");
                break;
            case ChessPiece.PieceType.ROOK:
                rowString = rowString.concat(blackSquareCheck ? OPEN_BLACK : OPEN_WHITE);
                rowString = rowString.concat(pieceColor == ChessGame.TeamColor.BLACK ?
                        EscapeSequences.BLACK_ROOK : EscapeSequences.WHITE_ROOK);
                rowString = rowString.concat(" ");
                break;
            case ChessPiece.PieceType.KNIGHT:
                rowString = rowString.concat(blackSquareCheck ? OPEN_BLACK : OPEN_WHITE);
                rowString = rowString.concat(pieceColor == ChessGame.TeamColor.BLACK ?
                        EscapeSequences.BLACK_KNIGHT : EscapeSequences.WHITE_KNIGHT);
                rowString = rowString.concat(" ");
                break;
        }
        return rowString;
    }


}
// use em-space as the replacement of a piece for board spacing