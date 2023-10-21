import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Task> listTask;
    private String status;

    public Epic(String nameTask, String discription, int uin, ArrayList<Task> listTask) {
        super(nameTask, discription, uin);
        this.listTask=listTask;
        this.status="NEW";
    }
    public Epic(String nameTask, String discription, ArrayList<Task> listTask) {
        super(nameTask, discription);
        this.listTask=listTask;
        this.status="NEW";
    }
    public void statusCalculation(String[] listStatus){
        int[] coinsStatus = {0,0,0};
        for (Task task :listTask){
            if(task.getStatus().equals(listStatus[0])) coinsStatus[0]++;
            if(task.getStatus().equals(listStatus[1])) coinsStatus[1]++;
            if(task.getStatus().equals(listStatus[2])) coinsStatus[2]++;
        }
        if (coinsStatus[0]<listTask.size()){
            if(coinsStatus[2]==listTask.size()){
                status=listStatus[2];
            }else{
                status=listStatus[1];
            }
        }

    }

    public ArrayList<Task> getListTask() {
        return this.listTask;
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
