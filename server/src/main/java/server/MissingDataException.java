package server;

// Thrown whenever improper or insufficient dataAccess is passed to the server
public class MissingDataException extends RuntimeException {
    public MissingDataException(String message) {
        super(message);
    }
}
