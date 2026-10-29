package dataaccess.sql;

import dataaccess.DatabaseManager;
import dataaccess.GameDAC;
import exceptions.DataAccessException;
import exceptions.DatabaseException;
import model.GameData;
import service.JoinRequestData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static dataaccess.DatabaseManager.getConnection;

public class GameDacSql implements GameDAC {

    public GameDacSql() throws DatabaseException {
        DatabaseManager.createDatabase();
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.addBatch("USE chess");
            stat.addBatch("""
                    CREATE TABLE IF NOT EXISTS games(
                        id INT NOT NULL AUTO_INCREMENT,
                        whiteUsername VARCHAR(255),
                        blackUsername VARCHAR(255),
                        gameName VARCHAR(255),
                        game VARCHAR(255),
                        PRIMARY KEY (id));
                    """);
            stat.executeBatch();
        } catch (SQLException s){
            throw new DatabaseException("sql failure");
        }
    }
    public Integer createGame(String gameName) {
        return 0;
    }

    public List<GameData> listGames() {
        return List.of();
    }

    public GameData getGame(Integer gameID) {
        return null;
    }

    public void joinGame(JoinRequestData joinRequestData) {

    }

    public void deleteAll() {

    }
}
