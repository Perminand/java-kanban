package kanban.model;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String nameTask, String description, int epicId) {
        super(nameTask, description, TypeTask.SUBTASK);
        this.epicId = epicId;
    }

    public SubTask( int uin, String nameTask, String description, Status status, int epicId) {
        super(nameTask, description, TypeTask.SUBTASK);
        this.setUin(uin);
        this.setStatus(status);
        this.epicId=epicId;
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
