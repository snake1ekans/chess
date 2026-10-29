package dataaccess.sql;

import dataaccess.AuthDAC;
import dataaccess.DatabaseManager;
import exceptions.DataConflictException;
import exceptions.DatabaseException;
import model.AuthData;
import java.sql.*;
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
                        PRIMARY KEY (token),
                        CONSTRAINT uniqueness UNIQUE (username,token));
                    """);
            stat.executeBatch();
        } catch (SQLException s) {
            throw new DatabaseException("failed to connect");
        }
    }

    public void writeToken(AuthData token) {
        try (Connection conn = getConnection()) {
            Statement stat = conn.createStatement();
            stat.execute("USE chess;");
            stat.execute("INSERT INTO auth (username, token) VALUES ('"
                    + token.username() + "', '" + token.authToken() + "');");
        } catch (SQLIntegrityConstraintViolationException e){
            throw new DataConflictException("duplicate attempt");
        } catch (SQLException e) {
            throw new DatabaseException("getusername auth database failure");
        }
    }

    public boolean checkToken(AuthData token) {
        try (Connection conn = getConnection()) {
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM auth WHERE token = '" + token.authToken() + "';");
            if (!res.next()) {
                return false;
            }
            return (Objects.equals(res.getString("token"), token.authToken()));
        } catch (SQLException e) {
            throw new DatabaseException("CheckToken database failure");
        }
    }

    public void deleteToken(AuthData token) {
        try (Connection conn = getConnection()) {
            Statement stat = conn.createStatement();
            stat.execute("USE chess;");
            stat.execute("DELETE FROM auth WHERE token='" + token.authToken() + "';");
        } catch (SQLException e) {
            throw new DatabaseException("getusername auth database failure");
        }
    }

    public String getUsername(AuthData token) {
        try (Connection conn = getConnection()) {
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM auth WHERE token = '" + token.authToken() + "';");
            res.next();
            return res.getString("username");
        } catch (SQLException e) {
            throw new DatabaseException("getusername auth database failure");
        }
    }

    public void deleteAll() {
        try (Connection conn = getConnection()) {
            Statement stat = conn.createStatement();
            stat.execute("DROP TABLE IF EXISTS auth;");
        } catch (SQLException e) {
            throw new DatabaseException("drop auth error");
        }
    }
}
