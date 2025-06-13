package model.buildings;

import java.util.ArrayList;
import java.util.List;

public class Castle {
    private List<Building> buildings;
    private int gold;
    private boolean isPlayerCastle;

    public Castle(boolean isPlayerCastle) {
        this.buildings = new ArrayList<>();
        this.gold = 1000;
        this.isPlayerCastle = isPlayerCastle;
    }


    public List<Building> getBuildings() {
        return buildings;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public boolean isPlayerCastle() {
        return isPlayerCastle;
    }

    public void setPlayerCastle(boolean playerCastle) {
        isPlayerCastle = playerCastle;
    }
} 