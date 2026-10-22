package dataaccess;

import model.UserData;

public interface UserDAC {
    void addUser(UserData data);
    UserData getUser(UserData data);
    void deleteAll();
}