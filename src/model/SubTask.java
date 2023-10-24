package model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String nameTask, String description, int epicId) {
        super(nameTask, description);
        this.epicId = epicId;
    }

    public SubTask(String nameTask, String description, int uin, String status) {
        super(nameTask, description);
        this.setUin(uin);
        this.setStatus(status);
    }

    public SubTask(String nameTask, String description) {
        super(nameTask, description);
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
}
