package kanban.model;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected final String nameTask;
    protected final String description;
    protected Integer id;
    protected Status status;
    protected TypeTask typeTask;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String nameTask, String description) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = Status.NEW;
        id = null;
        this.typeTask = TypeTask.TASK;
    }
    public Task(String nameTask, String description, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.duration = duration;
        this.status = Status.NEW;
        id = null;
        this.typeTask = TypeTask.TASK;
    }

    public Task(String nameTask, String description, TypeTask typeTask) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = Status.NEW;
        id = null;
        startTime = null;
        this.typeTask = typeTask;
    }
    public Task(String nameTask, String description, Status status, LocalDateTime localDateTime, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = status;
        id = null;
        this.typeTask = TypeTask.TASK;
        this.startTime = localDateTime;
        this.duration = duration;
    }

    protected Task(String nameTask, String description, TypeTask typeTask, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = Status.NEW;
        id = null;
        this.typeTask = typeTask;
        this.duration = duration;
    }

    protected Task(String nameTask, String description, TypeTask typeTask, Duration duration,
                   LocalDateTime localDateTime) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = Status.NEW;
        id = null;
        this.typeTask = typeTask;
        this.duration = duration;
        this.startTime = localDateTime;
    }

    protected Task(String nameTask, String description, int id, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.id = id;
        this.typeTask = TypeTask.TASK;
        this.duration = duration;
    }

    public Task(int id, String nameTask, String description, Status status, TypeTask typeTask,
                LocalDateTime startTime, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.id = id;
        this.status = status;
        this.typeTask = typeTask;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String newName, String newDescription, Status status, LocalDateTime localDateTime,
                Duration duration) {
        this.id = id;
        this.nameTask = newName;
        this.description = newDescription;
        this.status = status;
        this.typeTask = TypeTask.TASK;
        this.duration = duration;
        this.startTime = localDateTime;
    }

    public Task(int id, String newName, String newDescription, Status status, Duration duration) {
        this.id = id;
        this.nameTask = newName;
        this.description = newDescription;
        this.status = status;
        this.typeTask = TypeTask.TASK;
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public Integer getUin() {
        return id;
    }

    public void setUin(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TypeTask getTypeTask() {
        return typeTask;
    }

    public void setTypeTask(TypeTask typeTask) {
        this.typeTask = typeTask;
    }

    public String getNameTask() {
        return nameTask;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) return null;
        return startTime.plus(duration);
    }


    @Override
    public String toString() {
        return "Task{" +
                "nameTask='" + nameTask + '\'' +
                ", description='" + description + '\'' +
                ", typeTask=" + typeTask +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
