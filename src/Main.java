import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.createTask(new Task("Задача1","Описание1"));
        manager.createTask(new Task("Задача2","Описание2"));
        manager.createTask(new Task("Задача3","Описание3"));
        manager.createEpic(new Epic("Эпик1","Описание1"));
        manager.createEpic(new Epic("Эпик2","Описание2"));
        manager.createSubTask(new SubTask("ПодЗадача1","Описание1",3));
        manager.createSubTask(new SubTask("ПодЗадача2","Описание2",3));
        manager.createSubTask(new SubTask("ПодЗадача3","Описание3",4));
        manager.updateSubTask(new SubTask("ПодЗадача3","Описание3",5,"DONE"));
        System.out.println(manager.getListEpic());
//        manager.removeAllSubTaskByEpic(3);
        System.out.println(manager.getListSubTask());
        System.out.println(manager.getListEpic());
//        Task task2 = new Task("Задача2", "Описание2");
//        Task task3;
//        manager.createTask(task1);
//        manager.createTask(task2);
//        ArrayList<Task> taskList = new ArrayList<>();
//        taskList.add(new Task("подЗадача1", "Описание1"));
//        taskList.add(new Task("подЗадача2", "Описание2"));
//        Epic epic1 = new Epic("Эпик1","Описание1",taskList);
//        task2= new Task("Задача3", "Описание3", 2);
////        task3 = new Task("Задача3", "Описание3", 2,"IN_PROGRESS");
//        epic1= new Epic("Эпик4","Описание4",3,taskList);
//        manager.createEpic(epic1);
//        System.out.println(manager.getTaskByIdEpic(3));
//
////        manager.updateTask(task3);
//        manager.updateEpic(epic1);
//
//
////        System.out.println("Task1:" + task1);
////        task2 = manager.CreateTask("Задача2", "Описание2");
////        System.out.println("Task2:" + task2);
////        Task task = new Task("Задание1", "Описание1", manager.getUin());
////        ArrayList<Task> arrayList = new ArrayList<>();
////        arrayList.add(task);
////        epic1 = manager.CreateEpic("Эпик1", "Описание3", arrayList);
////        System.out.println("epic1:" + epic1);
////        Object object = manager.getById(4);
////        System.out.println(object);
////        manager.create(task1);
////        manager.create(epic1);
////        System.out.println("Task:"+task2);
//       manager.printManager();


    }
}
