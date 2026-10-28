package service;

import dataaccess.*;
import exceptions.DataAccessException;
import exceptions.DataConflictException;
import exceptions.IncompleteRequestException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class UserService {

    UserDAC dataAccess;
    public UserService() {
        dataAccess = new UserDacSql();
    }

// ---------- endpoint parents ----------
    public void register(UserData userData) throws DataConflictException, IncompleteRequestException {
        if (userData.username() == null ||
                userData.password() == null ||
                userData.email() == null) {
            throw new IncompleteRequestException("bad dataAccess");
        }
        //check if user exists, then throw error or add user
        if (dataAccess.getUser(userData)!= null) {
            throw new DataConflictException("already taken");
        } else {
            dataAccess.addUser(userData);
        }
    }

    public Boolean login(UserData userData) throws IncompleteRequestException, DataAccessException, DataConflictException {
        if (userData.username() == null || userData.password() == null){
            throw new IncompleteRequestException("missing Username or password");
        }

        UserData cred = dataAccess.getUser(userData);
        if (cred == null){
            throw new DataAccessException("user not found");
        }
        if (!BCrypt.checkpw(userData.password(), cred.password())){
            throw new DataConflictException("wrong password");
        }
        return true;
    }


    public void deleteDB() {
        dataAccess.deleteAll();
    }

// --------- helper and non-endpoint functions ----------

    public Boolean userExists(UserData userData){
        return (dataAccess.getUser(userData) != null);
    }
}
