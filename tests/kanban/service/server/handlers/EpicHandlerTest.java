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

class EpicHandlerTest {
    private static final int PORT = 8080;
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);
    private final Gson gson = Managers.getGson();

    EpicHandlerTest() throws IOException {
    }

    @BeforeEach
    void testInit()  {
        manager.removeAllTask();
        manager.removeAllEpic(); // удаляет все SubTask
        server.start();
    }
    @AfterEach
    void testEnd(){
        server.stop();
    }

    @Test
    void handleEmptyListTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/epics");
        String nameTask = "name";
        manager.createEpic(new Epic(nameTask,"description1"));
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.GET().uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        List<Epic> list = manager.getEpics();
        Assertions.assertEquals(200,response.statusCode());
        Assertions.assertNotNull(list,"Задачи не возвращаются");
        Assertions.assertEquals(1,list.size(),"Некорректное количество задач");
        Assertions.assertEquals(nameTask,list.get(0).getNameTask(),"Имя не корректно");

    }
    @Test
    void handleEmptyAddEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/epics");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String nameTask = "name";
        Epic task = new Epic(nameTask,"description1");
        String s = gson.toJson(task);
        HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(s))
                .uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        List<Epic> list = manager.getEpics();
        Assertions.assertEquals(201,response.statusCode());
        Assertions.assertNotNull(list,"Задачи не возвращаются");
        Assertions.assertEquals(1,list.size(),"Некорректное количество задач");
        Assertions.assertEquals(nameTask,list.get(0).getNameTask(),"Имя не корректно");
    }
    @Test
    void nandleDeleteEpic() throws IOException, InterruptedException {
        manager.createTask(new Epic("name","description"));
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/epics/0");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.DELETE()
                .uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode(),"Неверный код ответа");
        Assertions.assertEquals(0,manager.getEpics().size(),"Задача не удалена");
    }
    @Test
    void nandleDeleteAllEpic() throws IOException, InterruptedException {
        manager.createTask(new Epic("name1","description"));
        manager.createTask(new Epic("name2","description"));
        manager.createTask(new Epic("name3","description"));
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/epics/");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.DELETE()
                .uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode(),"Неверный код ответа");
        Assertions.assertEquals(0,manager.getEpics().size(),"Задача не удалена");
    }
    @Test
    void nandleGetEpicSubTask() throws IOException, InterruptedException {
        int idEpic = manager.createEpic(new Epic("name1","description"));
        int idEpic2 = manager.createEpic(new Epic("name1","description"));
        manager.createSubTask(new SubTask("name", "description", idEpic));
        manager.createSubTask(new SubTask("name", "description", idEpic2));
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/epics/0/subtask");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.GET()
                .uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode(),"Неверный код ответа");
        Assertions.assertEquals(1,manager.getSubTaskByIdEpic(manager.getEpic(idEpic)).size(),
                "Не верное количество задач");
    }
}