package kanban.service.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Получен запрос " + exchange.getRequestMethod());
        System.out.println(exchange.getRequestURI().getPath().split("/").length);
        String path = exchange.getRequestURI().getPath();
        int count = path.split("/").length;


        String response = null;
        if (exchange.getRequestMethod().equals("GET") || exchange.getRequestMethod().equals("DELETE")) {
            if (count == 2 && path.split("/")[1].equals("tasks") && exchange.getRequestMethod().equals("GET")) {
                exchange.sendResponseHeaders(200, 0);
                response = gson.toJson(manager.getPrioritizedTasks());
            } else if (count == 3) {
                switch (path.split("/")[2]) {
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
                                case "POST":
                                    InputStream inputStream = exchange.getRequestBody();
                                    String body = new String(inputStream.readAllBytes());
                                    System.out.println(body);
//                                    if (exchange.getRequestURI().getQuery() != null){
//
//                                    } else {
//                                        manager.createTask(gson.fromJson(куы))
//                                    }
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
//                        } catch (NumberFormatException e) {
//                            exchange.sendResponseHeaders(405, 0);
//                            break;
//                        }
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
        }
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }


    }
}
