package view;


import model.GameMap;
import model.Cell;
import model.Terrain;
import model.units.Hero;
import model.units.MegaKnightForTests;

import java.util.Objects;

public class MapRenderer {

    // опционально тут можно поставить любые символы/эмодзи, но вероятно, карта поедет
    private static final String EMPTY_CELL = "  ";
    private static final String PLAYER_UNIT = "P ";
    private static final String ENEMY_UNIT = "E ";
    private static final String PLAYER_HERO = "H ";
    private static final String ENEMY_HERO = "h ";
    private static final String MEGA_KNIGHT = "M ";
    private static final String PLAYER_CASTLE = "A ";
    private static final String ENEMY_CASTLE = "B ";
    private static final String ROAD = "═ ";
    private static final String PLAYER_TERRITORY = "· ";
    private static final String BOT_TERRITORY = "• ";
    private static final String OBSTACLE = "# ";
    private static final String SANCTUARY_OCCUPIED = "☥ ";
    private static final String SANCTUARY_UNOCCUPIED = "⛪ ";


    public void render(GameMap map) {
        System.out.println("\nCurrent Map:");
        System.out.print("  ");

        for (int i = 0; i < map.getWidth(); i++) {
            System.out.print(i + (i < 10 ? "    " : "   "));
        }
        System.out.println();

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                System.out.print("+----");
            }
            System.out.println("+");

            for (int x = 0; x < map.getWidth(); x++) {
                Cell cell = map.getCell(x, y);
                System.out.print("| " + getCellChar(cell) + " ");
            }
            System.out.println("|" + y);
        }

        for (int x = 0; x < map.getWidth(); x++) {
            System.out.print("+----");
        }
        System.out.println("+");
    }


    public String getCellChar(Cell cell) {
        if (cell.getTerrain() == Terrain.SANCTUARY) {
            return cell.getSanctuary().isOccupied() ? SANCTUARY_OCCUPIED : SANCTUARY_UNOCCUPIED;
        }

        if (cell.getUnit() instanceof Hero) {
            return cell.getUnit().isPlayerUnit() ? PLAYER_HERO : ENEMY_HERO;
        }

        if (cell.getUnit() != null) {
            if (cell.getUnit().isPlayerUnit() && Objects.equals(cell.getUnit().getName(), "Мега Рыцарь")){
                return MEGA_KNIGHT;
            }
            return cell.getUnit().isPlayerUnit() ? PLAYER_UNIT : ENEMY_UNIT;
        }


        if (cell.getCastle() != null){
            return cell.getCastle().isPlayerCastle() ? PLAYER_CASTLE : ENEMY_CASTLE;
        }

        switch (cell.getTerrain()){

            case Terrain.ROAD -> {
                return ROAD;
            }
            case Terrain.PLAYER_TERRITORY -> {
                return PLAYER_TERRITORY;
            }
            case Terrain.BOT_TERRITORY -> {
                return BOT_TERRITORY;
            }
            case Terrain.OBSTACLE -> {
                return OBSTACLE;
            }


        }

        return EMPTY_CELL;
    }
}