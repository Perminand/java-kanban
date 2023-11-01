package kanban.enumClass;

public enum TypeMenu {
    CREATE ("Добавить"),
    GET ("Получить"),
    UPDATE ("Обновить"),
    REMOVE("Удалить"),
    REMOVEALL("Удалить все");

    private final String title;

    TypeMenu(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
