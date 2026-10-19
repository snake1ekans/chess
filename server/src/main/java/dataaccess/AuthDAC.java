package dataaccess;

import model.AuthData;

public interface AuthDAC {
    void writeToken(AuthData token);
    boolean checkToken(AuthData token);
    void deleteToken(AuthData token);
    void deleteAll();
}
