public class Move {
    /**
     * Stores a Move, including the MainBoard, CellBoard index, Cell index, player, and value.
     */

    private MainBoard board;
    private int cellBoard;
    private int cell;
    private int player;
    private int value;

    public Move(MainBoard mb, int cb, int c, int p, int v) {
        board = mb;
        cellBoard = cb;
        cell = c;
        player = p;
        value = v;
    }

    /**
     * Gets the MainBoard for the move.
     * @return this.board
     */
    public MainBoard getBoard() {
        return board;
    }

    /**
     * Gets the CellBoard index for the move.
     * @return this.cellBoard
     */
    public int getCellBoard() {
        return cellBoard;
    }

    /**
     * Gets the Cell index for the move.
     * @return this.cell
     */
    public int getCell() {
        return cell;
    }

    /**
     * Gets the value of the Move.
     * @return this.value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the Move.
     * @param v the value to set
     */
    public void setValue(int v) {
        this.value = v;
    }

    /**
     * Expresses the Move in a String.
     * @return String
     */
    @Override
    public String toString() {
        return "Player: " + player + " Board: " + this.getCellBoard() + " Cell: " + this.getCell() + " Value: " + this.getValue();
    }
}
