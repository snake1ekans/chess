package service;

import dataaccess.*;
import exceptions.DataAccessException;
import exceptions.DataConflictException;
import exceptions.IncompleteRequestException;
import model.UserData;

import java.util.Objects;

public class UserService {

    UserDAC dataAccess;

    // todo: Switch to SQL when phase 4 starts
    public UserService() {
        dataAccess = new UserDacLoc();
    }

// ---------- endpoint parents ----------
    public void register(UserData userData) throws DataConflictException, IncompleteRequestException {
        //todo: need to reserve "Player Slot Available" or prohibit whitespace chars in username
        if (userData.username() == null ||
                userData.password() == null ||
                userData.email() == null) {
            throw new IncompleteRequestException("bad dataAccess");
            //todo: write test for this
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
        if (!Objects.equals(cred.password(), userData.password())){
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
