package kanban.service.server;

import com.sun.net.httpserver.HttpServer;
import kanban.service.Managers;
import kanban.service.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class TaskHandlerTest {
    private static final int PORT = 8080;
    TaskManager manager = Managers.getDefault();

    @BeforeEach
    void testInit() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.start();
    }

    @Test
    void handleEmptyListTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/tasks");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.GET().uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        System.out.println(response.body());
        System.out.println(manager.getPrioritizedTasks().toString());
        Assertions.assertEquals(manager.getPrioritizedTasks().toString(), response.body(),
                "Список задач не совпадает");
    }

    @Test
    void handleEmptyAddTask(){


    }
    private void requestBuild(URI uri)
}