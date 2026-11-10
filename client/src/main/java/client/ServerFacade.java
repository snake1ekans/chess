package client;

import com.google.gson.Gson;
import model.UserData;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Locale;

public class ServerFacade {

    private String authToken;
    private final HttpClient httpClient = HttpClient.newHttpClient();

// quits the game, with the "depth" determining the actions that are needed
// based on the level of the user (determined at time of call)
    public void quitClient(int depth){

    }

    public void login(String[] args) throws Exception {
        var JsonBody = new Gson().toJson(args);
        String urlstring = "http://localhost:8080/session/";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlstring))
                .timeout(java.time.Duration.ofSeconds(15))
                .POST(HttpRequest.BodyPublishers.ofString(JsonBody))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    }

    public void register(String[] args) throws Exception {
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
        }
    }
}
