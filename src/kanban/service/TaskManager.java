package kanban.service;

import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    int createTask(Task task);

    int createSubTask(SubTask subTask);

    int createEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void updateTask(Task task);

    Task getTask(int id);

    SubTask getSubTask(int id);

    Epic getEpic(int id);

    void deleteById(int id);

    void removeAllTask();

    void removeAllSubTask();

    void removeAllEpic();

    List<Task> getHistory();

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<SubTask> getSubTasks();

    Task getById(int i);

    TreeSet<Task> getPrioritizedTasks();
}
