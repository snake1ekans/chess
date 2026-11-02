package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.GameDAC;
import exceptions.DataConflictException;
import exceptions.DatabaseException;
import model.GameData;
import service.JoinRequestData;

import java.sql.*;
import java.util.*;

import static dataaccess.DatabaseManager.getConnection;

public class GameDacSql implements GameDAC {

    private static final int MAX_GAMES = 9999;
    ArrayList<Integer> USED_IDS = new ArrayList<>();


    public GameDacSql() throws DatabaseException {
        DatabaseManager.createDatabase();
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.addBatch("USE chess");
            stat.addBatch("""
                    CREATE TABLE IF NOT EXISTS games(
                        id INT NOT NULL,
                        whiteUsername VARCHAR(255),
                        blackUsername VARCHAR(255),
                        gameName VARCHAR(255),
                        game TEXT,
                        PRIMARY KEY (id));
                    """);
            stat.executeBatch();
        } catch (SQLException s){
            throw new DatabaseException("sql failure");
        }
    }

    public Integer createGame(String gameName) {
        Random rng = new Random();
        Integer gameID;
        while (true) {
            gameID = rng.nextInt(MAX_GAMES);
            if (!USED_IDS.contains(gameID)){
                USED_IDS.add(gameID);
                break;
            }
        }
        var serializer = new Gson();
        var gameJSON = serializer.toJson(new ChessGame());
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.execute("INSERT INTO games (id, gameName, game) VALUES ('" + gameID +
                    "', '" + gameName + "', '" + gameJSON + "');");
            return gameID;
        } catch(SQLException e){
            throw new DatabaseException("create game database failure");
        }
    }

    public List<GameData> listGames() {
        List<GameData> returnlist = new ArrayList<>();
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            ResultSet query = stat.executeQuery("SELECT id, gameName FROM games;");
            if(!query.next()){
                return null;
            }
            do {
                returnlist.add(new GameData(query.getInt("id"), null,
                        null, query.getString("gameName"), null));
            } while (query.next());
            return returnlist;
        } catch (SQLException e) {
            return null;
        }
    }

    public GameData getGame(Integer gameID) {
        return null;
    }

    public void joinGame(JoinRequestData joinRequestData) {

    }

    public void deleteAll() {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.execute("DROP TABLE IF EXISTS games");
        } catch(SQLException e){
            throw new DatabaseException("delete database failure");
        }
    }
}
