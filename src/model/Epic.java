package model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> idSubTask;

    public Epic(String nameEpic, String discription) {
        super(nameEpic, discription);
        this.idSubTask = new ArrayList<>();
    }

    public Epic(String nameEpic, String description, int uin) {//При создании обновления
        super(nameEpic, description, uin);
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
