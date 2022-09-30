package model;

import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class StrategoTest {

    Stratego c;

    @BeforeAll
    public void setUp() {
        System.out.println("setUp");
        Piece[][] board = new Piece[][] {
                { new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT), new Piece(PieceType.PLAYER2, ArmyPiece.MINER),
                        new Piece(PieceType.PLAYER2, ArmyPiece.FLAG), new Piece(PieceType.PLAYER2, ArmyPiece.CAPTAIN),
                        new Piece(PieceType.PLAYER2, ArmyPiece.BOMB), new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                        new Piece(PieceType.PLAYER2, ArmyPiece.LIEUTENANT),
                        new Piece(PieceType.PLAYER2, ArmyPiece.SERGEANT),
                        new Piece(PieceType.PLAYER2, ArmyPiece.GENERAL),
                        new Piece(PieceType.PLAYER2, ArmyPiece.MAJOR) },
                { new Piece(PieceType.PLAYER2, ArmyPiece.COLONEL), new Piece(PieceType.PLAYER2, ArmyPiece.BOMB),
                        new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT), new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                        new Piece(PieceType.PLAYER2, ArmyPiece.BOMB), new Piece(PieceType.PLAYER2, ArmyPiece.SPY),
                        new Piece(PieceType.PLAYER2, ArmyPiece.SERGEANT),
                        new Piece(PieceType.PLAYER2, ArmyPiece.LIEUTENANT),
                        new Piece(PieceType.PLAYER2, ArmyPiece.LIEUTENANT),
                        new Piece(PieceType.PLAYER2, ArmyPiece.COLONEL) },
                { new Piece(PieceType.PLAYER2, ArmyPiece.MINER), new Piece(PieceType.PLAYER2, ArmyPiece.CAPTAIN),
                        new Piece(PieceType.PLAYER2, ArmyPiece.SERGEANT), new Piece(PieceType.PLAYER2, ArmyPiece.MAJOR),
                        new Piece(PieceType.PLAYER2, ArmyPiece.BOMB), new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                        new Piece(PieceType.PLAYER2, ArmyPiece.CAPTAIN), new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                        new Piece(PieceType.PLAYER2, ArmyPiece.LIEUTENANT),
                        new Piece(PieceType.PLAYER2, ArmyPiece.MINER) },
                { new Piece(PieceType.PLAYER2, ArmyPiece.BOMB), new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT),
                        new Piece(PieceType.PLAYER2, ArmyPiece.SCOUT), new Piece(PieceType.PLAYER2, ArmyPiece.BOMB),
                        new Piece(PieceType.PLAYER2, ArmyPiece.SERGEANT), new Piece(PieceType.PLAYER2, ArmyPiece.MINER),
                        new Piece(PieceType.PLAYER2, ArmyPiece.MAJOR), new Piece(PieceType.PLAYER2, ArmyPiece.CAPTAIN),
                        new Piece(PieceType.PLAYER2, ArmyPiece.MINER),
                        new Piece(PieceType.PLAYER2, ArmyPiece.MARSHAL) },
                { new Piece(PieceType.EMPTY), new Piece(PieceType.EMPTY), new Piece(PieceType.LAKE),
                        new Piece(PieceType.LAKE), new Piece(PieceType.EMPTY), new Piece(PieceType.EMPTY),
                        new Piece(PieceType.LAKE), new Piece(PieceType.LAKE), new Piece(PieceType.EMPTY),
                        new Piece(PieceType.EMPTY) },
                { new Piece(PieceType.EMPTY), new Piece(PieceType.EMPTY), new Piece(PieceType.LAKE),
                        new Piece(PieceType.LAKE), new Piece(PieceType.EMPTY), new Piece(PieceType.EMPTY),
                        new Piece(PieceType.LAKE), new Piece(PieceType.LAKE), new Piece(PieceType.EMPTY),
                        new Piece(PieceType.EMPTY) },
                { new Piece(PieceType.PLAYER1, ArmyPiece.BOMB), new Piece(PieceType.PLAYER1, ArmyPiece.CAPTAIN),
                        new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT), new Piece(PieceType.PLAYER1, ArmyPiece.MAJOR),
                        new Piece(PieceType.PLAYER1, ArmyPiece.LIEUTENANT),
                        new Piece(PieceType.PLAYER1, ArmyPiece.SERGEANT), new Piece(PieceType.PLAYER1, ArmyPiece.BOMB),
                        new Piece(PieceType.PLAYER1, ArmyPiece.LIEUTENANT),
                        new Piece(PieceType.PLAYER1, ArmyPiece.MARSHAL),
                        new Piece(PieceType.PLAYER1, ArmyPiece.COLONEL) },
                { new Piece(PieceType.PLAYER1, ArmyPiece.GENERAL), new Piece(PieceType.PLAYER1, ArmyPiece.MINER),
                        new Piece(PieceType.PLAYER1, ArmyPiece.CAPTAIN), new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                        new Piece(PieceType.PLAYER1, ArmyPiece.BOMB), new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                        new Piece(PieceType.PLAYER1, ArmyPiece.LIEUTENANT),
                        new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT), new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                        new Piece(PieceType.PLAYER1, ArmyPiece.LIEUTENANT) },
                { new Piece(PieceType.PLAYER1, ArmyPiece.MINER), new Piece(PieceType.PLAYER1, ArmyPiece.CAPTAIN),
                        new Piece(PieceType.PLAYER1, ArmyPiece.BOMB), new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                        new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT), new Piece(PieceType.PLAYER1, ArmyPiece.SERGEANT),
                        new Piece(PieceType.PLAYER1, ArmyPiece.BOMB), new Piece(PieceType.PLAYER1, ArmyPiece.COLONEL),
                        new Piece(PieceType.PLAYER1, ArmyPiece.MINER), new Piece(PieceType.PLAYER1, ArmyPiece.MAJOR) },
                { new Piece(PieceType.PLAYER1, ArmyPiece.SERGEANT), new Piece(PieceType.PLAYER1, ArmyPiece.BOMB),
                        new Piece(PieceType.PLAYER1, ArmyPiece.MINER), new Piece(PieceType.PLAYER1, ArmyPiece.SCOUT),
                        new Piece(PieceType.PLAYER1, ArmyPiece.MINER), new Piece(PieceType.PLAYER1, ArmyPiece.SERGEANT),
                        new Piece(PieceType.PLAYER1, ArmyPiece.CAPTAIN), new Piece(PieceType.PLAYER1, ArmyPiece.SPY),
                        new Piece(PieceType.PLAYER1, ArmyPiece.FLAG), new Piece(PieceType.PLAYER1, ArmyPiece.MAJOR) },
        };
        c = new Stratego(board);
    }

    @AfterAll
    public void tearDown() {
        System.out.println("tearDown");
        c = null;
    }

    @Test
    public void testMove() {
        System.out.println("testMove");
        Move m;

        m = c.move(Player.PLAYER1, new Cell(5, 0), new Cell(4, 0));
        Assertions.assertEquals(Move.INVALID, m, "Move piece to EMPTY space.");
        m = c.move(Player.PLAYER1, new Cell(6, 0), new Cell(5, 0));
        Assertions.assertEquals(Move.INVALID, m, "Move BOMB.");
        m = c.move(Player.PLAYER1, new Cell(6, 1), new Cell(5, 1));
        Assertions.assertEquals(Move.VALID, m, "Move piece.");
        m = c.move(Player.PLAYER2, new Cell(3, 2), new Cell(4, 2));
        Assertions.assertEquals(Move.INVALID, m, "Move piece to LAKE.");
        m = c.move(Player.PLAYER2, new Cell(3, 5), new Cell(4, 5));
        Assertions.assertEquals(Move.VALID, m, "Move piece.");
        m = c.move(Player.PLAYER1, new Cell(7, 0), new Cell(6, 0));
        Assertions.assertEquals(Move.INVALID, m, "Move piece to BOMB being of the same player.");
        m = c.move(Player.PLAYER2, new Cell(3, 1), new Cell(4, 1));
        Assertions.assertEquals(Move.INVALID, m, "Move piece when its not my turn.");
        m = c.move(Player.PLAYER1, new Cell(6, 4), new Cell(5, 4));
        Assertions.assertEquals(Move.VALID, m, "Move piece.");
        m = c.move(Player.PLAYER2, new Cell(3, 1), new Cell(5, 1));
        Assertions.assertEquals(Move.VALID, m, "Scout run and attack.");
        m = c.move(Player.PLAYER1, new Cell(7, 1), new Cell(6, 1));
        Assertions.assertEquals(Move.VALID, m, "Move piece.");
        m = c.move(Player.PLAYER2, new Cell(2, 1), new Cell(3, 1));
        Assertions.assertEquals(Move.VALID, m, "Move piece.");
        m = c.move(Player.PLAYER1, new Cell(5, 1), new Cell(4, 1));
        Assertions.assertEquals(Move.VALID, m, "Move piece.");
        m = c.move(Player.PLAYER2, new Cell(3, 1), new Cell(4, 1));
        Assertions.assertEquals(Move.VALID, m, "Capture piece with same rank.");
        m = c.move(Player.PLAYER1, new Cell(6, 1), new Cell(5, 1));
        Assertions.assertEquals(Move.VALID, m, "Move piece.");
        m = c.move(Player.PLAYER2, new Cell(3, 2), new Cell(3, 1));
        Assertions.assertEquals(Move.VALID, m, "Move piece.");
        m = c.move(Player.PLAYER1, new Cell(5, 1), new Cell(5, 0));
        Assertions.assertEquals(Move.VALID, m, "Move piece.");
        m = c.move(Player.PLAYER2, new Cell(3, 1), new Cell(7, 1));
        Assertions.assertEquals(Move.VALID, m, "Scout run.");
        m = c.move(Player.PLAYER1, new Cell(5, 0), new Cell(4, 0));
        Assertions.assertEquals(Move.VALID, m, "Move piece.");
        m = c.move(Player.PLAYER2, new Cell(3, 4), new Cell(4, 4));
        Assertions.assertEquals(Move.VALID, m, "Move piece.");
        m = c.move(Player.PLAYER1, new Cell(4, 0), new Cell(3, 0));
        Assertions.assertEquals(Move.VALID, m, "MINER attacking BOMB.");
        m = c.move(Player.PLAYER2, new Cell(7, 1), new Cell(1, 1));
        Assertions.assertEquals(Move.INVALID, m, "SCOUT attacking piece of the same player.");
        m = c.move(Player.PLAYER2, new Cell(7, 1), new Cell(6, 2));
        Assertions.assertEquals(Move.INVALID, m, "SCOUT attacking piece in the diagonal.");
        m = c.move(Player.PLAYER1, new Cell(6, 9), new Cell(5, 9));
        Assertions.assertEquals(Move.INVALID, m, "Moving piece of PLAYER1 when it is the turn of PLAYER2.");
        m = c.move(Player.PLAYER2, new Cell(4, 4), new Cell(5, 4));
        Assertions.assertEquals(Move.VALID, m, "PLAYER2 SERGEANT attacking PLAYER1 LIEUTENANT top to bottom.");
        m = c.move(Player.PLAYER1, new Cell(8, 1), new Cell(7, 1));
        Assertions.assertEquals(Move.VALID, m, "PLAYER1 CAPTAIN attacking PLAYER2 SCOUT bottom to top.");
        m = c.move(Player.PLAYER2, new Cell(2, 0), new Cell(3, 0));
        Assertions.assertEquals(Move.VALID, m, "PLAYER2 MINER attacking PLAYER1 MINER top to bottom.");
        System.out.println(JsonbBuilder.create().toJson(c.getGraveyard()));
        System.out.println(c.toString());
    }
}
