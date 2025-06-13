package controller;

import model.Cell;
import model.GameMap;
import model.buildings.Castle;
import model.units.Spearman;

import java.util.List;

public class BotMovesController {
    private final GameMap map;
    private final UnitMovementController unitMovementController;
    private final UnitAttackController unitAttackController;
    private final BuyingController buyingController;

    public BotMovesController(GameMap map) {
        this.map = map;
        this.unitMovementController = new UnitMovementController(map);
        this.unitAttackController = new UnitAttackController(map);
        this.buyingController = new BuyingController();
    }

    public void handleBotMove() {
        List<Cell> cells = map.getCellList();

        Cell botCastleCell = map.findBotCastle();
        Castle botCastle = botCastleCell.getCastle();

        buyingController.buyUnit(botCastle, new Spearman(false), botCastleCell);

        for (Cell cell : cells) {
            if (cell.getUnit() != null && !cell.getUnit().isPlayerUnit()) {
                // cell - клетка с юнитом бота
                Cell target = map.findNearestPlayerUnit(cell);

                // Вообще, таргет никогда не будут нулл, т.к. тогда бот просто выигрывает.
                // Но пусть будет, спокойней, что не нулл
                if (target != null) {

                    if (unitMovementController.canReach(cell, target)) {
                        // или сразу бьет или сначала подходит
                        if (canAttack(cell, target)) {

                            unitAttackController.attackUnit(cell, target);

                        } else {

                            // юнит игрока в рэнже, можно подойти и ударить
                            Cell nearestToTargetCell = map.findFreeAdjacentCell(target);
                            unitMovementController.moveUnit(cell, nearestToTargetCell);

                            // И после передвижения туда - атака цели
                            unitAttackController.attackUnit(nearestToTargetCell, target);

                        }
                    } else {
                        // иначе идет прямо к замку
                        // у нас новый таргет
                        target = findNearestAvailableCellToCastle(cell);
                        unitMovementController.moveUnit(cell, target);
                    }

                }
            }

        }

    }

    // Возвращает ближайшую клетку к замку игрока в которую может прийти бот
    public Cell findNearestAvailableCellToCastle(Cell from) {
        Cell castleCell = map.findPlayerCastle();
        if (castleCell == null || from.getUnit() == null) return null;


        List<Cell> allCells = map.getCellList();
        int minDistance = Integer.MAX_VALUE;
        Cell nearestFreeCell = null;

        for (Cell cell : allCells) {
            // Проверяем, что клетка свободна
            if (cell.isEmpty()) {

                int distanceToCastle = map.getDistance(
                        castleCell.getX(), castleCell.getY(),
                        cell.getX(), cell.getY()
                );

                if (distanceToCastle < minDistance & (unitMovementController.canReach(from, cell))) {
                    minDistance = distanceToCastle;
                    nearestFreeCell = cell;
                }
            }
        }

        return nearestFreeCell;
    }




    public boolean canAttack(Cell cell, Cell nearestPlayerUnitCell) {
        // если дистанция до юнита не больше дальности перемещения
        return map.getDistance(cell.getX(), cell.getY(),
                nearestPlayerUnitCell.getX(), nearestPlayerUnitCell.getY()) == 1;
    }


}
