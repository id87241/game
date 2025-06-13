package model.units;

import java.util.Objects;

public abstract class Unit {
    protected int health;
    protected int attack;
    private int moveRange;
    private int attackRange;
    private final String name;
    private final boolean isPlayerUnit;
    private boolean hasMoved;
    private boolean hasAttacked;
    private final int cost;
    private boolean inSanctuary;

    public Unit(int health, int attack, int moveRange, String name, boolean isPlayerUnit, int cost ) {
        this.health = health;
        this.attack = attack;
        this.moveRange = moveRange;
        this.name = name;
        this.isPlayerUnit = isPlayerUnit;
        this.hasMoved = false;
        this.hasAttacked = false;
        this.attackRange = 1;
        this.cost = cost;
    }

    @Override
    public String toString() {
        String str = this.getName() + " ХП: " + this.getHealth() + " Сила: " + this.getAttack() +
                " Дистанция перемещения: " + this.getMoveRange();

        if(inSanctuary){
            str += "\n Юнит в святилище";
        }
        return str;
    }

    public void setInSanctuary(boolean inSanctuary){
        this.inSanctuary = inSanctuary;
        if (inSanctuary){

            if(this instanceof Hero){
                ((Hero) this).setImmune(true);
                System.out.println("Герой вступил в святилище и получил неуязвимость");
            }

            // Остальные юниты просто бафф
            else {
                this.health += 50;
                System.out.println(this.getName() + " вступил в святилище и получил 50 здоровья");
            }
        }
        else {
            // При выходе из святилища герой теряет неуязвимость
            if(this instanceof Hero){
                ((Hero) this).setImmune(false);
            }
        }

    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getMoveRange() {
        return moveRange;
    }

    public void setMoveRange(int moveRange) {
        this.moveRange = moveRange;
    }

    public String getName() {
        return name;
    }

    public boolean isPlayerUnit() {
        return isPlayerUnit;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean isHasAttacked() {
        return hasAttacked;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getCost() {
        return cost;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}