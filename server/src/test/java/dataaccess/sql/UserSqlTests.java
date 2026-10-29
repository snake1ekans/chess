package dataaccess.sql;

import model.UserData;

public class UserSqlTests {

    public UserSqlTests(){
        UserDacSql userDac;
        UserData user1 = new UserData("joe", "pass", "email1");
        UserData user2 = new UserData("johnny", "pass", "email2");
    }
}
