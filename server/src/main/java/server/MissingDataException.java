package server;

// Thrown whenever improper or insufficient data is passed to the server
public class MissingDataException extends RuntimeException {
    public MissingDataException(String message) {
        super(message);
    }
}
