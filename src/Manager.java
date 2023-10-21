import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private ArrayList<Task> listTask = new ArrayList<>();
    private ArrayList<Epic> listEpic = new ArrayList<>();
    private ArrayList<SubTask> listSubTask = new ArrayList<>();
    static String[] listStatus = {"NEW", "IN_PROGRESS", "DONE"};
    private int uin = 0;

    public void createTask(Task task) {
        if (task == null) return;
        task.setUin(getUin());
        listTask.add(task);
    }

    public void createSubTask(SubTask subTask) {
        if (subTask == null) return;
        subTask.setUin(getUin());
        listSubTask.add(subTask);
        Epic epic = findEpicToAffiliation(subTask.getAffiliation());
        if (epic == null) return;
        findEpicToAffiliation(subTask.getAffiliation()).getIdSubTask().add(subTask.getUin());
        ;
        statusCalc(subTask.getAffiliation());
    }

    public void createEpic(Epic epic) {
        if (epic == null) return;
        int id = getUin();
        epic.setUin(id);
        epic.setStatus("NEW");
        listEpic.add(epic);
    }

    public void updateTask(Task task) {
        if (task == null) return;
        for (Task line : listTask) {
            if (line.getUin() == task.getUin()) {
                line.setNameTask(task.getNameTask());
                line.setDiscription(task.getDiscription());
                line.setStatus(task.getStatus());
                return;
            }
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTask == null) return;
        int affilation = 0;
        for (int i = 0; i < listSubTask.size(); i++) {
            if (listSubTask.get(i).getUin() == subTask.getUin()) {
                affilation = listSubTask.get(i).getAffiliation();
                subTask.setAffiliation(affilation);
                listSubTask.set(i, subTask);
                break;
            }
        }
        statusCalc(affilation);
    }

    public void updateEpic(Epic epic) {
        if (epic == null) return;
        for (Epic line : listEpic) {
            if (line.getUin() == epic.getUin()) {
                line.setNameTask(epic.getNameTask());
                line.setDiscription(epic.getDiscription());
            }
        }
        statusCalc(uin);
    }

    public Object getById(int id) {
        if (id < 0) return null;
        for (Task line : listTask) {
            if (line.getUin() == id) {
                return line;
            }
        }
        for (Epic line : listEpic) {
            if (line.getUin() == id) {
                return line;
            }
        }
        for (SubTask line : listSubTask) {
            if (line.getUin() == id)
                return line;
        }
        return null;
    }


    public void remove(int id) {
        if (id < 0) return;
        for (int i = 0; i < listTask.size(); i++) {
            if (listTask.get(i).getUin() == id) listTask.remove(i);
        }
        for (int i = 0; i < listEpic.size(); i++) {
            if (listEpic.get(i).getUin() == id) {
                listEpic.remove(i);
            }
        }
        for (int i = 0; i < listSubTask.size(); i++) {
            if (listSubTask.get(i).getUin() == id) {
                int affiliation = listSubTask.get(i).getAffiliation();
                listSubTask.remove(i);
                statusCalc(affiliation);
            }

        }
    }

    void removeAllTask() {
        listTask.clear();
    }

    void removeAllSubTaskByEpic(int uinEpic) {
        ArrayList<SubTask> arrUinSubTask = getSubTaskByIdEpic(uinEpic);
        if (arrUinSubTask == null) return;
        for (SubTask subTask : arrUinSubTask) {
            for (int i = 0; i < listSubTask.size(); i++) {
                if (subTask.getUin() == listSubTask.get(i).getUin()) {
                    listSubTask.remove(i);
                    for (Epic epic : listEpic) {
                        if (epic.getUin() == uinEpic) {
                            epic.setStatus(listStatus[0]);
                        }
                    }
                }
            }
        }
    }

    void removeSubTask() {
        listSubTask.clear();
    }

    void removeAllEpic() {
        listEpic.clear();
    }

    public void statusCalc(int affilation) {
        if (affilation == 0) return;
        ArrayList<SubTask> listTask = getSubTaskByIdEpic(affilation);
        int[] status = {0, 0, 0};
        if (listTask.isEmpty()) {
            findEpicToAffiliation(affilation).setStatus(listStatus[0]);
            return;
        }
        for (SubTask subTask : listTask) {
            if (subTask.getStatus() == listStatus[0]) status[0]++;
            if (subTask.getStatus() == listStatus[1]) status[1]++;
            if (subTask.getStatus() == listStatus[2]) status[2]++;
        }
        if (status[0] == 0 && status[1] == 0) findEpicToAffiliation(affilation).setStatus(listStatus[2]);
        else if (status[0] < listTask.size()) findEpicToAffiliation(affilation).setStatus(listStatus[1]);
        else findEpicToAffiliation(affilation).setStatus(listStatus[0]);
    }

    public ArrayList<SubTask> getSubTaskByIdEpic(int id) {
        ArrayList<SubTask> arr = new ArrayList<>();
        for (Epic epic : listEpic) {
            if (epic.getUin() == id) {
                for (Integer i : epic.getIdSubTask()) {
                    for (SubTask subTask : listSubTask) {
                        if (subTask.getUin() == i) {
                            arr.add(subTask);
                        }
                    }
                }
                return arr;
            }
        }
        return new ArrayList<>();
    }

    private Epic findEpicToAffiliation(int affiliation) {
        for (Epic epic : listEpic) {
            if (epic.getUin() == affiliation) return epic;
        }
        return null;
    }

    public ArrayList<Task> getListTask() {
        return listTask;
    }

    public ArrayList<Epic> getListEpic() {
        return listEpic;
    }

    public ArrayList<SubTask> getListSubTask() {
        return listSubTask;
    }

    public int getUin() {
        return uin++;
    }
}
