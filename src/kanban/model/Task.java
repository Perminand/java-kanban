package kanban.model;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private final String nameTask;
    private final String description;
    private final TypeTask typeTask;
    protected LocalDateTime startTime = LocalDateTime.now();
    private Duration duration;
    private LocalDateTime endTime;
    private Integer uin;
    private Status status;

    public Task(String nameTask, String description, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.duration = duration;
        this.status = Status.NEW;
        uin = null;
        this.typeTask = TypeTask.TASK;
        this.endTime = initEndTime();
    }

    public Task(String nameTask, String description, TypeTask typeTask) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = Status.NEW;
        uin = null;
        startTime = null;
        this.typeTask = typeTask;
    }

    public Task(String nameTask, String description, Status status, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = status;
        uin = null;
        this.typeTask = TypeTask.TASK;
        this.duration = duration;
        this.endTime = initEndTime();

    }

    protected Task(String nameTask, String description, TypeTask typeTask, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = Status.NEW;
        uin = null;
        this.typeTask = typeTask;
        this.duration = duration;
        this.endTime = initEndTime();


    }

    protected Task(String nameTask, String description, TypeTask typeTask, LocalDateTime localDateTime,
                   Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = Status.NEW;
        uin = null;
        this.typeTask = typeTask;
        this.duration = duration;
        this.startTime = localDateTime;
        this.endTime = initEndTime();


    }

    protected Task(String nameTask, String description, int uin, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.uin = uin;
        this.typeTask = TypeTask.TASK;
        this.duration = duration;
        this.endTime = initEndTime();

    }

    public Task(int uin, String nameTask, String description, Status status, TypeTask typeTask,
                LocalDateTime startTime, Duration duration) {
        this.nameTask = nameTask;
        this.description = description;
        this.uin = uin;
        this.status = status;
        this.typeTask = typeTask;
        this.startTime = startTime;
        this.endTime = initEndTime();
        this.duration = duration;


    }

    public Task(int uin, String newName, String newDescription, Status status, LocalDateTime localDateTime,
                Duration duration) {
        this.uin = uin;
        this.nameTask = newName;
        this.description = newDescription;
        this.status = status;
        this.typeTask = TypeTask.TASK;
        this.duration = duration;
        this.startTime = localDateTime;
        this.endTime = initEndTime();


    }

    public Task(int uin, String newName, String newDescription, Status status, Duration duration) {
        this.uin = uin;
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
        return uin;
    }

    public void setUin(int uin) {
        this.uin = uin;
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
                ", uin=" + uin +
                ", status=" + status +
                '}';
    }
}
