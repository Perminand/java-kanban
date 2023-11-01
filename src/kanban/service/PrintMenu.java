package kanban.service;

import kanban.enumClass.TypeMenu;
import kanban.enumClass.TypeTask;
import kanban.model.TypeMenusAndTasks;

import java.util.HashMap;
import java.util.Scanner;

public class PrintMenu {
    public static void mainPrintMenu() {
        System.out.println("Выберите пункт меню:");
        System.out.println("1. Создать задачи");
        System.out.println("2. Получить задачи");
        System.out.println("3. Обновить задачи");
        System.out.println("4. Удалить задачи");
        System.out.println("5. Просмотр истории");
        System.out.println("0. Выход");
        System.out.print("Введите команду:");

    }

    public static void printMenuSubTask(Scanner scanner, InMemoryTaskTaskManager manager, TypeMenu typeMenu) {
        int i = 1;
        String command;
        HashMap<Integer, TypeMenusAndTasks> tasksMenu;
        System.out.println("*******************************");
        System.out.println("Меню " + typeMenu.getTitle().toLowerCase());
        System.out.println("Выберите тип задачи:");
        tasksMenu = new HashMap<>();
        if (typeMenu == TypeMenu.CREATE) {
            tasksMenu.put(i++, new TypeMenusAndTasks(typeMenu, TypeTask.TASK));
            if (!manager.getMapEpic().isEmpty()) {
                tasksMenu.put(i++, new TypeMenusAndTasks(typeMenu, TypeTask.SUBTASK));
            }
            tasksMenu.put(i++, new TypeMenusAndTasks(typeMenu, TypeTask.EPIC));
        }
        if (typeMenu == TypeMenu.GET || typeMenu == TypeMenu.UPDATE || typeMenu == TypeMenu.REMOVE) {
            if (!manager.getMapTask().isEmpty()) {
                tasksMenu.put(i++, new TypeMenusAndTasks(typeMenu, TypeTask.TASK));
            }
            if (!manager.getMapEpic().isEmpty()) {
                if (!manager.getMapSubTask().isEmpty()) {
                    tasksMenu.put(i++, new TypeMenusAndTasks(typeMenu, TypeTask.SUBTASK));
                }
                tasksMenu.put(i++, new TypeMenusAndTasks(typeMenu, TypeTask.EPIC));
            }
            if (typeMenu == TypeMenu.REMOVE) {
                tasksMenu.put(i, new TypeMenusAndTasks(TypeMenu.REMOVEALL, TypeTask.ALL));
            }
        }
        if (tasksMenu.isEmpty()) {
            System.out.println("Нет созданных задач.");
            return;
        }
        for (int j = 1; j <= tasksMenu.size(); j++) {
            System.out.println(j + "." + " " + tasksMenu.get(j).getTypeTask().getTitle());
        }
        System.out.print("Введите команду:");
        command = scanner.nextLine();
        int commandInt = Integer.parseInt(command);
        if (tasksMenu.get(commandInt) != null) {
            manager.NewTasks(tasksMenu.get(commandInt).getTypeMenu(), tasksMenu.get(commandInt).getTypeTask());
        } else {
            System.out.println("Команда не найдена");
        }
    }
}
