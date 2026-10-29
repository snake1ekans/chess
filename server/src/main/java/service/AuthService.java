package service;

import dataaccess.AuthDAC;
import dataaccess.sql.AuthDacSql;
import exceptions.IncompleteRequestException;
import exceptions.NotAuthorizedException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class AuthService {

    AuthDAC dataAccess;

    public AuthService(){
        dataAccess = new AuthDacSql() {};
    }

    // todo: Check to see if UUID duplicates are going to matter
    public AuthData login(UserData user) {
        AuthData token = new AuthData(user.username(), UUID.randomUUID().toString());
        dataAccess.writeToken(token);
        return token;
    }

    public Boolean authorize(AuthData authData) throws IncompleteRequestException {
        if (authData == null || authData.authToken() == null) {
            throw new IncompleteRequestException("missing token");
        }
        boolean auth = dataAccess.checkToken(authData);
        if (!auth) {
            throw new NotAuthorizedException("bad token");
        } else {
            return true;
        }
    }

    public String getUsername(AuthData authData) {
        return dataAccess.getUsername(authData);
    }

    public void logout(AuthData authData){
        dataAccess.deleteToken(authData);
    }

    public void deleteDB() {
        dataAccess.deleteAll();
    }
}
