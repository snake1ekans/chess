package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.Random;


public class GameDAC_Loc implements GameDAC {

    // Globals
    HashMap<Integer, GameData> gamesMap;
    private static final int maxGames = 1000;

    public GameDAC_Loc() {
        gamesMap = new HashMap<>();
    }

    public Integer createGame(String gameName) {
        Random rng = new Random();
        Integer gameID = rng.nextInt(maxGames);
        ChessGame newGame = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, newGame);
        gamesMap.put(gameID, gameData);
        return gameID;
    }

    public Boolean findGame(String gameID){
        return false; // todo:temp
    }
    public void deleteAll() {
        gamesMap.clear();
    }
}
