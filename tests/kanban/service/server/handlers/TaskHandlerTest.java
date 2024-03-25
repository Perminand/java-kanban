package kanban.service.server.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kanban.model.Task;
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
import java.time.Duration;
import java.util.List;

class TaskHandlerTest {
    private static final int PORT = 8080;
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer server = new HttpTaskServer(manager);
    Gson gson = Managers.getGson();

    TaskHandlerTest() throws IOException {
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
        Task task = new Task("Тест", "Тестовое описание",Duration.ofMinutes(1));
        int id = manager.createTask(task);
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/tasks/"+id);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.GET().uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        Assertions.assertEquals(200,response.statusCode());
        final Task task1= gson.fromJson(response.body(),new TypeToken<Task>() {}.getType());
        Assertions.assertEquals(task.getNameTask(),task1.getNameTask(),"Некорректное имя задачи");
        Assertions.assertEquals(task.getUin(),task1.getUin(),"Некорректный id задачи");


    }

    @Test
    void handleEmptyAddTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/tasks");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        String nameTask = "name";
        Task task = new Task(nameTask,"description1", Duration.ofMinutes(1));
        HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = httpClient.send(request, handler);
        List<Task> list = manager.getTasks();
        Assertions.assertEquals(201,response.statusCode());
        Assertions.assertNotNull(list,"Задачи не возвращаются");
        Assertions.assertEquals(1,list.size(),"Некорректное количество задач");
        Assertions.assertEquals(nameTask,list.get(0).getNameTask(),"Имя не корректно");
    }


    @Test
    void nandleDeleteTask() throws IOException, InterruptedException {
        manager.createTask(new Task("name","description",Duration.ofMinutes(1)));
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/tasks/0");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.DELETE()
                .uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode(),"Неверный код ответа");
        Assertions.assertEquals(0,manager.getTasks().size(),"Задача не удалена");
    }
    @Test
    void nandleDeleteAllTask() throws IOException, InterruptedException {
        manager.createTask(new Task("name","description"));
        manager.createTask(new Task("name","description"));
        manager.createTask(new Task("name","description"));
        URI uri = URI.create("http://localhost:" + PORT + "/api/v1/tasks/");
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        HttpRequest request = requestBuilder.DELETE()
                .uri(uri).version(HttpClient.Version.HTTP_1_1).build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200,response.statusCode(),"Неверный код ответа");
        Assertions.assertEquals(0,manager.getTasks().size(),"Задача не удалена");
    }
}