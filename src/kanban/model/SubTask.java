package kanban.model;

import kanban.enumClass.Status;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String nameTask, String description) {
     super(nameTask, description);
    }

    public SubTask(String nameTask, String description, int epicId) {
        super(nameTask, description);
        this.epicId = epicId;
    }

    public SubTask(String nameTask, String description, int uin, Status status) {
        super(nameTask, description);
        this.setUin(uin);
        this.setStatus(status);
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
