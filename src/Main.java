import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1;
        Task task2;
        Epic epic1;
        task1 = manager.CreateTask("Задача1", "Описание1");
        System.out.println("Task1:" + task1);
        task2 = manager.CreateTask("Задача2", "Описание2");
        System.out.println("Task2:" + task2);
        Task task = new Task("Задание1", "Описание1", manager.getUin());
        ArrayList<Task> arrayList = new ArrayList<>();
        arrayList.add(task);
        epic1 = manager.CreateEpic("Эпик1", "Описание3", arrayList);
        System.out.println("epic1:" + epic1);
        Object object = manager.getById(4);
        System.out.println(object);
        manager.create(task1);
        manager.create(epic1);
        System.out.println("Task:"+task2);
       manager.printManager();


    }
}
