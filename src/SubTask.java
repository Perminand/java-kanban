public class SubTask extends Task{
     int affiliation;

    public SubTask(String nameTask, String discription, int affiliation) {
        super(nameTask, discription);
        this.affiliation = affiliation;
    }
    public SubTask(String nameTask, String discription, int uin, String status) {
        super(nameTask, discription);
        this.setUin(uin);
        this.setStatus(status);
    }
    public int getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(int affiliation) {
        this.affiliation = affiliation;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "affiliation=" + affiliation +
                "} " + super.toString();
    }
}
