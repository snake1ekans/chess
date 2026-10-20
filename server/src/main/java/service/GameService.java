package service;

import chess.ChessGame;
import dataaccess.GameDAC;
import dataaccess.GameDAC_Loc;
import exceptions.IncompleteRequestException;
import model.GameData;

import java.util.HashMap;
import java.util.List;

//todo: create tests for these functions
public class GameService {

    GameDAC dataAccess;

    public GameService(){
        dataAccess = new GameDAC_Loc();
    }

    public Integer createGame(GameData gameData) throws IncompleteRequestException {
        if (gameData.gameName() == null) {
            throw new IncompleteRequestException("no ID");
        }
        return dataAccess.createGame(gameData.gameName());
    }

//    public HashMap<String, GameData> getGames() {
//        dataAccess.
//    }

    public void deleteDB(){
        dataAccess.deleteAll();
    }
}
