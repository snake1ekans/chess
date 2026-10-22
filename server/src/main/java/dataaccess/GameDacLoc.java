package dataaccess;

import chess.ChessGame;
import exceptions.DataAccessException;
import exceptions.DataConflictException;
import model.GameData;
import service.JoinRequestData;

import java.util.*;


public class GameDacLoc implements GameDAC {

    // Globals
    HashMap<Integer, GameData> gamesMap;
    List<GameData> listGamesMap;
    HashMap<String, Integer> gameNames;
    private static final int MAX_GAMES = 9999;

    public GameDacLoc() {
        gamesMap = new HashMap<>();
        listGamesMap = new ArrayList<>();
        gameNames = new HashMap<>();
    }

    public Integer createGame(String gameName) {
        Random rng = new Random();
        Integer gameID = rng.nextInt(MAX_GAMES);
        ChessGame newGame = new ChessGame();
        if(gameNames.get(gameName) != null){
            throw new DataConflictException("Already Exists");
        }
        gameNames.put(gameName, gameID);
        GameData gameData = new GameData(gameID, null, null, gameName, newGame);
        gamesMap.put(gameID, gameData);
        GameData listGameData = new GameData(gameID, null, null, gameName, null);
        listGamesMap.add(listGameData);

        return gameID;
    }

    public List<GameData> listGames(){
        return listGamesMap;
    }

    public GameData getGame(Integer gameID) {
        return gamesMap.get(gameID);
    }


    public void joinGame(JoinRequestData joinRequestData) throws DataConflictException, DataAccessException {
        GameData target = gamesMap.get(joinRequestData.gameID());
        if (target==null){
            throw new DataAccessException("game doesn't exist");
        }
        if (Objects.equals(joinRequestData.playerColor(), "BLACK")) {
            if (target.blackUsername() != null){
                throw new DataConflictException("slot already taken");
            }
            GameData updatedGame = new GameData(target.gameID(), target.whiteUsername(),
                    joinRequestData.userName(), target.gameName(), target.game());
            gamesMap.replace(joinRequestData.gameID(), updatedGame);
            listGamesMap.remove(new GameData(target.gameID(), target.whiteUsername(), target.blackUsername(), target.gameName(), null));
            listGamesMap.add(new GameData(target.gameID(), target.whiteUsername(), joinRequestData.userName(), target.gameName(), null));

        } else {

            if (target.whiteUsername() != null){
                throw new DataConflictException("slot already taken");
            }
            GameData updatedGame = new GameData(target.gameID(), joinRequestData.userName(),
                    target.blackUsername(), target.gameName(), target.game());
            gamesMap.replace(joinRequestData.gameID(), updatedGame);
            listGamesMap.remove(new GameData(target.gameID(), target.whiteUsername(), target.blackUsername(), target.gameName(), null));
            listGamesMap.add(new GameData(target.gameID(), joinRequestData.userName(), target.blackUsername(), target.gameName(), null));
        }
    }

    public void deleteAll() {
        gamesMap.clear();
        listGamesMap.clear();
        gameNames.clear();
    }
}
