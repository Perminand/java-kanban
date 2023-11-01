package kanban.service;

import kanban.enumClass.Status;
import kanban.enumClass.TypeMenu;
import kanban.enumClass.TypeOperation;
import kanban.enumClass.TypeTask;
import kanban.model.*;

import java.util.*;

import static kanban.enumClass.TypeTask.*;

public class InMemoryTaskManager implements Manager {
    Scanner scanner = new Scanner(System.in);
    private final HashMap<Integer, Task> mapTask = new HashMap<>();
    private final HashMap<Integer, SubTask> mapSubTask = new HashMap<>();
    private final HashMap<Integer, Epic> mapEpic = new HashMap<>();
    private int uin = 0;
    private final List<Task> getHistory = new ArrayList<>();
    private final List<String> fullHistory = new ArrayList<>();

    @Override
    public void createTask(Task task) {
        if (task == null) return;
        int key = getUin();
        task.setUin(key);
        mapTask.put(key, task);
        fullHistory.add(TypeOperation.CREATE + " " + task);
        System.out.println("Операция выполнена успешно");
        System.out.println();
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
        fullHistory.add(TypeOperation.CREATE + " " + subTask);
        statusCalc(epic);
        System.out.println("Операция выполнена успешно");
        System.out.println();
    }

    @Override
    public void createEpic(Epic epic) {
        int key = getUin();
        epic.setUin(key);
        epic.setStatus(Status.NEW);
        mapEpic.put(key, epic);
        fullHistory.add(TypeOperation.CREATE + " " + epic);
        System.out.println("Операция выполнена успешно");
        System.out.println();
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
        if (mapTask.get(id) != null) {
            return mapTask.get(id);
        }
        return null;
    }

    @Override
    public SubTask getSubTask(int id) {
        if (mapSubTask.get(id) != null) {
            return mapSubTask.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpic(int id) {
        if (mapEpic.get(id) != null) {
            return mapEpic.get(id);
        }
        return null;
    }


    @Override
    public Task getFindById(int id) {
        if (id < 0) return null;
        if (mapTask.get(id) != null) return mapTask.get(id);
        if (mapEpic.get(id) != null) return mapEpic.get(id);
        if (mapSubTask.get(id) != null) return mapSubTask.get(id);
        return null;
    }

    @Override
    public void deleteById(int id) {
        if (id < 0) return;
        if (mapTask.get(id) != null) {
            mapTask.remove(id);
            System.out.println("Задача удалена");
            return;
        }
        if (mapEpic.get(id) != null) {
            Epic epic = mapEpic.get(id);
            removeAllSubTaskByEpic(epic);
            mapEpic.remove(id);
            System.out.println("Задача удалена");
        }
        if (mapSubTask.get(id) != null) {
            Epic epic = mapEpic.get(mapSubTask.get(id).getEpicId());
            mapSubTask.remove(id);
            epic.getIdSubTask().remove((Integer) id);
            System.out.println("Задача удалена");
            statusCalc(epic);
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
    private void removeAllNewTask(){
        removeAllTask();
        removeAllSubTask();
        removeAllEpic();
        System.out.println("Операция выполнена успешно");
    }


    public void NewTasks(TypeMenu typeMenu, TypeTask typeTask) {
        if (typeMenu == TypeMenu.CREATE) {
            createNewTask(typeTask);
        } else if (typeMenu == TypeMenu.UPDATE) {
            updateNewTask(typeTask);
        } else if (typeMenu == TypeMenu.GET) {
            getNewTask(typeTask);
        }else if (typeMenu == TypeMenu.REMOVE){
            deleteNewTask();
        }else if(typeMenu==TypeMenu.REMOVEALL){
            removeAllNewTask();
        }
    }
    private void deleteNewTask(){
        System.out.println("Введите id задачи:");
        String idTask = scanner.nextLine();
        deleteById(Integer.parseInt(idTask));
    }
    private void getNewTask(TypeTask typeTask) {
        System.out.println("введите id задачи:");
        String idTask = scanner.nextLine();
        Task task = null;
        if (typeTask == TypeTask.TASK) {
            task = getTask(Integer.parseInt(idTask));
            if (task == null) {
                System.out.println("Задача не найдена");
            } else {
                System.out.println(task);
                addTaskToHistory(task);
                addTaskToHistory(TypeOperation.GET + " " + task);
                System.out.println("Операция выполнена");
            }

        } else if (typeTask == TypeTask.SUBTASK) {
            task = getSubTask(Integer.parseInt(idTask));
            if (task == null) {
                System.out.println("Задача не найдена");
            } else {
                addTaskToHistory(task);
                addTaskToHistory(TypeOperation.GET + " " + task);
                System.out.println(task);
                System.out.println("Операция выполнена");
            }
        } else if (typeTask == TypeTask.EPIC) {
            task = getEpic(Integer.parseInt(idTask));
            if (task == null) {
                System.out.println("Задача не найдена");
            } else {
                addTaskToHistory(task);
                addTaskToHistory(TypeOperation.GET + " " + task);
                System.out.println(task);
                System.out.println("Операция выполнена");
            }
        }
    }

    private void createNewTask(TypeTask typeTask) {
        System.out.print("Введите название:");
        String nameTask = scanner.nextLine();
        System.out.print("Введите описание:");
        String description = scanner.nextLine();
        if (typeTask == TASK) {
            createTask(new Task(nameTask, description));
        } else if (typeTask == TypeTask.SUBTASK) {
            ArrayList<Integer> listIdEpic = new ArrayList<>();
            for (Epic epic : mapEpic.values()) {
                listIdEpic.add(epic.getUin());
            }
            System.out.println("Доступные эпики:");
            for (Integer i : listIdEpic) {
                System.out.println("id=" + i + " Название:" + getEpic(i).getNameTask() + " Описание:"
                        + getEpic(i).getNameTask());
            }
            System.out.print("Введите id эпика:");
            String idEpic = scanner.nextLine();
            for (Integer id : listIdEpic) {
                if (id == Integer.parseInt(idEpic)) {
                    createSubTask(new SubTask(nameTask, description, Integer.parseInt(idEpic)));
                } else {
                    System.out.println("Нет эпика с указанным id.");
                }
            }
        } else {
            createEpic(new Epic(nameTask, description));
        }
    }

    private void updateNewTask(TypeTask typeTask) {
        System.out.print("Введите название:");
        String nameTask = scanner.nextLine();
        System.out.print("Введите описание:");
        String description = scanner.nextLine();
        if (typeTask == TASK) {
            System.out.println("Введите id задачи");
            int uin = Integer.parseInt(scanner.nextLine());
            if(getTask(uin)==null) {
                System.out.println("Нет такой задачи");
                return;
            }
            System.out.println("Статус задачи поменялся?");
            System.out.println("1.Да");
            System.out.println("2.Нет");
            System.out.print("Введите команду:");
            String status = scanner.nextLine();
            switch (status) {
                case "1":
                    System.out.println("Выберите новый статус");
                    System.out.println("1." + Status.NEW);
                    System.out.println("2." + Status.IN_PROGRESS);
                    System.out.println("3." + Status.DONE);
                    String choice = scanner.nextLine();
                    Status newStatus = null;
                    switch (choice) {
                        case "1":
                            newStatus = Status.NEW;
                            break;
                        case "2":
                            newStatus = Status.IN_PROGRESS;
                            break;
                        case "3":
                            newStatus = Status.DONE;
                            break;
                    }
                    updateTask(new Task(nameTask, description, uin, newStatus));
                    break;
                case "2":
                    Task task = getTask(uin);
                    newStatus = task.getStatus();
                    updateTask(new Task(nameTask, description, uin, newStatus));
                    break;
            }
        } else if (typeTask == TypeTask.SUBTASK) {
            System.out.print("Введите id подзадачи:");
            String id = scanner.nextLine();
            SubTask subTask = mapSubTask.get(Integer.parseInt(id));
            int epicId = subTask.getEpicId();
            updateSubTask(new SubTask(nameTask, description, epicId));
        } else {
            System.out.print("Введите id эпика':");
            String id = scanner.nextLine();
            updateEpic(new Epic(nameTask, description, Integer.parseInt(id)));
        }
    }

    private void addTaskToHistory(Task task) {
        if (getHistory.size() > 9) getHistory.remove(0);
        getHistory.add(task);
    }

    private void addTaskToHistory(String string) {
        if (fullHistory.size() > 9) fullHistory.remove(0);
        fullHistory.add(string);
    }


    public List<Task> getHistory() {
        return getHistory;
    }

    public List<String> fullHistory() {
        return fullHistory;
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

    private void findByIdForUpdate(int id) {

    }


    private void removeAllSubTaskByEpic(Epic epic) {
        ArrayList<Integer> listSubTask = epic.getIdSubTask();
        if (listSubTask.isEmpty()) return;
        for (int i = 0; i < listSubTask.size(); i++) {
            mapSubTask.remove(i);
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
