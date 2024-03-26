package kanban.service.server.handlers;

import com.google.gson.Gson;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.service.InMemoryTaskManager;
import kanban.service.Managers;
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
import java.util.List;

class SubTaskHandlerTest {
    private static final int PORT = 8080;
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = Managers.getGson();

    SubTaskHandlerTest() throws IOException {
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
    void handleEmptyListTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/subtasks");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.GET().uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        Assertions.assertEquals(200, response.statusCode());
        List<SubTask> list = manager.getSubTasks();
        Assertions.assertEquals(0, list.size(), "Список задач не пуст");

    }

    @Test
    void handleAddSubTask() throws IOException, InterruptedException {
        int idEpic = manager.createEpic(new Epic("name", "description"));
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/subtasks");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String name = "nameS";
        SubTask task = new SubTask(name, "description",idEpic);
        String s = gson.toJson(task);
        HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(s))
                .uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        Assertions.assertEquals(201, response.statusCode());
        List<SubTask> list = manager.getSubTasks();
        Assertions.assertNotNull(list,"Задачи не возвращаются");
        Assertions.assertEquals(1,list.size(),"Некорректное количество задач");
        Assertions.assertEquals(name,list.get(0).getNameTask(),"Имя не корректно");

    }

    @Test
    void nandleDeleteSubTask() throws IOException, InterruptedException {
        int idEpic = manager.createEpic(new Epic("name", "description"));
        manager.createSubTask(new SubTask("name", "description", idEpic));
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/subtasks/0");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.DELETE()
                .uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Не верный код ответа");
        Assertions.assertEquals(0, manager.getSubTasks().size(), "Задача не удалена");
    }
    @Test
    void nandleDeleteAllSubTask() throws IOException, InterruptedException {
        int idEpic = manager.createEpic(new Epic("name", "description"));
        manager.createSubTask(new SubTask("name", "description", idEpic));
        manager.createSubTask(new SubTask("name", "description", idEpic));
        manager.createSubTask(new SubTask("name", "description", idEpic));
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/subtasks/");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.DELETE()
                .uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Не верный код ответа");
        Assertions.assertEquals(0, manager.getSubTasks().size(), "Задача не удалена");
    }
}