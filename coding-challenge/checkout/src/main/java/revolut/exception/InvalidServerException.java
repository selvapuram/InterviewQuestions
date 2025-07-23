package revolut.exception;

public class InvalidServerException extends RuntimeException {
    public InvalidServerException(String message) {
        super(message);
    }
}
