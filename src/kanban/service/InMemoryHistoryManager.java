package kanban.service;

import kanban.interfaces.HistoryManager;
import kanban.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> getHistory = new ArrayList<>();
    private final List<String> fullHistory = new ArrayList<>();

    public void addGetTask(Task task) {
        if (getHistory.size() > 9) getHistory.remove(0);
        getHistory.add(task);
    }

    public void addFullTask(String string) {
        if (fullHistory.size() > 9) fullHistory.remove(0);
        fullHistory.add(string);
    }

    @Override
    public void add(Task task) {
    }

    @Override
    public List<Task> getHistory() {
        return getHistory;
    }

    public List<String> fullHistory() {
        return fullHistory;
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
