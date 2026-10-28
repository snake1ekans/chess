package dataaccess;

import exceptions.DataAccessException;
import exceptions.DatabaseException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static dataaccess.DatabaseManager.getConnection;

public class UserDacSql implements UserDAC {

    public UserDacSql() throws DatabaseException {
        DatabaseManager.createDatabase();
        try {
            Connection conn = getConnection();
            Statement stat = conn.createStatement();
            stat.addBatch("USE chess");
            stat.addBatch("""
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    PRIMARY KEY (username));
                """);
            stat.executeBatch();
        } catch (SQLException s){
            throw new DatabaseException("UserData failure");
        }
    }


    public void addUser(UserData data) {
        String hashedPassword = BCrypt.hashpw(data.password(), BCrypt.gensalt());
        List<String>queries  = new ArrayList<String>();
        queries.add("USE chess;");
        queries.add("INSERT INTO users (username, email, password) VALUES ('" + data.username()
                + "', '" + data.email() + "', '" + hashedPassword + "');");
        batcher(queries);
    }

    public UserData getUser(UserData data) {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            ResultSet query = stat.executeQuery("SELECT * FROM users");
            query.next();
            var user = query.getString("username");
            var pass = query.getString("password");
            var email = query.getString("email");
            return new UserData(user, pass, email);
        } catch (SQLException e) {
            return null;
        }
    }

    public void deleteAll() {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.execute("DROP TABLE users");
        } catch (SQLException e) {
            throw new DatabaseException("drop users error");
        }
    }

    private void batcher(List<String> queries) throws DatabaseException {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            for (String i : queries) {
                stat.addBatch(i);
            }
            stat.executeBatch();
        } catch (SQLException s){
            throw new DatabaseException("Database Failure");
        }
    }
}
