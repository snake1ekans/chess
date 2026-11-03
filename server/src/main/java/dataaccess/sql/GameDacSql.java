package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import dataaccess.GameDAC;
import exceptions.DataAccessException;
import exceptions.DataConflictException;
import exceptions.DatabaseException;
import model.GameData;
import service.JoinRequestData;

import javax.xml.transform.Result;
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
            ResultSet query = stat.executeQuery("SELECT * FROM games;");
            if(!query.next()){
                return returnlist;
            }
            do {
                returnlist.add(new GameData(query.getInt("id"), query.getString("whiteUsername"),
                        query.getString("blackUsername"), query.getString("gameName"), null));
            } while (query.next());
            return returnlist;
        } catch (SQLException e) {
            return null;
        }
    }

    public GameData getGame(Integer gameID) {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM games WHERE id = " + gameID + ";");
            if (!res.next()){
                return null;
            }
            var serializer = new Gson();
            ChessGame gameDecrypted = serializer.fromJson(res.getString("game"), ChessGame.class);

            return new GameData(res.getInt("id"), res.getString("whiteUsername"),
                    res.getString("blackUsername"), res.getString("gameName"), gameDecrypted);
        } catch(SQLException e){
            throw new DatabaseException("delete database failure");
        }
    }

    public void joinGame(JoinRequestData joinRequestData) throws DataAccessException {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM games WHERE id = " + joinRequestData.gameID() + ";");
            if (!res.next()){
                throw new DataAccessException("Game Id Not Found");
            } else if (((Objects.equals(joinRequestData.playerColor(), "WHITE") && res.getString("whiteUsername") != null))
                    || ((Objects.equals(joinRequestData.playerColor(), "BLACK") && res.getString("blackUsername") != null))) {
                throw new DataConflictException("Slot already Taken");
            } else {
                if (Objects.equals(joinRequestData.playerColor(), "WHITE")) {
                    stat.execute("UPDATE games SET whiteUsername = '" + joinRequestData.userName() +
                            "' WHERE id = " + joinRequestData.gameID() + ";");
                } else {
                    stat.execute("UPDATE games SET blackUsername = '" + joinRequestData.userName() +
                            "' WHERE id = " + joinRequestData.gameID() + ";");
                }
            }
        } catch(SQLException e){
            throw new DatabaseException("delete database failure");
        }
    }

    public void deleteAll() {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.execute("TRUNCATE TABLE games");
        } catch(SQLException e){
            throw new DatabaseException("delete database failure");
        }
    }
}
