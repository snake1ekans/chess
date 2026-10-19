package service;

import dataaccess.DataAccessException;
import dataaccess.DataConflictException;
import dataaccess.IncompleteRequestException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import server.UserService;

public class UserServiceTests {

    private final static UserService service = new UserService();
    private final static UserData billy =  new UserData("billy123", "password", "testa@gmail.com" );
    private final static UserData bob = new UserData("bob1", "pass", "testb@gmail.com");
    private final static UserData joe = new UserData("joseph", "sampletext", "testc@gmail.com" );
    private final static UserData juan = new UserData("juannyboi", "lorem", "testd@gmail.com");

    @AfterEach
    public void clearDB(){
        service.deleteDB();
    }

    @Test
    @DisplayName("AddUserSuccess")
    public void addUserSuccessful() throws IncompleteRequestException {
        service.register(billy);
        service.register(joe);
        assertTrue(service.userExists(billy));
        assertTrue(service.userExists(joe));
        assertFalse(service.userExists(bob));
        assertFalse(service.userExists(juan));
    }

    @Test
    @DisplayName("AddUserFail")
    public void addUserFail() throws IncompleteRequestException {
        service.register(billy);
        assertThrows(DataConflictException.class, () ->{service.register(billy);});
        service.register(bob);
        assertThrows(DataConflictException.class, () ->{service.register(billy);});
        assertThrows(DataConflictException.class, () ->{service.register(bob);});
    }

    @Test
    @DisplayName("CredentialCheckSuccess")
    public void credentialCheckSuccess() throws IncompleteRequestException, DataAccessException {
        service.register(billy);
        service.register(bob);
        assertTrue(service.login(billy));
        assertTrue(service.login(bob));
    }

    @Test
    @DisplayName("CredentialCheckBadPassword")
    public void badPassword() throws IncompleteRequestException {
        service.register(billy);
        service.register(bob);
        UserData badCred = new UserData("billy123", "passw0rd", null);
        assertThrows(DataConflictException.class, () -> service.login(badCred));
        assertThrows(DataAccessException.class, () -> service.login(joe));
    }

    @Test
    @DisplayName("CredentialCheckNoPassword")
    public void NoPassword() throws IncompleteRequestException {
        service.register(billy);
        service.register(bob);
        UserData badCred = new UserData("billy123", null, null);
        assertThrows(IncompleteRequestException.class, () -> service.login(badCred));
        assertThrows(DataAccessException.class, () -> service.login(joe));
    }



    @Test
    @DisplayName("NoUserAuth")
    public void noUser() {

    }
}
