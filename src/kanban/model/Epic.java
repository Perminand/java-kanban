package kanban.model;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> idSubTask =new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String nameEpic, String description) {
        super(nameEpic, description, TypeTask.EPIC);
        super.startTime = null;
    }

    public Epic(String nameEpic, String description, Duration duration) {
        super(nameEpic, description, TypeTask.EPIC);
        super.startTime = null;
        this.duration=duration;
    }

    public Epic(int uin, String nameEpic, String description, Status status) {
        super(uin, nameEpic, description, status, TypeTask.EPIC, LocalDateTime.now(), Duration.ZERO);
    }

    public Epic(int uin, String nameEpic, String description, Status status, LocalDateTime localDateTime,
                Duration duration) {
        super(uin, nameEpic, description, status, TypeTask.EPIC, localDateTime, duration);
    }

    public void setDuration(Duration duration) {

        this.duration = duration;
    }

    public ArrayList<Integer> getIdSubTask() {

        return idSubTask;
    }

    public void setIdSubTask(ArrayList<Integer> idSubTask) {

        this.idSubTask = idSubTask;
    }
       public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    @Override
    public String toString() {
        return "Epic{" + "idSubTask=" + idSubTask + " {" + super.toString() + "}";
    }

}
