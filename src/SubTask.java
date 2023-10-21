public class SubTask extends Task {
    int affiliation;

    public SubTask(String nameTask, String description, int affiliation) {
        super(nameTask, description);
        this.affiliation = affiliation;
    }

    public SubTask(String nameTask, String description, int uin, String status) {
        super(nameTask, description);
        this.setUin(uin);
        this.setStatus(status);
    }

    public SubTask(String nameTask, String description) {
        super(nameTask, description);
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
