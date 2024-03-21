package kanban.model;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected Integer id;
    protected final String nameTask;
    protected Status status;
    protected final String description;
    protected final TypeTask typeTask;
    protected LocalDateTime startTime = LocalDateTime.now();
    protected Duration duration;
    protected LocalDateTime endTime;



    public Task(String nameTask, String description, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.duration = duration;
        this.status = Status.NEW;
        id = null;
        this.typeTask = TypeTask.TASK;
        this.endTime = initEndTime();
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
        this.endTime = initEndTime();

    }

    protected Task(String nameTask, String description, TypeTask typeTask, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = Status.NEW;
        id = null;
        this.typeTask = typeTask;
        this.duration = duration;
        this.endTime = initEndTime();


    }

    protected Task(String nameTask, String description, TypeTask typeTask, LocalDateTime localDateTime,
                   Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = Status.NEW;
        id = null;
        this.typeTask = typeTask;
        this.duration = duration;
        this.startTime = localDateTime;
        this.endTime = initEndTime();


    }

    protected Task(String nameTask, String description, int id, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.id = id;
        this.typeTask = TypeTask.TASK;
        this.duration = duration;
        this.endTime = initEndTime();

    }

    public Task(int id, String nameTask, String description, Status status, TypeTask typeTask,
                LocalDateTime startTime, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.id = id;
        this.status = status;
        this.typeTask = typeTask;
        this.startTime = startTime;
        this.endTime = initEndTime();
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
        this.endTime = initEndTime();


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

    public String getNameTask() {
        return nameTask;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime initEndTime() {
        if (startTime == null || duration == null) return null;
        return startTime.plus(duration);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "nameTask='" + nameTask + '\'' +
                ", description='" + description + '\'' +
                ", typeTask=" + typeTask +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", endTime=" + endTime +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
