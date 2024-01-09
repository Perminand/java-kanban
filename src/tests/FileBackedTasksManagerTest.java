package tests;

import kanban.model.Task;
import kanban.service.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private File file;

    @BeforeEach
    void beforeEach() {
        file = new File("./resources/test" + System.nanoTime() + ".csv");
        manager = new FileBackedTasksManager(file);
        initTasks();

    }

    @AfterEach
    void afterEach() {
        Assertions.assertTrue(file.delete());
    }

    @Test
    public void loadFromFile() {
        FileBackedTasksManager fileManager2 = FileBackedTasksManager.loadFromFile(file);
        final List<Task> tasks = fileManager2.getTasks();
        Assertions.assertNotNull(tasks, "Список не пуст");
        Assertions.assertEquals(1, tasks.size(), "Список не пуст");
    }

    @Test
    public void emptyMapTaskListTest() {
        Assertions.assertNotNull(manager.getTasks(), "Список не пуст");
    }

    @Test
    public void EpicNoTask() {
        manager.removeAllSubTask();
        FileBackedTasksManager fileManager2 = FileBackedTasksManager.loadFromFile(file);
        Assertions.assertNull(fileManager2.getEpic(0).getIdSubTask(), "idSubTask не Null");
        Assertions.assertEquals(0, fileManager2.getSubTasks().size());

    }

    @Test
    public void HistoryIsEmpty() {
        manager.removeAllTask();
        manager.removeAllEpic();
        FileBackedTasksManager fileManager2 = FileBackedTasksManager.loadFromFile(file);
        Assertions.assertEquals(0, fileManager2.getHistory().size());
    }
}

