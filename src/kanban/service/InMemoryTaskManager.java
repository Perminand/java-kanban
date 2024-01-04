package kanban.service;

import kanban.comparator.DateTimeComparator;
import kanban.enumClass.Status;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> mapTask = new HashMap<>();
    protected final HashMap<Integer, SubTask> mapSubTask = new HashMap<>();
    protected final HashMap<Integer, Epic> mapEpic = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected Map<Integer, Task> sortTaskTime = new LinkedHashMap<>();
    private int uin = 0;

    public static void statusCalc(Epic epic, List<SubTask> listSubTask) {
        if (epic == null) return;
        if (listSubTask.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int[] status = {0, 0, 0};//Подсчитываем статусы ["NEW"]["IN_PROGRESS"]["DONE"]
        Duration sumDuration = Duration.ZERO;
        LocalDateTime startMinTask;
        LocalDateTime endMaxTask;
        if (epic.getStartTime() == null) startMinTask = LocalDateTime.MAX;
        else startMinTask = epic.getStartTime();
        if (epic.getEndTime() == null) endMaxTask = LocalDateTime.MIN;
        else endMaxTask = epic.getEndTime();
        for (SubTask subTask : listSubTask) {
            if (subTask.getStatus().equals(Status.NEW)) status[0]++;
            if (subTask.getStatus().equals(Status.IN_PROGRESS)) status[1]++;
            if (subTask.getStatus().equals(Status.DONE)) status[2]++;
            sumDuration = sumDuration.plus(subTask.getDuration());
            if (subTask.getStartTime().isBefore(startMinTask))
                startMinTask = subTask.getStartTime();
            if (subTask.getEndTime().isAfter(endMaxTask))
                endMaxTask = subTask.getEndTime();


        }
        if (status[0] == 0 && status[1] == 0) epic.setStatus(Status.DONE);
        else if (status[0] < listSubTask.size()) epic.setStatus(Status.IN_PROGRESS);
        else epic.setStatus(Status.NEW);
        epic.setDuration(sumDuration);
        epic.setStartTime(startMinTask);
        epic.setEndTime(endMaxTask);
    }

    public LinkedHashMap<Integer, Task> getPrioritizedTasks() {
        DateTimeComparator comparator = new DateTimeComparator();
        TreeSet<Task> treeListTask = new TreeSet<>(comparator);
        treeListTask.addAll(mapTask.values());
        LinkedHashMap<Integer, Task> linkedHash = new LinkedHashMap<>();
        for (Task task : treeListTask) {
            linkedHash.put(task.getUin(), task);
        }
        return linkedHash;
    }

    @Override
    public int createTask(Task task) {
        if (task == null) return -1;
        int key = getUin();
        task.setUin(key);
        mapTask.put(key, task);
        sortTaskTime = getPrioritizedTasks();
        return key;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (subTask == null) return -1;
        int key = getUin();
        subTask.setUin(key);
        Epic epic = mapEpic.get(subTask.getEpicId());
        if (epic == null) return -1;
        mapSubTask.put(key, subTask);
        epic.getIdSubTask().add(subTask.getUin());
        List<SubTask> listSubTask = new ArrayList<>();
        for (Integer i : epic.getIdSubTask()) {
            for (SubTask st : getSubTasks()) {
                if (st.getUin().equals(i))
                    listSubTask.add(st);
            }
        }
        statusCalc(epic, listSubTask);
        return key;
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
        sortTaskTime = getPrioritizedTasks();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTask == null) return;
        int epicId = subTask.getEpicId();
        Epic epic = mapEpic.get(epicId);
        if (epic == null) return;
        int key = subTask.getUin();
        subTask.setEpicId(epicId);
        mapSubTask.put(key, subTask);
        List<SubTask> listSubTask = new ArrayList<>();
        for (Integer i : epic.getIdSubTask()) {
            for (SubTask st : getSubTasks()) {
                if (st.getUin() == i)
                    listSubTask.add(st);
            }
        }
        statusCalc(epic, listSubTask);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null || epic.getUin() == null) return;
        int key = epic.getUin();
        ArrayList<Integer> idSubTask = mapEpic.get(key).getIdSubTask();
        epic.setIdSubTask(idSubTask);
        mapEpic.put(key, epic);
    }

    @Override
    public Task getTask(int id) {

        Task task = mapTask.get(id);
        if (task != null)
            historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubTask(int id) throws NullPointerException {
        SubTask task = mapSubTask.get(id);
        if (task != null)
            historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic task = mapEpic.get(id);
        if (task != null)
            historyManager.add(task);
        return task;
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
            List<SubTask> listSubTask = new ArrayList<>();
            for (Integer i : epic.getIdSubTask()) {
                for (SubTask st : getSubTasks()) {
                    if (st.getUin() == i)
                        listSubTask.add(st);
                }
            }
            statusCalc(epic, listSubTask);
        }
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

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(mapTask.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(mapEpic.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(mapSubTask.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
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
            return task;
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

    private int getUin() {
        return uin++;
    }

    protected void setUin(int generatorId) {
        this.uin = generatorId;
    }
}
