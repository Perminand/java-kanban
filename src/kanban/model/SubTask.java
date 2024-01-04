package kanban.model;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private int epicId;
    public SubTask(String nameTask, String description, int epicId, Duration duration) {
        super(nameTask, description, TypeTask.SUBTASK, duration);
        this.epicId = epicId;
    }
    public SubTask(String nameTask, String description, int epicId, LocalDateTime localDateTime, Duration duration) {
        super(nameTask, description, TypeTask.SUBTASK, localDateTime, duration);
        this.epicId = epicId;
    }

    public SubTask(int uin, String nameTask, String description, Status status, int epicId, LocalDateTime localDateTime,
                   Duration duration) {
        super(nameTask, description, TypeTask.SUBTASK, localDateTime, duration);
        this.setUin(uin);
        this.setStatus(status);
        this.epicId = epicId;
    }
    public SubTask(int uin, String nameTask, String description, Status status, int epicId,
                   Duration duration) {
        super(nameTask, description, TypeTask.SUBTASK, duration);
        this.setUin(uin);
        this.setStatus(status);
        this.epicId = epicId;
    }



    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SubTask)) return false;
        SubTask subTask = (SubTask) object;
        return getEpicId() == subTask.getEpicId();
    }


    @Override
    public int hashCode() {
        return Objects.hash(getEpicId()) * 31;
    }
}
