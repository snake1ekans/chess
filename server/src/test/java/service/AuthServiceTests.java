package java.service;

import exceptions.IncompleteRequestException;
import exceptions.NotAuthorizedException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests {

    private final static AuthService AUTH_SERVICE = new AuthService();
    private final static UserData BILLY =  new UserData("billy123", "password", "testa@gmail.com" );
    private final static UserData BOB = new UserData("bob1", "pass", "testb@gmail.com");

    @AfterEach
    public void clearDB(){
        AUTH_SERVICE.deleteDB();
    }

    @Test
    @DisplayName("GenerateTokenSuccess")
    public void generateTokenSuccess() throws IncompleteRequestException {
        var billyToken = AUTH_SERVICE.login(BILLY);
        var bobToken = AUTH_SERVICE.login(BOB);
        assertTrue(AUTH_SERVICE.authorize(billyToken));
        assertTrue(AUTH_SERVICE.authorize(bobToken));
    }

    @Test
    @DisplayName("NoTokenPassed")
    public void noTokenPassed() throws IncompleteRequestException {
        var billyToken = AUTH_SERVICE.login(BILLY);
        AuthData badToken = new AuthData(null, null);
        assertThrows(IncompleteRequestException.class, () -> AUTH_SERVICE.authorize(null));
        assertThrows(IncompleteRequestException.class, () -> AUTH_SERVICE.authorize(badToken));
        assertTrue(AUTH_SERVICE.authorize(billyToken));
    }

    @Test
    @DisplayName("TokenNotInDB")
    public void tokenNotInDB() throws IncompleteRequestException, NotAuthorizedException {
        var billyToken = AUTH_SERVICE.login(BILLY);
        AuthData badToken = new AuthData(null, UUID.randomUUID().toString());
        assertThrows(NotAuthorizedException.class, () -> AUTH_SERVICE.authorize(badToken));
        assertTrue(AUTH_SERVICE.authorize(billyToken));
    }

    @Test
    @DisplayName("LogoutSuccess")
    public void logoutSuccess() throws IncompleteRequestException {
        var billyToken = AUTH_SERVICE.login(BILLY);
        assertTrue(AUTH_SERVICE.authorize(billyToken));
        AUTH_SERVICE.logout(billyToken);
        assertThrows(NotAuthorizedException.class, () -> AUTH_SERVICE.authorize(billyToken));
    }

    // todo: this test could be needed to prevent multiple login
//    @Test
//    @DisplayName("MultipleLogin")
//    public void muiltipleLogin() throws IncompleteRequestException {
//        var billyToken = AUTH_SERVICE.login(billy);
//        assertTrue(AUTH_SERVICE.authorize(billyToken));
//        assertThrows(AUTH_SERVICE.login(billy))
//    }

}