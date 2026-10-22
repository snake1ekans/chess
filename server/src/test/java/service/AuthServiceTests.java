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

    private final static AuthService TestAuthService = new AuthService();
    private final static UserData UserBilly =  new UserData("billy123", "password", "testa@gmail.com" );
    private final static UserData UserBob = new UserData("bob1", "pass", "testb@gmail.com");

    @AfterEach
    public void clearDB(){
        TestAuthService.deleteDB();
    }

    @Test
    @DisplayName("GenerateTokenSuccess")
    public void generateTokenSuccess() throws IncompleteRequestException {
        var billyToken = TestAuthService.login(UserBilly);
        var bobToken = TestAuthService.login(UserBob);
        assertTrue(TestAuthService.authorize(billyToken));
        assertTrue(TestAuthService.authorize(bobToken));
    }

    @Test
    @DisplayName("NoTokenPassed")
    public void noTokenPassed() throws IncompleteRequestException {
        var billyToken = TestAuthService.login(UserBilly);
        AuthData badToken = new AuthData(null, null);
        assertThrows(IncompleteRequestException.class, () ->TestAuthService.authorize(null));
        assertThrows(IncompleteRequestException.class, () ->TestAuthService.authorize(badToken));
        assertTrue(TestAuthService.authorize(billyToken));
    }

    @Test
    @DisplayName("TokenNotInDB")
    public void tokenNotInDB() throws IncompleteRequestException, NotAuthorizedException {
        var billyToken = TestAuthService.login(UserBilly);
        AuthData badToken = new AuthData(null, UUID.randomUUID().toString());
        assertThrows(NotAuthorizedException.class, () -> TestAuthService.authorize(badToken));
        assertTrue(TestAuthService.authorize(billyToken));
    }

    @Test
    @DisplayName("LogoutSuccess")
    public void logoutSuccess() throws IncompleteRequestException {
        var billyToken = TestAuthService.login(UserBilly);
        assertTrue(TestAuthService.authorize(billyToken));
        TestAuthService.logout(billyToken);
        assertThrows(NotAuthorizedException.class, () -> TestAuthService.authorize(billyToken));
    }

    // todo: this test could be needed to prevent multiple login
//    @Test
//    @DisplayName("MultipleLogin")
//    public void muiltipleLogin() throws IncompleteRequestException {
//        var billyToken = TestAuthService.login(billy);
//        assertTrue(TestAuthService.authorize(billyToken));
//        assertThrows(TestAuthService.login(billy))
//    }

}