package kanban.service.server.handlers;

import kanban.enumClass.Status;
import kanban.model.Task;
import kanban.service.InMemoryTaskManager;
import kanban.service.TaskManager;
import kanban.service.server.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

class PrioritizedHandlerTest {
    private static final int PORT = 8080;
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);

    PrioritizedHandlerTest() throws IOException {
    }

    @BeforeEach
    void testInit() {
        manager.removeAllTask();
        manager.removeAllEpic(); // удаляет все SubTask
        server.start();
    }
    @AfterEach
    void testEnd() {
        server.stop();
    }

    @Test
    void handle() throws IOException, InterruptedException {
        manager.createTask(new Task("name", "description"));
        manager.createTask(new Task("name", "description"));
        manager.createTask(new Task("name", "description", Status.NEW,
                LocalDateTime.of(2024, 1, 1, 0, 0), Duration.ofMinutes(1)));
        manager.createTask(new Task("name", "description", Status.NEW,
                LocalDateTime.of(2024, 1, 1, 0, 2), Duration.ofMinutes(1)));
        manager.createTask(new Task("name", "description", Status.NEW,
                LocalDateTime.of(2024, 1, 1, 0, 4), Duration.ofMinutes(1)));
        manager.createTask(new Task("name", "description"));
        manager.createTask(new Task("name", "description"));
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/prioritized");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.GET().uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(3, response.body().split("},").length);
    }
}