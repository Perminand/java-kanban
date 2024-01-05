package kanban.model;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> idSubTask;
    private Duration duration;

    public Epic(String nameEpic, String description) {
        super(nameEpic, description, TypeTask.EPIC);
        this.idSubTask = new ArrayList<>();
        startTime = null;
    }

    public Epic(int uin, String nameEpic, String description, Status status) {
        super(uin, nameEpic, description, status, TypeTask.EPIC, LocalDateTime.now(), Duration.ZERO);

    }

    public Epic(int uin, String nameEpic, String description, Status status, LocalDateTime localDateTime,
                Duration duration) {
        super(uin, nameEpic, description, status, TypeTask.EPIC, localDateTime, duration);

    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public ArrayList<Integer> getIdSubTask() {
        return idSubTask;
    }

    public void setIdSubTask(ArrayList<Integer> idSubTask) {
        this.idSubTask = idSubTask;
    }


    @Override
    public String toString() {
        return "Epic{" + "idSubTask=" + idSubTask + " {" + super.toString() + "}";
    }
}
