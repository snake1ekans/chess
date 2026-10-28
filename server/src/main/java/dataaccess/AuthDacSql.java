package dataaccess;

import exceptions.DataAccessException;
import exceptions.DatabaseException;
import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;

import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dataaccess.DatabaseManager.*;

public class AuthDacSql implements AuthDAC {

    public AuthDacSql() throws DatabaseException {
        DatabaseManager.createDatabase();
        try {
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            stat.addBatch("USE chess;");
            stat.addBatch("""
                    CREATE TABLE IF NOT EXISTS auth(
                        username VARCHAR(255) NOT NULL,
                        token VARCHAR(255) NOT NULL,
                        PRIMARY KEY (token));
                    """);
            stat.executeBatch();
        } catch (SQLException s){
            throw new DatabaseException("failed to connect");
        }
    }

    public void writeToken(AuthData token) {
        List<String>queries  = new ArrayList<String>();
        queries.add("USE chess;");
        queries.add("INSERT INTO auth (username, token) VALUES ('"
                + token.username() + "', '" + token.authToken() + "');");
        batcher(queries);
    }

    public boolean checkToken(AuthData token) {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM auth WHERE token = '"+token.authToken()+"';");
            res.next();
            String userToken = res.getString("token");
            return (Objects.equals(userToken, token.authToken()));
        } catch (SQLException e) {
            return false;
        }
    }

    public void deleteToken(AuthData token) {
        List<String>queries = new ArrayList<>();
        queries.add("USE chess;");
        queries.add("DELETE FROM auth WHERE token='" + token.authToken() + "';");
        batcher(queries);
    }

    public String getUsername(AuthData token) {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM auth WHERE token = '"+token.authToken()+"';");
            res.next();
            return res.getString("username");
        } catch (SQLException e) {
            return "";
        }
    }

    public void deleteAll() {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.execute("DROP TABLE auth");
        } catch (SQLException e) {
            throw new DatabaseException("drop auth error");
        }
    }

    private void batcher(List<String> queries) throws DatabaseException {
        try {
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            for (String i : queries) {
                stat.addBatch(i);
            }
            stat.executeBatch();
        } catch (SQLException s){
            throw new DatabaseException("Auth Failure");
        }
    }
}
