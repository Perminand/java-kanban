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


    // IOException могут сгенерировать методы create() и bind(...)
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.start(); // запускаем сервер

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

//    static class HelloHandler implements HttpHandler {
//        @Override
//        public void handle(HttpExchange httpExchange) throws IOException {
//            System.out.println("Началась обработка /tasks запроса от клиента.");
//
//            String response = "Hey! Glad to see you on our server.";
//            httpExchange.sendResponseHeaders(200, 0);
//
//            try (OutputStream os = httpExchange.getResponseBody()) {
//                os.write(response.getBytes());
//            }
//        }
//    }
}