package model.units;

public class Hero extends Unit {
    private boolean isImmune;

    public Hero( String name, boolean isPlayerHero) {
        super(100, 50, 4, name, isPlayerHero, 400);
    }

    @Override
    public void setInSanctuary(boolean inSanctuary) {

    }

    public boolean isImmune() {
        return isImmune;
    }

    public void setImmune(boolean immune) {
        isImmune = immune;
    }
}
