package tests;

import controller.UnitAttackController;
import model.*;
import model.buildings.Castle;
import model.units.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AttackTest {
    private GameMap map;
    private UnitAttackController attackController;
    private Castle playerCastle;
    private Castle botCastle;
    private Unit playerUnit;
    private Unit botUnit;
    private Hero immuneHero;

    @BeforeEach
    void setUp() {
        map = new GameMap(5, 5, "Tester");
        attackController = new UnitAttackController(map);

        // Инициализация замков
        playerCastle = new Castle(true);
        botCastle = new Castle(false);
        map.getCell(0, 0).setCastle(playerCastle);
        map.getCell(4, 4).setCastle(botCastle);

        // Создание тестовых юнитов
        playerUnit = new TestUnit(true);
        botUnit = new TestUnit(false);
        immuneHero = new Hero("TestHero", false);
        immuneHero.setImmune(true);
    }

    @Test
    void testAttackUnitDealsDamage() {
        Cell attackerCell = map.getCell(1, 1);
        Cell targetCell = map.getCell(1, 2);
        attackerCell.setUnit(playerUnit);
        targetCell.setUnit(botUnit);
        playerUnit.setAttack(30);
        botUnit.setHealth(100);

        attackController.attackUnit(attackerCell, targetCell);

        assertEquals(70, botUnit.getHealth());
        assertTrue(playerUnit.isHasAttacked());
    }

    @Test
    void testKillUnitGivesReward() {
        Cell attackerCell = map.getCell(2, 2);
        Cell targetCell = map.getCell(2, 3);
        attackerCell.setUnit(playerUnit);
        targetCell.setUnit(botUnit);
        playerUnit.setAttack(100);
        botUnit.setHealth(50);
        playerCastle.setGold(0);

        attackController.attackUnit(attackerCell, targetCell);

        assertNull(targetCell.getUnit());
        assertEquals(500, playerCastle.getGold());
    }

    @Test
    void testCannotAttackTwice() {
        Cell attackerCell = map.getCell(0, 1);
        Cell targetCell = map.getCell(0, 2);
        attackerCell.setUnit(playerUnit);
        targetCell.setUnit(botUnit);
        playerUnit.setAttack(10);
        botUnit.setHealth(100);

        attackController.attackUnit(attackerCell, targetCell);
        int healthAfterFirstAttack = botUnit.getHealth();

        attackController.attackUnit(attackerCell, targetCell);

        assertEquals(healthAfterFirstAttack, botUnit.getHealth());
    }

    @Test
    void testAttackInvalidPosition() {
        Cell attackerCell = map.getCell(4, 0);
        attackerCell.setUnit(playerUnit);

        attackController.attackUnit(4, 0, 5, 0); // Выход за границы карты

        assertNotNull(attackerCell.getUnit());
    }

    @Test
    void testAttackEmptyCell() {
        Cell attackerCell = map.getCell(1, 0);
        Cell emptyCell = map.getCell(1, 1);
        attackerCell.setUnit(playerUnit);

        attackController.attackUnit(attackerCell, emptyCell);

        assertNull(emptyCell.getUnit());
    }

    @Test
    void testBotKillsPlayerUnit() {
        Cell attackerCell = map.getCell(4, 3);
        Cell targetCell = map.getCell(4, 2);
        attackerCell.setUnit(botUnit);
        targetCell.setUnit(playerUnit);
        botUnit.setAttack(100);
        playerUnit.setHealth(50);
        botCastle.setGold(0);

        attackController.attackUnit(attackerCell, targetCell);

        assertNull(targetCell.getUnit());
        assertEquals(500, botCastle.getGold());
    }
}