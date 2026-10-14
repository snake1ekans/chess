package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

// todo: create instances of broader services (userservice, gameservice etc) at server instantiation
// todo: tests from TAs are looking for the specific codes given in phase 3 specs


public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", this::registerHandler);
        javalin.post("/session", this::loginHandler);
        javalin.delete("/session", this::logoutHandler);
        javalin.get("/game", this::listHandler);
        javalin.get("/game", this::createHandler);
        javalin.put("/game", this::joinHandler);
        javalin.delete("/db", this::clearHandler);

        //start services at server instantiation
        AuthService AuthService = new AuthService();
        GameService GameService = new GameService();
        UserService UserService = new UserService();
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }
    public void stop() {
        javalin.stop();
    }



    private void registerHandler(Context context){
         UserData requestData = new Gson().fromJson(context.body(), UserData.class);


    }
    private void loginHandler(Context context){

    }
    private void logoutHandler(Context context){

    }
    private void listHandler(Context context){

    }
    private void createHandler(Context context){

    }
    private void joinHandler(Context context){

    }
    private void clearHandler(Context context){

    }
}
