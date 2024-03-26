package kanban.exceptions;

public class NoTaskException extends RuntimeException {
    public NoTaskException(String message) {
        super(message);
    }
}
