package kanban.service;

import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

import java.util.ArrayList;

interface Manager {
    void createTask(Task task);

    void createSubTask(SubTask subTask);

    void createEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void updateTask(Task task);

    Task getTask(int id);

    SubTask getSubTask(int id);

    Epic getEpic(int id);

    Task getFindById(int id);

    void deleteById(int id);

    void removeAllTask();

    void removeAllSubTask();

    void removeAllEpic();

}
