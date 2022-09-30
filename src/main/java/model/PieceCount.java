package model;

public class PieceCount {

    private ArmyPiece piece;
    private long count;

    public PieceCount() {
    }

    public PieceCount(ArmyPiece piece, long count) {
        this.piece = piece;
        this.count = count;
    }

    public ArmyPiece getPiece() {
        return piece;
    }

    public void setPiece(ArmyPiece piece) {
        this.piece = piece;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
