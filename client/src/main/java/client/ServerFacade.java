package client;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.JoinRequestData;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ServerFacade {

    private AuthData authData;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    JoinRequestData joinedGame = null;

    // functions with boolean return values can change the state of the program.
    // a True return means that the state will change to the appropriate state.

    public boolean register(String[] args) throws Exception {
        if (args.length != 4){
            System.out.println("Missing information. Please reenter information and try again.");
            return false;
        }
        UserData register = new UserData(args[1], args[2], args[3]);
        var JsonBody = new Gson().toJson(register);
        String urlstring = "http://localhost:8080/user";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlstring))
                .POST(HttpRequest.BodyPublishers.ofString(JsonBody))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 500) {
            System.out.println("Server Error. Please try again.");
            return false;
        } else if (response.statusCode() == 400) {
            System.out.println("Missing Information. Please reenter information and try again.");
            return false;
        } else if (response.statusCode()== 403) {
            System.out.println("A user with that name already exists. Please use a different username or login.");
            return false;
        } else if (response.statusCode() == 200) {
            authData = new Gson().fromJson(response.body(), AuthData.class);
            System.out.println("Registered Successfully. You are logged in. Please create or join a Game.");
            return true;
        }
        return false;
    }

    public boolean login(String[] args) throws Exception {
        if (args.length < 3){
            System.out.println("Missing Information. Please reenter information and try again.");
            return false;
        }
        UserData register = new UserData(args[1], args[2], null);
        var JsonBody = new Gson().toJson(register);
        String urlstring = "http://localhost:8080/session";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlstring))
                .POST(HttpRequest.BodyPublishers.ofString(JsonBody))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 500) {
            System.out.println("Server Error. Please try again.");
            return false;
        } else if (response.statusCode() == 400) {
            System.out.println("Missing Information. Please reenter information and try again.");
            return false;
        } else if (response.statusCode() == 401) {
            System.out.println("Username or Password is incorrect. Please reenter information and try again.");
            return false;
        } else if (response.statusCode() == 200) {
            authData = new Gson().fromJson(response.body(), AuthData.class);
            System.out.println("Logged in Successfully. Please create or join a Game.");
            return true;
        }
        return false;
    }

    public boolean logout() throws Exception {
        String urlstring = "http://localhost:8080/session";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlstring))
                .DELETE()
                .header("Content-Type", "application/json")
                .header("Authorization", authData.authToken())
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 500) {
            System.out.println("Server Error. Please try again.");
            return false;
        } else if (response.statusCode() == 401) {
            System.out.println("Missing information. Please try again.");
            return false;
        } else if (response.statusCode() == 200) {
            authData = new Gson().fromJson(response.body(), AuthData.class);
            System.out.println("Logged out Successfully. You may log in or quit the program.");
            return true;
        }
        return false;
    }

    public void create(String[] args) throws Exception {
        GameData game = new GameData(null, null, null, args[1], null);
        var JsonBody = new Gson().toJson(game);
        String urlstring = "http://localhost:8080/game";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlstring))
                .POST(HttpRequest.BodyPublishers.ofString(JsonBody))
                .header("Content-Type", "application/json")
                .header("Authorization", authData.authToken())
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 500) {
            System.out.println("Server Error. Please try again.");
        } else if (response.statusCode() == 400) {
            System.out.println("Missing Information. Please reenter information and try again.");
        } else if (response.statusCode() == 401) {
            System.out.println("Missing Authorization. Please log out, log back in, and try again.");
        } else if (response.statusCode() == 200) {
            var gameId = new Gson().fromJson(response.body(), Map.class);
            System.out.printf("Game '%s' created with ID : %.0f. Join the game to play.\n", game.gameName(), gameId.get("gameID"));
        }
    }

    public List<GameData> list() throws Exception {
        String urlstring = "http://localhost:8080/game";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlstring))
                .GET()
                .header("Authorization", authData.authToken())
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 500) {
            System.out.println("Server Error. Please try again.");
        } else if (response.statusCode() == 400) {
            System.out.println("Missing Information. Please reenter information and try again.");
        } else if (response.statusCode() == 401) {
            System.out.println("Missing Authorization. Please log out, log back in, and try again.");
        } else if (response.statusCode() == 200) {
            Type listType = new TypeToken<Map<String, List<GameData>>>(){}.getType();
            Map<String, List<GameData>> gamesMap =  new Gson().fromJson(response.body(), listType);
            return gamesMap.get("games");
        }
        return new ArrayList<>();
    }

    public boolean join(String[] args) throws Exception {
        try{
            JoinRequestData joinRequest = new JoinRequestData(Integer.parseInt(args[1]), args[2], authData.username());
            if (joinOrLeave(joinRequest)) {
                joinedGame = joinRequest;
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException n) {
            System.out.println("Invalid game ID. Try again.");
            return false;
        } catch (Exception e) {
            System.out.println("Server Error. Try again.");
        }
        return false;
    }

    public boolean leave(String[] args) {
        try{
            JoinRequestData leaveRequest = new JoinRequestData(Integer.parseInt(args[1]), args[2], null);
            if (joinOrLeave(leaveRequest)) {
                joinedGame = null;
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException n) {
            System.out.println("Invalid game ID. Try again.");
            return false;
        } catch (Exception e) {
            System.out.println("Server Error. Try again.");
        }
        return false;
    }


// ---------- helpers ----------
    private boolean joinOrLeave(JoinRequestData requestData) throws Exception {
        var JsonBody = new Gson().toJson(requestData);
        String urlstring = "http://localhost:8080/game";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlstring))
                .PUT(HttpRequest.BodyPublishers.ofString(JsonBody))
                .header("Content-Type", "application/json")
                .header("Authorization", authData.authToken())
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 500) {
            System.out.println("Server Error. Please try again.");
        } else if (response.statusCode() == 400) {
            System.out.println("Missing Information. Please reenter information and try again.");
        } else if (response.statusCode() == 401) {
            System.out.println("Missing Authorization. Please log out, log back in, and try again.");
        } else if (response.statusCode() == 403) {
            System.out.println("Spot already taken. Please join another game.");
        } else if (response.statusCode() == 200) {
            System.out.println("Joined game successfully.");
            joinedGame = requestData;
            return true;
        }
        return false;
    }
}
