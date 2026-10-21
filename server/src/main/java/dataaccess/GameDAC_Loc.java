package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.Random;


public class GameDAC_Loc implements GameDAC {

    // Globals
    HashMap<Integer, GameData> gamesMap;
    HashMap<Integer, GameData> listGamesMap;
    private static final int maxGames = 9999;

    public GameDAC_Loc() {
        gamesMap = new HashMap<>();
        listGamesMap = new HashMap<>();
    }

    public Integer createGame(String gameName) {
        Random rng = new Random();
        Integer gameID = rng.nextInt(maxGames);
        ChessGame newGame = new ChessGame();

        GameData gameData = new GameData(gameID, null, null, gameName, newGame);
        gamesMap.put(gameID, gameData);
        GameData listGameData = new GameData(gameID, "Player Slot Available", "Player Slot Available", gameName, null);
        listGamesMap.put(gameID, listGameData);

        return gameID;
    }

    public HashMap<Integer, GameData> listGames(){
        return listGamesMap;
    }
    public Boolean findGame(String gameID){
        return false; // todo:temp
    }
    public void deleteAll() {
        gamesMap.clear();
        listGamesMap.clear();
    }
}
