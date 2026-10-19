package server;

import dataaccess.AuthDAC;
import dataaccess.AuthDAC_Loc;
import model.AuthData;
import model.UserData;

import java.util.HashSet;
import java.util.UUID;

public class AuthService {

    AuthDAC dataAccess;
    // todo: switch to SQL at the beginning of phase 4
    public AuthService(){
        dataAccess = new AuthDAC_Loc() {};
    }

    // todo: Check to see if UUID duplicates are going to matter
    public AuthData login(UserData user) {
        AuthData token = new AuthData(user.username(), UUID.randomUUID().toString());
        dataAccess.writeToken(token);
        return token;
    }

    public boolean authorize(AuthData authData) {
        return dataAccess.checkToken(authData);
    }

    public void logout(AuthData authData){


    }
    public void deleteDB() {
        dataAccess.deleteAll();
    }
}
