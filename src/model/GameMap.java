package model;

import model.buildings.Castle;
import model.units.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.Math.*;

public class GameMap {
    private Cell[][] cells;
    private int width;
    private int height;
    private String player;

    public GameMap(int width, int height, String player) {
        this.player = player;
        this.width = width;
        this.height = height;
        this.cells = new Cell[height][width];
        initializeMap();
    }

    private void initializeMap() {

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(x, y);

                if(x==y){
                    cells[x][y].setTerrain(Terrain.ROAD);
                }
                else if(x < width/2 && y < height/2){
                    cells[x][y].setTerrain(Terrain.PLAYER_TERRITORY);
                }
                else if(x >= width/2 && y >= height/2){
                    cells[x][y].setTerrain(Terrain.BOT_TERRITORY);
                }
                else if (x < 3 && y > 6){
                    cells[x][y].setTerrain(Terrain.OBSTACLE);
                }
                else if (x > 6 && y < 3){
                    cells[x][y].setTerrain(Terrain.OBSTACLE);
                }
            }
        }
        generateSanctuary();

    }

    public void placeUnit(int x, int y, Unit unit) {
        if (isValidPosition(x, y)) {
            cells[x][y].setUnit(unit);
        }
    }

    public void placeCastle(int x, int y, Castle castle) {
        if (isValidPosition(x, y)) {
            cells[y][x].setCastle(castle);
            System.out.println("castle set");
        }
    }

    public Cell getCell(int x, int y) {
        if (isValidPosition(x, y)) {
            return cells[x][y];
        }
        return null;
    }

    public List<Cell> getCellList() {
        // Преобразует Cell[][] в лист через стрим, в целом можно сделать нагляднее циклом любым удобным способом,
        // но и так работает
        return Arrays.stream(cells).flatMap(Arrays::stream).collect(Collectors.toList());
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDistance(int fromX, int fromY, int toX, int toY) {
        return (int) sqrt(pow(toX - fromX, 2) + pow(toY - fromY, 2));
    }

    public Cell findNearestPlayerUnit(Cell fromCell) {

        List<Cell> list = getCellList();
        int minDistance = Integer.MAX_VALUE;
        Cell nearestCell = null;
        for (Cell cell : list) {
            if (cell.getUnit() != null){
                if (cell.getUnit().isPlayerUnit()) {

                    int distanceToCell = getDistance(cell.getX(), cell.getY(), fromCell.getX(), fromCell.getY());

                    if (distanceToCell < minDistance) {

                        nearestCell = cell;
                        minDistance = distanceToCell;

                    }

                }
            }

        }
        return nearestCell;
    }


    public Cell findFreeAdjacentCell(Cell targetCell) {
        int targetX = targetCell.getX();
        int targetY = targetCell.getY();

        // перебираю соседей, начиная с самых нижних
        for (int dx = 1; dx >= -1; dx--) {
            for (int dy = 1; dy >= -1; dy--) {

                // саму клетку скип
                if (dx == 0 && dy == 0) continue;

                int nx = targetX + dx;
                int ny = targetY + dy;

                if (isValidPosition(nx, ny)) {
                    Cell cell = getCell(nx, ny);
                    if (cell.isEmpty()) {
                        return cell;
                    }
                }
            }
        }

        return null;

    }

    public Cell findPlayerCastle(){
        List<Cell> allCells = getCellList();
        for (Cell cell : allCells){
            if(cell.getCastle() != null){
                if (cell.getCastle().isPlayerCastle()) {return cell;}
            }
        }
        return null;
    }

    public Cell findBotCastle(){
        List<Cell> allCells = getCellList();
        for (Cell cell : allCells){
            if(cell.getCastle() != null){
                if (!cell.getCastle().isPlayerCastle()) {return cell;}
            }
        }
        return null;
    }

    // дорога считается нейтральной территорией, но она есть как на территории игрока,
    // так и на территории бот а, что формально не должно бы быть нейтральной территорией.
    // В остальном метод работает корректно
    public void generateSanctuary() {
        Random random = new Random();
        int x, y;

        // Ищем случайную нейтральную клетку
        do {
            x = random.nextInt(width);
            y = random.nextInt(height);
        } while (!isNeutralTerrain(x, y));

        Cell cell = getCell(x, y);
        cell.setTerrain(Terrain.SANCTUARY);
        cell.setSanctuary(new Sanctuary());
    }

    private boolean isNeutralTerrain(int x, int y) {
        Terrain terrain = getCell(x, y).getTerrain();
        return terrain == Terrain.GRASS || terrain == Terrain.ROAD;
    }

    public void initializeDefaultMap() {

        //замок игрока
        placeCastle(0, 0, new Castle(true));

        //замок бота
        placeCastle(width - 1, height - 1, new Castle(false));

        // Размещаем героя игрока
        placeUnit(1, 0, new Hero( "PlayerHero", true));

        // Размещаем юниты игрока
        placeUnit(1, 1, new Spearman(true));

        placeUnit(1, 1, new Spearman(true));

        placeUnit(9, 0, new MegaKnightForTests(true));

        placeUnit(2, 0, new Archer(true));

        // Размещаем вражеского героя
        placeUnit(width - 2, height - 2, new Hero("EnemyHero", false));

        // Размещаем вражеские юниты
        placeUnit(width - 2, height - 1, new Spearman(false));
        placeUnit(width - 3, height - 1, new Archer(false));
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }


}