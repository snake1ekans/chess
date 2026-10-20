package exceptions;

//

public class IncompleteRequestException extends Exception{
    public IncompleteRequestException(String message) {
        super(message);
    }
    public IncompleteRequestException(String message, Throwable ex) {
        super(message, ex);
    }
}
