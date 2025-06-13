package view;

import controller.BuyingController;
import model.Cell;
import model.GameMap;
import model.buildings.*;
import model.units.*;
import controller.GameSaver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CastleMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private final Castle castle;
    private final BuyingController buyingController;
    private final GameMap map;
    private final Cell castleCell;

    public CastleMenu(Castle castle, GameMap map) {
        this.castle = castle;
        this.buyingController = new BuyingController();
        this.map = map;
        castleCell = map.findPlayerCastle();
    }


    private void printMenu() {
        System.out.println("\n=== Замок ===");
        System.out.println("1. Список зданий");
        System.out.println("2. Купить здание");
        System.out.println("3. Выйти из замка");
        System.out.print("Выберите действие: ");
    }

    public void showMenu() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    showBuildingList();
                    break;

                case 2:
                    buyBuilding();
                    break;
                case 3:
                    System.out.println("Выход из замка...");
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

    }


    private void showBuildingList() {
        System.out.println("\n=== Список построек ===");
        List<Building> buildingList = castle.getBuildings();
        if (buildingList.isEmpty()) {
            System.out.println("Пока зданий нет");
            return;
        }
        for (int i = 0; i < buildingList.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, buildingList.get(i).toString());  //Принт можно сделать понятнее, без % (опционально)
        }
        System.out.println("Использовать здание: ");
        int choice = getIntInput();

        if (choice > 0 && choice <= buildingList.size()) {
            useBuilding(buildingList.get(choice - 1));
        }
    }

    private void useBuilding(Building building) {
        System.out.printf("\n=== %s ===%n", building.getName());

        switch (building) {
            case Barracks barracks -> useBarracks();
            case Stable stable -> useStable();
            case Tavern tavern -> useTavern();
            default -> System.out.println("Это здание пока не имеет функционала");
        }
    }

    private void useBarracks() {

        List<Unit> availableUnits = List.of(
                new Spearman(true), //
                new Archer(true)
        );
        unitBuyer(availableUnits);

    }

    private void useStable() {

        List<Unit> availableUnits = List.of(
                new Knight(true)
        );

        System.out.println("\n=== Услуги конюшни ===");
        System.out.println("1. Улучшить перемещение (+1 к движению) - " + Stable.UPGRADE_COST);
        System.out.println("2. Купить юнита");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                if (Stable.UPGRADE_COST <= castle.getGold() && castleCell.getUnit() != null) {
                    buyingController.upgradeUnit(castle, castleCell);
                } else {
                    System.out.println("В замке нет юнита или не достаточно золота");
                }
                break;
            case 2:
                unitBuyer(availableUnits);
                break;
            default:
                System.out.println("неверный ввод");
                break;
        }

    }

    private void useTavern() {

        List<Unit> availableUnits = List.of(
                new Hero("Новый Герой", true)
        );
        unitBuyer(availableUnits);
    }

    private void unitBuyer(List<Unit> availableUnits) {

        System.out.println("\n=== Нанять юнитов ===");
        for (int i = 0; i < availableUnits.size(); i++) {
            Unit unit = availableUnits.get(i);
            System.out.printf("%d. %s - %d золота (Атака: %d, Здоровье: %d)%n", //TODO форматирование запомнить
                    i + 1,
                    unit.getName(),
                    unit.getCost(),
                    unit.getAttack(),
                    unit.getHealth());
        }

        System.out.print("Выберите юнита (0 - отмена): ");
        int choice = getIntInput();

        if (choice > 0 && choice <= availableUnits.size()) {
            Unit selectedUnit = availableUnits.get(choice - 1);
            if (castle.getGold() >= selectedUnit.getCost() && castleCell.getUnit() == null) {
                buyingController.buyUnit(castle, selectedUnit, castleCell);
                System.out.println("Куплен " + selectedUnit.getName());
            } else {
                System.out.println("Недостаточно золота или клетка занята"); //TODO можно разбить на 2 отдельных случая (опционально)
            }
        }
    }


    private void buyBuilding() {
        System.out.println("\n=== магазин зданий ===");
        List<Building> allBuildings = List.of(
                new Barracks(),
                new Stable(),
                new Tavern()
        );
        System.out.println("\n=== Доступные для покупки здания ===");

        // Фильтруем - оставляем только те, которых еще нет в замке.
        /*
        Вариант со стримом, менее понятный визуально, однако более читаемый, любой способ рабочий
        List<Building> availableBuildings = allBuildings
                .stream()
                .filter(building ->
                        castle
                        .getBuildings()
                        .stream()
                        .noneMatch(b ->
                                building
                                .getClass()
                                .equals(b.getClass())))
                .toList();

        if (availableBuildings.isEmpty()) {
            System.out.println("Все здания уже построены!");
            return;
        }*/

        ArrayList<Building> availableBuildings = new ArrayList<>();
        for (Building b : allBuildings) {
            boolean exists = false;
            for (Building built : castle.getBuildings()) {
                if (built.getClass() == b.getClass()) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                availableBuildings.add(b);
            }
        }

        // Выводим список
        for (int i = 0; i < availableBuildings.size(); i++) {
            Building b = availableBuildings.get(i);
            System.out.printf("%d. %s - %d золота%n", i + 1, b.getName(), b.getCost());  //опционально упрощение
        }

        System.out.print("Выберите здание (0 - отмена): ");
        int choice = getIntInput();

        if (choice > 0 && choice <= availableBuildings.size()) {
            Building building = availableBuildings.get(choice - 1);

            if (building.getCost() <= castle.getGold()) {
                buyingController.buyBuilding(castle, building);
                System.out.println("Куплено: " + building.getName());
            } else {
                System.out.println("Недостаточно золота, милорд");
            }

        }

    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите число.");
            scanner.next();
        }
        return scanner.nextInt();
    }


}
