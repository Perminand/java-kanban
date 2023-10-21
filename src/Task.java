public class Task {
    private String nameTask;
    private String discription;
    private int uin;
    private String status;

    public Task(String nameTask, String discription) {
        this.uin++;
        this.nameTask = nameTask;
        this.discription = discription;
        this.status = "NEW";

    }
        public Task(String nameTask, String discription, int uin, String status) {
        this.nameTask = nameTask;
        this.discription = discription;
        this.uin = uin;
        this.status = status;

    }
    public Task(String nameTask, String discription,String status) {
        this.nameTask = nameTask;
        this.discription = discription;
//        this.uin = uin;
        this.status = status;

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

    public void setUin(int uin) {
        this.uin = uin;
    }

    public int getUin() {
        return uin;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
