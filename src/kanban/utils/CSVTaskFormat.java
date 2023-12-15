package kanban.utils;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.service.HistoryManager;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


public class CSVTaskFormat {
    public static String toString(Task task) {
        return String.join(",", String.valueOf(task.getUin()), String.valueOf(task.getTypeTask()),
                String.valueOf(task.getNameTask()), String.valueOf(task.getStatus()),
                String.valueOf(task.getDescription()));
    }

    public static String toString(SubTask task) {
        return String.join(",", String.valueOf(task.getUin()), String.valueOf(task.getTypeTask()),
                String.valueOf(task.getNameTask()), String.valueOf(task.getStatus()),
                String.valueOf(task.getDescription()), String.valueOf(task.getEpicId()));
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> listTask = manager.getHistory();
        StringJoiner sj = new StringJoiner(",");
        if (!listTask.isEmpty()) {
            for (Task task : listTask) {
                sj.add(String.valueOf(task.getUin()));
            }
        }
        return sj.toString();
    }

    public static List<Integer> historyFromString(String value) {
        ArrayList<Integer> list = new ArrayList<>();
        for (String s :  value.split(",")) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    public static Task stringFromTask(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TypeTask type = TypeTask.valueOf(values[1]);
        final String name = values[2];
        final Status status = Status.valueOf(values[3]);
        final String descriptions = values[4];
        if (type == TypeTask.TASK)
            return new Task(id, type, name, descriptions, status);
        else if (type == TypeTask.SUBTASK) {
            final int epicId = Integer.parseInt(values[5]);
            return new SubTask(id, name, descriptions, status, epicId);
        } else if (type == TypeTask.EPIC)
            return new Epic(id, name, descriptions, status);
        else return null;
    }

}
