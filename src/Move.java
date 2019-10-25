public class Move {

    private MainBoard board;
    private int cellBoard;
    private int cell;
    private int value;

    public Move(MainBoard mb, int cb, int c, int v) {
        board = mb;
        cellBoard = cb;
        cell = c;
        value = v;
    }

    public MainBoard getBoard() {
        return board;
    }

    public int getCellBoard() {
        return cellBoard;
    }

    public int getCell() {
        return cell;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int v) {
        this.value = v;
    }

    @Override
    public String toString() {
        return "Board: " + this.getCellBoard() + " Cell: " + this.getCell() + " Value: " + this.getValue();
    }
}