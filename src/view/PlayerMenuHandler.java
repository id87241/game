package view;


import model.GameMap;
import controller.UnitAttackController;
import controller.UnitMovementController;
import model.CellAction;

import java.util.List;
import java.util.Scanner;

public class PlayerMenuHandler {
    private final GameMap map;
    private final MapRenderer renderer;
    private final Scanner scanner;
    private final UnitMovementController unitMovementController;
    private final UnitAttackController unitAttackController;
    private final CastleMenu castleMenu;

    public PlayerMenuHandler(GameMap map, MapRenderer renderer) {
        this.map = map;
        this.renderer = renderer;
        this.scanner = new Scanner(System.in);
        this.unitMovementController = new UnitMovementController(map);
        this.unitAttackController = new UnitAttackController(map);
        this.castleMenu = new CastleMenu(map.findPlayerCastle().getCastle(), map);
    }

    public void handlePlayerTurn() {
        boolean turnCompleted = false;

        while (!turnCompleted) {
            printMainMenu();
            int choice = getIntInput("Выберите действие: ");

            switch (choice) {
                case 1 -> inspectCell();
                case 2 -> interactWithCell();
                case 3 -> turnCompleted = true;
                case 4 -> renderer.render(map);
                default -> System.out.println("Неверный выбор!");
            }

            if (!turnCompleted && choice != 4) {
                turnCompleted = confirmEndTurn();
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n=== МЕНЮ ИГРОКА ===");
        System.out.println("1. Осмотреть клетку");
        System.out.println("2. Взаимодействовать с клеткой");
        System.out.println("3. Пропустить ход");
        System.out.println("4. Показать карту");
    }

    private void inspectCell() {
        System.out.println("\n--- Осмотр клетки ---");
        int x = getIntInput("Введите координату X: ");   //(опционально) можно сделать инпут в одну строку через пробел
        int y = getIntInput("Введите координату Y: ");

        if (map.isValidPosition(x, y)) {
            System.out.println(map.getCell(x, y).toString());
        } else {
            System.out.println("Неверные координаты!");
        }
    }

    private void interactWithCell() {
        System.out.println("\n--- Взаимодействие с клеткой ---");
        int x = getIntInput("Введите координату X исходной клетки: ");
        int y = getIntInput("Введите координату Y исходной клетки: ");

        if (!map.isValidPosition(x, y)) {
            System.out.println("Неверные координаты!");
            return;
        }

        System.out.println(map.getCell(x, y).toString());
        System.out.println("Доступные действия:");
        List<CellAction> actions = map.getCell(x, y).getAvailableActions();

        for (int i = 0; i < actions.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, actions.get(i));
        }

        int actionNum = getIntInput("Выберите действие: ");

        try {
            CellAction action = actions.get(actionNum - 1);     // -1 т.к. индексация с 0

            switch (action) {
                case MOVE -> handleMove(x, y);
                case ATTACK -> handleAttack(x, y);
                case ENTER_CASTLE -> castleMenu.showMenu();
            }

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Неверный ввод");
        }
    }

    private void handleAttack(int fromX, int fromY) {
        if (map.getCell(fromX, fromY).getUnit().isHasAttacked()) {
            System.out.println("Юнит уже атаковал");
            return;
        }

        int toX = getIntInput("Введите координату X цели: ");
        int toY = getIntInput("Введите координату Y цели: ");

        if (fromX == toX && fromY == toY) {
            System.out.println("Нельзя стрелять себе в ногу!");
            return;
        }

        if (map.getDistance(fromX, fromY, toX, toY) > map.getCell(fromX, fromY).getUnit().getAttackRange()) {
            System.out.println("Цель вне дистанции атаки");
            return;
        }

        if (!map.isValidPosition(toX, toY)) {
            System.out.println("Неверные координаты!");
            return;
        }

        unitAttackController.attackUnit(fromX, fromY, toX, toY);

        //TODO тут можно добавить рефлексию на атаку (опционально)

        renderer.render(map);
    }

    private void handleMove(int fromX, int fromY) {
        if (map.getCell(fromX, fromY).getUnit().isHasMoved()) {
            System.out.println("Юнит уже передвигался");
            return;
        }
        int toX = getIntInput("Введите координату X конечной клетки: ");
        int toY = getIntInput("Введите координату Y конечной клетки: ");

        if (!map.isValidPosition(toX, toY)) {
            System.out.println("Неверные координаты!");
            return;
        }

        if (!unitMovementController.canReach(map.getCell(fromX, fromY), map.getCell(toX, toY))) {
            System.out.println("Цель вне зоны перемещения");
            return;
        }

        if (map.getCell(toX, toY).getUnit() != null) {
            System.out.println("Клетка занята");
            return;
        }

        unitMovementController.moveUnit(fromX, fromY, toX, toY);
        renderer.render(map);
    }


    private boolean confirmEndTurn() {
        String input = getStringInput("\nЗавершить ход? (y/n): ");
        return input.equalsIgnoreCase("y");
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите число!");
            scanner.next();
            System.out.print(prompt);
        }
        return scanner.nextInt();
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.next();
    }

}