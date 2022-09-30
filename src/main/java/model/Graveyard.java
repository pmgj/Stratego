package model;

import java.util.List;
import jakarta.json.bind.JsonbBuilder;

public class Graveyard {

    private Player player;
    private List<PieceCount> pieceCount;

    public Graveyard() {
    }

    public Graveyard(Player player, List<PieceCount> pieceCount) {
        this.player = player;
        this.pieceCount = pieceCount;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<PieceCount> getPieceCount() {
        return pieceCount;
    }

    public void setPieceCount(List<PieceCount> pieceCount) {
        this.pieceCount = pieceCount;
    }

    @Override
    public String toString() {
        return JsonbBuilder.create().toJson(this);
    }
}
