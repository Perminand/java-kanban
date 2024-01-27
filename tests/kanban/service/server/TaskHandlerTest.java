package kanban.service.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import kanban.model.Task;
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
import java.time.Duration;

class TaskHandlerTest {
    private static final int PORT = 8080;

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

    }

    @Test
    void handleEmptyAddTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/tasks");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        Task task = new Task("name1","description1", Duration.ofMinutes(1));
        Gson gson = new Gson();
        HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);


    }
    @Test
    void test() throws IOException, InterruptedException {
        TaskManager manager = Managers.getDefault();
        manager.createTask(new Task("","",Duration.ofMinutes(1)));
    }
}