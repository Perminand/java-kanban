package kanban.exceptions;

public class ManagerSaveException extends RuntimeException {
    int i = 0;
    public ManagerSaveException(String message) {
        super(message);
    }
}
