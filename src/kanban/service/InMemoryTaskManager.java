package kanban.service;

import kanban.enumClass.Status;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> mapTask = new HashMap<>();
    private final HashMap<Integer, SubTask> mapSubTask = new HashMap<>();
    private final HashMap<Integer, Epic> mapEpic = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int uin = 0;

    @Override
    public void createTask(Task task) {
        if (task == null) return;
        int key = getUin();
        task.setUin(key);
        mapTask.put(key, task);
    }

    @Override
    public void createSubTask(SubTask subTask) {
        if (subTask == null) return;
        int key = getUin();
        subTask.setUin(key);
        Epic epic = mapEpic.get(subTask.getEpicId());
        if (epic == null) return;
        mapSubTask.put(key, subTask);
        epic.getIdSubTask().add(subTask.getUin());
        statusCalc(epic);
    }

    @Override
    public int createEpic(Epic epic) {
        int key = getUin();
        epic.setUin(key);
        epic.setStatus(Status.NEW);
        mapEpic.put(key, epic);
        return key;
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) return;
        mapTask.put(task.getUin(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTask == null) return;
        int epicId = mapSubTask.get(subTask.getUin()).getEpicId();
        Epic epic = mapEpic.get(epicId);
        int key = subTask.getUin();
        subTask.setEpicId(epicId);
        mapSubTask.put(key, subTask);
        statusCalc(epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) return;
        int key = epic.getUin();
        ArrayList<Integer> idSubTask = mapEpic.get(key).getIdSubTask();
        epic.setIdSubTask(idSubTask);
        mapEpic.put(key, epic);
    }

    @Override
    public Task getTask(int id) {
        Task task = mapTask.get(id);
        if (task != null) {
            historyManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask task = mapSubTask.get(id);
        if (task != null) {
            historyManager.add(task);
            return mapSubTask.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpic(int id) {
        Epic task = mapEpic.get(id);
        if (task != null) {
            historyManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        if (id < 0) return;
        if (mapTask.get(id) != null) {
            mapTask.remove(id);
        }
        if (mapEpic.get(id) != null) {
            Epic epic = mapEpic.get(id);
            removeAllSubTaskByEpic(epic);
            mapEpic.remove(id);
        }
        if (mapSubTask.get(id) != null) {
            Epic epic = mapEpic.get(mapSubTask.get(id).getEpicId());
            mapSubTask.remove(id);
            epic.getIdSubTask().remove((Integer) id);
            statusCalc(epic);
        }
        historyManager.remove(id);
    }

    @Override
    public void removeAllTask() {
        mapTask.clear();

    }

    @Override
    public void removeAllSubTask() {
        mapSubTask.clear();
        for (Epic epic : mapEpic.values()) {
            epic.getIdSubTask().clear();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public void removeAllEpic() {
        mapSubTask.clear();
        mapEpic.clear();
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public Task getById(int id) {
        if (id < 0) return null;
        if (mapTask.get(id) != null) {
            Task task = mapTask.get(id);
            historyManager.add(task);
            return task;
        }
        if (mapEpic.get(id) != null) {
            Epic task = mapEpic.get(id);
            historyManager.add(task);
            return task;
        }
        if (mapSubTask.get(id) != null) {
            SubTask task = mapSubTask.get(id);
            historyManager.add(task);
            return mapSubTask.get(id);
        }
        return null;
    }

    private void removeAllSubTaskByEpic(Epic epic) {
        ArrayList<Integer> listSubTask = epic.getIdSubTask();
        if (listSubTask.isEmpty()) return;
        for (Integer integer : listSubTask) {
            mapSubTask.remove(integer);
            historyManager.remove(integer);
        }
        epic.setStatus(Status.NEW);
    }

    private void statusCalc(Epic epic) {
        if (epic == null) return;
        ArrayList<SubTask> subTasks = getSubTaskByIdEpic(epic);
        if (subTasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int[] status = {0, 0, 0};//Подсчитываем статусы ["NEW"]["IN_PROGRESS"]["DONE"]
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus().equals(Status.NEW)) status[0]++;
            if (subTask.getStatus().equals(Status.IN_PROGRESS)) status[1]++;
            if (subTask.getStatus().equals(Status.DONE)) status[2]++;
        }
        if (status[0] == 0 && status[1] == 0) epic.setStatus(Status.DONE);
        else if (status[0] < subTasks.size()) epic.setStatus(Status.IN_PROGRESS);
        else epic.setStatus(Status.NEW);
    }

    private int getUin() {
        return uin++;
    }

}
