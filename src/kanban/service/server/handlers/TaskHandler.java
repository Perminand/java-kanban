package kanban.service.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kanban.exceptions.IntersectionOfTime;
import kanban.model.Task;
import kanban.service.Managers;
import kanban.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;

public class TaskHandler implements HttpHandler {

    TaskManager manager = Managers.getDefault();
    Gson gson = Managers.getGson();
    SendResponse sendResponse = new SendResponse();

    @Override
    public void handle(HttpExchange exchange) throws IOException, NumberFormatException {
        System.out.println("Пришел запрос на TASK");
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        path = path.split("/api/v1")[1];
        int count = path.split("/").length;
        switch (method) {
            case "GET":
                switch (count) {
                    case 2:
                        System.out.println(manager.getPrioritizedTasks());
                        String s = gson.toJson(manager.getPrioritizedTasks());
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
                        manager.deleteById(id);
                        sendResponse.send(exchange, 200, "Задача с id: " + id + " удалена");
                        break;
                    default:
                        break;
                }
            default:
                sendResponse.send(exchange, 400, "Метод не поддерживается");

        }
    }


//        System.out.println(manager.getPrioritizedTasks());
//        System.out.println("Получен запрос " + exchange.getRequestMethod());
//        String path = exchange.getRequestURI().getPath();
//        String method = exchange.getRequestMethod();
//        int count = path.split("/").length;
//
//        String response = null;
//        System.out.println(path);
//        if (Pattern.matches("^/tasks.*", path)) {
//            switch (method) {
//                case "GET":
//                    sendResponse(exchange, 200, gson.toJson(manager.getPrioritizedTasks()));
//                case "DELETE":
//                    sendResponse(exchange,201,"Все задачи удалены");
//                default:
//                    sendResponse(exchange,400,"Метод не поддерживается");
//            }
//        }
//        if (exchange.getRequestMethod().equals("GET") || exchange.getRequestMethod().equals("DELETE")) {
//            if (count == 2 && path.split("/")[1].equals("tasks") && exchange.getRequestMethod().equals("GET")) {
//
//            } else if (count == 3) {
//                switch (path.split("/")[2]) {
//                    case "task":
//                        switch (exchange.getRequestMethod()) {
//                            case "GET":
//                                if (exchange.getRequestURI().getQuery() != null) {
//                                    int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
//                                    response = gson.toJson(manager.getTask(id));
//                                } else {
//                                    exchange.sendResponseHeaders(200, 0);
//                                    response = gson.toJson(manager.getTasks());
//                                }
//                                break;
//                            case "DELETE":
//                                if (exchange.getRequestURI().getQuery() != null) {
//                                    int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
//                                    manager.deleteById(id);
//                                    exchange.sendResponseHeaders(200, 0);
//                                } else {
//                                    exchange.sendResponseHeaders(200, 0);
//                                    manager.removeAllTask();
//                                }
//                                break;
//
//                        }
//                        break;
//                    case "subtask":
//                        switch (exchange.getRequestMethod()) {
//                            case "GET":
//                                if (exchange.getRequestURI().getQuery() != null) {
//                                    int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
//                                    response = gson.toJson(manager.getSubTask(id));
//                                } else {
//                                    exchange.sendResponseHeaders(200, 0);
//                                    response = gson.toJson(manager.getSubTasks());
//                                }
//                                break;
//                            case "DELETE":
//                                if (exchange.getRequestURI().getQuery() != null) {
//                                    int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
//                                    manager.deleteById(id);
//                                    exchange.sendResponseHeaders(200, 0);
//                                } else {
//                                    exchange.sendResponseHeaders(200, 0);
//                                    manager.removeAllSubTask();
//                                }
//                                break;
//                        }
//                        break;
//                    case "epic":
//                        switch (exchange.getRequestMethod()) {
//                            case "GET":
//                                if (exchange.getRequestURI().getQuery() != null) {
//                                    int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
//                                    response = gson.toJson(manager.getEpic(id));
//                                } else {
//                                    exchange.sendResponseHeaders(200, 0);
//                                    response = gson.toJson(manager.getEpics());
//                                }
//                                break;
//
//                            case "DELETE":
//                                if (exchange.getRequestURI().getQuery() != null) {
//                                    int id = Integer.parseInt(exchange.getRequestURI().getQuery().split("=")[1]);
//                                    manager.deleteById(id);
//                                    exchange.sendResponseHeaders(200, 0);
//                                } else {
//                                    exchange.sendResponseHeaders(200, 0);
//                                    manager.removeAllEpic();
//                                }
//                                break;
//                        }
//                        break;
//                    case "history":
//                        exchange.sendResponseHeaders(200, 0);
//                        response = gson.toJson(manager.getHistory());
//                        break;
//                    default:
//                        exchange.sendResponseHeaders(404, 0);
//                        response = "Неожиданный запрос";
//                }
//            } else if (count == 4) {
//                System.out.println(path.split("/")[3]);
//                if (exchange.getRequestHeaders().containsKey("id")) {
//                    if (exchange.getRequestHeaders().get("id").size() == 1) {
//                        System.out.println("coin=4");
//                        response = exchange.getRequestHeaders().get("id").get(0);
//                    }
//                }
//            }
//        } else if (exchange.getRequestMethod().equals("POST")) {
//            if (count == 3) {
//                if (exchange.getRequestBody() == null) {
//                    exchange.sendResponseHeaders(404, 0);
//                    System.out.println("Пустой POST запрос");
//                    return;
//                }
//                InputStream inputStream = exchange.getRequestBody();
//                String body = new String(inputStream.readAllBytes());
//                Task task = gson.fromJson(body, Task.class);
//                switch (path.split("/")[2]) {
//                    case "task":
//                        manager.createTask(task);
//                        exchange.sendResponseHeaders(201, 0);
//                        response = "OK, Запись добавлена";
//
//                        break;
//                    case "subtask":
//                        manager.createSubTask((SubTask) task);
//                        exchange.sendResponseHeaders(201, 0);
//                        response = "OK, Запись добавлена";
//                        break;
//                    case "epic":
//                        manager.createEpic((Epic) task);
//                        exchange.sendResponseHeaders(201, 0);
//                        response = "OK, Запись добавлена";
//                        break;
//                    default:
//                        exchange.sendResponseHeaders(404, 0);
//                        response = "Неожиданный запрос";
//                }
//            } else if (count == 4) {
//                if (exchange.getRequestHeaders().containsKey("id")) {
//                    if (exchange.getRequestHeaders().get("id").size() == 1) {
//                        if (exchange.getRequestBody() == null) {
//                            exchange.sendResponseHeaders(404, 0);
//                            System.out.println("Пустой POST запрос");
//                            return;
//                        }
//                        InputStream inputStream = exchange.getRequestBody();
//                        String body = new String(inputStream.readAllBytes());
//                        Task task = gson.fromJson(body, Task.class);
//                        switch (path.split("/")[2]) {
//                            case "task":
//                                manager.updateTask(task);
//                                exchange.sendResponseHeaders(201, 0);
//                                response = "OK, Запись обновалена";
//
//                                break;
//                            case "subtask":
//                                manager.updateSubTask((SubTask) task);
//                                exchange.sendResponseHeaders(201, 0);
//                                response = "OK, Запись обновлена";
//                                break;
//                            case "epic":
//                                manager.updateEpic((Epic) task);
//                                exchange.sendResponseHeaders(201, 0);
//                                response = "OK, Запись обновлена";
//                                break;
//                            default:
//                                exchange.sendResponseHeaders(404, 0);
//                                response = "Неожиданный запрос";
//                        }
//                    }
//                }
//            }
//        }


}
