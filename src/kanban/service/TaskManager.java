package kanban.service;

import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

import java.util.List;
import java.util.ArrayList;

public interface TaskManager {
    void createTask(Task task);

    void createSubTask(SubTask subTask);

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

    ArrayList<Task> getMapTask();

    ArrayList<Epic> getMapEpic();

    ArrayList<SubTask> getMapSubTask();

    Task getById(int i);
}
