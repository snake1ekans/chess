package dataaccess;

//todo: also functions as the local database for testing and phase 3 code

import model.UserData;

import java.util.HashSet;

public class UserDAC_Loc implements UserDAC{
    HashSet<UserData> userDataLocal;

    public UserDAC_Loc(){
        userDataLocal = new HashSet<>();
    }

    public void addUser(UserData data){
        userDataLocal.add(data);
    }

    public Boolean getUser(UserData data) {
        return (userDataLocal.contains(data));
    }

    public void removeUser(UserData data) {
        userDataLocal.remove(data);
    }
}
