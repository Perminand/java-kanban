public class Task {
    String nameTask;
    String discription;
    private int uin=0;
    String status ="NEW";

    public Task(String nameTask, String discription, int uin) {
        this.uin=uin;
        this.nameTask = nameTask;
        this.discription = discription;

    }

    public String getNameTask() {
        return nameTask;
    }

    public String getDiscription() {
        return discription;
    }

     public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "nameTask='" + nameTask + '\'' +
                ", discription='" + discription + '\'' +
                ", uin=" + uin +
                ", status='" + status + '\'' +
                '}';
    }

    public int getUin() {
        return uin;
    }
}
