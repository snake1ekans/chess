package service;

import dataaccess.DataConflictException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import server.MissingDataException;
import server.Server;
import server.UserService;

import javax.xml.crypto.Data;
import java.rmi.AccessException;
import java.util.Vector;

public class UserServiceTests {

    private static UserService service = new UserService();
    private static UserData billy =  new UserData("billy123", "password", "testa@gmail.com" );
    private static UserData bob = new UserData("bob1", "pass", "testb@gmail.com");
    private static UserData joe = new UserData("joseph", "sampletext", "testc@gmail.com" );
    private static UserData juan = new UserData("juannyboi", "lorem", "testd@gmail.com");


    @Test
    @DisplayName("AddUserSuccess")
    public void addUserSuccessful() {
        service.register(billy);
        service.register(joe);
        assertTrue(service.userExists(billy));
        assertTrue(service.userExists(joe));
        assertFalse(service.userExists(bob));
        assertFalse(service.userExists(juan));
    }

    @Test
    @DisplayName("AddUserFail")
    public void addUserFail() {
        service.register(billy);
        assertThrows(DataConflictException.class, () ->{service.register(billy);});
        service.register(bob);
        assertThrows(DataConflictException.class, () ->{service.register(billy);});
        assertThrows(DataConflictException.class, () ->{service.register(bob);});
    }

}
