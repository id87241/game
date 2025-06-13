package tests;

import controller.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import model.Cell;
import model.GameMap;
import model.GameResult;

public class BotWinsTest {

    private GameController gameController;
    private GameMap map;

    @BeforeEach
    public void setUp() {
        gameController = new GameController(5, 5, "Tester");
        map = gameController.getMap();
    }

    // Бот побеждает, захватив замок игрока
    @Test
    public void testBotWinsByCapturingPlayersCastle() {
        // Находим замок игрока и ставим на него юнита бота
        Cell playerCastle = map.findPlayerCastle();
        playerCastle.setUnit(new TestUnit(false)); // Юнит бота

        assertEquals(GameResult.BOT_WINS, gameController.checkGameOver());
    }

    @Test
    public void testBotWinsByEliminatingAllPlayersUnits() {
        map.getCellList().forEach(cell -> {
            if (cell.getUnit() != null && cell.getUnit().isPlayerUnit()) {
                cell.setUnit(null);
            }
        });

        boolean playerHasUnits = map.getCellList().stream().anyMatch(cell -> cell.getUnit() != null && cell.getUnit().isPlayerUnit());
        assertFalse(playerHasUnits, "У игрока не должно остаться юнитов");

        assertEquals(GameResult.BOT_WINS, gameController.checkGameOver());}

    //Игра продолжается, если нет победителя
    @Test
    public void testGameContinuesWhenNoWinner() {
        // Оставляем замки и юнитов в "нейтральном" состоянии
        Cell playerCastle = map.findPlayerCastle();
        Cell botCastle = map.findBotCastle();
        playerCastle.setUnit(new TestUnit(true));  // Игрок контролирует свой замок
        botCastle.setUnit(new TestUnit(false));   // Бот контролирует свой замок

        map.getCellList().get(1).setUnit(new TestUnit(true));  // Юнит игрока
        map.getCellList().get(2).setUnit(new TestUnit(false)); // Юнит бота

        assertEquals(GameResult.CONTINUE, gameController.checkGameOver());
    }
}

//
