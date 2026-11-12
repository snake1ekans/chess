package client;

import chess.*;
import ui.Renderer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMain {

    public static ServerFacade facade = new ServerFacade();
    public static Renderer renderer = new Renderer();
    public enum States {
        NO_LOGIN, LOGGED_IN, GAMEPLAY
    }
    public static HashMap<Function<String, Boolean>, String> VALID_COMMANDS;

    private static void initializeCommands(){
        VALID_COMMANDS = new HashMap<>();
        VALID_COMMANDS.put(x->Pattern.matches("^[Hh]elp|^h", x), "help");
        VALID_COMMANDS.put(x->Pattern.matches("^[Qq]uit|^q$", x), "quit");
        VALID_COMMANDS.put(x->Pattern.matches("^[Ll]ogin$", x), "login");
        VALID_COMMANDS.put(x->Pattern.matches("^[Rr]egister", x), "register");
        VALID_COMMANDS.put(x->Pattern.matches("^[Ll]ogout", x), "logout");
        VALID_COMMANDS.put(x->Pattern.matches("^[Cc]reate|^[Cc]reate [Gg]ame", x), "create");
        VALID_COMMANDS.put(x->Pattern.matches("^[Ll]ist|^[Ll]ist [Gg]ames?", x), "list");
        VALID_COMMANDS.put(x->Pattern.matches("^[Pp]lay|^[Pp]lay game", x), "play");
        VALID_COMMANDS.put(x->Pattern.matches("^[Oo]bserve|^[Oo]bserve [Gg]ame", x), "observe");
        VALID_COMMANDS.put(x->Pattern.matches("^[Yy]|^[Yy]es", x), "confirm");


//        VALID_COMMANDS.put("^[Yy]|^[Yy]es", "confirm");
    }

// ---------- REPL ----------

    public static void main(String[] args) throws Exception {
        States userState = States.NO_LOGIN;
        initializeCommands();
        renderer.introString();
        while (true) {
            // startup flow WORKS
            while (userState == States.NO_LOGIN) {
                String[] input = readInput();
                //help command code
                if (Objects.equals(input[0], "help")) {renderer.help(userState);}
                //quit command code
                if (Objects.equals(input[0], "quit")) {
                    System.out.println("Are you sure you would like to quit? [Yes | No]");
                    String in = readInput()[0];
                    if (Objects.equals(in, "confirm")) {
                        //todo: this may be a bit inelegant, figure out if there's more that needs to be done
                        System.out.println("Goodbye!");
                        System.exit(0);
                    }
                }
                //login command code
                if (Objects.equals(input[0], "login")) {
                    System.out.println("Attempting to login " + input[1] + ". Please wait.");
                    try {
                        if (facade.login(input)) {
                            userState = States.LOGGED_IN;
                        }
                    } catch (Exception e) {
                        System.out.println("Server Error. Please restart.");
                    }
                }
                // register command code
                if (Objects.equals(input[0], "register")) {
                    System.out.println("Attempting to register " + input[1] + " as a new user. Please wait.");
                    try {
                        if (facade.register(input)) {
                            userState = States.LOGGED_IN;
                        }
                    } catch (Exception e) {
                        System.out.println("Server Error. Please restart.");
                    }
                }
                // out-of flow command
                if (Objects.equals(input[0], "create") ||
                    Objects.equals(input[0], "logout") ||
                    Objects.equals(input[0], "list") ||
                    Objects.equals(input[0], "observe") ||
                    Objects.equals(input[0], "play") ||
                    Objects.equals(input[0], "confirm")) {
                    renderer.invalidCommand();
                }
            }
            //logged-in flow WIP
            while (userState == States.LOGGED_IN) {
                String[] input = readInput();
                // help command
                if (Objects.equals(input[0], "help")) {
                    renderer.help(userState);
                }
                // quit command
                if (Objects.equals(input[0], "quit")) {
                    System.out.println("Are you sure you would like to quit? [Yes | No]");
                    String in = readInput()[0];
                    if (Objects.equals(in, "confirm")) {
                        try {
                            if (facade.logout()) {
                                userState = States.NO_LOGIN;
                            }
                        } catch (Exception e) {
                            System.out.println("Server Error. Please restart.");
                        }
                    }
                }
                // double-login catch
                if (Objects.equals(input[0], "login")) {
                    System.out.println("Already logged in. Create or Join a game, or type 'help'");
                }
                // logout command
                if (Objects.equals(input[0], "logout")) {
                    System.out.println("Logging out.");
                    try {
                        if (facade.logout()) {
                            userState = States.NO_LOGIN;
                        }
                    } catch (Exception e) {
                        System.out.println("Server Error. Please restart.");
                    }
                }
                // create game command
                if (Objects.equals(input[0], "create")){
                    System.out.println("Creating Game.");
                    try {
                        facade.create(input);
                    } catch (Exception e) {
                        System.out.println("Server Error. Please restart.");
                    }
                }
            }
        }
    }

// ---------- Helpers ----------

    // todo: identify where exception could be thrown
    private static String[] readInput() throws IOException {
        //Read Input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();

        //Convert to ArgsVector
        for (HashMap.Entry<Function<String, Boolean>, String> entry: VALID_COMMANDS.entrySet()) {
            String[] splitInput = input.split(" ");
            //lambda to see if the function is right
            if (entry.getKey().apply(splitInput[0])) {
                String[] inputConversion = new String[splitInput.length];
                inputConversion[0] = entry.getValue();
                if (splitInput.length == 1) {
                    return inputConversion;
                } else {
                    if (Pattern.matches("^games?$", splitInput[1])) {
                        System.arraycopy(splitInput, 2, inputConversion, 1, splitInput.length - 2);
                    } else {
                        System.arraycopy(splitInput, 1, inputConversion, 1, splitInput.length - 1);
                    }
                }
                return inputConversion;
            }
            continue;
        }
        // invalid command
        renderer.invalidCommand();
        return new String[1];
    }
}


