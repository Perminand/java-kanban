package kanban.service;

import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

import java.util.ArrayList;

interface Manager {


    void createTask(Task task);

    void createSubTask(SubTask subTask);

    int createEpic(Epic epic);


    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void updateTask(Task task);

    Task getById(int id);

    void deleteById(int id);

    void removeAllTask();

    void removeAllSubTask();

    void removeAllEpic();

    ArrayList<SubTask> getSubTaskByIdEpic(Epic epic);

    ArrayList<Task> getMapTask();

    ArrayList<Epic> getMapEpic();

    ArrayList<SubTask> getMapSubTask();

}
