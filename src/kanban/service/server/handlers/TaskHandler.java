package kanban.service.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kanban.exceptions.IntersectionOfTime;
import kanban.exceptions.NoTaskException;
import kanban.model.Task;
import kanban.service.Managers;
import kanban.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;

public class TaskHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;
    private final SendResponse sendResponse = new SendResponse();
    private HttpExchange exchange;
    private int countRows;

    public TaskHandler(TaskManager taskManager) {
        this.manager = taskManager;
        this.gson = Managers.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws NumberFormatException, IOException {
        this.exchange = exchange;
        System.out.println("Пришел запрос " + exchange.getRequestMethod() + " на TASK");
        String path = exchange.getRequestURI().getPath().split("/api/v1")[1];
        countRows = path.split("/").length;
        switch (exchange.getRequestMethod()) {
            case "GET":
                acceptGet(path);
                break;
            case "POST":
                acceptPost();
                break;
            case "DELETE":
                acceptDelete(path);
                break;
            default:
                sendResponse.send(exchange, 405, "Метод не поддерживается");
        }
    }

    private void acceptDelete(String path) throws IOException {
        switch (countRows) {
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
    }

    private void acceptPost() throws IOException {
        if (exchange.getRequestBody() == null) {
            sendResponse.send(exchange, 400, "Пустой POST запрос");
            return;
        }

        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes());
        Task task = gson.fromJson(body, Task.class);
        switch (countRows) {
            case 2:
                try {
                    int id = manager.createTask(task);
                    sendResponse.send(exchange, 201, gson.toJson("id:" + id));
                } catch (IntersectionOfTime | NullPointerException e) {
                    sendResponse.send(exchange, 406, e.getMessage());
                }
                break;
            case 3:
                try {
                    manager.updateTask(task);
                    sendResponse.send(exchange, 201, "Задача обновлена");
                } catch (IntersectionOfTime e) {
                    sendResponse.send(exchange, 406, e.getMessage());
                } catch (NullPointerException e) {
                    sendResponse.send(exchange, 406, "На это время есть задача в системе");
                }

                break;
            default:
                break;
        }
    }

    private void acceptGet(String path) throws IOException {
        switch (countRows) {
            case 2:
                String s = gson.toJson(manager.getTasks());
                sendResponse.send(exchange, 200, s);
                break;
            case 3:
                int idTask = Integer.parseInt(path.split("/")[2]);
                Task task = manager.getTask(idTask);
                if (task == null) {
                    sendResponse.send(exchange, 404, "Not Found");
                } else {
                    sendResponse.send(exchange, 200, gson.toJson(
                            manager.getTask(idTask)));
                    break;

                }
            default:
                break;
        }
    }
}

