package kanban.service;

import kanban.interfaces.TaskManager;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;

public class Managers {
    public TaskManager getDefault() {
        return new TaskManager() {
            @Override
            public void createTask(Task task) {
            }

            @Override
            public void createSubTask(SubTask subTask) {
            }

            @Override
            public void createEpic(Epic epic) {
            }

            @Override
            public void updateSubTask(SubTask subTask) {
            }

            @Override
            public void updateEpic(Epic epic) {
            }

            @Override
            public void updateTask(Task task) {
            }

            @Override
            public Task getTask(int id) {
                return null;
            }

            @Override
            public SubTask getSubTask(int id) {
                return null;
            }

            @Override
            public Epic getEpic(int id) {
                return null;
            }

            @Override
            public void deleteById(int id) {
            }

            @Override
            public void removeAllTask() {
            }

            @Override
            public void removeAllSubTask() {
            }

            @Override
            public void removeAllEpic() {
           }
        };
    }

    public InMemoryHistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
