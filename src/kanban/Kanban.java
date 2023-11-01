package kanban;

import kanban.enumClass.TypeMenu;
import kanban.service.InMemoryTaskTaskManager;
import kanban.service.PrintMenu;

import java.util.Scanner;

public class Kanban {
    public static void main(String[] args) {
        InMemoryTaskTaskManager manager = new InMemoryTaskTaskManager();
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
                case "5":
                    System.out.println("Выберите историю");
                    System.out.println("1.Запросов просмотра");
                    System.out.println("2.Полную историю");
                    System.out.print("Введите команду:");
                    command = scanner.nextLine();
                    switch (command) {
                        case "1":
                            System.out.println(manager.getHistoryManager().getHistory());
                            break;
                        case "2":
                            System.out.println(manager.getHistoryManager().fullHistory());
                            break;
                        default:
                            System.out.println("Нет такой команды");
                    }
                    break;
                case "0":
                    System.out.println("Вы вышли");
                    return;
                default:
                    System.out.println("Нет такой команды");
            }

        }
    }
}

    
    
