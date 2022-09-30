package model;

public enum ArmyPiece {
    SPY, SCOUT, MINER, SERGEANT, LIEUTENANT, CAPTAIN, MAJOR, COLONEL, GENERAL, MARSHAL, BOMB, FLAG, ENEMY;

    public String getName() {
        return this.name();
    }
    
    public int getNumber() {
        return this.ordinal();
    }
}
