import com.bethecoder.ascii_table.ASCIITable;

import java.util.ArrayList;

public class Manager {
    static ArrayList<Task> listTask = new ArrayList<>();
    static ArrayList<Epic> listEpic = new ArrayList<>();
    static String[] listStatus = {"NEW", "IN_PROGRESS", "DONE"};
    private int uin = 1;

    public void create(Task task){
        listTask.add(task);

    }
    public void create(Epic epic){
        listEpic.add(epic);
    }
    public Object getById(int id) {
        for (int i = 0; i < listTask.size(); i++) {
            if(listTask.get(i).getUin()==id){
                return listTask.get(i);
            }

        }
        for(Epic line :listEpic){
            if(line.getUin()==id){
                return line;
            }
        }
        return null;
    }

    public void removeTask(int id) {
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
                    listEpic.get(i).listTask.remove(j);
                }
            }

        }
    }

    public void printManager() {
        //Метод заполняет таблицу
        String[] tableHeaders = listStatus;
        int maxlineStatus = getMaxCoinStatus();
        String[][] str = new String[maxlineStatus][3];
        int[] coinStatus = {0, 0, 0};
        for (int i = 0; i < listTask.size(); i++) {
            if (listTask.get(i).getStatus().equals(listStatus[0])) {
                str[i][0] = "id:" + listTask.get(i).getUin() + "|" + listTask.get(i).getNameTask() + "|" + listTask.get(i).getDiscription() + "|";
                coinStatus[0]++;
            }
            if (listTask.get(i).getStatus().equals(listStatus[1])) {
                str[i][1] = "id:" + listTask.get(i).getUin() + "|" + listTask.get(i).getNameTask() + "|" + listTask.get(i).getDiscription() + "|";
                coinStatus[1]++;
            }
            if (listTask.get(i).getStatus().equals(listStatus[2])) {
                str[i][2] = "id:" + listTask.get(i).getUin() + "|" + listTask.get(i).getNameTask() + "|" + listTask.get(i).getDiscription() + "|";
                coinStatus[2]++;
            }
        }
        for (int i = 0; i < listEpic.size(); i++) {
            if (listEpic.get(i).getStatus().equals(listStatus[0])) {
                str[coinStatus[0]][0] = "==>id:" + listEpic.get(i).getUin() + "|" + listEpic.get(i).getNameTask() + "|" + listEpic.get(i).getDiscription() + "|";
                if (!listEpic.get(i).getListTask().isEmpty()) {
                    str[coinStatus[0]][0] += "(";
                }
                for (int j = 0; j < listEpic.get(i).getListTask().size(); j++) {
                    str[coinStatus[0]][0] += "id:" + listEpic.get(i).listTask.get(j).getUin() + "." + listEpic.get(i).listTask.get(j).nameTask;
                    if (j == listEpic.get(i).getListTask().size() - 1) {
                        str[coinStatus[0]][0] += ")";
                    } else {
                        str[coinStatus[0]][0] += " |";
                    }
                }
                coinStatus[0]++;
            }
            if (listEpic.get(i).getStatus().equals(listStatus[1])) {
                str[coinStatus[1]][1] = "id:" + listEpic.get(i).getUin() + "|" + listEpic.get(i).getNameTask() + "|" + listEpic.get(i).getDiscription() + "|";
                coinStatus[1]++;
            }
            if (listEpic.get(i).getStatus().equals(listStatus[2])) {
                str[coinStatus[2]][2] = "id:" + listEpic.get(i).getUin() + "|" + listEpic.get(i).getNameTask() + "|" + listEpic.get(i).getDiscription() + "|";
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

    public Task CreateTask(String name, String discription) {
        listTask.add(new Task(name, discription, this.uin));
        for (int i = 0; i < listTask.size(); i++) {
            if (listTask.get(i).getUin() == uin) {
                uin++;
                return listTask.get(i);
            }
        }
        return null;
    }

    public Epic CreateEpic(String name, String discription, ArrayList<Task> Task) {
        listEpic.add(new Epic(name, discription, uin, Task));
        for (int i = 0; i < listEpic.size(); i++) {
            if (listEpic.get(i).getUin() == uin) {
                uin++;
                return listEpic.get(i);
            }
        }
        return null;
    }

    static int getMaxCoinStatus() {
        // Метод ищет максимальное количество строк в таблице
        int coin = 0;
        for (String headers : listStatus) {
            int i = 0;
            for (Task line : listTask) {
                if (line.status.equals(headers)) {
                    i++;
                }
            }
            for (Task line : listEpic) {
                if (line.status.equals(headers)) {
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
