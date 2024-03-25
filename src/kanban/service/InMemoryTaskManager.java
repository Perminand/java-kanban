package kanban.service;

import kanban.enumClass.Status;
import kanban.enumClass.TypeTask;
import kanban.exceptions.IntersectionOfTime;
import kanban.exceptions.NoEpicExceptionForSubTask;
import kanban.exceptions.NoTaskException;
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
    protected Set<Task> sortTaskTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private int uin = 0;

    public static boolean isFreeTime(Task task, Set<Task> sortTaskTime) {
        for (Task taskSort : sortTaskTime) {
            if (task.getEndTime().isAfter(taskSort.getStartTime()) && task.getStartTime().isBefore(taskSort.getEndTime()) && !task.getUin().equals(taskSort.getUin()))
                return false;
        }
        return true;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortTaskTime);
    }

    @Override
    public int createTask(Task task) throws IntersectionOfTime, NullPointerException {
        if (task == null) throw new NullPointerException("Передан Null");
        if (task.getStatus() == null) task.setStatus(Status.NEW);
        int key = getUin();
        task.setUin(key);
        if (task.getTypeTask() == null) task.setTypeTask(TypeTask.TASK);
        if (task.getStartTime() != null) {
            if (isFreeTime(task, sortTaskTime)) {
                sortTaskTime.add(task);
            } else throw new IntersectionOfTime("Задача пересекается с существующей");
        }
        mapTask.put(key, task);
        return key;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (subTask == null) return -1;
        int key = getUin();
        subTask.setUin(key);
        if (subTask.getTypeTask() == null) subTask.setTypeTask(TypeTask.SUBTASK);
        if (subTask.getStatus() == null) subTask.setStatus(Status.NEW);
        Epic epic = mapEpic.get(subTask.getEpicId());
        if (epic == null) throw new NoEpicExceptionForSubTask("Epic с id: " + subTask.getEpicId() + " не найден.");
        if (subTask.getStartTime() != null) {
            if (isFreeTime(subTask, sortTaskTime)) {
                sortTaskTime.add(subTask);
            } else throw new IntersectionOfTime("Задача пересекается с существующей");
            mapSubTask.put(key, subTask);
            epic.getIdSubTask().add(key);
            calculateTimeEpic(epic);
            statusCalc(epic);
            return key;
        } else {
            mapSubTask.put(key, subTask);
            epic.getIdSubTask().add(key);
            statusCalc(epic);
            return key;
        }
    }

    @Override
    public int createEpic(Epic epic) {
        int key = getUin();
        epic.setUin(key);
        epic.setStatus(Status.NEW);
        if (epic.getTypeTask() == null) epic.setTypeTask(TypeTask.EPIC);
        mapEpic.put(key, epic);
        return key;
    }

    @Override
    public void updateTask(Task task) {
        if (task == null) return;
        Set<Task> doubleSetSort = sortTaskTime;
        doubleSetSort.removeIf(taskDouble -> task.getUin().equals(taskDouble.getUin()));
        if (task.getStartTime() != null) {
            if (isFreeTime(task, doubleSetSort)) {
                sortTaskTime.add(task);
            } else throw new IntersectionOfTime("Задача пересекается с существующей");
        }
        mapTask.put(task.getUin(), task);

    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTask == null) return;
        int epicId = subTask.getEpicId();
        Epic epic = mapEpic.get(epicId);
        if (epic == null) return;
        int key = subTask.getUin();
        subTask.setEpicId(epicId);
        Set<Task> doubleSetSort = sortTaskTime;
        doubleSetSort.removeIf(taskDouble -> subTask.getUin().equals(taskDouble.getUin()));
        if (subTask.getStartTime() != null) {
            if (isFreeTime(subTask, doubleSetSort)) {
                calculateTimeEpic(epic);
                sortTaskTime.add(subTask);
            } else throw new IntersectionOfTime("Задача пересекается с существующей");
        }
        mapSubTask.put(key, subTask);
        statusCalc(epic);
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
        Optional<Task> optionalTask = getOptionalTasks(id, TypeTask.TASK);
        return optionalTask.orElse(null);
    }

    @Override
    public SubTask getSubTask(int id) {
        Optional<Task> optionalTask = getOptionalTasks(id, TypeTask.SUBTASK);
        return (SubTask) optionalTask.orElse(null);
    }

    @Override
    public Epic getEpic(int id) {
        Optional<Task> optionalTask = getOptionalTasks(id, TypeTask.EPIC);
        return (Epic) optionalTask.orElse(null);
    }


    @Override
    public void deleteById(int id) throws NoTaskException {
        if (id < 0) return;
        if (mapTask.get(id) != null) {
            try {
                sortTaskTime.remove(mapTask.remove(id));
            } catch (NullPointerException ignored) {
            }
        } else if (mapEpic.get(id) != null) {
            Epic epic = mapEpic.get(id);
            removeAllSubTaskByEpic(epic);
            mapEpic.remove(id);
        } else if (mapSubTask.get(id) != null) {
            Epic epic = mapEpic.get(mapSubTask.get(id).getEpicId());
            sortTaskTime.remove(mapSubTask.remove(id));
            epic.getIdSubTask().remove((Integer) id);
            statusCalc(epic);
            calculateTimeEpic(epic);
        }
    }

    @Override
    public void removeAllTask() {
        mapTask.clear();
        sortTaskTime.clear();
        sortTaskTime.addAll(mapSubTask.values());
    }


    @Override
    public void removeAllSubTask() {
        mapSubTask.clear();
        sortTaskTime.clear();
        for (Task task : mapTask.values()) {
            if (task.getStartTime() != null) {
                sortTaskTime.add(task);
            }
        }
        for (Epic epic : mapEpic.values()) {
            epic.getIdSubTask().clear();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public void removeAllEpic() {
        mapSubTask.clear();
        mapEpic.clear();
        for (Task task : getPrioritizedTasks()) {
            if (task.getTypeTask() != TypeTask.TASK)
                sortTaskTime.remove(task);
        }
    }

    @Override
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

    private void calculateTimeEpic(Epic epic) {
        if (epic == null) return;
        ArrayList<SubTask> subTasks = getSubTaskByIdEpic(epic);
        Duration sumDuration = Duration.ZERO;
        LocalDateTime startMinTask = epic.getStartTime();
        LocalDateTime endMaxTask = epic.getEndTime();

        for (SubTask subTask : subTasks) {
            sumDuration = sumDuration.plus(subTask.getDuration());
            if (startMinTask != null) {
                if (subTask.getStartTime().isBefore(startMinTask))
                    startMinTask = subTask.getStartTime();
            } else {
                startMinTask = subTask.getStartTime();
            }
            if (endMaxTask != null) {
                if (subTask.getEndTime().isBefore(endMaxTask))
                    endMaxTask = subTask.getEndTime();
            } else {
                endMaxTask = subTask.getEndTime();
            }
        }

        epic.setDuration(sumDuration);
        epic.setStartTime(startMinTask);
        epic.setEndTime(endMaxTask);
    }


    private void removeAllSubTaskByEpic(Epic epic) {
        ArrayList<Integer> listSubTask = epic.getIdSubTask();
        if (listSubTask.isEmpty()) return;
        for (Integer integer : listSubTask) {
            sortTaskTime.remove(mapSubTask.remove(integer));
            historyManager.remove(integer);
        }
        epic.setStatus(Status.NEW);
    }


    private Optional<Task> getOptionalTasks(int id, TypeTask typeTask) {
        Optional<Task> optionalTask;
        switch (typeTask) {
            case TASK:
                optionalTask = Optional.ofNullable(mapTask.get(id));
                break;
            case SUBTASK:
                optionalTask = Optional.ofNullable(mapSubTask.get(id));
                break;
            case EPIC:
                optionalTask = Optional.ofNullable(mapEpic.get(id));
                break;
            default:
                optionalTask = Optional.empty();
        }
        optionalTask.ifPresent(historyManager::add);
        return optionalTask;
    }

    private int getUin() {
        return uin++;
    }

    protected void setUin(int generatorId) {
        this.uin = generatorId;
    }
}
