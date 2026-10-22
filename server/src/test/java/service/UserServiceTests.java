package java.service;

import exceptions.DataAccessException;
import exceptions.DataConflictException;
import exceptions.IncompleteRequestException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    private final static UserService USER_SERVICE = new UserService();
    private final static UserData BILLY =  new UserData("BILLY123", "password", "testa@gmail.com" );
    private final static UserData BOB = new UserData("BOB1", "pass", "testb@gmail.com");
    private final static UserData JOE = new UserData("joseph", "sampletext", "testc@gmail.com" );
    private final static UserData JUAN = new UserData("juannyboi", "lorem", "testd@gmail.com");

    @AfterEach
    public void clearDB(){
        USER_SERVICE.deleteDB();
    }

    @Test
    @DisplayName("AddUserSuccess")
    public void addUserSuccessful() throws IncompleteRequestException {
        USER_SERVICE.register(BILLY);
        USER_SERVICE.register(JOE);
        assertTrue(USER_SERVICE.userExists(BILLY));
        assertTrue(USER_SERVICE.userExists(JOE));
        assertFalse(USER_SERVICE.userExists(BOB));
        assertFalse(USER_SERVICE.userExists(JUAN));
    }

    @Test
    @DisplayName("AddUserMissingField")
    public void addUserMissingField() {
        assertThrows(IncompleteRequestException.class, () -> {USER_SERVICE.register(new UserData(null, "pass", "testeemail"));});
    }
    @Test
    @DisplayName("AddDuplicateUser")
    public void addUserFail() throws IncompleteRequestException {
        USER_SERVICE.register(BILLY);
        assertThrows(DataConflictException.class, () ->{USER_SERVICE.register(BILLY);});
        USER_SERVICE.register(BOB);
        assertThrows(DataConflictException.class, () ->{USER_SERVICE.register(BILLY);});
        assertThrows(DataConflictException.class, () ->{USER_SERVICE.register(BOB);});
    }

    @Test
    @DisplayName("CredentialCheckSuccess")
    public void credentialCheckSuccess() throws IncompleteRequestException, DataAccessException {
        USER_SERVICE.register(BILLY);
        USER_SERVICE.register(BOB);
        assertTrue(USER_SERVICE.login(BILLY));
        assertTrue(USER_SERVICE.login(BOB));
    }

    @Test
    @DisplayName("CredentialCheckBadPassword")
    public void badPassword() throws IncompleteRequestException {
        USER_SERVICE.register(BILLY);
        USER_SERVICE.register(BOB);
        UserData badCred = new UserData("BILLY123", "passw0rd", null);
        assertThrows(DataConflictException.class, () -> USER_SERVICE.login(badCred));
        assertThrows(DataAccessException.class, () -> USER_SERVICE.login(JOE));
    }

    @Test
    @DisplayName("CredentialCheckNoPassword")
    public void noPassword() throws IncompleteRequestException {
        USER_SERVICE.register(BILLY);
        USER_SERVICE.register(BOB);
        UserData badCred = new UserData("BILLY123", null, null);
        assertThrows(IncompleteRequestException.class, () -> USER_SERVICE.login(badCred));
        assertThrows(DataAccessException.class, () -> USER_SERVICE.login(JOE));
    }

}
