package server;

import dataaccess.GameDAC;
import dataaccess.GameDAC_Loc;

public class GameService {

    GameDAC dataAccess;

    GameService(){
        dataAccess = new GameDAC_Loc();
    }
    public void deleteDB(){
        dataAccess.deleteAll();
    }
}
