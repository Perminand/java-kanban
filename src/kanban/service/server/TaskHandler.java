package kanban.service.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kanban.exceptions.ManagerSaveException;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.service.FileBackedTasksManager;
import kanban.service.Managers;
import kanban.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class TaskHandler implements HttpHandler {
    Gson gson = new Gson();
    TaskManager manager = Managers.getDefault();

    @Override
    public void handle(HttpExchange exchange) throws IOException, NumberFormatException {
        System.out.println(manager.getPrioritizedTasks());
        System.out.println("Получен запрос " + exchange.getRequestMethod());
        System.out.println(exchange.getRequestURI().getPath().split("/").length);
        String path = exchange.getRequestURI().getPath();
        int count = path.split("/").length;
        String recurse = path.split("/")[2];
        String response = null;
        if (exchange.getRequestMethod().equals("GET") || exchange.getRequestMethod().equals("DELETE")) {
            if (count == 2 && path.split("/")[1].equals("tasks") && exchange.getRequestMethod().equals("GET")) {
                exchange.sendResponseHeaders(200, 0);
                response = gson.toJson(manager.getPrioritizedTasks());
            } else if (count == 3) {
                switch (recurse) {
                    case "task":
                        switch (exchange.getRequestMethod()) {
                            case "GET":
                                if (exchange.getRequestURI().getQuery() != null) {
                                    int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                                    response = gson.toJson(manager.getTask(id));
                                } else {
                                    exchange.sendResponseHeaders(200, 0);
                                    response = gson.toJson(manager.getTasks());
                                }
                                break;
                            case "DELETE":
                                if (exchange.getRequestURI().getQuery() != null) {
                                    int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                                    manager.deleteById(id);
                                    exchange.sendResponseHeaders(200, 0);
                                } else {
                                    exchange.sendResponseHeaders(200, 0);
                                    manager.removeAllTask();
                                }
                                break;

                        }
                        break;
                    case "subtask":
                        switch (exchange.getRequestMethod()) {
                            case "GET":
                                if (exchange.getRequestURI().getQuery() != null) {
                                    int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                                    response = gson.toJson(manager.getSubTask(id));
                                } else {
                                    exchange.sendResponseHeaders(200, 0);
                                    response = gson.toJson(manager.getSubTasks());
                                }
                                break;
                            case "DELETE":
                                if (exchange.getRequestURI().getQuery() != null) {
                                    int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                                    manager.deleteById(id);
                                    exchange.sendResponseHeaders(200, 0);
                                } else {
                                    exchange.sendResponseHeaders(200, 0);
                                    manager.removeAllSubTask();
                                }
                                break;
                        }
                        break;
                    case "epic":
                        switch (exchange.getRequestMethod()) {
                            case "GET":
                                if (exchange.getRequestURI().getQuery() != null) {
                                    int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                                    response = gson.toJson(manager.getEpic(id));
                                } else {
                                    exchange.sendResponseHeaders(200, 0);
                                    response = gson.toJson(manager.getEpics());
                                }
                                break;

                            case "DELETE":
                                if (exchange.getRequestURI().getQuery() != null) {
                                    int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
                                    manager.deleteById(id);
                                    exchange.sendResponseHeaders(200, 0);
                                } else {
                                    exchange.sendResponseHeaders(200, 0);
                                    manager.removeAllEpic();
                                }
                                break;
                        }
                        break;
                    case "history":
                        exchange.sendResponseHeaders(200, 0);
                        response = gson.toJson(manager.getHistory());
                        break;
                    default:
                        exchange.sendResponseHeaders(404, 0);
                        response = "Неожиданный запрос";
                }
            } else if (count == 4) {
                System.out.println(path.split("/")[3]);
                if (exchange.getRequestHeaders().containsKey("id")) {
                    if (exchange.getRequestHeaders().get("id").size() == 1) {
                        System.out.println("coin=4");
                        //                       System.out.println(manager.getTask(Integer.parseInt(exchange.getRequestHeaders().get("id").get(0))));
                        response = exchange.getRequestHeaders().get("id").get(0);
                    }
                    //                  exchange=gson.toJson(manager.getTask(Integer.parseInt(exchange.getRequestHeaders().get("id").get(0))));
                }
            }
        } else if (exchange.getRequestMethod().equals("POST")) {
            if (count == 3) {
                if (exchange.getRequestBody() == null) {
                    exchange.sendResponseHeaders(404, 0);
                    System.out.println("Пустой POST запрос");
                    return;
                }
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes());
                Task task = gson.fromJson(body, Task.class);
                switch (recurse) {
                    case "task":
                        manager.createTask(task);
                        exchange.sendResponseHeaders(201, 0);
                        response = "OK, Запись добавлена";

                        break;
                    case "subtask":
                        manager.createSubTask((SubTask) task);
                        exchange.sendResponseHeaders(201, 0);
                        response = "OK, Запись добавлена";
                        break;
                    case "epic":
                        manager.createEpic((Epic) task);
                        exchange.sendResponseHeaders(201, 0);
                        response = "OK, Запись добавлена";
                        break;
                    default:
                        exchange.sendResponseHeaders(404, 0);
                        response = "Неожиданный запрос";
                }
            } else if (count == 4) {
                if (exchange.getRequestHeaders().containsKey("id")) {
                    if (exchange.getRequestHeaders().get("id").size() == 1) {
                        if (exchange.getRequestBody() == null) {
                            exchange.sendResponseHeaders(404, 0);
                            System.out.println("Пустой POST запрос");
                            return;
                        }
                        InputStream inputStream = exchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes());
                        Task task = gson.fromJson(body, Task.class);
                        switch (recurse) {
                            case "task":
                                manager.updateTask(task);
                                exchange.sendResponseHeaders(201, 0);
                                response = "OK, Запись обновалена";

                                break;
                            case "subtask":
                                manager.updateSubTask((SubTask) task);
                                exchange.sendResponseHeaders(201, 0);
                                response = "OK, Запись обновлена";
                                break;
                            case "epic":
                                manager.updateEpic((Epic) task);
                                exchange.sendResponseHeaders(201, 0);
                                response = "OK, Запись обновлена";
                                break;
                            default:
                                exchange.sendResponseHeaders(404, 0);
                                response = "Неожиданный запрос";
                        }
                    }
                }
            }
        }
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }


    }
}
