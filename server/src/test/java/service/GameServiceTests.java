package service;
import exceptions.DataAccessException;
import exceptions.DataConflictException;
import exceptions.IncompleteRequestException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
public class GameServiceTests {

    private final static GameService GAME_SERVICE = new GameService();
    private final GameData testGame1 = new GameData(1, null, null, "game1", null);
    private final GameData testGame2 = new GameData(2, null, null, "game2", null);

    @AfterEach
    public void clearDB(){
        GAME_SERVICE.deleteDB();
    }

    @Test
    @DisplayName("CreateGameSuccess")
    public void createGameSuccess() throws IncompleteRequestException {
        Integer gameID1 = GAME_SERVICE.createGame(testGame1);
        Integer gameID2 = GAME_SERVICE.createGame(testGame2);
        GameData addedGame1 = GAME_SERVICE.getGame(gameID1);
        GameData addedGame2 = GAME_SERVICE.getGame(gameID2);
        GameData game1 = new GameData(gameID1, null, null, "game1", addedGame1.game());
        GameData game2 = new GameData(gameID2, null, null, "game2", addedGame2.game());
        assertEquals(addedGame1, game1);
        assertEquals(addedGame2, game2);
    }

    @Test
    @DisplayName("CreateGameNameConflict")
    public void createGameNameConflict() throws IncompleteRequestException, DataConflictException {
        GAME_SERVICE.createGame(testGame1);
        assertThrows(DataConflictException.class, ()-> GAME_SERVICE.createGame(testGame1));
    }

    @Test
    @DisplayName("CreateGameNoName")
    public void createGameNoName() throws IncompleteRequestException, DataConflictException {
        GameData newGame = new GameData(1, null, null, null, null);
        assertThrows(IncompleteRequestException.class, ()-> GAME_SERVICE.createGame(newGame));
    }

    @Test
    @DisplayName("JoinSuccess")
    public void joinSuccess() throws IncompleteRequestException, DataAccessException {
        Integer gameID1 = GAME_SERVICE.createGame(testGame1);
        Integer gameID2 = GAME_SERVICE.createGame(testGame2);
        GAME_SERVICE.joinGame(new JoinRequestData(gameID1, "WHITE", "TestUser1"));
        GAME_SERVICE.joinGame(new JoinRequestData(gameID1, "BLACK", "TestUser2"));
        GAME_SERVICE.joinGame(new JoinRequestData(gameID2, "WHITE", "TestUser2"));
        GAME_SERVICE.joinGame(new JoinRequestData(gameID2, "BLACK", "TestUser1"));

        GameData game1 = GAME_SERVICE.getGame(gameID1);
        GameData game2 = GAME_SERVICE.getGame(gameID2);
        assertEquals("TestUser2", game1.blackUsername());
        assertEquals("TestUser1", game1.whiteUsername());
        assertEquals("TestUser1", game2.blackUsername());
        assertEquals("TestUser2", game2.whiteUsername());
    }

    @Test
    @DisplayName("JoinDoesntExist")
    public void joinDoesntExist() throws IncompleteRequestException, DataAccessException {
        Integer gameID1 = GAME_SERVICE.createGame(testGame1);
        Integer gameID2 = GAME_SERVICE.createGame(testGame2);
        assertThrows(DataAccessException.class, ()  -> GAME_SERVICE.joinGame(
                new JoinRequestData(gameID1-2, "WHITE", "TestUser1")));
        assertThrows(DataAccessException.class, ()  -> GAME_SERVICE.joinGame(
                new JoinRequestData(gameID1-2, "BLACK", "TestUser2")));
        assertThrows(DataAccessException.class, ()  -> GAME_SERVICE.joinGame(
                new JoinRequestData(gameID2-2, "WHITE", "TestUser2")));
        assertThrows(DataAccessException.class, ()  -> GAME_SERVICE.joinGame(
                new JoinRequestData(gameID2-2, "BLACK", "TestUser1")));
    }

    @Test
    @DisplayName("JoinSlotFilled")
    public void joinSlotFilled() throws IncompleteRequestException, DataAccessException {
        Integer gameID1 = GAME_SERVICE.createGame(testGame1);
        GAME_SERVICE.joinGame(new JoinRequestData(gameID1, "WHITE", "TestUser1"));
        GAME_SERVICE.joinGame(new JoinRequestData(gameID1, "BLACK", "TestUser2"));
        assertThrows(DataConflictException.class, ()  -> GAME_SERVICE.joinGame(
                new JoinRequestData(gameID1, "WHITE", "conflictUser1")));
        assertThrows(DataConflictException.class, ()  -> GAME_SERVICE.joinGame(
                new JoinRequestData(gameID1, "BLACK", "conflictUser2")));
    }

    @Test
    @DisplayName("BadColor")
    public void badColor() throws IncompleteRequestException, DataAccessException {
        Integer gameID1 = GAME_SERVICE.createGame(testGame1);
        assertThrows(DataAccessException.class, ()  -> GAME_SERVICE.joinGame(
                new JoinRequestData(gameID1, "Blue", "conflictUser1")));
        assertThrows(DataAccessException.class, ()  -> GAME_SERVICE.joinGame(
                new JoinRequestData(gameID1, "Purple", "conflictUser2")));
    }
}
