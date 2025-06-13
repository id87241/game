package controller;

import model.Cell;
import model.GameMap;
import model.Terrain;

import java.util.Map;

public class UnitMovementController {

    private final GameMap map;

    // Штрафы за перемещение для каждого типа местности
    private final Map<Terrain, Double> terrainPenalties = Map.of(
            Terrain.GRASS, 1.0,
            Terrain.ROAD, 0.7,
            Terrain.PLAYER_TERRITORY, 1.3,
            Terrain.BOT_TERRITORY, 1.0,
            Terrain.OBSTACLE, Double.POSITIVE_INFINITY
    );

    public UnitMovementController(GameMap map) {
        this.map = map;
    }

    public void moveUnit(int fromX, int fromY, int toX, int toY) {
        if (!map.isValidPosition(toX, toY)) {
            return;
        }

        Cell fromCell = map.getCell(fromX, fromY);
        Cell targetCell = map.getCell(toX, toY);

        if (fromCell.getUnit() == null || targetCell.getUnit() != null) {
            return;
        }
        if (fromCell.getUnit().isHasMoved()) return;


        if (targetCell.getTerrain() == Terrain.SANCTUARY) {
            // пробуем войти в святилище и если не получается то ретерним из функции
            if(!targetCell.getSanctuary().enter(map.findPlayerCastle().getCastle(), fromCell.getUnit())){
                return;
            }
        }
        if (fromCell.getTerrain() == Terrain.SANCTUARY && canReach(fromCell, targetCell)) {
            // выходим из святилища
            fromCell.getSanctuary().leave();
        }

        if (canReach(fromCell, targetCell)) {
            // Логика перемещения
            targetCell.setUnit(fromCell.getUnit());
            fromCell.setUnit(null);
            targetCell.getUnit().setHasMoved(true);
        }

    }

    public void moveUnit(Cell from, Cell to) {
        moveUnit(from.getX(), from.getY(), to.getX(), to.getY());
    }

    public boolean canReach(Cell from, Cell to) {

        if (from.getUnit() == null) return false;

        double distance = map.getDistance(from.getX(), from.getY(),
                to.getX(), to.getY());

        // Получаем штраф
        double penalty = terrainPenalties.getOrDefault(to.getTerrain(), 1.0);

        // Проверяем, хватит ли очков движения с учётом штрафа
        return distance * penalty <= from.getUnit().getMoveRange();
    }
}