package dataaccess;

// A conflict file that is thrown whenever unauthorized write requests are made within the database
// Useful for
public class DataConflictException extends RuntimeException {
    public DataConflictException(String message) {
        super(message);
    }
}
