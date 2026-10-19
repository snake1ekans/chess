package service;

import dataaccess.DataConflictException;
import dataaccess.IncompleteRequestException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import server.AuthService;
import server.MissingDataException;
import server.Server;
import server.UserService;

import javax.xml.crypto.Data;
import java.rmi.AccessException;
import java.util.Vector;

public class AuthServiceTests {

    private final static AuthService authService = new AuthService();
    private final static UserData billy =  new UserData("billy123", "password", "testa@gmail.com" );
    private final static UserData bob = new UserData("bob1", "pass", "testb@gmail.com");
    private final static UserData joe = new UserData("joseph", "sampletext", "testc@gmail.com" );
    private final static UserData juan = new UserData("juannyboi", "lorem", "testd@gmail.com");


    @Test
    @DisplayName("GenerateTokenSuccess")
    public void generateTokenSuccess() throws IncompleteRequestException {
        var billyToken = authService.login(billy);
        var bobToken = authService.login(bob);
        assertTrue(authService.authorize(billyToken));
        assertTrue(authService.authorize(bobToken));
    }

//    @Test
//    @DisplayName("No Token Passed")
//    public void noTokenPassed() throws



}