package dataaccess;

import exceptions.DataAccessException;
import model.GameData;
import service.JoinRequestData;

import java.util.List;

public interface GameDAC {
    Integer createGame(String gameName);
    List<GameData> listGames();
    GameData getGame(Integer gameID);
    void joinGame(JoinRequestData joinRequestData) throws DataAccessException;
    void deleteAll();
}
