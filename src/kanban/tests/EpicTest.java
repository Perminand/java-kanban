package kanban.tests;

import kanban.enumClass.Status;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.service.Managers;
import kanban.service.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    TaskManager manager = Managers.getDefault();
    Epic epic = new Epic("epic1", "description1");
    int idEpic = manager.createEpic(epic);
    @Test
    public void getIdSubTaskTestFromEmpty() {
        Assertions.assertTrue(epic.getIdSubTask().isEmpty(),"Список не пустой");
    }

    @Test
    public void TaskEqualsCreateToGetById(){
        assertEquals(epic,manager.getEpic(idEpic),"Epic не совпадает");
    }

    @Test
    public void getStatusSubTaskAllStatusNew() {
        epic.setUin(idEpic);
        manager.createSubTask(new SubTask("Подзадача1", "Описание подзадачи1", idEpic));
        manager.createSubTask(new SubTask("Подзадача2", "Описание подзадачи2", idEpic));
        manager.createSubTask(new SubTask("Подзадача3", "Описание подзадачи3", idEpic));
        assertEquals(epic.getStatus(),Status.NEW,"Статусы не NEW");
    }

    @Test
    public void getStatusSubTaskAllStatusDone() {
        manager.createSubTask(new SubTask(1, "Подзадача1", "Описание подзадачи1", Status.DONE, idEpic));
        manager.createSubTask(new SubTask(2, "Подзадача2", "Описание подзадачи2", Status.DONE, idEpic));
        manager.createSubTask(new SubTask(3, "Подзадача3", "Описание подзадачи3", Status.DONE, idEpic));
        assertEquals(epic.getStatus(),Status.DONE,"Статусы не DONE");
    }
    @Test
    public void getStatusSubTaskStatusNewAndDone() {
        manager.createSubTask(new SubTask("Подзадача1", "Описание подзадачи1",idEpic));
        manager.createSubTask(new SubTask(2, "Подзадача2", "Описание подзадачи2", Status.DONE, idEpic));
        manager.createSubTask(new SubTask(3, "Подзадача3", "Описание подзадачи3", Status.DONE, idEpic));
        assertEquals(epic.getStatus(),Status.IN_PROGRESS,"Статусы не IN_PROGRESS");
    }
    @Test
    public void getStatusSubTaskAllStatusInProgress() {
        manager.createSubTask(new SubTask(1, "Подзадача1", "Описание подзадачи1", Status.IN_PROGRESS, idEpic));
        manager.createSubTask(new SubTask(2, "Подзадача2", "Описание подзадачи2", Status.IN_PROGRESS, idEpic));
        manager.createSubTask(new SubTask(3, "Подзадача3", "Описание подзадачи3", Status.IN_PROGRESS, idEpic));
        assertEquals(epic.getStatus(),Status.IN_PROGRESS,"Статусы не IN_PROGRESS");
    }

}