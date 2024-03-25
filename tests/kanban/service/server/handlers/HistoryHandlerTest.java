package kanban.service.server.handlers;

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

class HistoryHandlerTest {
    private static final int PORT = 8080;
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);

    HistoryHandlerTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
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
        manager.createTask(new Task("name1", "description"));
        manager.createTask(new Task("name2", "description"));
        manager.getTask(0);
        manager.getTask(1);
        manager.getTask(2);
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/history");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.GET().uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertEquals(3, response.body().split("},").length);


    }
}