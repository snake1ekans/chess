package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DatabaseManager;
import exceptions.DataAccessException;
import exceptions.DataConflictException;
import exceptions.DatabaseException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.JoinRequestData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static dataaccess.DatabaseManager.getConnection;
import static org.junit.jupiter.api.Assertions.*;

public class GameSqlTests {

    GameDacSql gameDac;
    String game1 = "testGame1";
    String game2 = "testGame2";
    String game3 = "testGame3";

    public GameSqlTests() {
        DatabaseManager.createDatabase();
        try (Connection conn = getConnection()) {
            gameDac = new GameDacSql();
            Statement s = conn.createStatement();
            s.execute("USE chess;");
        } catch (Exception e) {
            throw new RuntimeException("Database Initialization error. Check your environment and try again.");
        }
    }

    @AfterEach
    public void clearDatabase(){
        gameDac.deleteAll();
    }

    @Test
    @DisplayName("CreateGameSuccess")
    public void createGameSuccess() {
        List<Integer> idList = new ArrayList<>();
        List<Integer> checkList = new ArrayList<>();

        checkList.add(gameDac.createGame(game1));
        checkList.add(gameDac.createGame(game2));
        checkList.add(gameDac.createGame(game3));

        try (Connection conn = DatabaseManager.getConnection()) {
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM games");
            res.next();
            idList.add(res.getInt("id"));
            res.next();
            idList.add(res.getInt("id"));
            res.next();
            idList.add(res.getInt("id"));
            assertTrue(checkList.containsAll(idList));
            assertTrue(idList.containsAll(checkList));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("ListGames")
    public void listGames() {
        List<GameData> checkList = new ArrayList<>();
        Integer id1 = gameDac.createGame(game1);
        Integer id2 = gameDac.createGame(game2);
        Integer id3 = gameDac.createGame(game3);

        checkList.add(new GameData(id1, null, null, game1, null));
        checkList.add(new GameData(id2, null, null, game2, null));
        checkList.add(new GameData(id3, null, null, game3, null));

        List<GameData> idList = gameDac.listGames();
        assertTrue(checkList.containsAll(idList));
        assertTrue(idList.containsAll(checkList));
    }

    @Test
    @DisplayName("EmptyGames")
    public void listGamesEmpty() {
        assertEquals(new ArrayList<>(), gameDac.listGames());
    }

    @Test
    @DisplayName("GetGameTest")
    public void getGame() {
        assertNull(gameDac.getGame(1));
        Integer id = gameDac.createGame(game1);
        GameData comparisonGame = new GameData(id, null, null, game1, new ChessGame());
        assertEquals(comparisonGame, gameDac.getGame(id));
        assertNull(gameDac.getGame(100000));
    }

    @Test
    @DisplayName("JoinGameNormal")
    public void joinGameNormal() throws DataAccessException {
        Integer id1 = gameDac.createGame(game1);
        Integer id2 = gameDac.createGame(game2);
        Integer id3 = gameDac.createGame(game3);

        gameDac.joinGame(new JoinRequestData(id1, "BLACK", "user1"));
        gameDac.joinGame(new JoinRequestData(id2, "BLACK", "user2"));
        gameDac.joinGame(new JoinRequestData(id3, "BLACK", "user3"));
        gameDac.joinGame(new JoinRequestData(id1, "WHITE", "user5"));
        gameDac.joinGame(new JoinRequestData(id2, "WHITE", "user4"));

        assertEquals(gameDac.getGame(id1), new GameData(id1, "user5", "user1", game1, new ChessGame()));
        assertEquals(gameDac.getGame(id2), new GameData(id2, "user4", "user2", game2, new ChessGame()));
        assertEquals(gameDac.getGame(id3), new GameData(id3, null, "user3", game3, new ChessGame()));
    }

    @Test
    @DisplayName("JoinGameDoesntExist")
    public void joinGameNoGame() throws DataAccessException {
        Integer id1 = gameDac.createGame(game1);
        Integer id2 = gameDac.createGame(game2);
        gameDac.joinGame(new JoinRequestData(id1, "BLACK", "user1"));
        gameDac.joinGame(new JoinRequestData(id2, "BLACK", "user2"));

        assertThrows(DataAccessException.class, () ->
                gameDac.joinGame(new JoinRequestData(10000, "BLACK", "user3")));
        assertThrows(DataAccessException.class, () ->
                gameDac.joinGame(new JoinRequestData(null, "BLACK", "user3")));
        assertEquals(gameDac.getGame(id1), new GameData(id1, null, "user1", game1, new ChessGame()));
        assertEquals(gameDac.getGame(id2), new GameData(id2, null, "user2", game2, new ChessGame()));
    }

    @Test
    @DisplayName("JoinGameSlotConflict")
    public void joinGameConflict() throws DataAccessException {
        Integer id1 = gameDac.createGame(game1);
        Integer id2 = gameDac.createGame(game2);
        gameDac.joinGame(new JoinRequestData(id1, "BLACK", "user1"));
        gameDac.joinGame(new JoinRequestData(id2, "WHITE", "user2"));

        assertThrows(DataConflictException.class, () ->
                gameDac.joinGame(new JoinRequestData(id1, "BLACK", "user3")));
        assertThrows(DataConflictException.class, () ->
                gameDac.joinGame(new JoinRequestData(id2, "WHITE", "user3")));

        gameDac.joinGame(new JoinRequestData(id2, "BLACK", "user4"));
        assertThrows(DataConflictException.class, () ->
                gameDac.joinGame(new JoinRequestData(id2, "BLACK", "user3")));
    }


    @Test
    @DisplayName("DatabaseFailure")
    public void failDatabase() {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.execute("DROP TABLE games;");
        } catch (SQLException e) {
            throw new RuntimeException("Reinitialization failed.");
        }

        assertThrows(DatabaseException.class, () -> gameDac.createGame(game1));
        assertThrows(DatabaseException.class, () -> gameDac.getGame(1000));
        assertThrows(DatabaseException.class, () -> gameDac.joinGame(new JoinRequestData(1, "BLACK", "user")));
        assertThrows(DatabaseException.class, () -> gameDac.deleteAll());
        assertNull(gameDac.listGames());

        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.execute("""
                CREATE TABLE IF NOT EXISTS games(
                    id INT NOT NULL AUTO_INCREMENT,
                    whiteUsername VARCHAR(255),
                    blackUsername VARCHAR(255),
                    gameName VARCHAR(255),
                    game TEXT,
                    PRIMARY KEY (id));
                """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
