package controller;

import model.Cell;
import model.GameMap;
import model.GameResult;
import view.MapRenderer;
import view.PlayerMenuHandler;

import java.util.List;

public class GameController {
    private GameMap map;
    private boolean isPlayerTurn;
    private MapRenderer renderer;
    private PlayerMenuHandler playerMenuHandler;
    private BotMovesController botMovesController;

    public GameController(int mapWidth, int mapHeight, String player) {
        map = new GameMap(mapWidth, mapHeight, player);
        map.initializeDefaultMap();

        isPlayerTurn = true;
        renderer = new MapRenderer();
        playerMenuHandler = new PlayerMenuHandler(map, renderer);
        botMovesController = new BotMovesController(map);
    }

    public void start() {
        // Основной игровой цикл
        while (true) {
            System.out.println("rendering map");
            renderer.render(map);

            if (isPlayerTurn) {
                playerTurn();
                /*
                 Если после хода игрока бот все еще выигрывает, значит за свой ход игрок не смог ничего сделать,
                 ни докупить юнита, не отбить замок -> он проиграл
                 на этом держится факт того, что замок нужно удеражать хотя бы 1 ход.
                */
                if (checkGameOver() == GameResult.BOT_WINS){
                    break;
                }
            } else {
                enemyTurn();
                // то же, что и у игрока, но наоборот
                if(checkGameOver()==GameResult.PLAYER_WINS){
                    break;
                }
            }
            resetUnitsActions();
            isPlayerTurn = !isPlayerTurn;
        }
        renderer.render(map);
        System.out.println("Игра окончена\n");
        switch (checkGameOver()){
            case GameResult.BOT_WINS -> System.out.println("Победа бота");
            case GameResult.PLAYER_WINS -> System.out.println("Игрок победил");
        }
    }


    private void playerTurn() {
        System.out.println("\nХод игрока");
        playerMenuHandler.handlePlayerTurn();
    }

    private void enemyTurn() {
        System.out.println("\nХод компьютера");
        botMovesController.handleBotMove();
    }

    public GameResult checkGameOver() {

        Cell playerCastleCell = map.findPlayerCastle();
        if (playerCastleCell.getUnit() != null) {
            if(!playerCastleCell.getUnit().isPlayerUnit()){
                return GameResult.BOT_WINS;
            }
        }
        Cell botCastleCell = map.findBotCastle();
        if (botCastleCell.getUnit()!=null){
            if(botCastleCell.getUnit().isPlayerUnit()){
                return GameResult.PLAYER_WINS;
            }
        }

        List<Cell> list = map.getCellList();
        boolean playerHasUnits = false;
        boolean botHasUnits = false;
        for (Cell cell : list){
            if (cell.getUnit()!=null){
                if (cell.getUnit().isPlayerUnit()) {playerHasUnits = true;}
                if (!cell.getUnit().isPlayerUnit()) {botHasUnits = true;}
            }
        }
        if (!playerHasUnits) return GameResult.BOT_WINS;
        if (!botHasUnits) return GameResult.PLAYER_WINS;

        return GameResult.CONTINUE;
    }

    private void resetUnitsActions(){
        List<Cell> list = map.getCellList();
        for(int i = 0; i < list.size()-1; ++i){
            if (list.get(i).getUnit() != null){
                list.get(i).getUnit().setHasMoved(false);
                list.get(i).getUnit().setHasAttacked(false);
            }
        }
    }

    public GameMap getMap() {
        return map;
    }
}

