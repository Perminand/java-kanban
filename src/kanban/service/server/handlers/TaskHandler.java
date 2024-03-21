package kanban.service.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kanban.enumClass.TypeTask;
import kanban.exceptions.IntersectionOfTime;
import kanban.exceptions.NoTaskException;
import kanban.model.Task;
import kanban.service.Managers;
import kanban.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

public class TaskHandler implements HttpHandler {

    TaskManager manager = Managers.getDefault();
    Gson gson = Managers.getGson();
    SendResponse sendResponse = new SendResponse();

    @Override
    public void handle(HttpExchange exchange) throws IOException, NumberFormatException {
        System.out.println("Пришел запрос "+exchange.getRequestMethod()+" на TASK");
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        path = path.split("/api/v1")[1];
        int count = path.split("/").length;
        switch (method) {
            case "GET":
                switch (count) {
                    case 2:
                        String s = gson.toJson(manager.getPrioritizedTasks()
                                .stream()
                                .filter(task -> task.getTypeTask()== TypeTask.TASK)
                                .collect(Collectors.toList()));
                        sendResponse.send(exchange, 200, s);
                        break;
                    case 3:
                        Task task = manager.getTask(Integer.parseInt(path.split("/")[2]));
                        System.out.println(gson.toJson(task));
                        if (task == null) {
                            sendResponse.send(exchange, 404, "Not Found");
                        } else {
                            sendResponse.send(exchange, 200, gson.toJson(
                                    manager.getTask(Integer.parseInt(path.split("/")[2]))));
                            break;

                        }
                    default:
                        break;
                }
            case "POST":
                if (exchange.getRequestBody() == null) {
                    sendResponse.send(exchange, 400, "Пустой POST запрос");
                    return;
                }

                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes());
                Task task = gson.fromJson(body, Task.class);
                switch (count) {
                    case 2:
                        try {
                            manager.createTask(task);
                        } catch (IntersectionOfTime | NullPointerException e) {
                            sendResponse.send(exchange, 406, e.getMessage());
                        }
                        sendResponse.send(exchange, 200, "Задача добавлена");
                        break;
                    case 3:
                        try {
                            manager.updateTask(task);
                        } catch (IntersectionOfTime e) {
                            sendResponse.send(exchange, 406, e.getMessage());
                        } catch (NullPointerException e) {
                            sendResponse.send(exchange, 406, "На это время есть задача в системе");
                        }
                        sendResponse.send(exchange, 201, "Задача обновлена");
                        break;
                    default:
                        break;
                }
                break;
            case "DELETE":
                switch (count) {
                    case 2:
                        manager.removeAllTask();
                        sendResponse.send(exchange, 200, "Все задачи удалены");
                        break;
                    case 3:
                        int id = Integer.parseInt(path.split("/")[2]);
                        try {
                            manager.deleteById(id);
                            sendResponse.send(exchange, 200, "Задача с id: " + id + " удалена");
                        } catch (NoTaskException e) {
                            sendResponse.send(exchange, 404, e.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            default:
                sendResponse.send(exchange, 400, "Метод не поддерживается");

        }
    }
}
