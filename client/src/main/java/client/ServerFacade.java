package client;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ServerFacade {

    private AuthData authData;
    private final HttpClient httpClient = HttpClient.newHttpClient();

// quits the game, with the "depth" determining the actions that are needed
// based on the level of the user (determined at time of call)
    public void quitClient(int depth){

    }

    public void register(String[] args) throws Exception {
        if (args.length != 4){
            System.out.println("Missing information. Please reenter information and try again.");
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
        } else if (response.statusCode() == 400) {
            System.out.println("Missing information. Please reenter information and try again.");
        } else if (response.statusCode()== 403) {
            System.out.println("A user with that name already exists. Please use a different username or login.");
        } else if (response.statusCode() == 200) {
            authData = new Gson().fromJson(response.body(), AuthData.class);
            System.out.println("Registered Successfully. You are logged in. Please create or Join a Game.");
        }
    }


    public void login(String[] args) throws Exception {
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
        }
    }
}
