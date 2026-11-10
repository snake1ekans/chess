package ui;

import client.ClientMain;

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

}
