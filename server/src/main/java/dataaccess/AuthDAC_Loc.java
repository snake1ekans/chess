package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.HashSet;

public class AuthDAC_Loc implements AuthDAC{
    HashMap<String,AuthData> authData;

    protected AuthDAC_Loc(){
        authData = new HashMap<>();
    }

    public void writeToken(AuthData token) {
        authData.put(token.authToken(), token);
    }
    public boolean checkToken(AuthData token) {
        return authData.containsKey(token.authToken());
    }
    public void deleteToken(AuthData token) {
        authData.remove(token.authToken());
    }

    public void deleteAll() {
        authData.clear();
    }
}
