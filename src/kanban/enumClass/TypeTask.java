package kanban.enumClass;

public enum TypeTask {
    TASK("Задача"),
    SUBTASK("Подзадача"),
    EPIC("Эпик");
    final String title;

    TypeTask(String title) {
        this.title = title;
    }

}
