import service.Manager;
import model.*;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        System.out.println("Создаем две задачи:");
        manager.createTask(new Task("Задача1", "Описание1"));
        manager.createTask(new Task("Задача2", "Описание2"));
        System.out.println(manager.getMapTask());
        System.out.println("Создаем один эпик получаем его ИД:");
        int idEpic = manager.createEpic(new Epic("Эпик1", "Описание1"));
        System.out.println(manager.getMapEpic());
        System.out.println("Создаем две подзадачи по ИД эпика:");
        manager.createSubTask(new SubTask("ПодЗадача1", "Описание1", idEpic));
        manager.createSubTask(new SubTask("ПодЗадача2", "Описание2", idEpic));
        System.out.println(manager.getMapSubTask());
        System.out.println("Создаем один эпик получаем его ИД:");
        idEpic = manager.createEpic(new Epic("Эпик2", "Описание2"));
        System.out.println(manager.getMapEpic());
        System.out.println("Создаем одну подзадачу по ИД эпика:");
        manager.createSubTask(new SubTask("ПодЗадача2", "Описание2", idEpic));
        System.out.println(manager.getMapSubTask());
        System.out.println("Изменяем эпик:");
        manager.updateEpic(new Epic("Изменненый эпик1","Измененное расписание1",idEpic));
        System.out.println(manager.getMapEpic());
        System.out.println("Изменяем задачу:");
        manager.updateTask(new Task("ИзмененнаяЗадача1", "ИзмененноеОписание1", 0, "DONE"));
        System.out.println(manager.getMapEpic());
        manager.updateSubTask(new SubTask("ИзмененнаяПодЗадача3", "ИзмененнаяОписание3", 3, "DONE"));
        System.out.println(manager.getMapSubTask());
        System.out.println(manager.getMapEpic());
        System.out.println("Удаляем по ИД:");
        manager.deleteById(3);
        System.out.println("---------------------------------------");
        System.out.println("Получаем по ИД:");
        System.out.println(manager.getById(1));
        System.out.println("Удаляем все задачи:");
        manager.removeAllTask();
        System.out.println(manager.getMapTask());
        System.out.println("Удаляем все подзадачи эпика по ИД:");
        manager.removeAllSubTaskByEpic(idEpic);
        System.out.println(manager.getMapSubTask());
        System.out.println("Удаляем все подзадачи");
        manager.removeAllSubTask();
        System.out.println(manager.getMapSubTask());
        System.out.println("Удаляем все эпики");
        manager.removeAllEpic();
        manager.getMapEpic();
        System.out.println(manager.getMapEpic());
    }
}