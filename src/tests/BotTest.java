package tests;

import controller.BotMovesController;
import model.*;
import model.buildings.Castle;
import model.units.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {
    private GameMap map;
    private BotMovesController botController;
    private Unit botUnit;
    private Unit playerUnit;

    @BeforeEach
    void setUp() {
        map = new GameMap(5, 5, "Tester");
        botController = new BotMovesController(map);

        // Инициализация замков
        Castle botCastle = new Castle (false);
        Castle playerCastle = new Castle(true);
        map.getCell(0, 0).setCastle(botCastle);
        map.getCell(4, 4).setCastle(playerCastle);

        // Создание тестовых юнитов
        botUnit = new Spearman(false);
        playerUnit = new TestUnit(true);
    }

    @Test
    void testCannotMoveToOccupiedCell() {
        // Arrange
        Cell botCell = map.getCell(1, 1);
        Cell occupiedCell = map.getCell(1, 2);
        botCell.setUnit(botUnit);
        occupiedCell.setUnit(playerUnit);

        // Act
        botController.handleBotMove();

        // Assert
        assertEquals(botCell.getUnit(), botUnit, "Бот не должен перемещаться на занятую клетку");
        assertEquals(occupiedCell.getUnit(), playerUnit, "Юнит игрока должен остаться на месте");
    }

    @Test
    void testCannotMoveOutsideMap() {
        // Arrange
        Cell edgeCell = map.getCell(0, 0);
        edgeCell.setUnit(botUnit);

        // Act
        botController.handleBotMove();

        // Assert
        assertEquals(edgeCell.getUnit(), botUnit, "Бот не должен выходить за границы карты");
    }

    /*@Test
    void testMoveToFreeCell() {
        // Arrange
        Cell botCell = map.getCell(1, 1);
        Cell freeCell = map.getCell(1, 2);
        botCell.setUnit(botUnit);

        // Act
        botController.handleBotMove();

        // Assert
        assertNull(botCell.getUnit(), "Исходная клетка должна быть пуста");
        assertNotNull(freeCell.getUnit(), "Бот должен переместиться на свободную клетку");
    }*/

    /*@Test
    void testFindNearestToCastleWithObstacles() {
        // Arrange
        Cell botCell = map.getCell(2, 2);
        botCell.setUnit(botUnit);

        // Создаем препятствия вокруг
        map.getCell(2, 3).setTerrain(Terrain.OBSTACLE);
        map.getCell(3, 2).setTerrain(Terrain.OBSTACLE);

        // Act
        Cell target = botController.findNearestAvailableCellToCastle(botCell);

        // Assert
        assertNotNull(target, "Должна найтись доступная клетка");
        assertTrue(target.isEmpty(), "Клетка должна быть свободна");
        assertTrue(botController.canReach(botCell, target), "Клетка должна быть достижима");
    }*/

    @Test
    void testNoMovementWhenNoPath() {
        // Arrange
        Cell botCell = map.getCell(0, 0);
        botCell.setUnit(botUnit);

        // Полностью блокируем бота
        map.getCell(0, 1).setTerrain(Terrain.OBSTACLE);
        map.getCell(1, 0).setTerrain(Terrain.OBSTACLE);

        // Act
        botController.handleBotMove();

        // Assert
        assertEquals(botCell.getUnit(), botUnit, "Бот должен остаться на месте при отсутствии пути");
    }

    @Test
    void testAttackInsteadOfMoveWhenInRange() {
        // Arrange
        Cell botCell = map.getCell(1, 1);
        Cell playerCell = map.getCell(1, 2);
        botCell.setUnit(botUnit);
        playerCell.setUnit(playerUnit);
        playerUnit.setHealth(100);

        // Act
        botController.handleBotMove();

        // Assert
        assertTrue(playerUnit.getHealth() < 100, "Бот должен атаковать при возможности");
        assertTrue(botUnit.isHasAttacked(), "Флаг атаки должен быть установлен");
    }

    // Вспомогательный класс для тестов
    private static class TestUnit extends Unit {
        public TestUnit(boolean isPlayerUnit) {
            super(100, 10, 1, "TestUnit", isPlayerUnit, 50);
        }
    }
}