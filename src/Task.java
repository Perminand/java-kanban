public class Task {
    private String nameTask;
    private String description;
    private int uin;
    private String status;

    public Task(String nameTask, String description) {
        this.uin++;
        this.nameTask = nameTask;
        this.description = description;
        this.status = "NEW";
    }

    public Task(String nameTask, String description, int uin) {
        this.nameTask = nameTask;
        this.description = description;
        this.uin = uin;
    }

    public Task(String nameTask, String description, int uin, String status) {
        this.nameTask = nameTask;
        this.description = description;
        this.uin = uin;
        this.status = status;
    }

    public String getNameTask() {
        return nameTask;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
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

    public void setUin(int uin) {
        this.uin = uin;
    }

    public int getUin() {
        return uin;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
