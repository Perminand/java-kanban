package kanban.enumClass;

public enum TypeTask {
    TASK("Задача"),
    SUBTASK("Подзадача"),
    EPIC("Эпик"),
    ALL("Все");

    final String title;

    TypeTask(String title){
        this.title=title;
    }

    public String getTitle() {
        return title;
    }
}
