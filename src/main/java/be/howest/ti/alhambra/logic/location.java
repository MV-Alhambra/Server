package be.howest.ti.alhambra.logic;

public class location {
    private final int col;
    private final int row;

    public location(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}
