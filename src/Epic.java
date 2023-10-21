import java.util.ArrayList;

public class Epic extends Task {
    private String status;

    private ArrayList<Integer> idSubTask;

    public Epic(String nameEpic, String discription) {
        super(nameEpic, discription);
        this.status = "NEW";
        this.idSubTask = new ArrayList<>();
    }

    public Epic(String nameEpic, String discription, int uin, String status) {
        super(nameEpic,discription,uin,status);
    }


    public ArrayList<Integer> getIdSubTask() {
        return idSubTask;
    }

    public void setIdSubTask(ArrayList<Integer> idSubTask) {
        this.idSubTask = idSubTask;
    }

    @Override
    public String toString() {
        return "Epic{" + "idSubTask=" + idSubTask +" {" + super.toString()+"}";
    }
}
