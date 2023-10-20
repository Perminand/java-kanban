import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<Task> listTask;
    String status ="NEW";
    public Epic(String nameTask, String discription, int uin, ArrayList<Task> listTask) {
        super(nameTask, discription, uin);
        this.listTask=listTask;
    }

    public ArrayList<Task> getListTask() {
        return listTask;
    }

    public void setListTask(ArrayList<Task> listTask) {
        this.listTask = listTask;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "listTask=" + listTask +
                ", status='" + status + '\'' +
                '}';
    }
}
