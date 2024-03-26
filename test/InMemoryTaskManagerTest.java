import kanban.enumClass.Status;
import kanban.model.Task;
import kanban.service.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.TreeSet;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    Duration duration = Duration.ofMinutes(1);

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
        initTasks();

    }


    @Test
    public void createInMemoryTaskManager() {
        Assertions.assertNotNull(manager.getTasks(), "Возвращает пустой список задач ");
    }

    @Test
    public void getPrioritizedTasksTest() {
        manager.removeAllTask();
        TreeSet<Task> treeSet = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        Task task1 = new Task(0, "Задача1", "Описание1", Status.NEW,
                LocalDateTime.of(2024, 1, 2, 0, 0, 0, 0),
                duration);
        Task task2 = new Task(1, "Задача1", "Описание1", Status.NEW,
                LocalDateTime.of(2024, 1, 1, 0, 0, 0, 0),
                duration);
        treeSet.add(task1);
        treeSet.add(task2);

        Assertions.assertEquals(task2, treeSet.first());

    }

}
