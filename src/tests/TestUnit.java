package tests;

import model.units.Unit;

public class TestUnit extends Unit {
    public TestUnit(boolean isPlayerUnit) {
        super(
                100,
                10,
                2,
                "TestUnit",
                isPlayerUnit,
                50
        );
    }

    public void AttackRange(int attackRange) {
        this.setAttackRange(attackRange);
    }
}