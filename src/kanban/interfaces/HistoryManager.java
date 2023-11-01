package kanban.interfaces;

import kanban.model.Task;
import kanban.service.InMemoryHistoryManager;

import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
    List<String> fullHistory();
}
