package dataaccess;

import chess.ChessGame;
import exceptions.DataAccessException;
import exceptions.DataConflictException;
import model.GameData;
import service.JoinRequestData;

import java.util.*;


public class GameDAC_Loc implements GameDAC {

    // Globals
    HashMap<Integer, GameData> gamesMap;
    List<GameData> listGamesMap;
    private static final int maxGames = 9999;

    public GameDAC_Loc() {
        gamesMap = new HashMap<>();
        listGamesMap = new ArrayList<>();
    }

    public Integer createGame(String gameName) {
        Random rng = new Random();
        Integer gameID = rng.nextInt(maxGames);
        ChessGame newGame = new ChessGame();

        GameData gameData = new GameData(gameID, null, null, gameName, newGame);
        gamesMap.put(gameID, gameData);
        GameData listGameData = new GameData(gameID, null, null, gameName, null);
        listGamesMap.add(listGameData);

        return gameID;
    }

    public List<GameData> listGames(){
        return listGamesMap;
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
    }
}
