package view;

import javax.swing.JButton;

public class Square extends JButton {
    private final int row;
    private final int col;

    public Square(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
