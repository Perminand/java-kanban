package kanban.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kanban.adapter.DurationAdapter;
import kanban.adapter.LocalDateAdapter;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTasksManager(new File("./resources/Tasks.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter());
        return gson.create();
    }

}
