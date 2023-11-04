package kanban.service;

public class Managers {
    public TaskManager getDefault() {
        return new InMemoryTaskTaskManager();
    }

    public InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
