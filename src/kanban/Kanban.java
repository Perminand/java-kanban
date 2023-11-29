package kanban;

import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.service.Managers;
import kanban.service.TaskManager;


public class Kanban {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        System.out.println("Создаем две задачи:");
        manager.createTask(new Task("Задача1", "Описание1"));
        manager.createTask(new Task("Задача2", "Описание2"));
        System.out.println("Создаем эпик");
        int idEpic = manager.createEpic(new Epic("Эпик1", "Описание1"));
        int idEpic2 = manager.createEpic(new Epic("Эпик1", "Эпик без подзадач"));
        System.out.println("Создаем три подзадачи:");
        manager.createSubTask(new SubTask("Подзадача1", "Описание подзадачи1", idEpic));
        manager.createSubTask(new SubTask("Подзадача2", "Описание подзадачи2", idEpic));
        manager.createSubTask(new SubTask("Подзадача3", "Описание подзадачи3", idEpic));

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

        System.out.println("Проверяем историю:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        manager.deleteById(2);
        System.out.println("Проверяем историю:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
