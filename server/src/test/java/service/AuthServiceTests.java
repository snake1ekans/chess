package java.service;

import org.junit.jupiter.api.BeforeAll;
import server.AuthService;

public class AuthServiceTests {

    @BeforeAll
    public static void initializeTestEnvironment(){
        AuthService testService = new AuthService();

    }

}