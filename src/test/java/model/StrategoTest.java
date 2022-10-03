package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.json.bind.JsonbBuilder;

public class StrategoTest {

        @Test
        public void testMove() {
                Piece[][] board = new Piece[][] {
                                { new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.MINER),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.FLAG),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.CAPTAIN),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.BOMB),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.LIEUTENANT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.SERGEANT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.GENERAL),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.MAJOR) },
                                { new Piece(PieceType.PLAYER2, ArmyPiece.COLONEL),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.BOMB),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.BOMB),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.SPY),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.SERGEANT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.LIEUTENANT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.LIEUTENANT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.COLONEL) },
                                { new Piece(PieceType.PLAYER2, ArmyPiece.MINER),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.CAPTAIN),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.SERGEANT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.MAJOR),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.BOMB),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.CAPTAIN),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.LIEUTENANT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.MINER) },
                                { new Piece(PieceType.PLAYER2, ArmyPiece.BOMB),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.BOMB),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.SERGEANT),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.MINER),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.MAJOR),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.CAPTAIN),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.MINER),
                                                new Piece(PieceType.PLAYER2, ArmyPiece.MARSHAL) },
                                { new Piece(PieceType.EMPTY), new Piece(PieceType.EMPTY), new Piece(PieceType.LAKE),
                                                new Piece(PieceType.LAKE), new Piece(PieceType.EMPTY),
                                                new Piece(PieceType.EMPTY),
                                                new Piece(PieceType.LAKE), new Piece(PieceType.LAKE),
                                                new Piece(PieceType.EMPTY),
                                                new Piece(PieceType.EMPTY) },
                                { new Piece(PieceType.EMPTY), new Piece(PieceType.EMPTY), new Piece(PieceType.LAKE),
                                                new Piece(PieceType.LAKE), new Piece(PieceType.EMPTY),
                                                new Piece(PieceType.EMPTY),
                                                new Piece(PieceType.LAKE), new Piece(PieceType.LAKE),
                                                new Piece(PieceType.EMPTY),
                                                new Piece(PieceType.EMPTY) },
                                { new Piece(PieceType.PLAYER1, ArmyPiece.BOMB),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.CAPTAIN),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.MAJOR),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.LIEUTENANT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.SERGEANT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.BOMB),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.LIEUTENANT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.MARSHAL),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.COLONEL) },
                                { new Piece(PieceType.PLAYER1, ArmyPiece.GENERAL),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.MINER),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.CAPTAIN),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.BOMB),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.LIEUTENANT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.LIEUTENANT) },
                                { new Piece(PieceType.PLAYER1, ArmyPiece.MINER),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.CAPTAIN),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.BOMB),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.SERGEANT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.BOMB),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.COLONEL),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.MINER),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.MAJOR) },
                                { new Piece(PieceType.PLAYER1, ArmyPiece.SERGEANT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.BOMB),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.MINER),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.MINER),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.SERGEANT),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.CAPTAIN),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.SPY),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.FLAG),
                                                new Piece(PieceType.PLAYER1, ArmyPiece.MAJOR) },
                };
                Stratego c = new Stratego(board);

                try {
                        c.move(Player.PLAYER1, new Cell(5, 0), new Cell(4, 0));
                        Assertions.fail("Move piece to EMPTY space.");
                } catch (Exception ex) {

                }
                try {
                        c.move(Player.PLAYER1, new Cell(6, 0), new Cell(5, 0));
                        Assertions.fail("Move BOMB.");
                } catch (Exception ex) {

                }
                try {
                        c.move(Player.PLAYER1, new Cell(6, 1), new Cell(5, 1));
                } catch (Exception ex) {
                        Assertions.fail("Move piece.");
                }
                try {
                        c.move(Player.PLAYER2, new Cell(3, 2), new Cell(4, 2));
                        Assertions.fail("Move piece to LAKE.");
                } catch (Exception ex) {

                }
                try {
                        c.move(Player.PLAYER2, new Cell(3, 5), new Cell(4, 5));
                } catch (Exception ex) {
                        Assertions.fail("Move piece.");
                }
                try {
                        c.move(Player.PLAYER1, new Cell(7, 0), new Cell(6, 0));
                        Assertions.fail("Move piece to BOMB being of the same player.");
                } catch (Exception ex) {

                }
                try {
                        c.move(Player.PLAYER2, new Cell(3, 1), new Cell(4, 1));
                        Assertions.fail("Move piece when its not my turn.");
                } catch (Exception ex) {

                }
                try {
                        c.move(Player.PLAYER1, new Cell(6, 4), new Cell(5, 4));
                        c.move(Player.PLAYER2, new Cell(3, 1), new Cell(5, 1));
                        c.move(Player.PLAYER1, new Cell(7, 1), new Cell(6, 1));
                        c.move(Player.PLAYER2, new Cell(2, 1), new Cell(3, 1));
                        c.move(Player.PLAYER1, new Cell(5, 1), new Cell(4, 1));
                        c.move(Player.PLAYER2, new Cell(3, 1), new Cell(4, 1));
                        c.move(Player.PLAYER1, new Cell(6, 1), new Cell(5, 1));
                        c.move(Player.PLAYER2, new Cell(3, 2), new Cell(3, 1));
                        c.move(Player.PLAYER1, new Cell(5, 1), new Cell(5, 0));
                        c.move(Player.PLAYER2, new Cell(3, 1), new Cell(7, 1));
                        c.move(Player.PLAYER1, new Cell(5, 0), new Cell(4, 0));
                        c.move(Player.PLAYER2, new Cell(3, 4), new Cell(4, 4));
                        c.move(Player.PLAYER1, new Cell(4, 0), new Cell(3, 0));
                } catch (Exception ex) {
                        Assertions.fail("Move piece.");
                }
                try {
                        c.move(Player.PLAYER2, new Cell(7, 1), new Cell(1, 1));
                        Assertions.fail("SCOUT attacking piece of the same player.");
                } catch (Exception ex) {

                }
                try {
                        c.move(Player.PLAYER2, new Cell(7, 1), new Cell(6, 2));
                        Assertions.fail("SCOUT attacking piece in the diagonal.");
                } catch (Exception ex) {

                }
                try {
                        c.move(Player.PLAYER1, new Cell(6, 9), new Cell(5, 9));
                        Assertions.fail("Moving piece of PLAYER1 when it is the turn of PLAYER2.");
                } catch (Exception ex) {

                }
                try {
                        c.move(Player.PLAYER2, new Cell(4, 4), new Cell(5, 4));
                        c.move(Player.PLAYER1, new Cell(8, 1), new Cell(7, 1));
                        c.move(Player.PLAYER2, new Cell(2, 0), new Cell(3, 0));
                } catch (Exception ex) {
                        Assertions.fail("Move piece.");
                }
                System.out.println(JsonbBuilder.create().toJson(c.getGraveyard()));
                System.out.println(c.toString());
        }
}
