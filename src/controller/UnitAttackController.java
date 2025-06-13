package controller;

import model.Cell;
import model.GameMap;
import model.units.Hero;

public class UnitAttackController {
    private final GameMap map;
    private final static int KILL_REWARD = 500;

    // этим методом пользуется и бот
    public UnitAttackController(GameMap map) {
        this.map = map;
    }

    public void attackUnit(int fromX, int fromY, int toX, int toY) {
        if (!map.isValidPosition(toX, toY)) {
            return;
        }

        Cell fromCell = map.getCell(fromX, fromY);
        Cell targetCell = map.getCell(toX, toY);

        if (fromCell.getUnit() == null || targetCell.getUnit() == null) {
            return;
        }

        if (fromCell.getUnit().isHasAttacked()) return;
        if(targetCell.getUnit() instanceof Hero && ((Hero) targetCell.getUnit()).isImmune()){
            System.out.println("Цель неуязвима!");
        }
        int attackerStrength = fromCell.getUnit().getAttack();

        if(targetCell.getUnit().isPlayerUnit()){
            System.out.println("Юнит игрока " + targetCell.getUnit() + "\nПолучил " + attackerStrength + " единиц урона");
            targetCell.getUnit().takeDamage(attackerStrength);
            System.out.println("текущее здоровье: " + targetCell.getUnit().getHealth() +"\n");
        }else {
            System.out.println("Юнит бота " + targetCell.getUnit() + "\nПолучил " + attackerStrength + " единиц урона");
            targetCell.getUnit().takeDamage(attackerStrength);
            System.out.println("текущее здоровье: " + targetCell.getUnit().getHealth() +"\n");
        }


        if (targetCell.getUnit().getHealth() <= 0) {
            targetCell.setUnit(null);

            // Награда тому кто уничтожил юнита
            if(fromCell.getUnit().isPlayerUnit()){
                map.findPlayerCastle().getCastle().setGold(
                        map.findPlayerCastle().getCastle().getGold() + KILL_REWARD
                );

                /*
                 Вообще, не очень хорошо, что UI в контроллере, опционально можно перенести как-то во view
                 тут вопрос как раз о рефлексии на атаку. Можно добавить observer, но чет сложго и не требуется, так что ну его
                 Это же касается всех других элементов ui вне ui слоя
                */
                System.out.println("Юнит врага уничтожен, награда: " + KILL_REWARD);
            }
            else {
                map.findBotCastle().getCastle().setGold(
                        map.findBotCastle().getCastle().getGold() + KILL_REWARD
                );
                System.out.println("Юнит игрока уничтожен");

            }
        }

        fromCell.getUnit().setHasAttacked(true);
    }

    public void attackUnit(Cell from, Cell To) {
        attackUnit(from.getX(), from.getY(), To.getX(), To.getY());
    }
}
