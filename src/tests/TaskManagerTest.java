package tests;

import kanban.enumClass.Status;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.service.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
    protected Task task;
    protected SubTask subTask1;
    protected SubTask subTask2;
    protected SubTask subTask3;
    protected Epic epic;
    Duration duration = Duration.ofMinutes(1);
    private int idEpic;

    protected void initTasks() {
        this.task = new Task("name1", "description1", duration);
        this.epic = new Epic("name1", "description1");
        this.idEpic = manager.createEpic(epic); // id 0
        this.subTask1 = new SubTask("Подзадача1", "Описание подзадачи1", idEpic,
                LocalDateTime.of(2024, 1, 1, 0, 0), duration);
        this.subTask2 = new SubTask("Подзадача2", "Описание подзадачи2", idEpic,
                LocalDateTime.of(2024, 1, 1, 0, 2), duration);
        this.subTask3 = new SubTask("Подзадача3", "Описание подзадачи3", idEpic,
                LocalDateTime.of(2024, 1, 1, 0, 4), duration);
        manager.createSubTask(subTask1); // id 1
        manager.createSubTask(subTask2); // id 2
        manager.createSubTask(subTask3); // id 3
        manager.createTask(task);

    }

    @Test
    void createTaskTest() {
        Assertions.assertNotNull(manager.getTasks().get(0), "Задача не найдена");
        Assertions.assertEquals(task, manager.getTasks().get(0), "Задачи не совпадают");
        Assertions.assertSame(manager.getTasks().get(0).getStatus(), Status.NEW, "Статус не NEW");
        Assertions.assertEquals(manager.getTasks().get(0).getUin(), 4, "UIN не назначается");

        final List<Task> listTask = manager.getTasks();
        Assertions.assertNotNull(listTask, "Задачи не возвращаются");
        Assertions.assertEquals(1, listTask.size(), "Неверное количество задач");
        Assertions.assertEquals(task, listTask.get(0), "Задачи не совпадают");
        Assertions.assertNotNull(task.getStartTime(), "startTime null");
        Assertions.assertNotNull(task.getDuration(), "Duration null");
        Assertions.assertEquals(epic.getStartTime(), subTask1.getStartTime(), "startTime не null");
        Assertions.assertEquals(epic.getDuration(), subTask1.getDuration().plus(
                subTask2.getDuration().plus(subTask3.getDuration())), "Duration не null");
        Assertions.assertNotNull(subTask1.getStartTime(), "startTime null");
        Assertions.assertNotNull(subTask1.getDuration(), "Duration null");
    }


    @Test
    void createSubTaskTest() {
        SubTask subTask = new SubTask("Подзадача1", "Описание подзадачи1", idEpic,
                LocalDateTime.of(2024, 1, 1, 0, 10), duration);
        final int idSubTask = manager.createSubTask(subTask);
        Task savedTask = manager.getSubTask(idSubTask);
        Assertions.assertEquals(subTask, savedTask, "Задачи не совпадают");
        Assertions.assertNotNull(savedTask, "Задача не найдена");
        Assertions.assertSame(savedTask.getStatus(), Status.NEW, "Статус не NEW");
        final List<SubTask> listSubTask = manager.getSubTasks();
        Assertions.assertNotNull(listSubTask, "Задачи не возвращаются");
        Assertions.assertEquals(4, listSubTask.size(), "Неверное количество задач");
        Assertions.assertEquals(subTask, listSubTask.get(3), "Задачи не совпадают");
        Assertions.assertEquals(subTask.getEpicId(), idEpic, "id эпик не совпадает");
        manager.removeAllSubTask();
        Assertions.assertNull(manager.getSubTask(1));
    }

    @Test
    void createEpicTestStatusDone() {
        assertEquals(epic, manager.getEpic(idEpic), "Epic не совпадает");
        assertEquals(epic.getStatus(), Status.NEW, "Статусы не NEW");
        manager.removeAllSubTask();
        Assertions.assertTrue(epic.getIdSubTask().isEmpty(), "Список не пустой");
        manager.createSubTask(new SubTask(1, "Подзадача1", "Описание подзадачи1", Status.DONE,
                idEpic, LocalDateTime.of(2024, 1, 1, 0, 0), duration));
        manager.createSubTask(new SubTask(2, "Подзадача2", "Описание подзадачи2", Status.DONE,
                idEpic, LocalDateTime.of(2024, 1, 1, 0, 2), duration));
        manager.createSubTask(new SubTask(3, "Подзадача3", "Описание подзадачи3", Status.DONE,
                idEpic, LocalDateTime.of(2024, 1, 1, 0, 4), duration));
        assertEquals(epic.getStatus(), Status.DONE, "Статусы не DONE");
    }

    @Test
    void createEpicTestStatusDoneAndNew() {
        manager.removeAllSubTask();
        manager.createSubTask(new SubTask("Подзадача1", "Описание подзадачи1", idEpic,
                LocalDateTime.of(2024, 1, 1, 0, 0), duration));
        manager.createSubTask(new SubTask(2, "Подзадача2", "Описание подзадачи2", Status.DONE,
                idEpic, LocalDateTime.of(2024, 1, 1, 0, 2), duration));
        manager.createSubTask(new SubTask(3, "Подзадача3", "Описание подзадачи3", Status.DONE,
                idEpic, LocalDateTime.of(2024, 1, 1, 0, 4), duration));
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статусы не IN_PROGRESS");
    }

    @Test
    void createEpicTestStatusIN_PROGRESS() {
        manager.removeAllSubTask();
        manager.createSubTask(new SubTask(1, "Подзадача1", "Описание подзадачи1",
                Status.IN_PROGRESS, idEpic, LocalDateTime.of(2024, 1, 1, 0, 0), duration));
        manager.createSubTask(new SubTask(2, "Подзадача2", "Описание подзадачи2",
                Status.IN_PROGRESS, idEpic, LocalDateTime.of(2024, 1, 1, 0, 2), duration));
        manager.createSubTask(new SubTask(3, "Подзадача3", "Описание подзадачи3",
                Status.IN_PROGRESS, idEpic, LocalDateTime.of(2024, 1, 1, 0, 4), duration));
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статусы не IN_PROGRESS");
    }

    @Test
    void createEpicTest() {
        final List<Epic> listTask = manager.getEpics();
        Assertions.assertNotNull(listTask, "Задачи не возвращаются");
        Assertions.assertEquals(1, listTask.size(), "Неверное количество задач");
        Assertions.assertEquals(epic, listTask.get(0), "Задачи не совпадают");
    }

    @Test
    void updateTaskValidId() {
        manager.removeAllTask();
        assertEquals(new ArrayList<Task>(), manager.getTasks(), "getTasks не пустой");
        Task task = new Task("name1", "description1", Status.NEW,
                LocalDateTime.of(2024, 1, 1, 0, 0), duration);
        int id = manager.createTask(task);
        Task newTask = new Task(id, "newName1", "newDescription1", Status.DONE,
                LocalDateTime.of(2024, 1, 1, 0, 2), duration);
        manager.updateTask(newTask);
        assertEquals(newTask, manager.getTask(id));
    }

    @Test
    void updateTaskNoValidId() {
        Task task = new Task("name1", "description1", duration);
        int id = manager.createTask(task);
        Task newTask = new Task(-1, "newName1", "newDescription1", Status.DONE, duration);
        manager.updateTask(newTask);
        assertNotEquals(manager.getTask(id), newTask);
    }

    @Test
    void updateSubTaskValidId() {
        manager.removeAllSubTask();
        manager.removeAllTask();
        assertEquals(new ArrayList<SubTask>(), manager.getSubTasks(), "getSubTasks не пустой");
        Epic epic = new Epic("name1", "description");
        int idEpic = manager.createEpic(epic);
        SubTask subTask = new SubTask("name1", "description1", idEpic, duration);
        int idSubTask = manager.createSubTask(subTask);
        SubTask newSubTask = new SubTask(idSubTask, "newName1", "newDescription1", Status.DONE,
                idEpic, duration);
        SubTask newSubTask2 = new SubTask(idSubTask, "newName2", "newDescription2", Status.DONE,
                idEpic, LocalDateTime.of(2024, 1, 1, 0, 0), duration);
        manager.updateSubTask(newSubTask);
        assertEquals(newSubTask, manager.getSubTask(newSubTask.getUin()), "Задача создалась при пересечении");
        manager.updateSubTask(newSubTask2);
        assertEquals(newSubTask2, manager.getSubTask(newSubTask2.getUin()));
    }

    @Test
    void updateSubTaskNoValidId() {
        SubTask subTasksValid1 = subTask1;
        SubTask subTasksValid2 = subTask2;
        SubTask subTasksValid3 = subTask3;
        SubTask newSubTask = new SubTask(4, "newName1", "newDescription1", Status.DONE, 3, duration);
        manager.updateSubTask(newSubTask);
        assertTrue(subTasksValid1 == subTask1 && subTasksValid2 == subTask2 && subTasksValid3 == subTask3);
    }

    @Test
    void updateEpicValidIEpicFormat() {
        Epic newEpic2 = new Epic(idEpic, "name1", "description", Status.IN_PROGRESS);
        manager.updateEpic(newEpic2);
        Assertions.assertEquals(newEpic2.getStatus(), manager.getEpic(idEpic).getStatus());
    }

    @Test
    void updateEpicNoValidIEpicFormat() {
        manager.removeAllEpic();
        assertEquals(new ArrayList<Epic>(), manager.getEpics(), "getEpic не пустой");
        Epic epic = new Epic("name1", "description");
        int idEpic = manager.createEpic(epic);
        assertEquals(epic, manager.getEpic(idEpic));
        Epic newEpic = new Epic("name1", "description");
        manager.updateEpic(newEpic);
        assertNotEquals(newEpic, manager.getEpic(idEpic));
    }

    @Test
    void getTaskValidId() {
        Assertions.assertNotNull(manager.getTask(4));
    }

    @Test
    void getTaskNoValidId() {
        Assertions.assertNull(manager.getTask(0));
        Assertions.assertNull(manager.getTask(-1));
    }


    @Test
    void getSubTaskValidId() {
        Assertions.assertEquals(subTask1, manager.getSubTask(1));
    }

    @Test
    void getSubTaskNoValidId() {
        Assertions.assertNull(manager.getSubTask(-1));
    }

    @Test
    void getEpicValidId() {
        Assertions.assertEquals(epic, manager.getEpic(0));
    }

    @Test
    void getEpicNoValidId() {
        manager.removeAllEpic();
        Assertions.assertNull(manager.getEpic(0));
    }

    @Test
    void deleteByINoValidIdTask() {
        int size = manager.getTasks().size();
        for (Integer i : List.of(-1, 3, 23))
            manager.deleteById(i);
        Assertions.assertEquals(size, manager.getTasks().size());
    }

    @Test
    void deleteByIdNoValidIdSubTask() {
        int size = manager.getSubTasks().size();
        for (Integer i : List.of(-1, 4, 23))
            manager.deleteById(i);
        Assertions.assertEquals(size, manager.getSubTasks().size());
    }

    @Test
    void deleteByIdValidIdEpic() {
        int size = manager.getTasks().size();
        manager.deleteById(4);
        Assertions.assertEquals(size - 1, manager.getTasks().size(), "Элемент TASK не удалился");
        size = manager.getSubTasks().size();
        manager.deleteById(1);
        Assertions.assertEquals(size - 1, manager.getSubTasks().size(), "Элемент SUBTASK не удалился");
        size = manager.getEpics().size();
        manager.deleteById(0);
        Assertions.assertEquals(size - 1, manager.getEpics().size(), "Элемент EPIC не удалился");
    }

    @Test
    void deleteByIdNoValidIdEpic() {
        int size = manager.getEpics().size();
        for (Integer i : List.of(-1, 3, 23))
            manager.deleteById(i);
        Assertions.assertEquals(manager.getEpics().size(), size);
    }


    @Test
    void removeAllTask() {
        //Проверяем удаления списка TASK
        manager.removeAllTask();
        Assertions.assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void removeAllSubTask() {
        //Проверяем изменение статуса после удаление
        for (SubTask subTask : manager.getSubTasks()) subTask.setStatus(Status.DONE);
        manager.getEpic(0).setStatus(Status.DONE);
        //Проверяем удаления списка SUBTASK
        manager.removeAllSubTask();
        Assertions.assertTrue(manager.getSubTasks().isEmpty());
        Assertions.assertEquals(Status.NEW, manager.getEpic(0).getStatus(), "Статус не поменялся");
        manager.removeAllSubTask();
        Assertions.assertTrue(manager.getSubTasks().isEmpty());
    }

    @Test
    void removeAllEpic() {
        //Проверяем удаления списка Epic,Subtask
        manager.removeAllEpic();
        Assertions.assertTrue(manager.getEpics().isEmpty());
        Assertions.assertTrue(manager.getSubTasks().isEmpty());
        manager.removeAllEpic();
        Assertions.assertEquals(new ArrayList<>(), manager.getEpics());
    }

    @Test
    void getHistoryValidIsEmpty() {
        //Проверяем на пустую историю
        Assertions.assertEquals(new ArrayList<Task>(), manager.getHistory());
    }

    @Test
    void getHistoryValid() {
        manager.getById(0);
        Assertions.assertEquals(manager.getById(0), manager.getHistory().get(0));
    }

    @Test
    void getTasksValid() {
        int size = manager.getTasks().size();
        manager.createTask(new Task("n", "d", Status.NEW,
                LocalDateTime.of(2024, 1, 1, 0, 1), duration));
        Assertions.assertEquals(manager.getTasks().size(), size + 1);
    }

    @Test
    void getTasksValidIsEmpty() {
        manager.removeAllTask();
        Assertions.assertEquals(manager.getTasks().size(), 0);
    }

    @Test
    void getSubTasksValid() {
        int size = manager.getSubTasks().size();
        manager.createSubTask(new SubTask("n", "d", 0,
                LocalDateTime.of(2024, 1, 1, 0, 1), duration));
        Assertions.assertEquals(manager.getSubTasks().size(), size + 1);
    }

    @Test
    void getSubTasksValidIsEmpty() {
        manager.removeAllSubTask();
        Assertions.assertEquals(manager.getSubTasks().size(), 0);
    }

    @Test
    void getEpicsValid() {
        int size = manager.getEpics().size();
        manager.createEpic(new Epic("n", "d"));
        Assertions.assertEquals(manager.getEpics().size(), size + 1);
    }

    @Test
    void getEpicsValidIsEmpty() {
        manager.removeAllEpic();
        Assertions.assertEquals(manager.getEpics().size(), 0);
    }

    @Test
    void getByIdNoValidId() {
        Assertions.assertNull(manager.getById(-1));
        Assertions.assertNull(manager.getById(23));
    }

    @Test
    void getByIdValidId() {
        Assertions.assertEquals(manager.getById(1), subTask1);
        Assertions.assertEquals(manager.getById(0), epic);
        Assertions.assertEquals(manager.getById(4), task);
    }

    @Test
    void getByIdNoValidIdFromTypeTask() {
        assertNotEquals(manager.getById(0), subTask1);
        assertNotEquals(manager.getById(1), epic);
        assertNotEquals(manager.getById(6), task);
    }
}
