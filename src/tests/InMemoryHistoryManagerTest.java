package tests;

import kanban.enumClass.Status;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.service.HistoryManager;
import kanban.service.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;


public class InMemoryHistoryManagerTest {
    protected Task taskNoValid;
    protected Task taskValid;
    protected SubTask subTask;
    protected Epic epic;
    HistoryManager historyManager;
    Duration duration = Duration.ofMinutes(60);

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        taskNoValid = new Task("n", "d", duration);
        taskValid = new Task(0, "n", "d", Status.NEW, duration);
        epic = new Epic(1, "n", "d", Status.NEW);
        subTask = new SubTask("n", "d", epic.getUin(), duration);
    }

    @Test
    void add_IsEmptyTask() {
        Assertions.assertEquals(historyManager.getHistory(), new ArrayList<>());
        historyManager.add(taskValid);
        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void add_IsEmptySubTask() {
        Assertions.assertEquals(historyManager.getHistory(), new ArrayList<>());
        subTask.setUin(0);
        historyManager.add(subTask);
        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void add_IsEmptyEpic() {
        Assertions.assertEquals(historyManager.getHistory(), new ArrayList<>());
        historyManager.add(epic);
        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void add_Double() {
        getHistory_Double();
    }

    @Test
    void add_Delete() {
        getHistory_Delete();
    }

    @Test
    void getHistory_IsEmpty() {
        Assertions.assertEquals(historyManager.getHistory(), new ArrayList<>());
    }

    @Test
    void getHistory_IsEqualsNoValid() {
        historyManager.add(taskNoValid);
        historyManager.add(subTask);
        Assertions.assertEquals(0, historyManager.getHistory().size());

    }

    @Test
    void getHistory_IsEqualsValid() {
        historyManager.add(taskValid);
        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void getHistory_Double() {
        historyManager.add(taskValid);
        historyManager.add(taskValid);
        Assertions.assertEquals(1, historyManager.getHistory().size());

    }

    @Test
    void getHistory_Delete() {
        historyManager.add(taskValid);
        historyManager.remove(0);
        Assertions.assertEquals(0, historyManager.getHistory().size());
        historyManager.add(taskValid);
        historyManager.add(new Task(1, "n", "d", Status.NEW, duration));
        historyManager.add(new Task(2, "n", "d", Status.NEW, duration));
        historyManager.remove(0);
        Assertions.assertEquals(2, historyManager.getHistory().size());
        historyManager.add(taskValid);
        historyManager.remove(1);
        Assertions.assertEquals(2, historyManager.getHistory().size());
        historyManager.add(new Task(1, "n", "d", Status.NEW, duration));
        historyManager.remove(2);
        Assertions.assertEquals(2, historyManager.getHistory().size());
    }
}




