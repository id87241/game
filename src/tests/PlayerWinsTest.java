package tests;

import controller.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import model.Cell;
import model.GameMap;
import model.GameResult;

public class PlayerWinsTest {

    private GameController gameController;
    private GameMap map;

    @BeforeEach
    public void setUp() {
        gameController = new GameController(5, 5, "Tester");
        map = gameController.getMap();
    }

    //Игрок побеждает, захватив замок бота
    @Test
    public void testPlayerWinsByCapturingBotCastle() {
        Cell botCastle = map.findBotCastle();
        botCastle.setUnit(new TestUnit(true)); // Юнит игрока

        assertEquals(GameResult.PLAYER_WINS, gameController.checkGameOver());
    }

    //Игрок побеждает, уничтожив все юниты бота
    @Test
    public void testPlayerWinsByEliminatingAllBotUnits() {
        //Удаление юнитов бота
        map.getCellList().forEach(cell -> {
            if (cell.getUnit() != null && !cell.getUnit().isPlayerUnit()) {
                cell.setUnit(null); // Удаляем юнитов бота
            }
        });

        Cell playerCastle = map.findPlayerCastle();
        playerCastle.setUnit(new TestUnit(true));

        assertEquals(GameResult.PLAYER_WINS, gameController.checkGameOver());
    }

    //Игра продолжается, если нет победителя
    @Test
    public void testGameContinuesWhenNoWinner() {
        // Оставляем замки и юнитов в "нейтральном" состоянии
        Cell playerCastle = map.findPlayerCastle();
        Cell botCastle = map.findBotCastle();
        playerCastle.setUnit(new TestUnit(true));  // Игрок контролирует свой замок
        botCastle.setUnit(new TestUnit(false));   // Бот контролирует свой замок

        map.getCellList().get(1).setUnit(new TestUnit(true));
        map.getCellList().get(2).setUnit(new TestUnit(false));

        assertEquals(GameResult.CONTINUE, gameController.checkGameOver());
    }
}
