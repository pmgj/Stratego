package controller;

import java.util.List;

import model.Cell;
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
    private Piece attackingPiece;
    private Piece defendingPiece;
    private Cell attackingCell;
    private Cell defendingCell;

    public Message() {
    }

    public Message(ConnectionType type, Player turn) {
        this.type = type;
        this.turn = turn;
    }

    public Message(ConnectionType type, Player turn, List<Graveyard> g) {
        this.type = type;
        this.turn = turn;
        this.graveyard = g;
    }

    public Message(ConnectionType type, Player turn, Piece[][] board, List<Graveyard> g) {
        this.type = type;
        this.turn = turn;
        this.board = board;
        this.graveyard = g;
    }

    public Message(ConnectionType type, List<Graveyard> g, Winner winner) {
        this.type = type;
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

    public Piece getAttackingPiece() {
        return attackingPiece;
    }

    public void setAttackingPiece(Piece attackingPiece) {
        this.attackingPiece = attackingPiece;
    }

    public Piece getDefendingPiece() {
        return defendingPiece;
    }

    public void setDefendingPiece(Piece defendingPiece) {
        this.defendingPiece = defendingPiece;
    }

    public Cell getAttackingCell() {
        return attackingCell;
    }

    public void setAttackingCell(Cell attackingCell) {
        this.attackingCell = attackingCell;
    }

    public Cell getDefendingCell() {
        return defendingCell;
    }

    public void setDefendingCell(Cell defendingCell) {
        this.defendingCell = defendingCell;
    }
}
