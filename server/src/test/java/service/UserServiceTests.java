package service;

import exceptions.DataAccessException;
import exceptions.DataConflictException;
import exceptions.IncompleteRequestException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    private static UserService userService = new UserService();
    private final static UserData BILLY =  new UserData("BILLY123", "password", "testa@gmail.com" );
    private final static UserData BOB = new UserData("BOB1", "pass", "testb@gmail.com");
    private final static UserData JOE = new UserData("joseph", "sampletext", "testc@gmail.com" );
    private final static UserData JUAN = new UserData("juannyboi", "lorem", "testd@gmail.com");

    @AfterEach
    public void clearDB(){
        userService.deleteDB();
        userService = new UserService();
    }

    @Test
    @DisplayName("AddUserSuccess")
    public void addUserSuccessful() throws IncompleteRequestException {
        userService.register(BILLY);
        userService.register(JOE);
        assertTrue(userService.userExists(BILLY));
        assertTrue(userService.userExists(JOE));
        assertFalse(userService.userExists(BOB));
        assertFalse(userService.userExists(JUAN));
    }

    @Test
    @DisplayName("AddUserMissingField")
    public void addUserMissingField() {
        assertThrows(IncompleteRequestException.class, () ->
            userService.register(new UserData(null, "pass", "testeemail")));
    }
    @Test
    @DisplayName("AddDuplicateUser")
    public void addUserFail() throws IncompleteRequestException {
        userService.register(BILLY);
        assertThrows(DataConflictException.class, () -> userService.register(BILLY));
        userService.register(BOB);
        assertThrows(DataConflictException.class, () -> userService.register(BILLY));
        assertThrows(DataConflictException.class, () -> userService.register(BOB));
    }

    @Test
    @DisplayName("CredentialCheckSuccess")
    public void credentialCheckSuccess() throws IncompleteRequestException, DataAccessException {
        userService.register(BILLY);
        userService.register(BOB);
        assertTrue(userService.login(BILLY));
        assertTrue(userService.login(BOB));
    }

    @Test
    @DisplayName("CredentialCheckBadPassword")
    public void badPassword() throws IncompleteRequestException {
        userService.register(BILLY);
        userService.register(BOB);
        UserData badCred = new UserData("BILLY123", "passw0rd", null);
        assertThrows(DataConflictException.class, () -> userService.login(badCred));
        assertThrows(DataAccessException.class, () -> userService.login(JOE));
    }

    @Test
    @DisplayName("CredentialCheckNoPassword")
    public void noPassword() throws IncompleteRequestException {
        userService.register(BILLY);
        userService.register(BOB);
        UserData badCred = new UserData("BILLY123", null, null);
        assertThrows(IncompleteRequestException.class, () -> userService.login(badCred));
        assertThrows(DataAccessException.class, () -> userService.login(JOE));
    }

}
