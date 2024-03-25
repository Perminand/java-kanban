package kanban.service.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kanban.service.Managers;
import kanban.service.TaskManager;

import java.io.IOException;

public class PrioritizedHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;
    public PrioritizedHandler(TaskManager taskManager) {
        this.manager = taskManager;
        this.gson = Managers.getGson();
    }
    SendResponse sendResponse = new SendResponse();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Пришел запрос "+exchange.getRequestMethod()+" на prioritized");
        sendResponse.send(exchange,200,gson.toJson(manager.getPrioritizedTasks()));

    }
}