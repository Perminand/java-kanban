package kanban.service;

import kanban.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final byte SIZE_HISTORY = 10;
    private static final byte ID_REMOVE_FOR_HISTORY = 0;
    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() >= SIZE_HISTORY) history.remove(ID_REMOVE_FOR_HISTORY);
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }
}


