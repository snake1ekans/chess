package client;

import chess.*;
import model.GameData;
import ui.Renderer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public class ClientMain {

    public static ServerFacade facade = new ServerFacade(8080);
    public static Renderer renderer = new Renderer();

    public enum States {NO_LOGIN, LOGGED_IN, GAMEPLAY}

    public enum RenderType {WHITE, BLACK, OBSERVE}

    public static HashMap<Function<String, Boolean>, String> validCommands;
    public static GameData activeGame = null;
    public static String activeUser = null;
    private static States userState = States.NO_LOGIN;
    private static RenderType renderType = RenderType.OBSERVE;

    private static void initializeCommands() {
        validCommands = new HashMap<>();
        validCommands.put(x -> Pattern.matches("^[Hh]elp|^h", x), "help");
        validCommands.put(x -> Pattern.matches("^[Qq]uit|^q$", x), "quit");
        validCommands.put(x -> Pattern.matches("^[Ll]ogin$", x), "login");
        validCommands.put(x -> Pattern.matches("^[Rr]egister", x), "register");
        validCommands.put(x -> Pattern.matches("^[Ll]ogout", x), "logout");
        validCommands.put(x -> Pattern.matches("^[Cc]reate|^[Cc]reate [Gg]ame", x), "create");
        validCommands.put(x -> Pattern.matches("^[Ll]ist|^[Ll]ist [Gg]ames?", x), "list");
        validCommands.put(x -> Pattern.matches("^[Pp]lay|^[Pp]lay game", x), "play");
        validCommands.put(x -> Pattern.matches("^[Oo]bserve|^[Oo]bserve [Gg]ame", x), "observe");
        validCommands.put(x -> Pattern.matches("^[Ll]eave$|^[Rr]esign$", x), "leave");
        validCommands.put(x -> Pattern.matches("^[Yy]|^[Yy]es", x), "confirm");
        validCommands.put(x -> Pattern.matches("^[Nn]o?", x), "deny");

    }

// ---------- REPL ----------

    public static void main(String[] args) throws Exception {
        initializeCommands();
        renderer.introString();
        while (true) {
            while (userState == States.NO_LOGIN) {
                String[] input = readInput();
                switch (input[0]) {
                    case ("help"):
                        renderer.help(userState);
                        break;
                    case ("quit"):
                        quitHelper(input);
                        break;
                    case ("login"):
                        loginHelper(input);
                        break;
                    case ("register"):
                        registerHelper(input);
                        break;
                    default:
                        renderer.invalidCommand();
                        break;
                }
            }
            while (userState == States.LOGGED_IN) {
                String[] input = readInput();
                // help command
                switch (input[0]) {
                    case ("help"):
                        renderer.help(userState);
                        break;
                    case ("quit"):
                        quitHelper(input);
                        break;
                    case ("login"):
                        System.out.println("Already logged in. Create or Join a game, or type 'help'");
                        break;
                    case ("logout"):
                        logoutHelper(input);
                        break;
                    case ("create"):
                        createHelper(input);
                        break;
                    case ("list"):
                        listHelper(input);
                        break;
                    case ("play"):
                        joinHelper(input);
                        break;
                    default:
                        break;
                }
            }
            while (userState == States.GAMEPLAY) {
                //todo: De-initialize base game in phase 6
                if (renderType == RenderType.WHITE || renderType == RenderType.OBSERVE) {
                    renderer.renderBoardWhite(activeGame);
                } else {
                    renderer.renderBoardBlack(activeGame);
                }
                String[] input = readInput();
                if (Objects.equals(input[0], "leave")) {
                    String[] leaveArg = new String[3];
                    leaveArg[0] = "leave";
                    leaveArg[1] = activeGame.gameID().toString();
                    leaveArg[2] = (Objects.equals(activeUser, activeGame.blackUsername())) ? "BLACK" : "WHITE";
//                    facade.leave(leaveArg);
                    userState = States.LOGGED_IN;
                    System.out.println("Left game successfully.");
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
        for (HashMap.Entry<Function<String, Boolean>, String> entry : validCommands.entrySet()) {
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
        }
        // invalid command
        renderer.invalidCommand();
        String[] rString = {"bad"};
        return rString;
    }
    private static void joinHelper(String[] input) throws Exception {
        if (input.length < 3){
            System.out.println("Missing arguments. Please enter new data.");
        }
        if (facade.join(input)) {
            userState = States.GAMEPLAY;
            System.out.println("Joined game successfully.");
        }
        if (Objects.equals(input[2], "OBSERVE") || Objects.equals(input[2], "WHITE") ) {
            renderType = RenderType.WHITE;
            activeGame = new GameData(Integer.parseInt(input[1]), activeUser, null, null, new ChessGame());

        } else if (Objects.equals(input[2], "BLACK")) {
            renderType = RenderType.BLACK;
            activeGame = new GameData(Integer.parseInt(input[1]), null, activeUser, null, new ChessGame());

        } else if (Objects.equals(input[2], "OBSERVE")) {
            renderType = RenderType.OBSERVE;
            activeGame = new GameData(Integer.parseInt(input[1]), null, null, null, new ChessGame());
        }
    }
    private static void loginHelper(String[] input){
        try {
            if (facade.login(input)) {
                userState = States.LOGGED_IN;
                activeUser = input[1];
            }
        } catch (Exception e) {
            System.out.println("Server Error. Please restart.");
        }
    }
    private static void registerHelper(String[] input){
        try {
            if (facade.register(input)) {
                userState = States.LOGGED_IN;
            }
        } catch (Exception e) {
            System.out.println("Server Error. Please restart.");
        }
    }
    private static void quitHelper(String[] input) throws IOException {
        System.out.println("Are you sure you would like to quit? [Yes | No]");
        String in = readInput()[0];
        if (Objects.equals(in, "confirm")) {
            if (userState == States.LOGGED_IN) {
                try {
                    if (facade.logout()) {
                        userState = States.NO_LOGIN;
                        System.out.println("Goodbye!");
                        System.exit(0);
                    }
                } catch (Exception e) {
                    System.out.println("Server Error. Please restart.");
                }
            } else {
                System.out.println("Goodbye!");
                System.exit(0);
            }
        }
    }
    private static void logoutHelper(String[] input){
        try {
            if (facade.logout()) {
                userState = States.NO_LOGIN;
            }
        } catch (Exception e) {
            System.out.println("Server Error. Please restart.");
        }
    }
    private static void createHelper(String[] input) {
        try {
            facade.create(input);
        } catch (Exception e) {
            System.out.println("Server Error. Please restart.");
        }
    }
    private static void listHelper(String[] input) {
        try {
            renderer.list(facade.list());
        } catch (Exception e) {
            System.out.println("Server Error. Please restart.");
        }
    }
}
