package kanban;

import kanban.enumClass.Status;
import kanban.model.Epic;
import kanban.model.SubTask;
import kanban.model.Task;
import kanban.service.Managers;
import kanban.service.TaskManager;


public class Kanban {
    public static void main(String[] args) {
        TaskManager manager= Managers.getDefault();
        System.out.println("Создаем десять задачи:");
        manager.createTask(new Task("Задача1", "Описание1"));
        manager.createTask(new Task("Задача2", "Описание2"));
        manager.createTask(new Task("Задача3", "Описание3"));
        manager.createTask(new Task("Задача4", "Описание4"));
        manager.createTask(new Task("Задача5", "Описание5"));
        manager.createTask(new Task("Задача6", "Описание6"));
        manager.createTask(new Task("Задача7", "Описание7"));
        manager.createTask(new Task("Задача8", "Описание8"));
        manager.createTask(new Task("Задача9", "Описание9"));
        manager.createTask(new Task("Задача10", "Описание10"));
        for (Task task : manager.getMapTask()) {
            System.out.println(task);
        }
        System.out.println("Создаем один эпик получаем его ИД:");
        int idEpic = manager.createEpic(new Epic("Эпик1", "Описание1"));
        int idEpicForReplaces = idEpic;
        for (Task task : manager.getMapEpic()) {
            System.out.println(task);
        }
        System.out.println("Создаем две подзадачи по ИД эпика:");
        manager.createSubTask(new SubTask("ПодЗадача1", "Описание1", idEpic));
        manager.createSubTask(new SubTask("ПодЗадача2", "Описание2", idEpic));
        for (Task task : manager.getMapSubTask()) {
            System.out.println(task);
        }
        System.out.println("Создаем второй эпик получаем его ИД:");
        idEpic = manager.createEpic(new Epic("Эпик2", "Описание2"));
        for (Task task : manager.getMapEpic()) {
            System.out.println(task);
        }
        System.out.println("Создаем две подзадачи по ИД эпика:");
        manager.createSubTask(new SubTask("ПодЗадача3", "Описание3", idEpic));
        manager.createSubTask(new SubTask("ПодЗадача4", "Описание4", idEpic));
        for (Task task : manager.getMapSubTask()) {
            System.out.println(task);
        }
        System.out.println("************************************************************************");
        System.out.println("Список Epic");
        for (Task task : manager.getMapEpic()) {
            System.out.println(task);
        }
        System.out.println("Список SubTask");
        for (Task task : manager.getMapSubTask()) {
            System.out.println(task);
        }
        System.out.println("Список Task");
        for (Task task : manager.getMapTask()) {
            System.out.println(task);
        }
        System.out.println("***********************************************************************************");
        System.out.println("Изменяем эпик:");
        manager.updateEpic(new Epic("Измененный эпик1", "Измененное расписание1", idEpicForReplaces));
        System.out.println("Список эпик");
        for (Task task : manager.getMapEpic()) {
            System.out.println(task);
        }
        System.out.println("Изменяем подзадачу");
        manager.updateSubTask(new SubTask("ИзмененнаяПодЗадача3", "ИзмененнаяОписание3", 11, Status.DONE));
        System.out.println("Список подзадач:");
        for (Task task : manager.getMapSubTask()) {
            System.out.println(task);
        }
        System.out.println("Изменяем задачу:");
        manager.updateTask(new Task("ИзмененнаяЗадача1", "ИзмененноеОписание1", 0,
                Status.DONE));
        System.out.println("Список задач:");
        for (Task task : manager.getMapTask()) {
            System.out.println(task);
        }
        System.out.println("Создание закончено.");
        System.out.println("*****************************************************************************************");
        System.out.println("Запросы к задачам");

        for (int i = 0; i < manager.getMapTask().size(); i++) {
            System.out.println("Запрос к задаче " + i);// не буду выводить задачу на экран и так много.
            manager.getTask(i);
            System.out.println("Запрос выполнен");
        }
        for (int i = 0; i < manager.getMapSubTask().size(); i++) {
            System.out.println("Запрос к подзадаче " + i);
            manager.getSubTask(manager.getMapSubTask().get(i).getUin());
            System.out.println("Запрос выполнен");
        }
        for (int i = 0; i < manager.getMapEpic().size(); i++) {
            System.out.println("Запрос к эпику " + i);
            manager.getEpic(manager.getMapEpic().get(i).getUin());
            System.out.println("Запрос выполнен");
        }
        System.out.println("Смотрим историю запросов:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("*****************************************************************************");
        System.out.println("Удаление:");
        System.out.println("Удаляем по ИД:");
        manager.deleteById(3);
        System.out.println("---------------------------------------");
        System.out.println("Получаем по ИД:");
        System.out.println(manager.getById(1));
        System.out.println("Удаляем все задачи:");
        manager.removeAllTask();
        System.out.println(manager.getMapTask());
        System.out.println("Удаляем все подзадачи эпика по ИД:");
        System.out.println(manager.getMapSubTask());
        System.out.println("Удаляем все подзадачи");
        manager.removeAllSubTask();
        System.out.println(manager.getMapSubTask());
        System.out.println("Удаляем все эпики");
        manager.removeAllEpic();
        manager.getMapEpic();
        System.out.println(manager.getMapEpic());
        System.out.println("*****************************************************************");
        System.out.println("После удаления смотрим историю");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}

    
    
