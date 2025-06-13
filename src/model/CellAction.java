package model;

public enum CellAction {
    MOVE("Перемещение"),
    ATTACK("Атака"),
    ENTER_CASTLE("Войти в замок");

    private final String name;

    CellAction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
