package ui;

import client.ClientMain;
import model.GameData;
import java.util.List;

public class Renderer {

    public void introString() {
        System.out.println("Welcome to this glorious mess. Please Log In, Register, or Type 'Help' for assistance." );
    }

    public void invalidCommand(){
        System.out.println("Invalid command. Please enter a valid command or type 'help' for a list of commands.");
    }

    //todo: alter the input to recognize the different states in clientMain
    public void help(ClientMain.States state) {
        //universal commands
        System.out.println("Here are the commands:");
        System.out.println("\t- help (h) - prints out the help menu with available commands");
        System.out.println("\t- quit (q) - quits the game, logs you out, and quits the program");

        //specific to no_login
        if (state == ClientMain.States.NO_LOGIN){
            System.out.println("\t- register [username] [password] [email] - registers a new user");
            System.out.println("\t- login [username] [password] - logs in an existing user");

        // for a logged-in user
        } else if (state == ClientMain.States.LOGGED_IN){
            System.out.println("\t- logout - logs you out");
            System.out.println("\t- create game [Game Name] - creates a new game");
            System.out.println("\t- list games - lists all games, players, and IDs");
            System.out.println("\t- play game [Game ID] [WHITE | BLACK] - Joins the game with the given Game ID and chosen position");
            System.out.println("\t- observe game [Game ID] - allows you to spectate the game with the given Game ID");
        }
    }

    public void list(List<GameData> games){
        //todo: potential for status
//        String renderName = x->(x!=null ? EscapeSequences.SET_TEXT_COLOR_GREEN + "OPEN" : EscapeSequences.SET_TEXT_COLOR_RED + );
        System.out.print(EscapeSequences.SET_TEXT_BOLD);
        System.out.print("|  ID  |  NAME  |  WHITE  |  BLACK  |  STATUS  |");
        for (GameData game : games) {
            boolean open = game.whiteUsername() == null || game.blackUsername() == null;
            System.out.print(EscapeSequences.SET_TEXT_ITALIC);
            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLUE);
            System.out.print("| "  + game.gameID() + " | " + game.gameName() + " | ");
            System.out.print(EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_COLOR_WHITE);
            System.out.print(game.whiteUsername() );
            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(" | " + game.blackUsername() + " | ");
            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
            if (open) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN + "OPEN");
            } else {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "FULL");
            }
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK + " |\n");
        }
    }
}
