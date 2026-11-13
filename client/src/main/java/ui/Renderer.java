package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ClientMain;
import model.GameData;
import java.util.List;

public class Renderer {

    String EMPTY_WHITE = EscapeSequences.SET_BG_COLOR_WHITE + " " + EscapeSequences.EMPTY + " ";
    String EMPTY_BLACK = EscapeSequences.SET_BG_COLOR_BLACK + " " + EscapeSequences.EMPTY + " ";

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

    public void list(List<GameData> games) {
        //todo: potential for status
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.SET_TEXT_BOLD);
        System.out.print("|  ID  |  NAME  |  WHITE  |  BLACK  |  STATUS  |" + EscapeSequences.RESET_BG_COLOR + "\n");
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

        for (int i=8; i>0; i--){ // iterate column-wise
            for (int j=1; j<9; j++){ // iterate row-wise
                ChessPiece piece = board.getPiece(new ChessPosition(j,i));
            }
        }



    }

    public void renderBoardBlack(GameData game) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        String emptyWhite =



    }



}
// use em-space as the replacement of a piece for board spacing