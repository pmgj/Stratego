package model;

public class Piece implements Cloneable {

    private PieceType type;
    private ArmyPiece army;
    private boolean visible;

    public Piece() {
    }

    public Piece(PieceType jogador, ArmyPiece numero) {
        this.type = jogador;
        this.army = numero;
    }

    public Piece(PieceType jogador) {
        this.type = jogador;
    }

    public PieceType getPlayer() {
        return type;
    }

    public void setPlayer(PieceType jogador) {
        this.type = jogador;
    }

    public ArmyPiece getArmyPiece() {
        return army;
    }

    public void setArmyPiece(ArmyPiece numero) {
        this.army = numero;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        String t = "";
        switch (type) {
            case PLAYER1:
                t = "PLY1";
                break;
            case PLAYER2:
                t = "PLY2";
                break;
            case EMPTY:
                t = "EMPT";
                break;
            case LAKE:
                t = "LAKE";
                break;
        }
        String a = "";
        if (army == null) {
            a = "NULL";
        } else {
            switch (army) {
                case BOMB:
                    a = "BOMB";
                    break;
                case COLONEL:
                    a = "CLNL";
                    break;
                case CAPTAIN:
                    a = "CAPT";
                    break;
                case ENEMY:
                    a = "ENEM";
                    break;
                case FLAG:
                    a = "FLAG";
                    break;
                case GENERAL:
                    a = "GNRL";
                    break;
                case LIEUTENANT:
                    a = "LTNT";
                    break;
                case MAJOR:
                    a = "MAJR";
                    break;
                case MARSHAL:
                    a = "MARS";
                    break;
                case MINER:
                    a = "MINR";
                    break;
                case SCOUT:
                    a = "SCOT";
                    break;
                case SERGEANT:
                    a = "SERG";
                    break;
                case SPY:
                    a = "SPY ";
                    break;
            }
        }
        return "{" + t + ", " + a + "}";
    }

//    @Override
//    public String toString() {
//        return "{" + type + ", " + army + "}";
//    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
