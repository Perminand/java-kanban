package kanban.service;

import kanban.enumClass.Status;
import kanban.exceptions.ManagerSaveException;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.utils.CSVTaskFormat;

import java.io.*;
import java.time.Duration;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }
    static Duration duration = Duration.ofMinutes(60);

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        System.out.println("Создаем две задачи:");
        manager.createTask(new Task("Задача1", "Описание1", duration));
        manager.createTask(new Task("Задача2", "Описание2", duration));
        System.out.println("Создаем эпик");
        int idEpic = manager.createEpic(new Epic("Эпик1", "Описание1"));
        int idEpic2 = manager.createEpic(new Epic("Эпик1", "Эпик без подзадач"));
        System.out.println("Создаем три подзадачи:");
        manager.createSubTask(new SubTask("Подзадача1", "Описание подзадачи1", idEpic,duration));
        manager.createSubTask(new SubTask("Подзадача2", "Описание подзадачи2", idEpic,duration));
        manager.createSubTask(new SubTask("Подзадача3", "Описание подзадачи3", idEpic,duration));



        System.out.println("Получаем Task");
        System.out.println(manager.getTask(0));
        System.out.println(manager.getEpic(idEpic));
        System.out.println(manager.getTask(1));
        System.out.println(manager.getTask(0));
        System.out.println(manager.getTask(0));
        System.out.println(manager.getSubTask(3));
        System.out.println(manager.getSubTask(4));
        System.out.println(manager.getSubTask(5));
        System.out.println(manager.getEpic(idEpic2));
        manager.getSubTask(4).setStatus(Status.DONE);
        manager.getSubTask(5).setStatus(Status.DONE);
        manager.getSubTask(6).setStatus(Status.DONE);
        System.out.println("*****************Первый менеджер*****************");
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubTasks());
        System.out.println(manager.getEpics());
        System.out.println();
        System.out.println("История");
        System.out.println(manager.getHistory());

        TaskManager newManager = FileBackedTasksManager.loadFromFile(new File("./resources/Tasks.csv"));
        System.out.println("*******************Новый менеджер*********************");
        System.out.println(newManager.getTasks());
        System.out.println(newManager.getSubTasks());
        System.out.println(newManager.getEpics());
        System.out.println();
        System.out.println("История");
        System.out.println(newManager.getHistory());
        System.out.println(manager.getPrioritizedTasks());
        


    }

    public static FileBackedTasksManager loadFromFile(File file) {
        final FileBackedTasksManager tasksManager = new FileBackedTasksManager(file);
        int generatorId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int i = 0;
            boolean isHistory = false;
            while (br.ready()) {
                String line = br.readLine();
                if (i < 1) {
                    i++;
                    continue;
                }
                if (line.equals("")) {
                    isHistory = true;
                    i++;
                    continue;
                }

                if (!isHistory) {
                    Task task = CSVTaskFormat.stringFromTask(line);
                    if (task != null) {
                        if (generatorId < task.getUin()) {
                            generatorId = task.getUin();
                        }
                        tasksManager.addTask(task);
                        i++;
                    }
                } else {
                    List<Integer> idTask = CSVTaskFormat.historyFromString(line);
                    for (Integer id : idTask) {
                        tasksManager.getById(id);
                    }
                }

            }
        } catch (IOException e) {
            throw new ManagerSaveException("Файл не найден");
        }
        tasksManager.setUin(generatorId);
        return tasksManager;
    }

    private void addTask(Task task) {
        switch (task.getTypeTask()) {
            case TASK:
                mapTask.put(task.getUin(), task);
                break;
            case SUBTASK:
                SubTask subTask = (SubTask) task;
                int idEpic = subTask.getEpicId();
                Epic epic = mapEpic.get(idEpic);
                ArrayList<Integer> listIdSubTask = epic.getIdSubTask();
                if (listIdSubTask == null)
                    listIdSubTask = new ArrayList<>();
                listIdSubTask.add(subTask.getUin());
                mapEpic.get(idEpic).setIdSubTask(listIdSubTask);
                mapSubTask.put(subTask.getUin(), subTask);
                break;
            case EPIC:
                mapEpic.put(task.getUin(), (Epic) task);
                break;
        }

    }

    protected void save() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            final ArrayList<Task> listTask = super.getTasks();
            final ArrayList<SubTask> listSubTask = super.getSubTasks();
            final ArrayList<Epic> listEpic = super.getEpics();
            fileWriter.write("id,type,name,status,description,epic,startDate,duration");
            fileWriter.newLine();
            for (Task task : listTask) {
                fileWriter.write(CSVTaskFormat.toString(task));
                fileWriter.newLine();
            }
            for (Epic task : listEpic) {
                fileWriter.write(CSVTaskFormat.toString(task));
                fileWriter.newLine();
            }
            for (SubTask task : listSubTask) {
                fileWriter.write(CSVTaskFormat.toString(task));
                fileWriter.newLine();
            }
            fileWriter.newLine();
            fileWriter.write(CSVTaskFormat.historyToString(super.historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Файл не найден");
        }
    }

    @Override
    public int createTask(Task task) {
        int key = super.createTask(task);
        save();
        return key;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        int key = super.createSubTask(subTask);
        save();
        return key;
    }
    @Override
    public LinkedHashMap<Integer,Task> getPrioritizedTasks(){
        return super.getPrioritizedTasks();

    }

    @Override
    public int createEpic(Epic epic) {
        int i = super.createEpic(epic);
        save();
        return i;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllSubTask() {
        super.removeAllSubTask();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public ArrayList<SubTask> getSubTaskByIdEpic(Epic epic) {
        ArrayList<SubTask> listSubTask = super.getSubTaskByIdEpic(epic);
        save();
        return listSubTask;
    }

}
