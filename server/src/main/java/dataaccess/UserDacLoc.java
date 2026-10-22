package dataaccess;

//todo: also functions as the local database for testing and phase 3 code

import model.UserData;

import java.util.HashMap;

public class UserDacLoc implements UserDAC{
    HashMap<String, UserData> userDataLocal;

    public UserDacLoc(){
        userDataLocal = new HashMap<>();
    }

    public void addUser(UserData data){
        userDataLocal.put(data.username(), data);
    }

    public UserData getUser(UserData data) {
        return (userDataLocal.get(data.username()));
    }

    public void deleteAll(){
        userDataLocal.clear();
    }
}
