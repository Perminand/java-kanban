package kanban.service.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import kanban.service.Managers;
import kanban.service.TaskManager;
import kanban.service.server.TaskHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.start(); // запускаем сервер
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }
}