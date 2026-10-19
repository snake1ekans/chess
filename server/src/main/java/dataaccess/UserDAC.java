package dataaccess;

import model.UserData;

public interface UserDAC {
    void addUser(UserData data);
    Boolean getUser(UserData data);
    void removeUser(UserData data);
    void deleteAll();
}