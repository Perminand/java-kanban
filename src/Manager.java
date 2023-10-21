import java.util.ArrayList;
import java.util.Objects;

public class Manager {
    private final ArrayList<Task> listTask = new ArrayList<>();
    private final ArrayList<Epic> listEpic = new ArrayList<>();
    private final ArrayList<SubTask> listSubTask = new ArrayList<>();
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
        Objects.requireNonNull(findEpicToAffiliation(subTask.getAffiliation())).getIdSubTask().add(subTask.getUin());
        statusCalc(subTask.getAffiliation());
    }

    public void createEpic(Epic epic, ArrayList<SubTask> arrayList) {
        if (epic == null) return;
        epic.setUin(getUin());
        epic.setStatus("NEW");
        for (SubTask subTask : arrayList) {
            subTask.setAffiliation(epic.getUin());
            subTask.setUin(getUin());
            listSubTask.add(subTask);
            epic.getIdSubTask().add(subTask.getUin());
        }
        listEpic.add(epic);
    }

    public void updateTask(Task task) {
        if (task == null) return;
        for (Task line : listTask) {
            if (line.getUin() == task.getUin()) {
                line.setNameTask(task.getNameTask());
                line.setDescription(task.getDescription());
                if (task.getStatus() != null) line.setStatus(task.getStatus());
                return;
            }
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTask == null) return;
        int affiliation = 0;
        for (int i = 0; i < listSubTask.size(); i++) {
            if (listSubTask.get(i).getUin() == subTask.getUin()) {
                affiliation = listSubTask.get(i).getAffiliation();
                subTask.setAffiliation(affiliation);
                if (subTask.getStatus() == null) subTask.setStatus(listSubTask.get(i).getStatus());
                listSubTask.set(i, subTask);
                break;
            }
        }
        statusCalc(affiliation);
    }

    public void updateEpic(Epic epic) {
        if (epic == null) return;
        for (Epic line : listEpic) {
            if (line.getUin() == epic.getUin()) {
                line.setNameTask(epic.getNameTask());
                line.setDescription(epic.getDescription());
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
        int indTask = -1;
        for (int i = 0; i < listTask.size(); i++) {
            if (listTask.get(i).getUin() == id)
                indTask = i;//Вместо listEpic.remove(i);
        }
        if (indTask >= 0) listTask.remove(indTask);
        for (int i = 0; i < listEpic.size(); i++) {
            if (listEpic.get(i).getUin() == id) {
                indTask = i;//Вместо listEpic.remove(i);
            }
        }
        if (indTask >= 0) listEpic.remove(indTask);
        int affiliation = -1;
        for (int i = 0; i < listSubTask.size(); i++) {
            if (listSubTask.get(i).getUin() == id) {
                affiliation = listSubTask.get(i).getAffiliation();
                indTask = i;//Вместо listEpic.remove(i);
                Epic epic = findEpicToAffiliation(affiliation);
                for (int j = 0; j < Objects.requireNonNull(epic).getIdSubTask().size(); j++) {
                    epic.getIdSubTask().remove((Integer) id);
                }
            }
        }
        if (indTask >= 0) listSubTask.remove(indTask);
        if (affiliation >= 0) statusCalc(affiliation);
    }

    void removeAllTask() {
        listTask.clear();
    }

    void removeAllSubTaskByEpic(int uinEpic) {
        ArrayList<SubTask> arrUinSubTask = getSubTaskByIdEpic(uinEpic);
        if (arrUinSubTask == null) return;

        for (SubTask subTask : arrUinSubTask) {
            int indTask = -1;
            for (int i = 0; i < listSubTask.size(); i++) {
                if (subTask.getUin() == listSubTask.get(i).getUin()) {
                    indTask = i;
                    for (Epic epic : listEpic) {
                        if (epic.getUin() == uinEpic) {
                            epic.setStatus(listStatus[0]);
                            epic.getIdSubTask().clear();
                        }
                    }
                }
            }
            if (indTask >= 0) listSubTask.remove(indTask);
        }
    }

    void removeSubTask() {
        listSubTask.clear();
        for (Epic epic : listEpic) {
            epic.getIdSubTask().clear();
        }
    }

    void removeAllEpic() {
        listSubTask.clear();
        listEpic.clear();
    }

    public void statusCalc(int affiliation) {
        ArrayList<SubTask> listTask = getSubTaskByIdEpic(affiliation);
        int[] status = {0, 0, 0};
        Epic epic = findEpicToAffiliation(affiliation);
        if (epic == null) return;
        if (listTask.isEmpty()) {
            epic.setStatus(listStatus[0]);
            return;
        }
        for (SubTask subTask : listTask) {
            if (subTask.getStatus().equals(listStatus[0])) status[0]++;
            if (subTask.getStatus().equals(listStatus[1])) status[1]++;
            if (subTask.getStatus().equals(listStatus[2])) status[2]++;
        }
        if (status[0] == 0 && status[1] == 0) epic.setStatus(listStatus[2]);
        else if (status[0] < listTask.size()) epic.setStatus(listStatus[1]);
        else epic.setStatus(listStatus[0]);
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
