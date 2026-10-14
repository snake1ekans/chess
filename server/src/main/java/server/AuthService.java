package server;

import dataaccess.AuthDAC;

import java.util.HashSet;

public class AuthService {

    public AuthService(){
        AuthDAC dataAccess = new AuthDAC() {};
    }

    private void login(){

    }

    private boolean authorize(){
        return false;
    }

    private void logout(){

    }
}
