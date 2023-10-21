import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.createTask(new Task("Задача1", "Описание1"));
        manager.createTask(new Task("Задача2", "Описание2"));
        ArrayList<SubTask> arrayList = new ArrayList<>();
        arrayList.add(new SubTask("ПодЗадача1", "Описание1"));
        arrayList.add(new SubTask("ПодЗадача2", "Описание2"));
        manager.createEpic(new Epic("Эпик1", "Описание1"), arrayList);
        arrayList.clear();
        arrayList.add(new SubTask("ПодЗадача3", "Описание3"));
        manager.createEpic(new Epic("Эпик2", "Описание2"), arrayList);
        System.out.println(manager.getListEpic());
        System.out.println(manager.getListTask());
        manager.updateTask(new Task("ИзмененнаяЗадача1", "ИзмененноеОписание1", 0, "DONE"));
        System.out.println(manager.getListEpic());
        manager.updateSubTask(new SubTask("ПодЗадача3", "Описание3", 3, "DONE"));
        System.out.println(manager.getListSubTask());
        System.out.println(manager.getListEpic());
        manager.remove(3);
        System.out.println("---------------------------------------");
        System.out.println(manager.getListTask());
        System.out.println(manager.getListSubTask());
        System.out.println(manager.getListEpic());
    }
}