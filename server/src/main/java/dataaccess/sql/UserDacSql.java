package dataaccess.sql;

import dataaccess.DatabaseManager;
import dataaccess.UserDAC;
import exceptions.DataConflictException;
import exceptions.DatabaseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
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
                    CONSTRAINT uniqueness UNIQUE (username,email),
                    PRIMARY KEY (username));
                """);
            stat.executeBatch();
        } catch (SQLException s){
            throw new DatabaseException("UserData failure");
        }
    }

    public void addUser(UserData data) {
        String hashedPassword = BCrypt.hashpw(data.password(), BCrypt.gensalt());
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.execute("INSERT INTO users (username, email, password) VALUES ('" + data.username() +
                    "', '" + data.email() + "', '" + hashedPassword + "');");
        } catch (SQLIntegrityConstraintViolationException e){
            throw new DataConflictException("duplicate user added");
        } catch(SQLException e){
            throw new DatabaseException("AddUser Failed");
        }
    }

    public UserData getUser(UserData data) {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            ResultSet query = stat.executeQuery("SELECT * FROM users WHERE username = '" + data.username() + "';");
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
            stat.execute("TRUNCATE TABLE users;");
        } catch (SQLException e) {
            throw new DatabaseException("Drop Users Failed");
        }
    }
}
