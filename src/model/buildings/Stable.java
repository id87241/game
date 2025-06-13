package model.buildings;

import model.units.Hero;

public class Stable extends Building {
    public static final int UPGRADE_COST = 50;
    public Stable() {
        super("Конюшня", 300);
    }

    @Override
    public void applyEffect(Hero hero) {
    }
} 