package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAC {
    Integer createGame(String gameName);
    Boolean findGame(String gameID);
    HashMap<Integer, GameData> listGames();
    void deleteAll();
}
