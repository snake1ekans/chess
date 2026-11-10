package client;

import chess.*;
import ui.Renderer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMain {

    public static ServerFacade facade = new ServerFacade();
    public static Renderer renderer = new Renderer();
    public enum States {
        NO_LOGIN, LOGGED_IN, GAMEPLAY
    }
    public static HashMap<Pattern, String> VALID_COMMANDS;

    private static void initializeCommands(){
        VALID_COMMANDS = new HashMap<>();
        VALID_COMMANDS.put(Pattern.compile("^help|^h", Pattern.CASE_INSENSITIVE), "help");
        VALID_COMMANDS.put(Pattern.compile("^quit|^q$", Pattern.CASE_INSENSITIVE), "quit");
        VALID_COMMANDS.put(Pattern.compile("^login", Pattern.CASE_INSENSITIVE), "login");
        VALID_COMMANDS.put(Pattern.compile("^register", Pattern.CASE_INSENSITIVE), "register");
        VALID_COMMANDS.put(Pattern.compile("^logout", Pattern.CASE_INSENSITIVE), "logout");
        VALID_COMMANDS.put(Pattern.compile("^create|^create game", Pattern.CASE_INSENSITIVE), "create");
        VALID_COMMANDS.put(Pattern.compile("^list|^list game|^list games", Pattern.CASE_INSENSITIVE), "list");
        VALID_COMMANDS.put(Pattern.compile("^play|^play game", Pattern.CASE_INSENSITIVE), "play");
        VALID_COMMANDS.put(Pattern.compile("^observe|^observe game", Pattern.CASE_INSENSITIVE), "observe");
        VALID_COMMANDS.put(Pattern.compile("^y|^yes", Pattern.CASE_INSENSITIVE), "confirm");
    }



    public static void main(String[] args) throws IOException {
        States userState = States.NO_LOGIN;

        initializeCommands();
        renderer.introString();
        while (userState == States.NO_LOGIN) {
            String[] input = readInput();
            //help comand code
            if (Objects.equals(input[0], "help")){
                renderer.help(userState);
            //quit command code
            } else if (Objects.equals(input[0], "quit")){
                System.out.println("Are you sure you would like to quit? [Yes | No]");
                String in = readInput()[0];
                if (Objects.equals(in, "confirm")){
                    //todo: this may be a bit inelegant, figure out if there's more that needs to be done
                    System.exit(0);
                }
           //login command code
            } else if (Objects.equals(input[0], "login")) {
                System.out.println("Attempting to login " + input[1] + ". Please wait.");
                try {
                    facade.login(input);
                } catch (Exception e) {
                    System.out.println("Server Error. Please restart.");
                }
            } else if (Objects.equals(input[0], "register")) {
                System.out.println("Attempting to register " + input[1] + " as a new user. Please wait.");
                try {
                    facade.register(input);
                } catch (Exception e) {
                    System.out.println("Server Error. Please restart.");
                }
            }

        }
        while (userState == States.LOGGED_IN) {
            String[] input = readInput();
            if (Objects.equals(input[0], "help")){
                renderer.help(userState);
            } else if (Objects.equals(input[0], "quit")){

            }
        }


    }



    // todo: identify where exception could be thrown
    private static String[] readInput() throws IOException {
        //Read Input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();

        //Convert to ArgsVector
        for (HashMap.Entry<Pattern, String> entry: VALID_COMMANDS.entrySet()){
            try {
                Matcher matcher = entry.getKey().matcher(input);
                while (matcher.find()){
                    if (matcher.start() == 0 || matcher.start() == 1){
                        String[] splitInput = input.split(" ");
                        String[] inputConversion = new String[splitInput.length];
                        inputConversion[0] = entry.getValue();
                        System.arraycopy(splitInput, 1, inputConversion, 1, splitInput.length-1);
                        return inputConversion;
                    }
                }
            } catch (IllegalStateException n){
                continue;
            }
        }
        // invalid command
        return new String[1];
    }
}
