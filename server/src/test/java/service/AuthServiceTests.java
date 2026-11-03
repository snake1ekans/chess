package service;

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

    private static AuthService authService = new AuthService();
    private final static UserData BILLY =  new UserData("billy123", "password", "testa@gmail.com" );
    private final static UserData BOB = new UserData("bob1", "pass", "testb@gmail.com");

    @AfterEach
    public void clearDB(){
        authService.deleteDB();
        authService = new AuthService();
    }

    @Test
    @DisplayName("GenerateTokenSuccess")
    public void generateTokenSuccess() throws IncompleteRequestException {
        var billyToken = authService.login(BILLY);
        var bobToken = authService.login(BOB);
        assertTrue(authService.authorize(billyToken));
        assertTrue(authService.authorize(bobToken));
    }

    @Test
    @DisplayName("NoTokenPassed")
    public void noTokenPassed() throws IncompleteRequestException {
        var billyToken = authService.login(BILLY);
        AuthData badToken = new AuthData(null, null);
        assertThrows(IncompleteRequestException.class, () -> authService.authorize(null));
        assertThrows(IncompleteRequestException.class, () -> authService.authorize(badToken));
        assertTrue(authService.authorize(billyToken));
    }

    @Test
    @DisplayName("TokenNotInDB")
    public void tokenNotInDB() throws IncompleteRequestException, NotAuthorizedException {
        var billyToken = authService.login(BILLY);
        AuthData badToken = new AuthData(null, UUID.randomUUID().toString());
        assertThrows(NotAuthorizedException.class, () -> authService.authorize(badToken));
        assertTrue(authService.authorize(billyToken));
    }

    @Test
    @DisplayName("LogoutSuccess")
    public void logoutSuccess() throws IncompleteRequestException {
        var billyToken = authService.login(BILLY);
        assertTrue(authService.authorize(billyToken));
        authService.logout(billyToken);
        assertThrows(NotAuthorizedException.class, () -> authService.authorize(billyToken));
    }
}