package controller;

import model.Cell;
import model.buildings.Building;
import model.buildings.Castle;
import model.buildings.Stable;
import model.units.Unit;


public class BuyingController {

    public BuyingController(){

    }

    public void buyBuilding(Castle castle, Building building){
        if(building.getCost() <= castle.getGold()){
            castle.setGold(castle.getGold() - building.getCost());
            castle.addBuilding(building);
        }
    }

    public void buyUnit(Castle castle, Unit unit, Cell cell){
        if (unit.getCost() <= castle.getGold() && cell.getUnit() == null){
            castle.setGold(castle.getGold() - unit.getCost());
            cell.setUnit(unit);
        }
    }

    public void upgradeUnit(Castle castle, Cell cell){
        if (Stable.UPGRADE_COST <= castle.getGold() && cell.getUnit() != null){
            castle.setGold(castle.getGold() - Stable.UPGRADE_COST);
            cell.getUnit().setMoveRange(cell.getUnit().getMoveRange() + 1);
        }

    }
}
