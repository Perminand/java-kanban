package kanban.exceptions;

public class NoEpicExceptionForSubTask extends RuntimeException {
    public NoEpicExceptionForSubTask(String message) {
        super(message);
    }
}
