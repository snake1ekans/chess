package service;

import dataaccess.GameDAC;
import dataaccess.GameDacLoc;
import dataaccess.GameDacSql;
import exceptions.DataAccessException;
import exceptions.DataConflictException;
import exceptions.IncompleteRequestException;
import model.GameData;

import java.util.List;
import java.util.Objects;

//todo: create tests for these functions
public class GameService {

    GameDAC dataAccess;

    public GameService(){
        dataAccess = new GameDacSql();
    }

    public Integer createGame(GameData gameData) throws IncompleteRequestException {
        if (gameData.gameName() == null) {
            throw new IncompleteRequestException("no ID");
        }
        return dataAccess.createGame(gameData.gameName());
    }

    public List<GameData> getGames() {
        return dataAccess.listGames();
    }

    public void joinGame(JoinRequestData requestData) throws DataConflictException, DataAccessException {
        if (!Objects.equals(requestData.playerColor(), "WHITE") && !Objects.equals(requestData.playerColor(), "BLACK")) {
            throw new DataAccessException("invalid color");
        }
        dataAccess.joinGame(requestData);
    }
    public GameData getGame(Integer gameID){ //testing-only
        return dataAccess.getGame(gameID);
    }

    public void deleteDB(){
        dataAccess.deleteAll();
    }
}
