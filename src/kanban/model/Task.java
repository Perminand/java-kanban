package kanban.model;

import kanban.enumClass.Status;

public class Task {
    private final String nameTask;//обновление идет полным объектом Task
    private final String description;
    private Integer uin;
    private Status status;

    public Task(String nameTask, String description) {
        this.nameTask = nameTask;
        this.description = description;
        this.status = Status.NEW;
        uin = null;
    }

    public Task(String nameTask, String description, int uin) {
        this.nameTask = nameTask;
        this.description = description;
        this.uin = uin;
    }

    public Task(String nameTask, String description, int uin, Status status) {
        this.nameTask = nameTask;
        this.description = description;
        this.uin = uin;
        this.status = status;
    }

    public void setUin(int uin) {
        this.uin = uin;
    }

    public int getUin() {
        return uin;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNameTask() {
        return nameTask;
    }

    public Status getStatus() {
        return status;
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
