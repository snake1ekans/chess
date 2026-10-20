package dataaccess;

public interface GameDAC {
    Integer createGame(String gameName);
    Boolean findGame(String gameID);
    void deleteAll();
}
