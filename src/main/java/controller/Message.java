package controller;

import java.util.List;
import model.Graveyard;
import model.Piece;
import model.Player;
import model.Winner;

public class Message {
    private ConnectionType type;
    private Player turn;
    private Piece[][] board;
    private List<Graveyard> graveyard;
    private Winner winner;

    public Message() {
    }

    public Message(ConnectionType type, Player turn) {
        this.type = type;
        this.turn = turn;
    }

    public Message(ConnectionType type, Player turn, Piece[][] board, List<Graveyard> g) {
        this.type = type;
        this.turn = turn;
        this.board = board;
        this.graveyard = g;
    }

    public Message(ConnectionType type, Piece[][] board, List<Graveyard> g, Winner winner) {
        this.type = type;
        this.board = board;
        this.graveyard = g;
        this.winner = winner;
    }

    public ConnectionType getType() {
        return type;
    }

    public void setType(ConnectionType type) {
        this.type = type;
    }

    public Player getTurn() {
        return turn;
    }

    public void setTurn(Player turn) {
        this.turn = turn;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void setBoard(Piece[][] tabuleiro) {
        this.board = tabuleiro;
    }

    public List<Graveyard> getGraveyard() {
        return graveyard;
    }

    public void setGraveyard(List<Graveyard> graveyard) {
        this.graveyard = graveyard;
    }

    public Winner getWinner() {
        return winner;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }
}
