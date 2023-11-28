package kanban;

import kanban.model.Epic;
import kanban.model.Node;
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
        manager.createEpic(new Epic("Эпик1", "Описание1"));
        System.out.println("Создаем три подзадачи:");
        manager.createSubTask(new SubTask("Подзадача1", "Описание подзадачи1", 2));
        manager.createSubTask(new SubTask("Подзадача2", "Описание подзадачи2", 2));
        manager.createSubTask(new SubTask("Подзадача3", "Описание подзадачи3", 2));

        System.out.println("Получаем Task");
        System.out.println(manager.getTask(0));
        System.out.println(manager.getEpic(2));
        System.out.println(manager.getTask(1));
        System.out.println(manager.getTask(0));
        System.out.println(manager.getTask(0));
        System.out.println(manager.getSubTask(3));
        System.out.println(manager.getSubTask(4));
        System.out.println(manager.getSubTask(5));

        System.out.println("Проверяем историю:");
        for (Node<Task> node : manager.getHistory()) {
            System.out.println(node);
        }
        manager.deleteById(2);
        System.out.println("Проверяем историю:");
        for (Node<Task> node : manager.getHistory()) {
            System.out.println(node);
        }
    }
}
