package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.AuthData;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

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
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }
    public void stop() {
        javalin.stop();
    }

    private boolean authCheck(Context ctx){
        HashSet<String> valids = AuthService.getValidTokens();
        String token = ctx.header("authorization");
        if (valids == null || !valids.contains(token)){
            ctx.contentType("application/json");
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("msg", "invalid authorization")));
            return false;
        }
        return true;
    }

    private void registerHandler(Context context){
         Vector<String> data = context.bodyAsClass(Vector.class);
         String username = data.elementAt(0);
         String password = data.elementAt(1);
         String email = data.elementAt(2);

         RegisterService registration = new RegisterService(username, password,email);


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
