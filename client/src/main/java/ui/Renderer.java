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
        System.out.println("help (h) - prints out the help menu with available commands");
        System.out.println("quit (q) - quits the game, logs you out, and quits the program");

        if (state == ClientMain.States.NO_LOGIN){
            System.out.println("login - ");

        }
    }

}
