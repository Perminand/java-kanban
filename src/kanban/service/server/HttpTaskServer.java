package kanban.service.server;

import com.sun.net.httpserver.HttpServer;
import kanban.service.Managers;
import kanban.service.TaskManager;
import kanban.service.server.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    HttpServer server;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        String uriPrefixV1 = "/api/v1/";
        server.createContext(uriPrefixV1 + "tasks", new TaskHandler(taskManager));
        server.createContext(uriPrefixV1 + "subtasks", new SubTaskHandler(taskManager));
        server.createContext(uriPrefixV1 + "epics", new EpicHandler(taskManager));
        server.createContext(uriPrefixV1 + "history", new HistoryHandler(taskManager));
        server.createContext(uriPrefixV1 + "prioritized", new PrioritizedHandler(taskManager));
        System.out.println("Сервер создан на порту " + PORT);
    }


    public static void main(String[] args) throws IOException {
        final HttpTaskServer httpServerClass = new HttpTaskServer(Managers.getDefault());
        httpServerClass.start();
    }

    public void start() {

        server.start();
        System.out.println("HTTP-сервер запущен и слушает " + PORT + " порт!");
    }

    public void stop() {
        server.stop(0);
    }
}