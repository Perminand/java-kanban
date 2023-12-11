package kanban.model;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> idSubTask;

    public Epic(String nameEpic, String description) {
        super(nameEpic, description, TypeTask.EPIC);
        this.idSubTask = new ArrayList<>();
    }

    public Epic(String nameEpic, String description, int uin) {
        super(nameEpic, description, uin);
    }
    public Epic(int id, String nameEpic, String description, Status status) {
        super(id,nameEpic, description, status);

    }

    public ArrayList<Integer> getIdSubTask() {
        return idSubTask;
    }

    public void setIdSubTask(ArrayList<Integer> idSubTask) {
        this.idSubTask = idSubTask;
    }

    @Override
    public String toString() {
        return "Epic{" + "idSubTask=" + idSubTask + " {" + super.toString() + "}";
    }
}
