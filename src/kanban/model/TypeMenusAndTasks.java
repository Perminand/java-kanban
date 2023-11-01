package kanban.model;

import kanban.enumClass.TypeMenu;
import kanban.enumClass.TypeTask;

public class TypeMenusAndTasks {
    private final TypeMenu typeMenu;
    private final TypeTask typeTask;

    public TypeMenusAndTasks(TypeMenu typeMenu, TypeTask typeTask){
        this.typeMenu=typeMenu;
        this.typeTask=typeTask;
    }
    public TypeMenu getTypeMenu() {
        return typeMenu;
    }

    public TypeTask getTypeTask() {
        return typeTask;
    }
}
