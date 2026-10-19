package dataaccess;

import chess.ChessBoard;

import java.util.HashMap;

public class GameDAC_Loc implements GameDAC {
    HashMap<String, ChessBoard> gamesMap;


    public GameDAC_Loc() {
        gamesMap = new HashMap<>();
    }


    public void deleteAll() {
        gamesMap.clear();
    }
}
