package dataaccess.sql;

import com.mysql.cj.protocol.Resultset;
import dataaccess.DatabaseManager;
import exceptions.DataConflictException;
import exceptions.DatabaseException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static dataaccess.DatabaseManager.getConnection;
import static org.junit.jupiter.api.Assertions.*;

public class UserSqlTests {

    UserDacSql userDac;
    UserData user1 = new UserData("billy", "pass", "email3");
    UserData user2 = new UserData("bobby", "pass", "email4");
    UserData user3 = new UserData("joe", "pass", "email1");
    UserData user4 = new UserData("johnny", "pass", "email2");

    public UserSqlTests(){
        DatabaseManager.createDatabase();
        try (Connection conn = getConnection()) {
            userDac = new UserDacSql();
            Statement s = conn.createStatement();
            s.execute("USE chess;");
        } catch (Exception e) {
            throw new RuntimeException("Database Initialization error. Check your environment and try again.");
        }
    }

    @AfterEach
    public void clearDatabase(){
        userDac.deleteAll();
        try (Connection conn = getConnection()){
            Statement s = conn.createStatement();
            s.execute("USE chess;");
            Statement stat = conn.createStatement();
            stat.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    CONSTRAINT uniqueness UNIQUE (username,email),
                    PRIMARY KEY (username));
                """);
        } catch (SQLException e) {
            throw new RuntimeException("Reinitialization failed.");
        }
    }
    @Test
    @DisplayName("AddUsersSuccess")
    public void addUsersSuccess() {
        userDac.addUser(user1);
        userDac.addUser(user2);
        userDac.addUser(user3);
        userDac.addUser(user4);

        try (Connection conn = DatabaseManager.getConnection()) {
            Statement stat = conn.createStatement();
            ResultSet res = stat.executeQuery("SELECT * FROM users");
            res.next();
            assertEquals(user1.email(), res.getString("email"));
            assertEquals(user1.username(), res.getString("username"));
            res.next();
            assertEquals(user2.email(), res.getString("email"));
            assertEquals(user2.username(), res.getString("username"));
            res.next();
            assertEquals(user3.email(), res.getString("email"));
            assertEquals(user3.username(), res.getString("username"));
            res.next();
            assertEquals(user4.email(), res.getString("email"));
            assertEquals(user4.username(), res.getString("username"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("AddDuplicateUser")
    public void addDuplicateUser() {
        userDac.addUser(user1);
        userDac.addUser(user2);
        userDac.addUser(user3);
        userDac.addUser(user4);

        assertThrows(DataConflictException.class, () -> userDac.addUser(user1));
        assertThrows(DataConflictException.class, () -> userDac.addUser(user2));
        assertThrows(DataConflictException.class, () -> userDac.addUser(user3));
        assertThrows(DataConflictException.class, () -> userDac.addUser(user4));
    }

    @Test
    @DisplayName("GetUserExists&DoesntExist")
    public void getUserSuccess() {
        userDac.addUser(user1);
        userDac.addUser(user2);
        userDac.addUser(user3);

        assertEquals(userDac.getUser(user1).username(), user1.username());
        assertEquals(userDac.getUser(user2).username(), user2.username());
        assertEquals(userDac.getUser(user3).username(), user3.username());
        assertNull(userDac.getUser(user4));
    }

    @Test
    @DisplayName("DatabaseFailure")
    public void failDatabase() {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.execute("DROP TABLE users;");
        } catch (SQLException e) {
            throw new RuntimeException("Reinitialization failed.");
        }
        assertThrows(DatabaseException.class, () -> userDac.addUser(user1));
        assertThrows(DatabaseException.class, () -> userDac.deleteAll());
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    CONSTRAINT uniqueness UNIQUE (username,email),
                    PRIMARY KEY (username));
                """);
        } catch (SQLException e) {
            throw new RuntimeException("Reinitialization failed.");
        }
    }
}
