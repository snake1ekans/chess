package server;

import com.google.gson.Gson;
import exceptions.DataAccessException;
import exceptions.DataConflictException;
import exceptions.IncompleteRequestException;
import exceptions.NotAuthorizedException;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;

import java.util.Map;

// todo: questions: how to get the specific objects the API tests are looking for? How to know code coverage for tests?

public class Server {

    private final Javalin javalin; //server obj
    private final AuthService authService; // AuthService object
    private final GameService gameService; // AuthService object
    private final UserService userService; // AuthService object


    // ------- Publics ---------

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", this::registerHandler);
        javalin.post("/session", this::loginHandler);
        javalin.delete("/session", this::logoutHandler);
        javalin.get("/game", this::listHandler);
        javalin.post("/game", this::createHandler);
        javalin.put("/game", this::joinHandler);
        javalin.delete("/db", this::clearHandler);

        //start services at server instantiation
        authService = new AuthService();
        gameService = new GameService();
        userService = new UserService();
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }
    public void stop() {
        javalin.stop();
    }


    // ---------- Handlers ----------

    private void registerHandler(Context context){
         UserData requestData = new Gson().fromJson(context.body(), UserData.class);
         try {
             userService.register(requestData);
             AuthData token = authService.login(requestData);
             context.contentType("application/json");
             context.status(200);
             context.result(new Gson().toJson(Map.of("username", requestData.username(), "authToken", token.authToken())));
         } catch (DataConflictException user_exists) {
             context.contentType("application/json");
             context.status(403);
             context.result(new Gson().toJson(Map.of("message", "error: already taken")));return;
         } catch (IncompleteRequestException missing_data) {
            badRequestHelper(context);
         }
    }
    private void loginHandler(Context context) throws IncompleteRequestException, DataAccessException, DataConflictException {
        UserData requestData = new Gson().fromJson(context.body(), UserData.class);
        try {
            userService.login(requestData);
            AuthData token = authService.login(requestData);
            context.contentType("application/json");
            context.status(200);
            context.result(new Gson().toJson(Map.of("username", requestData.username(), "authToken", token.authToken())));
        } catch (IncompleteRequestException missingData) {
            badRequestHelper(context);
        } catch (DataConflictException | DataAccessException badData) {
            unauthorizedHelper(context);
        }
    }

    //todo: finish this function
    private void logoutHandler(Context context){
        AuthData authData = new AuthData(null, context.header("authorization"));
        try {
            authService.authorize(authData);
            authService.logout(authData);
            context.status(200);
            context.result();
        } catch (NotAuthorizedException | IncompleteRequestException n) {
            unauthorizedHelper(context);
        }
    }

    // todo: finish this function
    private void listHandler(Context context){
        AuthData authData = new AuthData(null, context.header("authorization"));
        try {
            authService.authorize(authData);
            authService.logout(authData);
            context.status(200);
            context.result();
        } catch (NotAuthorizedException | IncompleteRequestException n) {
            unauthorizedHelper(context);
        }
    }

    // todo: finish this function
    private void createHandler(Context context){

    }

    // todo: finish this function
    private void joinHandler(Context context){

    }

    private void clearHandler(Context context){
        authService.deleteDB();
        userService.deleteDB();
        gameService.deleteDB();

    }

    // ---------- Helpers ----------

    private void badRequestHelper(Context context) {
        context.contentType("application/json");
        context.status(400);
        context.result(new Gson().toJson(Map.of("message", "error: bad request")));
    }

    private void unauthorizedHelper(Context context) {
        context.contentType("application/json");
        context.status(401);
        context.result(new Gson().toJson(Map.of("message", "error: unauthorized")));
    }
}
