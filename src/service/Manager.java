package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private final HashMap<Integer, Task> mapTask = new HashMap<>();
    private final HashMap<Integer, SubTask> mapSubTask = new HashMap<>();
    private final HashMap<Integer, Epic> mapEpic = new HashMap<>();
    Status statusNew = Status.NEW;
    Status statusInProgress = Status.IN_PROGRESS;
    Status statusDone = Status.DONE;
    private int uin = 0;

    public void createTask(Task task) {
        if (task == null) return;
        int key = getUin();
        task.setUin(key);
        mapTask.put(key, task);
    }

    public void createSubTask(SubTask subTask) {
        if (subTask == null) return;
        int key = getUin();
        subTask.setUin(key);
        mapSubTask.put(key, subTask);
        Epic epic = mapEpic.get(subTask.getEpicId());
        if (epic == null) return;
        epic.getIdSubTask().add(subTask.getUin());
        statusCalc(epic);
    }

    public int createEpic(Epic epic) {
        int key = getUin();
        epic.setUin(key);
        epic.setStatus(String.valueOf(statusNew));
        mapEpic.put(key, epic);
        return key;
    }

    public void updateTask(Task task) {
        if (task == null) return;
        mapTask.put(task.getUin(), task);
    }

    public void updateSubTask(SubTask subTask) {
        if (subTask == null) return;
        int epicId = mapSubTask.get(subTask.getUin()).getEpicId();
        Epic epic = mapEpic.get(mapSubTask.get(subTask.getUin()).getEpicId());
        int key = subTask.getUin();
        subTask.setEpicId(epicId);
        subTask.setUin(key);
        mapSubTask.put(key, subTask);
        statusCalc(epic);
    }

    public void updateEpic(Epic epic) {
        if (epic == null) return;
        int key = epic.getUin();
        ArrayList<Integer> idSubTask = mapEpic.get(key).getIdSubTask();
        epic.setIdSubTask(idSubTask);
        mapEpic.put(key, epic);
    }

    public Task getById(int id) {
        if (id < 0) return null;
        if (mapTask.get(id) != null) return mapTask.get(id);
        if (mapEpic.get(id) != null) return mapEpic.get(id);
        if (mapSubTask.get(id) != null) return mapSubTask.get(id);
        return null;
    }


    public void deleteById(int id) {
        if (id < 0) return;
        if (mapTask.get(id) != null) {
            mapTask.remove(id);
            return;
        }
        if (mapEpic.get(id) != null) {
            ArrayList<Integer> listSubTask = mapEpic.get(id).getIdSubTask();
            for (Integer i : listSubTask) {
                deleteById(i);
                return;
            }
        }
        if (mapSubTask.get(id) != null) {
            Epic epic = mapEpic.get(mapSubTask.get(id).getEpicId());
            mapSubTask.remove(id);
            epic.getIdSubTask().remove((Integer) id);
            statusCalc(epic);
        }
    }

    public void removeAllTask() {
        mapTask.clear();
    }

    public void removeAllSubTaskByEpic(int uinEpic) {
        ArrayList<Integer> listSubTask = mapEpic.get(uinEpic).getIdSubTask();
        if (listSubTask == null) return;
        for (int i = 0; i < listSubTask.size(); i++) {
            deleteById(i);
        }
        statusCalc(mapEpic.get(uinEpic));
    }


    public void removeAllSubTask() {
        mapSubTask.clear();
        for (Epic epic : mapEpic.values()) {
            epic.getIdSubTask().clear();
            statusCalc(epic);
        }
    }

    public void removeAllEpic() {
        mapSubTask.clear();
        mapEpic.clear();
    }

    private void statusCalc(Epic epic) {
        if (epic == null) return;
        ArrayList<SubTask> subTasks = getSubTaskByIdEpic(epic);
        if (subTasks.isEmpty()) {
            epic.setStatus(String.valueOf(statusNew));
            return;
        }
        int[] status = {0, 0, 0};//Подсчитываем статусы ["NEW"]["IN_PROGRESS"]["DONE"]
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus().equals(String.valueOf(statusNew))) status[0]++;
            if (subTask.getStatus().equals(String.valueOf(statusInProgress))) status[1]++;
            if (subTask.getStatus().equals(String.valueOf(statusDone))) status[2]++;
        }
        if (status[0] == 0 && status[1] == 0) epic.setStatus(String.valueOf(statusDone));
        else if (status[0] < subTasks.size()) epic.setStatus(String.valueOf(statusInProgress));
        else epic.setStatus(String.valueOf(statusNew));
    }

    public ArrayList<SubTask> getSubTaskByIdEpic(Epic epic) {
        ArrayList<SubTask> arr = new ArrayList<>();
        for (Integer i : epic.getIdSubTask()) {
            arr.add(mapSubTask.get(i));
        }
        return arr;
    }

    public ArrayList<Task> getMapTask() {
        return new ArrayList<>(mapTask.values());
    }

    public ArrayList<Epic> getMapEpic() {
        return new ArrayList<>(mapEpic.values());
    }

    public ArrayList<SubTask> getMapSubTask() {
        return new ArrayList<>(mapSubTask.values());
    }

    private int getUin() {
        return uin++;
    }
}
