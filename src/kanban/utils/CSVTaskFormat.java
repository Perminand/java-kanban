package kanban.utils;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.service.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


public class CSVTaskFormat {
    public static String toString(Task task) {
        return String.join(",", String.valueOf(task.getUin()), String.valueOf(task.getTypeTask()),
                String.valueOf(task.getNameTask()), String.valueOf(task.getDescription()),
                String.valueOf(task.getStatus()), String.valueOf(task.getStartTime()),
                String.valueOf(task.getDuration()));
    }

    public static String toString(SubTask task) {
        return String.join(",", String.valueOf(task.getUin()), String.valueOf(task.getTypeTask()),
                String.valueOf(task.getNameTask()), String.valueOf(task.getDescription()),
                String.valueOf(task.getStatus()), String.valueOf(task.getEpicId()), String.valueOf(task.getStartTime()),
                String.valueOf(task.getDuration()));
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> listTask = manager.getHistory();
        StringJoiner sj = new StringJoiner(",");
        for (Task task : listTask) {
            sj.add(String.valueOf(task.getUin()));
        }
        return sj.toString();
    }

    public static List<Integer> historyFromString(String value) {
        ArrayList<Integer> list = new ArrayList<>();
        for (String s : value.split(",")) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    public static Task stringFromTask(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TypeTask type = TypeTask.valueOf(values[1]);
        final String name = values[2];
        final String descriptions = values[3];
        final Status status = Status.valueOf(values[4]);
        LocalDateTime localDate;

        switch (type) {
            case TASK: {
                localDate = LocalDateTime.parse(values[5]);
                final Duration duration = Duration.parse(values[6]);
                return new Task(id, name, descriptions, status, type, localDate, duration);
            }
            case SUBTASK: {
                final int epicId = Integer.parseInt(values[5]);
                localDate = LocalDateTime.parse(values[6]);
                Duration duration = Duration.parse(values[7]);
                return new SubTask(id, name, descriptions, status, epicId, localDate, duration);
            }
            case EPIC: {
                if (values[5].equals("null")) localDate = null;
                else localDate = LocalDateTime.parse(values[5]);

                final Duration duration;
                if (values[6].equals("null")) duration = null;
                else duration = Duration.parse(values[6]);
                return new Epic(id, name, descriptions, status, localDate, duration);
            }
            default:
                return null;
        }
    }

}
