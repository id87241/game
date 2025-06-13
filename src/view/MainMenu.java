package view;

import controller.GameController;

import java.util.Scanner;

public class MainMenu {
    private static final Scanner scanner = new Scanner(System.in);

    public static void showMenu() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = getIntInput();

            switch (choice) {
                case 1:
                    startGame();
                    break;
                case 2:
                    System.out.println("Выход из игры...");
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== Heroes of IU3 ===");
        System.out.println("1. Начать игру");
        System.out.println("2. Выход");
        System.out.print("Выберите действие: ");
    }

    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите число.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void startGame() {
        System.out.println("\nНачало новой игры...");
        System.out.println("Введите ваше имя: ");
        String player = scanner.next();

        GameController gameController;

        System.out.println("Хотите ли вы продолжить с картой по умолчанию? (0 - да, 1 - нет): ");
        int temp_choice = scanner.nextInt();

        switch (temp_choice) {
            case 0:
                gameController = new GameController(10, 10, player);
                gameController.start();
                break;
            case 1:
                System.out.println("Введите ширину поля: ");
                int width = scanner.nextInt();
                System.out.println("Введите высоту поля: ");
                int height = scanner.nextInt();
                gameController = new GameController(width, height, player);
                gameController.start();
                break;
        }
    }
}
