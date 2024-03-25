package kanban.service;

import kanban.exceptions.ManagerSaveException;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.utils.CSVTaskFormat;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
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
                if (line.isEmpty()) {//Поменял Equals("")
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


    @Override
    public int createTask(Task task) {
        int key = super.createTask(task);
        save();
        return key;
    }

    @Override
    public int createSubTask(SubTask subTask) {
        int key = super.createSubTask(subTask);
        if (!(key < 0)) save();
        return key;
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
    public void deleteById(int id){
        super.deleteById(id);
        save();
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
    protected void save() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            fileWriter.write("id,type,name,status,description,epic,startDate,duration");
            fileWriter.newLine();
            for (Task task : super.getTasks()) {
                fileWriter.write(CSVTaskFormat.toString(task));
                fileWriter.newLine();
            }
            for (Epic task : super.getEpics()) {
                fileWriter.write(CSVTaskFormat.toString(task));
                fileWriter.newLine();
            }
            for (SubTask task : super.getSubTasks()) {
                fileWriter.write(CSVTaskFormat.toString(task));
                fileWriter.newLine();
            }
            fileWriter.newLine();
            fileWriter.write(CSVTaskFormat.historyToString(super.historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Файл не найден");
        }
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

}
