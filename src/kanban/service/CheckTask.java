package kanban.service;

import kanban.enumClass.TypeOperation;
import kanban.enumClass.TypeTask;
import kanban.model.SubTask;
import kanban.model.Task;

import java.util.Scanner;

public class CheckTask<T extends Task> {
    public void createTask(int typeTask, TypeOperation typeOperation) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название:");
        String nameTask = scanner.nextLine();
        System.out.println("Введите описание:");
        String description = scanner.nextLine();

    }
}
