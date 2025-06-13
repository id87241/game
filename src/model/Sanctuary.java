package model;

import model.buildings.Castle;
import model.units.Hero;
import model.units.Unit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Sanctuary {
    private static final Logger logger = LogManager.getLogger(Sanctuary.class);
    private final int ENTRANCE_FEE = 50;

    private boolean occupied;
    private Unit occupant;

    public Sanctuary() {
        logger.debug("Создание нового Sanctuary (занятость по умолчанию: {})", occupied);
        this.occupied = false;
        this.occupant = null;
    }

    public boolean enter(Castle playerCastle, Unit unit) {
        if (occupied || playerCastle.getGold() < ENTRANCE_FEE) {
            return false;
        }

        System.out.println("юнит " + unit.toString() + " посетил святилище");

        this.occupied = true;
        this.occupant = unit;
        playerCastle.setGold(playerCastle.getGold() - ENTRANCE_FEE);
        unit.setInSanctuary(true);
        return true;
    }

    public void leave() {
        System.out.println("юнит " + this.occupant.toString() + " покинул святилище");
        this.occupied = false;
        this.occupant = null;
    }

    public boolean isOccupied(){
        logger.debug("Проверка занятости: {}", occupied);
        return occupied;
    }

    public void setOccupied(boolean occupied){
        logger.info("Изменение состояния occupied: {} -> {}", this.occupied, occupied);
        this.occupied = occupied;
    }

}