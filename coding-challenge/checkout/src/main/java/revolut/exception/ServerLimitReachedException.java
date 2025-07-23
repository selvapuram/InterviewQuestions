package revolut.exception;

public class ServerLimitReachedException extends RuntimeException {
    public ServerLimitReachedException(String message) {
        super(message);
    }
}
