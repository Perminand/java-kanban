package kanban;

import kanban.enumClass.TypeMenu;
import kanban.service.InMemoryTaskManager;
import kanban.model.*;
import kanban.service.PrintMenu;

import java.util.Scanner;

public class Kanban {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        System.out.println("Менеджер задач:");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            PrintMenu.mainPrintMenu();
            String command = scanner.nextLine();
            switch (command) {
                case "1":
                    PrintMenu.printMenuSubTask(scanner, manager, TypeMenu.CREATE);
                    break;

                case "2":
                    PrintMenu.printMenuSubTask(scanner, manager, TypeMenu.GET);
                    break;
                case "3":
                    PrintMenu.printMenuSubTask(scanner, manager, TypeMenu.UPDATE);
                    break;
                case "4":
                    PrintMenu.printMenuSubTask(scanner, manager, TypeMenu.REMOVE);
                    break;
                case "0":
                    System.out.println("Вы вышли");
                    return;
                default:
                    System.out.println("Нет такой команды");
            }
            System.out.println("---------------------------------------------");
            for (Task task : manager.getHistory()) {
                System.out.println(task);
            }
        }

    }


}

    
    
