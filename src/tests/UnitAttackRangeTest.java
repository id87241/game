package tests;

import controller.UnitMovementController;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnitAttackRangeTest {

    private GameMap map;
    private UnitMovementController movementController;
    private TestUnit attacker;
    private TestUnit enemy;

    @BeforeEach
    public void setUp() {
        // Создаем карту 5x5 для тестов
        map = new GameMap(5, 5, "Tester");
        movementController = new UnitMovementController(map);

        // Создаем тестовых юнитов
        attacker = new TestUnit(true);  // Юнит игрока (атакующий)
        enemy = new TestUnit(false);    // Юнит бота (цель)
    }

    // Проверка атаки на стандартной дистанции (attackRange = 1)
    @Test
    public void testDefaultAttackRange() {
        attacker.setAttackRange(1);

        // Размещаем юниты на расстоянии 1 клетки
        Cell attackerCell = map.getCell(0, 0);
        Cell enemyCell = map.getCell(0, 1);
        attackerCell.setUnit(attacker);
        enemyCell.setUnit(enemy);

        assertTrue(movementController.canReach(attackerCell, enemyCell),
                "Атака на дистанции 1 должна быть возможна");
    }

    // Проверка атаки на увеличенной дистанции (attackRange = 2)
    @Test
    public void testExtendedAttackRange() {
        attacker.setAttackRange(2);

        Cell attackerCell = map.getCell(0, 0);
        Cell enemyCell = map.getCell(0, 2);
        attackerCell.setUnit(attacker);
        enemyCell.setUnit(enemy);

        assertTrue(movementController.canReach(attackerCell, enemyCell),
                "Атака на дистанции 2 должна быть возможна");
    }

    // Проверка невозможности атаки на дистанции > attackRange
    @Test
    public void testOutOfRangeAttack() {
        attacker.setAttackRange(1);

        // Размещаем юниты на расстоянии 3 клетки (недостижимо)
        Cell attackerCell = map.getCell(0, 0);
        Cell enemyCell = map.getCell(0, 3);
        attackerCell.setUnit(attacker);
        enemyCell.setUnit(enemy);

        assertFalse(movementController.canReach(attackerCell, enemyCell),
                "Атака на дистанции 3 должна быть невозможна при attackRange=1");
    }
}