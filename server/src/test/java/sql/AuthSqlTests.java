package sql;

import dataaccess.AuthDacSql;
import dataaccess.DatabaseManager;
import exceptions.DataConflictException;
import exceptions.DatabaseException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static org.junit.jupiter.api.Assertions.*;
import static dataaccess.DatabaseManager.getConnection;

public class AuthSqlTests {

    static AuthDacSql authDac;
    AuthData user1 = new AuthData("user1", "1");
    AuthData user2 = new AuthData("user2", "2");
    AuthData user3 = new AuthData("user3", "3");
    AuthData user4 = new AuthData("user4", "4");

    @BeforeAll
    static public void initializeDatabase(){
        DatabaseManager.createDatabase();
        try (Connection conn = getConnection()) {
            authDac = new AuthDacSql();
            Statement s = conn.createStatement();
            s.execute("USE chess;");
        } catch (Exception e) {
            throw new RuntimeException("Database Initialization error. Check your environment and try again.");
        }
    }

    @AfterEach
    public void clearDatabase(){
        authDac.deleteAll();
        try (Connection conn = getConnection()){
            Statement s = conn.createStatement();
            s.execute("USE chess;");
            Statement stat = conn.createStatement();
            stat.execute("""
                CREATE TABLE IF NOT EXISTS auth(
                    username VARCHAR(255) NOT NULL,
                    token VARCHAR(255) NOT NULL,
                    PRIMARY KEY (token),
                    CONSTRAINT uniqueness UNIQUE (username,token));
                """);
        } catch (SQLException e) {
            throw new RuntimeException("Reinitialization failed.");
        }
    }

    @Test
    @DisplayName("AddFourUsers")
    public void addUsers() {
        authDac.writeToken(user1);
        authDac.writeToken(user2);
        authDac.writeToken(user3);
        authDac.writeToken(user4);
        try(Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            ResultSet results = stat.executeQuery("SELECT * FROM auth");
            results.next();
            assertEquals(user1.authToken(), results.getString("token"));
            assertEquals(user1.username(), results.getString("username"));
            results.next();
            assertEquals(user2.authToken(), results.getString("token"));
            assertEquals(user2.username(), results.getString("username"));
            results.next();
            assertEquals(user3.authToken(), results.getString("token"));
            assertEquals(user3.username(), results.getString("username"));
            results.next();
            assertEquals(user4.authToken(), results.getString("token"));
            assertEquals(user4.username(), results.getString("username"));
        } catch (SQLException e) {
            throw new RuntimeException("database failed at 4 users");
        }
    }

    @Test
    @DisplayName("DuplicateSign-in")
    public void addDuplicates() {
        authDac.writeToken(user1);
        authDac.writeToken(user2);
        authDac.writeToken(user3);
        authDac.writeToken(user4);

        assertThrows(DataConflictException.class, () -> authDac.writeToken(user1));
        assertThrows(DataConflictException.class, () -> authDac.writeToken(user2));
        assertThrows(DataConflictException.class, () -> authDac.writeToken(user3));
        assertThrows(DataConflictException.class, () -> authDac.writeToken(user4));
    }

    @Test
    @DisplayName("LogoutFourUsers")
    public void logoutUsers() {
        authDac.writeToken(user1);
        authDac.writeToken(user2);
        authDac.writeToken(user3);
        authDac.writeToken(user4);

        authDac.deleteToken(user1);
        authDac.deleteToken(user2);
        authDac.deleteToken(user3);
        authDac.deleteToken(user4);

        assertFalse(authDac.checkToken(user1));
        assertFalse(authDac.checkToken(user2));
        assertFalse(authDac.checkToken(user3));
        assertFalse(authDac.checkToken(user4));
    }

    @Test
    @DisplayName("DoubleLogout")
    public void doubleLogout() {
        authDac.writeToken(user1);
        authDac.writeToken(user2);
        authDac.writeToken(user3);
        authDac.writeToken(user4);

        authDac.deleteToken(user1);
        authDac.deleteToken(user2);
        authDac.deleteToken(user3);
        authDac.deleteToken(user4);

        authDac.deleteToken(user1);
        authDac.deleteToken(user2);
        authDac.deleteToken(user3);
        authDac.deleteToken(user4);
    }

    @Test
    @DisplayName("DatabaseFailure")
    public void failDatabase() {
        try (Connection conn = getConnection()){
            Statement stat = conn.createStatement();
            stat.execute("DROP TABLE auth;");
        } catch (SQLException e) {
            throw new RuntimeException("Reinitialization failed.");
        }
        assertThrows(DatabaseException.class, () -> authDac.writeToken(user1));
        assertThrows(DatabaseException.class, () -> authDac.deleteToken(user1));
        assertThrows(DatabaseException.class, () -> authDac.checkToken(user1));
        assertThrows(DatabaseException.class, () -> authDac.getUsername(user1));
    }
}
