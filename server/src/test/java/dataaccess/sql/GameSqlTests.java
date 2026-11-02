package dataaccess.sql;

import dataaccess.DatabaseManager;
import exceptions.DatabaseException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        try (Connection conn = getConnection()){
            Statement s = conn.createStatement();
            s.execute("USE chess;");
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
            throw new RuntimeException("Reinitialization failed.");
        }
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
    @DisplayName("DatabaseFailure")
    public void failDatabase() {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.execute("DROP TABLE games;");
        } catch (SQLException e) {
            throw new RuntimeException("Reinitialization failed.");
        }
        assertThrows(DatabaseException.class, () -> gameDac.createGame(game1));
        assertNull(gameDac.listGames());

    }
}
