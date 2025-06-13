package controller;

import model.GameMap;
import model.units.Unit;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GameSaver {
    private static final String SAVES_DIR = "game_saves/";
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public void saveGameState (String playerName, GameMap map, List < Unit > units){
        new File(SAVES_DIR).mkdirs();
        String fileName = SAVES_DIR + generateSaveFileName(playerName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            writer.write(String.format("player,%s%n", playerName));
            writer.write(String.format("timestamp,%s%n", LocalDateTime.now().format(DATE_FORMAT)));
            writer.write(String.format("map_size,%d,%d%n", map.getWidth(), map.getHeight()));

            // Состояние карты
            writer.write("\n# Map terrain\n");
            for (int y = 0; y < map.getHeight(); y++) {
                for (int x = 0; x < map.getWidth(); x++) {
                    writer.write(map.getCell(x, y).getTerrain().name());
                    if (x < map.getWidth() - 1) writer.write(",");
                }
                writer.newLine();
            }

            // Записываем юнитов
            writer.write("\n# Units\n");
            writer.write("type,isPlayer,health\n");
            for (Unit unit : units) {
                writer.write(String.format("%s,%b,%d%n",
                        unit.getClass().getSimpleName(),
                        unit.isPlayerUnit(),
                        unit.getHealth()));
            }

            System.out.println("Игра сохранена: " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения игры: " + e.getMessage());
        }
    }

    private String generateSaveFileName (String playerName){
        String cleanName = playerName.replaceAll("[^a-zA-Z0-9]", "_");
        return String.format("%s_%s.csv",
                cleanName,
                LocalDateTime.now().format(DATE_FORMAT));
    }

}


//c редактором - добавление кастомных препятствий - свой символ, тип, со штрафом/без, можно ли пройти. после создарния карты вывести в JSON с описанием всчех клеток