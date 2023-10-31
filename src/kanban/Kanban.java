package kanban;

import kanban.service.InMemoryTaskManager;
import kanban.model.*;

import java.util.Scanner;

public class Kanban {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        System.out.println("Менеджер задач:");
        Scanner scanner = new Scanner(System.in);
        while (true){
            printMenu();
            int command = Integer.parseInt(scanner.nextLine());
            switch (command){
                case 1:
                    printMenuCreateTask();
            }
        }

//        for (int i = 0; i < 17; i++) {
//            manager.createSubTask(new SubTask("Задача" + i, "Описание" + i));
//        }
//        for (int i = 0; i < manager.getMapSubTask().size(); i++) {
//            System.out.println(manager.getSubTask(i));
//        }
//        System.out.println("-------------------------------------------");
//        for (int i = 0; i < manager.getHistory().size(); i++) {
//            System.out.println(manager.getHistory().get(i));
//        }
//        System.out.println(manager.getMapTask());
//        System.out.println("Создаем один эпик получаем его ИД:");
//        int idEpic = manager.createEpic(new Epic("Эпик1", "Описание1"));
//        System.out.println(manager.getMapEpic());
//        System.out.println("Создаем две подзадачи по ИД эпика:");
//        manager.createSubTask(new SubTask("ПодЗадача1", "Описание1", idEpic));
//        manager.createSubTask(new SubTask("ПодЗадача2", "Описание2", idEpic));
//        System.out.println(manager.getMapSubTask());
//        System.out.println("Создаем один эпик получаем его ИД:");
//        idEpic = manager.createEpic(new Epic("Эпик2", "Описание2"));
//        System.out.println(manager.getMapEpic());
//        System.out.println("Создаем одну подзадачу по ИД эпика:");
//        manager.createSubTask(new SubTask("ПодЗадача2", "Описание2", idEpic));
//        System.out.println(manager.getMapSubTask());
//        System.out.println("Изменяем эпик:");
//        manager.updateEpic(new Epic("Измененный эпик1","Измененное расписание1",idEpic));
//        System.out.println(manager.getMapEpic());
//        System.out.println("Изменяем задачу:");
//        manager.updateTask(new Task("ИзмененнаяЗадача1", "ИзмененноеОписание1", 0, Status.DONE));
//        System.out.println(manager.getMapEpic());
//        manager.updateSubTask(new SubTask("ИзмененнаяПодЗадача3", "ИзмененнаяОписание3", 3, Status.DONE));
//        System.out.println(manager.getMapSubTask());
//        System.out.println(manager.getMapEpic());
//        System.out.println("Удаляем по ИД:");
//        manager.deleteById(3);
//        System.out.println("---------------------------------------");
//        System.out.println("Получаем по ИД:");
//        System.out.println(manager.getFindById(1));
//        System.out.println("Удаляем все задачи:");
//        manager.removeAllTask();
//        System.out.println(manager.getMapTask());
//        System.out.println("Удаляем все подзадачи эпика по ИД:");
//        System.out.println(manager.getMapSubTask());
//        System.out.println("Удаляем все подзадачи");
//        manager.removeAllSubTask();
//        System.out.println(manager.getMapSubTask());
//        System.out.println("Удаляем все эпики");
//        manager.removeAllEpic();
//        manager.getMapEpic();
//        System.out.println(manager.getMapEpic());
    }

    private static void printMenu() {
        System.out.println("Выберите пункт меню:");
        System.out.println("1. Создать задачу:");

    }
    private static void printMenuCreateTask(){
        System.out.println("Выберите тип задачи:");
        System.out.println("1.Создать задачу:");
        System.out.println("2.Создать подзадачу:");
        System.out.println("3.Создать эпик:");
    }
}