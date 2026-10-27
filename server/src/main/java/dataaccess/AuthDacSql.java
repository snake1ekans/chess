package dataaccess;

import exceptions.DataAccessException;
import exceptions.DatabaseException;
import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;

import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        queries.add("INSERT INTO users (username, token) VALUES ('"
                + token.username() + "', '" + token.authToken() + "');");
        batcher(queries);
    }

    public boolean checkToken(AuthData token) {
        return false;
    }

    public void deleteToken(AuthData token) {

    }

    public String getUsername(AuthData token) {
        return "";
    }

    public void deleteAll() {

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
            throw new DatabaseException("failed to create database");
        }
    }


}
