package kanban.model;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    protected int epicId;

    public SubTask(String nameTask, String description, int epicId, Duration duration) {
        super(nameTask, description, TypeTask.SUBTASK, duration);
        this.epicId = epicId;
    }
    public SubTask(String nameTask, String description, int epicId) {
        super(nameTask, description, TypeTask.SUBTASK);
        this.epicId = epicId;
    }

    public SubTask(String nameTask, String description, int epicId, LocalDateTime localDateTime, Duration duration) {
        super(nameTask, description, TypeTask.SUBTASK, duration, localDateTime);
        this.epicId = epicId;
    }

    public SubTask(int uin, String nameTask, String description, Status status, int epicId, LocalDateTime localDateTime,
                   Duration duration) {
        super(nameTask, description, TypeTask.SUBTASK, duration, localDateTime);
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
                ", id=" + id +
                ", nameTask='" + nameTask + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", typeTask=" + typeTask +
                ", startTime=" + startTime +
                ", duration=" + duration +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SubTask subTask)) return false;
        return getEpicId() == subTask.getEpicId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEpicId())*31;
    }
}
