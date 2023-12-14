package kanban.model;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;

public class Task {
    private final String nameTask;
    private final String description;
    private final TypeTask typeTask;
    private Integer uin;
    private Status status;

    public Task(String nameTask, String description) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = Status.NEW;
        uin = null;
        this.typeTask = TypeTask.TASK;
    }

    protected Task(String nameTask, String description, TypeTask typeTask) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = Status.NEW;
        uin = null;
        this.typeTask = typeTask;
    }

    protected Task(String nameTask, String description, int uin) {
        this.nameTask = nameTask;
        this.description = description;
        this.uin = uin;
        this.typeTask = TypeTask.TASK;
    }

    public Task(int uin, TypeTask typeTask, String nameTask, String description,  Status status) {
        this.nameTask = nameTask;
        this.description = description;
        this.uin = uin;
        this.status = status;
        this.typeTask = typeTask;
    }

    public int getUin() {
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

    @Override
    public String toString() {
        return "Task{" +
                "nameTask='" + nameTask + '\'' +
                ", description='" + description + '\'' +
                ", uin=" + uin +
                ", status='" + status + '\'' +
                '}';
    }
}
