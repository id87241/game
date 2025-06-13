package tests;

import model.*;
import model.units.Hero;
import view.MapRenderer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SanctuaryTest {

    @Test
    public void testUnoccupiedSanctuaryRendering() {
        GameMap map = new GameMap(1, 1, "Tester");
        Cell cell = map.getCell(0, 0);
        cell.setTerrain(Terrain.SANCTUARY);

        Sanctuary sanctuary = new Sanctuary();
        //sanctuary.setOccupied(false);
        cell.setSanctuary(sanctuary);

        MapRenderer renderer = new MapRenderer();
        String cellChar = renderer.getCellChar(cell);

        assertEquals("⛪ ", cellChar,
                "Незанятое святилище должно отображаться как ⛪");
    }

    @Test
    public void testOccupiedSanctuaryRendering() {
        GameMap map = new GameMap(1, 1, "Tester");
        Cell cell = map.getCell(0, 0);
        cell.setTerrain(Terrain.SANCTUARY);

        Sanctuary sanctuary = new Sanctuary();
        sanctuary.setOccupied(true);
        cell.setSanctuary(sanctuary);

        MapRenderer renderer = new MapRenderer();
        String cellChar = renderer.getCellChar(cell);

        assertEquals("☥ ", cellChar,
                "Занятое святилище должно отображаться как ☥");
    }

    @Test
    public void testSanctuaryPriorityOverUnit() {
        //Святилище + юнит (отображается ли святилище)
        GameMap map = new GameMap(1, 1, "Tester");
        Cell cell = map.getCell(0, 0);
        cell.setTerrain(Terrain.SANCTUARY);

        Sanctuary sanctuary = new Sanctuary();
        sanctuary.setOccupied(false);
        cell.setSanctuary(sanctuary);

        cell.setUnit(new Hero("TestHero", true));

        MapRenderer renderer = new MapRenderer();
        String cellChar = renderer.getCellChar(cell);

        assertEquals("⛪ ", cellChar,
                "Святилище должно иметь приоритет над юнитом");
    }
}