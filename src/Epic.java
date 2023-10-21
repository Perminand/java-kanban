import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> idSubTask;

    public Epic(String nameEpic, String discription) {
        super(nameEpic, discription);
        this.idSubTask = new ArrayList<>();
    }

    public Epic(String nameEpic, String description, int uin, String status) {//При создании обновления
        super(nameEpic, description, uin, status);
    }

    public ArrayList<Integer> getIdSubTask() {
        return idSubTask;
    }


    @Override
    public String toString() {
        return "Epic{" + "idSubTask=" + idSubTask + " {" + super.toString() + "}";
    }
}
