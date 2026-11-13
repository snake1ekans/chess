package client;


import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {
    private static Server server;
    private static ServerFacade facade;
    String[] user1 = {"register","user1","pass1","email1"};
    String[] user2 = {"register","user2","pass2","email2"};
    String[] game1 = {"create", "game1"};
    String[] game2 = {"create", "game2"};

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }
    @AfterAll
    static void stopServer() {

        server.stop();
    }
    @Test
    @Order(1)
    @DisplayName("RegisterSuccess")
    public void registerSuccess() throws Exception {
        assertTrue(facade.register(user1));
        assertTrue(facade.register(user2));
    }
    @Test
    @Order(2)
    @DisplayName("DoubleRegister")
    public void doubleRegister() throws Exception {
        assertFalse(facade.register(user1));
        assertFalse(facade.register(user2));
    }
    @Test
    @Order(3)
    @DisplayName("badRegister")
    public void badRegister() throws Exception {
        String[] missEmail = {"register","user1","pass1", null};
        String[] missPass = {"register","user1", null, null};
        String[] missUser = {"register",null, null, null};
        String[] shortArgs = {"register",null};

        assertFalse(facade.register(missEmail));
        assertFalse(facade.register(missPass));
        assertFalse(facade.register(missUser));
        assertFalse(facade.register(shortArgs));
    }
    @Test
    @Order(4)
    @DisplayName("LoginSuccess")
    public void loginSuccess() throws Exception {
        assertTrue(facade.login(user1));
        assertTrue(facade.login(user2));
    }
    @Test
    @Order(5)
    @DisplayName("BadPassword&missingpass")
    public void badPass() throws Exception {
        String[] badUser1 = {"register","user1","badPass","email1"};
        String[] badUser2 = {"register","user2", null};

        assertFalse(facade.login(badUser1));
        assertFalse(facade.login(badUser2));
    }
    @Test
    @Order(6)
    @DisplayName("NoPass")
    public void noPass() throws Exception {
        String[] badUser2 = {"register","user2"};
        assertFalse(facade.login(badUser2));
    }
    @Test
    @Order(7)
    @DisplayName("CreateGameSuccess")
    public void createGame() throws Exception {
        assertTrue(facade.login(user1));
        String[] noName = {"create"};
        String[] nullInjection = {"create", null};

        assertTrue(facade.create(game1));
        assertTrue(facade.create(game2));
        assertFalse(facade.create(noName));
        assertFalse(facade.create(nullInjection));

        List<GameData> gamesList = facade.list();
        GameData created1 = gamesList.get(0);
        GameData created2 = gamesList.get(1);
        assertTrue(Objects.equals(game1[1], created1.gameName()) || Objects.equals(game1[1], created2.gameName()));
    }
    @Test
    @Order(8)
    @DisplayName("JoinAndLeave")
    public void joinLeave() throws Exception {
        facade.login(user1);
        assertTrue(facade.create(game1));
        assertTrue(facade.create(game2));
        List<GameData> gamesList = facade.list();
        GameData created1 = gamesList.get(0);
        GameData created2 = gamesList.get(1);
        String[] join1W = {"join", created1.gameID().toString(), "WHITE"};
        String[] join1B = {"join", created1.gameID().toString(), "BLACK"};

        assertTrue(facade.join(join1B));
        assertTrue(facade.join(join1W));
        assertFalse(facade.join(join1B));
        assertFalse(facade.join(join1W));
    }
    @Test
    @Order(9)
    @DisplayName("LogoutSuccess")
    public void logoutSuccess() throws Exception {
        assertTrue(facade.logout());
    }
    @Test
    @Order(10)
    @DisplayName("badJoin")
    public void badJoinLeave() throws Exception {
        assertTrue(facade.login(user1));
        assertTrue(facade.create(game1));
        List<GameData> gamesList = facade.list();
        GameData created1 = gamesList.get(0);
        GameData created2 = gamesList.get(1);
        String[] join1W = {"join", "Test", "WHITE"};
        String[] join1B = {"join", "test", "BLACK"};
        String[] nullInjection1 = {"join", null, "WHITE"};

        assertFalse(facade.join(join1B));
        assertFalse(facade.join(join1W));
    }
    @Test
    @Order(11)
    @DisplayName("NoAuth")
    public void noAuth() throws Exception {
        facade.authData = null;
        facade.create(game1);
        facade.list();
        String[] join1W = {"join", "5000", "WHITE"};
        facade.join(join1W);
    }
    @Test
    @Order(12)
    @DisplayName("BadAuth")
    public void badAuth() throws Exception {
        facade.authData = new AuthData("bad", "bad");
        facade.create(game1);
        facade.list();
        String[] join1W = {"join", "5000", "WHITE"};
        facade.join(join1W);
    }
    @Test
    @Order(13)
    @DisplayName("DoubleLogout")
    public void doubleLogout() throws Exception {
        facade.authData = new AuthData("bad", "bad");
        assertFalse(facade.logout());
    }
}
