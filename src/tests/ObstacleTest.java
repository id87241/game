package tests;

import controller.UnitMovementController;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ObstacleTest {

    private GameMap map;
    private UnitMovementController movementController;
    private TestUnit playerUnit; // Используем TestUnit из предыдущего примера

    @BeforeEach
    public void setUp() {
        map = new GameMap(3, 3, "Tester");
        movementController = new UnitMovementController(map);

        playerUnit = new TestUnit(true);
        playerUnit.setMoveRange(2); // Устанавливаем дальность перемещения
    }

    @Test
    public void testTerrainPenalties() {

        Cell startCell = map.getCell(0, 0);
        startCell.setUnit(playerUnit);

        // 1. GRASS (штраф 1.0) - расстояние 1
        Cell grassCell = map.getCell(0, 1);
        grassCell.setTerrain(Terrain.GRASS);
        assertTrue(movementController.canReach(startCell, grassCell), "GRASS: 1 * 1.0 = 1.0 ≤ 2 → должно быть доступно");

        // 2. ROAD (штраф 0.7) - расстояние 1
        Cell roadCell = map.getCell(1, 0);
        roadCell.setTerrain(Terrain.ROAD);
        assertTrue(movementController.canReach(startCell, roadCell), "ROAD: 1 * 0.7 = 0.7 ≤ 2 → должно быть доступно");

        // PLAYER_TERRITORY (штраф 1.3) - расстояние корень из 2 (≈1.41)
        Cell playerTerritoryCell = map.getCell(1, 1);
        playerTerritoryCell.setTerrain(Terrain.PLAYER_TERRITORY);
        assertTrue(movementController.canReach(startCell, playerTerritoryCell), "PLAYER_TERRITORY: 1.41 * 1.3 ≈ 1.83 ≤ 2 → должно быть доступно");

        //OBSTACLE (штраф бесконечный - не пройти) - всегда недоступно
        Cell obstacleCell = map.getCell(0, 2);
        obstacleCell.setTerrain(Terrain.OBSTACLE);
        assertFalse(movementController.canReach(startCell, obstacleCell), "OBSTACLE: всегда недоступно");
    }

    // Проверка сброса флага hasMoved после неудачного перемещения
    @Test
    public void testMovementFlagOnFailure() {
        Cell startCell = map.getCell(0, 0);
        startCell.setUnit(playerUnit);

        Cell obstacleCell = map.getCell(0, 1);
        obstacleCell.setTerrain(Terrain.OBSTACLE);

        movementController.moveUnit(startCell, obstacleCell);
        assertFalse(playerUnit.isHasMoved(), "Unit's hasMoved flag was set after failed movement");
    }
}