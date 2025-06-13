package model;

import model.buildings.Castle;
import model.units.Unit;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private int x;
    private int y;
    private Terrain terrain;
    private Unit unit;
    private Castle castle;
    private Sanctuary sanctuary;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.terrain = Terrain.GRASS; // По умолчанию трава
    }

    @Override
    public String toString() {
        String string = "Cell: " + this.x + " " + this.y + "\n"
                + "Terrain: " + this.terrain;

        if (this.castle != null && this.castle.isPlayerCastle()){
            string += "\nCastle gold: " + this.castle.getGold();
        }

        if(this.unit != null){
            string += "\nUnit: " + this.unit;
        }
        return string;
    }

    public List<CellAction> getAvailableActions(){
        List<CellAction> actions = new ArrayList<>();
        // Взаимодействие с юнитом или замком, если есть
        if (this.unit != null && this.unit.isPlayerUnit()){
            actions.add(CellAction.ATTACK);
            actions.add(CellAction.MOVE);
        }
        if (this.castle != null && this.castle.isPlayerCastle()){
            actions.add(CellAction.ENTER_CASTLE);
        }

        return actions;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Castle getCastle() {
        return castle;
    }

    public void setCastle(Castle castle) {
        this.castle = castle;
    }

    public boolean isEmpty() { return unit==null; }

    public Sanctuary getSanctuary() {
        return sanctuary;
    }

    public void setSanctuary(Sanctuary sanctuary) {
        this.sanctuary = sanctuary;
    }
}