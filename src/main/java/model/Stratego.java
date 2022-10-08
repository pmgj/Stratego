package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Stratego {

    private final Piece[][] board;
    private Player turn = Player.PLAYER1;
    private Winner winner = Winner.NONE;
    private int rows = 10;
    private int cols = 10;
    private Piece attackingPiece;
    private Piece defendingPiece;
    private Winner attackResult;

    public Stratego() {
        this.board = new Piece[this.rows][this.cols];
        int[] PIECES_QUANTITIES = { 1, 8, 5, 4, 4, 4, 3, 2, 1, 1, 6, 1, 0 };
        var pecas1 = new ArrayList<Piece>();
        var pecas2 = new ArrayList<Piece>();
        for (var peca : ArmyPiece.values()) {
            int index = peca.ordinal();
            for (int i = 0; i < PIECES_QUANTITIES[index]; i++) {
                pecas1.add(new Piece(PieceType.PLAYER1, peca));
                pecas2.add(new Piece(PieceType.PLAYER2, peca));
            }
        }
        Collections.shuffle(pecas1);
        Collections.shuffle(pecas2);
        for (int i = 4; i < 6; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.board[i][j] = (j == 2 || j == 3 || j == 6 || j == 7) ? new Piece(PieceType.LAKE)
                        : new Piece(PieceType.EMPTY);
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.board[i][j] = pecas2.remove(0);
                this.board[i + 6][j] = pecas1.remove(0);
            }
        }
    }

    public Stratego(Piece[][] board) {
        this.board = board;
    }

    public Player getTurn() {
        return turn;
    }

    public Winner getWinner() {
        return winner;
    }

    public Piece getAttackingPiece() {
        return attackingPiece;
    }

    public Piece getDefendingPiece() {
        return defendingPiece;
    }

    public Winner getAttackResult() {
        return attackResult;
    }

    private Graveyard getGraveyard(Player player) {
        Function<Piece, Long> countPieces = piece -> Arrays.stream(board).flatMap(row -> Arrays.stream(row))
                .filter(p -> p.equals(piece)).count();
        // Function<Piece, Long> countPieces = piece -> Arrays.stream(board).flatMap(row
        // -> Arrays.stream(row))
        // .filter(p -> p.getPlayer() == piece.getPlayer() && p.getArmyPiece() ==
        // piece.getArmyPiece()).count();
        var numPieces = new ArrayList<PieceCount>();
        for (var peca : ArmyPiece.values()) {
            int index = peca.ordinal();
            if (index < 11) {
                var pc = new PieceCount(peca, countPieces
                        .apply(new Piece(player == Player.PLAYER1 ? PieceType.PLAYER1 : PieceType.PLAYER2, peca)));
                numPieces.add(pc);
            }
        }
        return new Graveyard(player, numPieces);
    }

    public List<Graveyard> getGraveyard() {
        var list = new ArrayList<Graveyard>();
        list.add(this.getGraveyard(Player.PLAYER1));
        list.add(this.getGraveyard(Player.PLAYER2));
        return list;
    }

    private Winner isGameOver(Cell endCell) {
        int dr = endCell.getX(), dc = endCell.getY();
        /* Se um jogador não tem mais peças a mexer, perde o jogo */
        Function<PieceType, Long> numMovablePieces = player -> Arrays.stream(board).flatMap(row -> Arrays.stream(row))
                .filter(p -> p.getPlayer() == player && p.getArmyPiece() != ArmyPiece.FLAG
                        && p.getArmyPiece() != ArmyPiece.BOMB)
                .count();
        long player1MovablePieces = numMovablePieces.apply(PieceType.PLAYER1);
        long player2MovablePieces = numMovablePieces.apply(PieceType.PLAYER2);
        if (player1MovablePieces == 0 && player2MovablePieces != 0) {
            return Winner.PLAYER2;
        } else if (player1MovablePieces != 0 && player2MovablePieces == 0) {
            return Winner.PLAYER1;
        } else if (player1MovablePieces == 0 && player2MovablePieces == 0) {
            return Winner.DRAW;
        }
        /* Se um jogador possui peças mas não pode movê-las, perde o jogo */
        boolean j1pm = this.canMove(PieceType.PLAYER1);
        boolean j2pm = this.canMove(PieceType.PLAYER2);
        if (!j1pm && j2pm) {
            return Winner.PLAYER2;
        } else if (j1pm && !j2pm) {
            return Winner.PLAYER1;
        } else if (!j1pm && !j2pm) {
            return Winner.DRAW;
        }
        /* Se um jogador capturou a bandeira, venceu o jogo */
        Piece p2 = board[dr][dc];
        if (p2.getArmyPiece() == ArmyPiece.FLAG) {
            if (p2.getPlayer() == PieceType.PLAYER1) {
                return Winner.PLAYER2;
            } else {
                return Winner.PLAYER1;
            }
        }
        return Winner.NONE;
    }

    private boolean canMove(PieceType player) {
        boolean ok = false;
        for (int i = 0; i < this.rows && !ok; i++) {
            for (int j = 0; j < this.cols && !ok; j++) {
                if (board[i][j].getPlayer() == player) {
                    Cell temp = new Cell(i, j);
                    if (canMove(temp, new Cell(i + 1, j)) || canMove(temp, new Cell(i - 1, j))
                            || canMove(temp, new Cell(i, j + 1)) || canMove(temp, new Cell(i, j - 1))) {
                        ok = true;
                    }
                }
            }
        }
        return ok;
    }

    private boolean canMove(Cell beginCell, Cell endCell) {
        int or = beginCell.getX(), oc = beginCell.getY(), dr = endCell.getX(), dc = endCell.getY();
        /* Origem e destino devem ser diferentes */
        if (beginCell.equals(endCell)) {
            return false;
        }
        /* Origem e destino estão no tabuleiro */
        if (!onBoard(beginCell) || !onBoard(endCell)) {
            return false;
        }
        /* Origem possui uma peça? Tentativa de mover a bandeira ou mina? */
        Piece p1 = board[or][oc];
        if (p1.getPlayer() == PieceType.EMPTY || p1.getArmyPiece() == ArmyPiece.BOMB
                || p1.getArmyPiece() == ArmyPiece.FLAG) {
            return false;
        }
        /* Destino não pode estar no lago */
        Piece p2 = board[dr][dc];
        if (p2.getPlayer() == PieceType.LAKE) {
            return false;
        }
        /*
         * Origem e destino estão na mesma linha ou coluna? Se for um SCOUT, pode andar
         * mais de uma casa na mesma linha e coluna.
         */
        if (dc != oc && dr != or && !scoutRun(beginCell, endCell)) {
            return false;
        }
        return true;
    }

    private boolean onBoard(Cell cell) {
        BiFunction<Integer, Integer, Boolean> inLimit = (value, limit) -> value >= 0 && value < limit;
        return (inLimit.apply(cell.getX(), this.rows) && inLimit.apply(cell.getY(), this.cols));
    }

    private boolean canMove(Player player, Cell beginCell, Cell endCell) {
        /* É a sua vez de jogar? */
        if (turn != player) {
            return false;
        }
        int or = beginCell.getX(), oc = beginCell.getY(), dr = endCell.getX(), dc = endCell.getY();
        /* Está tentando jogar uma peça sua? */
        Piece p1 = board[or][oc];
        if ((p1.getPlayer() == PieceType.PLAYER1 && turn == Player.PLAYER2)
                || (p1.getPlayer() == PieceType.PLAYER2 && turn == Player.PLAYER1)) {
            return false;
        }
        /* Destino não pode conter uma peça do mesmo jogador */
        Piece p2 = board[dr][dc];
        if ((player == Player.PLAYER1 && p2.getPlayer() == PieceType.PLAYER1)
                || (player == Player.PLAYER2 && p2.getPlayer() == PieceType.PLAYER2)) {
            return false;
        }
        return this.canMove(beginCell, endCell);
    }

    private boolean scoutRun(Cell beginCell, Cell endCell) {
        int or = beginCell.getX(), oc = beginCell.getY(), dr = endCell.getX(), dc = endCell.getY();
        if (board[or][oc].getArmyPiece() == ArmyPiece.SCOUT) {
            boolean move = true;
            if (or == dr) {
                for (int i = oc + 1; i < dc && move; i++) {
                    if (board[or][i].getPlayer() != PieceType.EMPTY) {
                        move = false;
                    }
                }
                for (int i = oc - 1; i > dc && move; i--) {
                    if (board[or][i].getPlayer() != PieceType.EMPTY) {
                        move = false;
                    }
                }
            } else if (oc == dc) {
                for (int i = or + 1; i < dr && move; i++) {
                    if (board[i][oc].getPlayer() != PieceType.EMPTY) {
                        move = false;
                    }
                }
                for (int i = or - 1; i > dr && move; i--) {
                    if (board[i][oc].getPlayer() != PieceType.EMPTY) {
                        move = false;
                    }
                }
            }
            if (move && (dr == or || dc == oc)
                    && (board[dr][dc].getPlayer() == PieceType.EMPTY || ((board[or][oc].getPlayer() == PieceType.PLAYER1
                            && board[dr][dc].getPlayer() == PieceType.PLAYER2)
                            || (board[or][oc].getPlayer() == PieceType.PLAYER2
                                    && board[dr][dc].getPlayer() == PieceType.PLAYER1)))) {
                return true;
            }
        }
        return false;
    }

    public void move(Player player, Cell beginCell, Cell endCell) {
        int or = beginCell.getX(), oc = beginCell.getY(), dr = endCell.getX(), dc = endCell.getY();
        /* Fim do jogo? */
        if (this.winner != Winner.NONE) {
            throw new IllegalArgumentException("This game is already finished.");
        }
        /* Pode mover esta peça para este destino? */
        if (!canMove(player, beginCell, endCell)) {
            throw new IllegalArgumentException("This move is invalid.");
        }
        this.attackingPiece = null;
        this.defendingPiece = null;
        this.attackResult = Winner.NONE;
        /* Jogada para casa vazia */
        if (board[dr][dc].getPlayer() == PieceType.EMPTY) {
            board[dr][dc] = board[or][oc];
        } else {
            /* Tentativa de ataque */
            this.attackingPiece = board[or][oc];
            this.defendingPiece = board[dr][dc];
            var atacante = board[or][oc].getArmyPiece();
            var atacado = board[dr][dc].getArmyPiece();
            if (atacante.ordinal() > atacado.ordinal() || (atacante == ArmyPiece.SPY && atacado == ArmyPiece.MARSHAL)
                    || (atacante == ArmyPiece.MINER && atacado == ArmyPiece.BOMB)) {
                board[dr][dc] = board[or][oc];
                this.attackResult = this.turn == Player.PLAYER1 ? Winner.PLAYER1 : Winner.PLAYER2;
            } else if (atacante.ordinal() == atacado.ordinal()) {
                board[dr][dc] = new Piece(PieceType.EMPTY);
                this.attackResult = Winner.DRAW;
            } else if (atacante.ordinal() < atacado.ordinal()) {
                this.attackResult = this.turn == Player.PLAYER1 ? Winner.PLAYER2 : Winner.PLAYER1;
            }
        }
        board[or][oc] = new Piece(PieceType.EMPTY);
        /* Mudar a vez de jogar */
        turn = turn == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1;
        /* Fim do jogo? */
        this.winner = isGameOver(endCell);
    }

    public Piece[][] hiddenBoard(Player jogador) {
        Piece[][] temp = new Piece[board.length][];
        for (int i = 0; i < temp.length; i++) {
            Piece[] row = new Piece[board[i].length];
            temp[i] = row;
            for (int j = 0; j < row.length; j++) {
                try {
                    if (board[i][j].getPlayer() != PieceType.EMPTY) {
                        Piece p = (Piece) board[i][j].clone();
                        if (((p.getPlayer() == PieceType.PLAYER1 && jogador == Player.PLAYER2)
                                || (p.getPlayer() == PieceType.PLAYER2 && jogador == Player.PLAYER1))
                                && p.getPlayer() != PieceType.LAKE) {
                            p.setArmyPiece(ArmyPiece.ENEMY);
                        }
                        row[j] = p;
                    } else {
                        row[j] = new Piece(PieceType.EMPTY);
                    }
                } catch (CloneNotSupportedException ex) {

                }
            }
        }
        return temp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Piece[] linha : board) {
            sb.append("[");
            sb.append(Arrays.toString(linha));
            sb.append("]\n");
        }
        return sb.toString();
    }
}
