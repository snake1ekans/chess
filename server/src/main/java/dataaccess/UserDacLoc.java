package dataaccess;

//functions as the local database for testing and phase 3 code

import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;

public class UserDacLoc implements UserDAC{
    HashMap<String, UserData> userDataLocal;

    public UserDacLoc(){
        userDataLocal = new HashMap<>();
    }

    public void addUser(UserData data){
        String hashedPassword = BCrypt.hashpw(data.password(), BCrypt.gensalt());
        UserData newPass = new UserData(data.username(), hashedPassword, data.email());
        userDataLocal.put(data.username(), newPass);
    }

    public UserData getUser(UserData data) {
        return (userDataLocal.get(data.username()));
    }

    public void deleteAll(){
        userDataLocal.clear();
    }
}
