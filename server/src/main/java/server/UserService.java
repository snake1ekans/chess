package server;

import dataaccess.DataConflictException;
import dataaccess.UserDAC;
import dataaccess.UserDAC_Loc;
import model.UserData;

public class UserService {

    UserDAC data;

    public UserService() {
        data = new UserDAC_Loc();
    }

    public void register(UserData userData) throws DataConflictException {
        if (data.getUser(userData)) {
            throw new DataConflictException("already taken");
        } else {
            data.addUser(userData);
        }
    }

    public Boolean userExists(UserData userData){
        return data.getUser(userData);
    }

}
