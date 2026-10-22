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

    private final static UserService ServiceObj = new UserService();
    private final static UserData UserBilly =  new UserData("UserBilly123", "password", "testa@gmail.com" );
    private final static UserData UserBob = new UserData("UserBob1", "pass", "testb@gmail.com");
    private final static UserData UserJoe = new UserData("joseph", "sampletext", "testc@gmail.com" );
    private final static UserData UserJuan = new UserData("juannyboi", "lorem", "testd@gmail.com");

    @AfterEach
    public void clearDB(){
        ServiceObj.deleteDB();
    }

    @Test
    @DisplayName("AddUserSuccess")
    public void addUserSuccessful() throws IncompleteRequestException {
        ServiceObj.register(UserBilly);
        ServiceObj.register(UserJoe);
        assertTrue(ServiceObj.userExists(UserBilly));
        assertTrue(ServiceObj.userExists(UserJoe));
        assertFalse(ServiceObj.userExists(UserBob));
        assertFalse(ServiceObj.userExists(UserJuan));
    }

    @Test
    @DisplayName("AddUserMissingField")
    public void addUserMissingField() {
        assertThrows(IncompleteRequestException.class, () -> {ServiceObj.register(new UserData(null, "pass", "testeemail"));});
    }
    @Test
    @DisplayName("AddDuplicateUser")
    public void addUserFail() throws IncompleteRequestException {
        ServiceObj.register(UserBilly);
        assertThrows(DataConflictException.class, () ->{ServiceObj.register(UserBilly);});
        ServiceObj.register(UserBob);
        assertThrows(DataConflictException.class, () ->{ServiceObj.register(UserBilly);});
        assertThrows(DataConflictException.class, () ->{ServiceObj.register(UserBob);});
    }

    @Test
    @DisplayName("CredentialCheckSuccess")
    public void credentialCheckSuccess() throws IncompleteRequestException, DataAccessException {
        ServiceObj.register(UserBilly);
        ServiceObj.register(UserBob);
        assertTrue(ServiceObj.login(UserBilly));
        assertTrue(ServiceObj.login(UserBob));
    }

    @Test
    @DisplayName("CredentialCheckBadPassword")
    public void badPassword() throws IncompleteRequestException {
        ServiceObj.register(UserBilly);
        ServiceObj.register(UserBob);
        UserData badCred = new UserData("UserBilly123", "passw0rd", null);
        assertThrows(DataConflictException.class, () -> ServiceObj.login(badCred));
        assertThrows(DataAccessException.class, () -> ServiceObj.login(UserJoe));
    }

    @Test
    @DisplayName("CredentialCheckNoPassword")
    public void noPassword() throws IncompleteRequestException {
        ServiceObj.register(UserBilly);
        ServiceObj.register(UserBob);
        UserData badCred = new UserData("UserBilly123", null, null);
        assertThrows(IncompleteRequestException.class, () -> ServiceObj.login(badCred));
        assertThrows(DataAccessException.class, () -> ServiceObj.login(UserJoe));
    }

}
