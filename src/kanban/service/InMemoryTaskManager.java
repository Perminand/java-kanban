package kanban.service;

import kanban.comparator.DateTimeComparator;
import kanban.enumClass.Status;
import kanban.model.Epic;
import kanban.model.FreeTimeManagement;
import kanban.model.SubTask;
import kanban.model.Task;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> mapTask = new HashMap<>();
    protected final HashMap<Integer, SubTask> mapSubTask = new HashMap<>();
    protected final HashMap<Integer, Epic> mapEpic = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final HashMap<LocalDate, Map<Integer, FreeTimeManagement>> freeTimeManagements = new HashMap<>();
    protected Set<Task> sortTaskTime;

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

    public TreeSet<Task> getPrioritizedTasks() {
        DateTimeComparator comparator = new DateTimeComparator();
        TreeSet<Task> treeListTask = new TreeSet<>(comparator);
        treeListTask.addAll(mapTask.values());
        treeListTask.addAll(mapSubTask.values());
        return treeListTask;
    }


    private boolean checkAddFreeManagement(Task task) {
        LocalDateTime startTime;
        if (task.getStartTime() == null) {
            return true;
        }
        Map<Integer, FreeTimeManagement> hashMap;
        if (freeTimeManagements.isEmpty()) {
            startTime = task.getStartTime();
            while (true) {
                if (task.getEndTime().isAfter(startTime.toLocalDate().
                        atTime(23, 59, 59, 999999999))) {
                    hashMap = new HashMap<>();
                    hashMap.put(task.getUin(), new FreeTimeManagement(task.getUin(), startTime, startTime.
                            toLocalDate().atTime(23, 59, 59, 999999999)));
                    freeTimeManagements.put(startTime.toLocalDate(), hashMap);
                    startTime = startTime.plusDays(1).toLocalDate().atTime(0, 0, 0, 0);
                } else {
                    hashMap = new HashMap<>();
                    hashMap.put(task.getUin(), new FreeTimeManagement(task.getUin(), startTime,
                            task.getEndTime()));
                    freeTimeManagements.put(startTime.toLocalDate(), hashMap);
                    return true;
                }
            }
        } else {
            boolean isFree = true;
            Collection<Map<Integer, FreeTimeManagement>> date = freeTimeManagements.values();
            for (Map<Integer, FreeTimeManagement> map : date)
                for (FreeTimeManagement freeTime : map.values()) {
                    if (task.getStartTime().plus(task.getDuration()).isAfter(freeTime.startDuration) &&
                            task.getStartTime().isBefore(freeTime.endDuration)) {
                        isFree = false;
                        break;
                    }

                    if (task.getStartTime().isAfter(freeTime.endDuration)) {
                        long coinDays = task.getDuration().toDays();
                        if (coinDays < 1) break;
                        for (long i = 1; i < coinDays; i++) {
                            if (freeTimeManagements.containsKey(task.getStartTime().toLocalDate().plusDays(i))) {
                                isFree = false;
                                break;
                            }
                        }
                        Collection<Map<Integer, FreeTimeManagement>> date2 = freeTimeManagements.values();
                        for (Map<Integer, FreeTimeManagement> map2 : date2)
                            for (FreeTimeManagement freeTime2 : map2.values()) {
                                if (task.getStartTime().plus(task.getDuration()).isAfter(freeTime2.startDuration)) {
                                    isFree = false;
                                    break;
                                }
                            }
                    }
                }
            if (isFree) {
                startTime = task.getStartTime();
                while (true) {
                    if (task.getEndTime().isAfter(startTime.toLocalDate().
                            atTime(23, 59, 59, 999999999))) {
                        hashMap = new HashMap<>();
                        hashMap.put(task.getUin(), new FreeTimeManagement(task.getUin(), startTime, startTime.
                                toLocalDate().atTime(23, 59, 59, 999999999)));
                        freeTimeManagements.put(startTime.toLocalDate(), hashMap);
                        startTime = startTime.plusDays(1).toLocalDate().atTime(0, 0, 0, 0);
                    } else {
                        if (freeTimeManagements.containsKey(startTime.toLocalDate()))
                            freeTimeManagements.get(startTime.toLocalDate()).put(task.getUin(),
                                    new FreeTimeManagement(task.getUin(), startTime, task.getEndTime()));
                        else {
                            hashMap = new HashMap<>();
                            hashMap.put(task.getUin(), new FreeTimeManagement(task.getUin(), startTime, task.getEndTime()));
                            freeTimeManagements.put(startTime.toLocalDate(), hashMap);
                        }
                        return true;
                    }
                }

            }
            return false;
        }
    }


    @Override
    public int createTask(Task task) {
        if (task == null) return -1;
        int key = getUin();
        task.setUin(key);
        if (checkAddFreeManagement(task)) {
            mapTask.put(key, task);
            sortTaskTime = getPrioritizedTasks();
            return key;
        }
        return -1;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (subTask == null) return -1;
        int key = getUin();
        subTask.setUin(key);
        Epic epic = mapEpic.get(subTask.getEpicId());
        if (epic == null) return -1;
        if (checkAddFreeManagement(subTask)) {
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
            sortTaskTime = getPrioritizedTasks();
            return key;
        }
        return -1;
    }

    @Override
    public int createEpic(Epic epic) {
        int key = getUin();
        epic.setUin(key);
        epic.setStatus(Status.NEW);
        mapEpic.put(key, epic);
        sortTaskTime = getPrioritizedTasks();
        return key;
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) return;
        if (checkAddFreeManagement(task)) {
            mapTask.put(task.getUin(), task);
            sortTaskTime = getPrioritizedTasks();
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTask == null) return;
        int epicId = subTask.getEpicId();
        Epic epic = mapEpic.get(epicId);
        if (epic == null) return;
        int key = subTask.getUin();
        subTask.setEpicId(epicId);
        if (checkAddFreeManagement(subTask)) {
            mapSubTask.put(key, subTask);
            List<SubTask> listSubTask = new ArrayList<>();
            for (Integer i : epic.getIdSubTask()) {
                for (SubTask st : getSubTasks()) {
                    if (st.getUin().equals(i))//(st.getUin() == i)
                        listSubTask.add(st);
                }
            }
            statusCalc(epic, listSubTask);
        }
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
            removeFreeTimeManagerTask(Set.of(id));
            mapTask.remove(id);
        }
        if (mapEpic.get(id) != null) {
            Epic epic = mapEpic.get(id);
            removeAllSubTaskByEpic(epic);
            mapEpic.remove(id);
        }
        if (mapSubTask.get(id) != null) {
            Epic epic = mapEpic.get(mapSubTask.get(id).getEpicId());
            removeFreeTimeManagerTask(Set.of(id));
            mapSubTask.remove(id);
            epic.getIdSubTask().remove((Integer) id);
            List<SubTask> listSubTask = new ArrayList<>();
            for (Integer i : epic.getIdSubTask()) {
                for (SubTask st : getSubTasks()) {
                    if (st.getUin().equals(i))//(st.getUin() == i)
                        listSubTask.add(st);
                }
            }
            statusCalc(epic, listSubTask);

        }
    }

    private void removeFreeTimeManagerTask(Set<Integer> set) {
        List<LocalDate> dateList = new ArrayList<>(freeTimeManagements.keySet());
        for (LocalDate localDate : dateList) {
            List<Integer> listIdFreeTime = new ArrayList<>(freeTimeManagements.get(localDate).keySet());
            for (Integer j : listIdFreeTime)
                for (Integer k : set) {
                    if (freeTimeManagements.get(localDate).get(j).idTask == k) {
                        freeTimeManagements.get(localDate).remove(j);
                        break;
                    }
                }
            if (freeTimeManagements.get(localDate).isEmpty())
                freeTimeManagements.remove(localDate);
        }
    }

    @Override
    public void removeAllTask() {
        Set<Integer> intSet = mapTask.keySet();
        removeFreeTimeManagerTask(intSet);
        mapTask.clear();

    }


    @Override
    public void removeAllSubTask() {
        Set<Integer> intSet = mapSubTask.keySet();
        removeFreeTimeManagerTask(intSet);
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
