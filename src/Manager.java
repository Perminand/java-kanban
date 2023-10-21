import com.bethecoder.ascii_table.ASCIITable;
import java.util.ArrayList;
public class Manager {
    static ArrayList<Task> listTask = new ArrayList<>();
    static ArrayList<Epic> listEpic = new ArrayList<>();
    static String[] listStatus = {"NEW", "IN_PROGRESS", "DONE"};
    private int uin = 1;
    public void createTask(Task task) {
        if (task == null) {
            return;
        }
        task.setUin(getUin());
        listTask.add(task);
    }
    public void createEpic(Epic epic) {
        if (epic == null) return;
        epic.setUin(getUin());
        epic.setStatus("NEW");
        for (int i = 0; i < epic.getListTask().size(); i++) {
            epic.getListTask().get(i).setUin(getUin());
        }
        epic.statusCalculation(listStatus);
        listEpic.add(epic);
    }
    public void createTaskByEpic(int idEpic, Task task) {
        if (task == null || idEpic < 0) return;
        for (Epic epic : listEpic) {
            if (epic.getUin() == idEpic) {
                epic.getListTask().add(task);
            }
            epic.statusCalculation(listStatus);
        }
    }
    public Epic getTaskByIdEpic(int id) {
        if (id < 0) return null;
        for (Epic list : listEpic) {
            if (list.getUin() == id) return list;
        }
        return null;
    }
    public void updateTask(Task task) {
        if (task == null) return;
        for (Task line : listTask) {
            if (line.getUin() == task.getUin()) {
                line.setNameTask(task.getNameTask());
                line.setDiscription(task.getDiscription());
                line.setStatus(task.getStatus());
                return;
            }
        }
        for (Epic epic : listEpic) {
            for (Task newTask : epic.getListTask()) {
                String taskStatus = task.getStatus();
                if (newTask.getUin() == task.getUin()) {
                    newTask.setNameTask(task.getNameTask());
                    newTask.setDiscription(task.getDiscription());
                    newTask.setStatus(taskStatus);
                    epic.statusCalculation(listStatus);
                }
            }

        }
    }
    public void updateEpic(Epic epic) {
        if (epic == null) return;
        for (Epic line : listEpic) {
            if (line.getUin() == epic.getUin()) {
                line.setNameTask(epic.getNameTask());
                line.setDiscription(epic.getDiscription());
                line.setStatus(epic.getStatus());
                line.setListTask(epic.getListTask());
                epic.statusCalculation(listStatus);
                return;
            }
        }
    }
    public Object getById(int id) {
        if (id < 0) return null;
        for (Task task : listTask) {
            if (task.getUin() == id) {
                return task;
            }
        }
        for (Epic line : listEpic) {
            if (line.getUin() == id) {
                return line;
            }
        }
        return null;
    }
    public void removeTask(int id) {
        if (id < 0) return;
        for (int i = 0; i < listTask.size(); i++) {
            if (listTask.get(i).getUin() == id) listTask.remove(i);
        }
        for (int i = 0; i < listEpic.size(); i++) {
            if (listEpic.get(i).getUin() == id) {
                listEpic.remove(i);
                return;
            }
            for (int j = 0; j < listEpic.get(i).getListTask().size(); j++) {
                if (listEpic.get(i).getListTask().get(j).getUin() == id) {
                    listEpic.get(i).getListTask().remove(j);
                    listEpic.get(i).statusCalculation(listStatus);
                }
            }
        }
    }
    public void printManager() {
        //Метод заполняет таблицу
        String[] tableHeaders = listStatus;
        int maxlineStatus = getMaxCoinStatus();//Максимальное количество строк в столбце
        String[][] str = new String[getMaxCoinStatus()][3];
        int[] coinStatus = {0, 0, 0};
        for (int i = 0; i < listTask.size(); i++) {
            if (listTask.get(i).getStatus().equals(listStatus[0])) {
                str[coinStatus[0]][0] = "id:" + listTask.get(i).getUin() + "|" + listTask.get(i).getNameTask() + "|" +
                        listTask.get(i).getDiscription() + "|";
                coinStatus[0]++;
            }
            if (listTask.get(i).getStatus().equals(listStatus[1])) {
                str[coinStatus[1]][1] = "id:" + listTask.get(i).getUin() + "|" + listTask.get(i).getNameTask() + "|" +
                        listTask.get(i).getDiscription() + "|";
                coinStatus[1]++;
            }
            if (listTask.get(i).getStatus().equals(listStatus[2])) {
                str[coinStatus[2]][2] = "id:" + listTask.get(i).getUin() + "|" + listTask.get(i).getNameTask() + "|" +
                        listTask.get(i).getDiscription() + "|";
                coinStatus[2]++;
            }
        }
        for (int i = 0; i < listEpic.size(); i++) {
            if (listEpic.get(i).getStatus().equals(listStatus[0])) {
                str[coinStatus[0]][0] = "==>id:" + listEpic.get(i).getUin() + "|" + listEpic.get(i).getNameTask() +
                        "|" + listEpic.get(i).getDiscription() + "|";
                if (!listEpic.get(i).getListTask().isEmpty()) {
                    str[coinStatus[0]][0] += "(";
                }
                for (int j = 0; j < listEpic.get(i).getListTask().size(); j++) {
                    str[coinStatus[0]][0] += "id:" + listEpic.get(i).getListTask().get(j).getUin() + "." +
                            listEpic.get(i).getListTask().get(j).getNameTask();
                    if (j == listEpic.get(i).getListTask().size() - 1) {
                        str[coinStatus[0]][0] += ")";
                    } else {
                        str[coinStatus[0]][0] += " |";
                    }
                }
                coinStatus[0]++;
            }
            if (listEpic.get(i).getStatus().equals(listStatus[1])) {
                str[coinStatus[1]][1] = "id:" + listEpic.get(i).getUin() + "|" + listEpic.get(i).getNameTask() +
                        "|" + listEpic.get(i).getDiscription() + "|";
                if (!listEpic.get(i).getListTask().isEmpty()) {
                    str[coinStatus[0]][0] += "(";
                }
                for (int j = 0; j < listEpic.get(i).getListTask().size(); j++) {
                    str[coinStatus[0]][0] += "id:" + listEpic.get(i).getListTask().get(j).getUin() + "." +
                            listEpic.get(i).getListTask().get(j).getNameTask();
                    if (j == listEpic.get(i).getListTask().size() - 1) {
                        str[coinStatus[0]][0] += ")";
                    } else {
                        str[coinStatus[0]][0] += " |";
                    }

                }
                coinStatus[1]++;
            }
            if (listEpic.get(i).getStatus().equals(listStatus[2])) {
                str[coinStatus[2]][2] = "id:" + listEpic.get(i).getUin() + "|" + listEpic.get(i).getNameTask() +
                        "|" + listEpic.get(i).getDiscription() + "|";
                if (!listEpic.get(i).getListTask().isEmpty()) {
                    str[coinStatus[0]][0] += "(";
                }
                for (int j = 0; j < listEpic.get(i).getListTask().size(); j++) {
                    str[coinStatus[0]][0] += "id:" + listEpic.get(i).getListTask().get(j).getUin() + "." +
                            listEpic.get(i).getListTask().get(j).getNameTask();
                    if (j == listEpic.get(i).getListTask().size() - 1) {
                        str[coinStatus[0]][0] += ")";
                    } else {
                        str[coinStatus[0]][0] += " |";
                    }
                }
                coinStatus[2]++;
            }
        }
        for (int i = 0; i < coinStatus.length; i++) {
            if (coinStatus[i] < maxlineStatus) {
                for (int j = coinStatus[i]; j < str.length; j++) {
                    str[j][i] = "";
                }
            }
        }
        System.out.println("Менеджер задач:");
        ASCIITable.getInstance().printTable(tableHeaders, str);
    }

    static int getMaxCoinStatus() {
        // Метод ищет максимальное количество строк в таблице
        int coin = 0;
        for (String headers : listStatus) {
            int i = 0;
            for (Task line : listTask) {
                if (line.getStatus().equals(headers)) {
                    i++;
                }
            }
            for (Task line : listEpic) {
                if (line.getStatus().equals(headers)) {
                    i++;
                }
            }
            if (i > coin) coin = i;
        }
        return coin;
    }
    public int getUin() {
        return uin++;
    }
}
