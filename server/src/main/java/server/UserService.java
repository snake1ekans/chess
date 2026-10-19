package server;

import dataaccess.DataConflictException;
import dataaccess.IncompleteRequestException;
import dataaccess.UserDAC;
import dataaccess.UserDAC_Loc;
import model.UserData;

public class UserService {

    UserDAC dataAccess;

    // todo: Switch to SQL when phase 4 starts
    public UserService() {
        dataAccess = new UserDAC_Loc();
    }

    public void register(UserData userData) throws DataConflictException, IncompleteRequestException {
        checkValue(userData);
        if (dataAccess.getUser(userData)) {
            throw new DataConflictException("already taken");
        } else {
            dataAccess.addUser(userData);
        }
    }

    public Boolean userExists(UserData userData){
        return dataAccess.getUser(userData);
    }

    public void deleteDB() {
        dataAccess.deleteAll();
    }


    private void checkValue(UserData userData) throws IncompleteRequestException {
        if (userData.username() == null ||
                userData.password() == null ||
                userData.email() == null) {
            throw new IncompleteRequestException("bad dataAccess");
        }
    }
}
