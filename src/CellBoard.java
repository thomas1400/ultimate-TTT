import java.awt.*;

class CellBoard {
    private Rectangle outline;
    private Cell[] cells;
    private int[][][] boardLines;

    private int index;
    private int victory; // 0 for not won, 1 for p1, 2 for p2
    private int lastClicked;

    private CellBoard(CellBoard copy) {
        this.outline = new Rectangle(copy.outline);
        this.cells = new Cell[copy.cells.length];
        for (int i = 0; i < this.cells.length; i++) {
            this.cells[i] = copy.cells[i].copy();
        }
        this.boardLines = new int[copy.boardLines.length]
                [copy.boardLines[0].length]
                [copy.boardLines[0][0].length];
        for (int i = 0; i < this.boardLines.length; i++) {
            for (int j = 0; j < this.boardLines[i].length; j++) {
                this.boardLines[i][j] = copy.boardLines[i][j].clone();
            }
        }

        this.index = copy.index;
        this.victory = copy.victory;
        this.lastClicked = copy.lastClicked;
    }

    CellBoard(MainBoard mb, int i) {
        index = i;
        victory = 0;

        // Calculate center of board from index and main board center
        int[] center = new int[2];
        center[0] = mb.getCenter()[0] + (index % 3 - 1) * mb.getSize() / 3;
        center[1] = mb.getCenter()[1] + (index / 3 - 1) * mb.getSize() / 3;

        int size = (int) (mb.getSize() / 3.0 * 0.9);

        outline = new Rectangle(center[0] - size / 2, center[1] - size / 2, size, size);

        // Create board lines
        boardLines = new int[4][2][2]; // 4 lines with 2 points of 2 coordinates each
        boardLines[0] = new int[][]{
                {center[0] - size / 6, center[1] - size / 2},
                {center[0] - size / 6, center[1] + size / 2}}; // vertical left
        boardLines[1] = new int[][]{
                {center[0] + size / 6, center[1] - size / 2},
                {center[0] + size / 6, center[1] + size / 2}};
        boardLines[2] = new int[][]{
                {center[0] - size / 2, center[1] - size / 6},
                {center[0] + size / 2, center[1] - size / 6}};
        boardLines[3] = new int[][]{
                {center[0] - size / 2, center[1] + size / 6},
                {center[0] + size / 2, center[1] + size / 6}};

        // Create Cells
        cells = new Cell[9];
        int cell_size = (int) (size / 3 * 0.8);
        for (int k = 0; k < 9; k++) {
            int cell_center_x = center[0] + (k % 3 - 1) * size / 3;
            int cell_center_y = center[1] + (k / 3 - 1) * size / 3;
            cells[k] = new Cell(cell_center_x, cell_center_y, cell_size);
        }
    }

    /**
     * Draws this CellBoard using Graphics2D.
     * @param g a Graphics2D instance
     * @param active the active board
     */
    void draw(Graphics2D g, int active) {
        if (victory == 0 && (active == -1 || index == active)) {
            g.setColor(new Color(255, 255, 220));
            g.fill(outline);
        }
        if (victory == 1) {
            g.setColor(new Color(255, 220, 220));
            g.fill(outline);
        }
        if (victory == 2) {
            g.setColor(new Color(220, 220, 255));
            g.fill(outline);
        }
        g.setColor(Color.BLACK);
        for (int[][] board_line : boardLines) {
            int x1 = board_line[0][0];
            int y1 = board_line[0][1];
            int x2 = board_line[1][0];
            int y2 = board_line[1][1];

            g.drawLine(x1, y1, x2, y2);
        }

        for (Cell c : cells) {
            c.draw(g);
        }
    }

    /**
     * Checks if a mouse click makes a valid move.
     * @param clickPt the Point of the mouse click
     * @param player the current player
     * @param active the active board
     * @return the index of the cell clicked, or -1 if not a valid move
     */
    int checkClick(Point clickPt, int player, int active) {
        if (victory != 0 || (index != active && active != -1)) {
            return -1;
        }
        for (int i = 0; i < cells.length; i++) {
            Cell c = cells[i];
            if (c.contains(clickPt) && !c.filled()) {
                lastClicked = i;
                c.fill(player);
                checkVictory(player);
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if a move is possible.
     * @param index the cell to check
     * @return true if the cell is not filled
     */
    boolean checkMove(int index) {
        return !cells[index].filled();
    }

    /**
     * Makes a move in a given cell.
     * @param index the cell index
     * @param player the current player
     */
    void makeMove(int index, int player) {
        cells[index].fill(player);
    }

    /**
     * Checks if the given player has won this CellBoard.
     * @param player the current player
     */
    void checkVictory(int player) {
        // check all three in a rows. check for victories by last player played using last_clicked index
        int x = lastClicked % 3;
        int y = lastClicked / 3;

        // check horizontals
        for (int j = 0; j < 3; j++) {
            int index = (j * 3) + x;
            if (!cells[index].player(player)) {
                break;
            }
            if (j == 2) {
                victory = player;
            }
        }

        // check verticals
        for (int j = 0; j < 3; j++) {
            int index = (y * 3) + j;
            if (!cells[index].player(player)) {
                break;
            }
            if (j == 2) {
                victory = player;
            }
        }

        // check diagonal
        if (x == y) {
            for (int j = 0; j < 3; j++) {
                int index = (j * 3) + j;
                if (!cells[index].player(player)) {
                    break;
                }
                if (j == 2) {
                    victory = player;
                }
            }
        }

        // check anti-diagonal
        if (x + y == 2) {
            for (int j = 0; j < 3; j++) {
                int index = (j * 3) + (2 - j);
                if (!cells[index].player(player)) {
                    break;
                }
                if (j == 2) {
                    victory = player;
                }
            }
        }
    }

    /**
     * Checks if this board is won.
     * @return true if a player has won this board
     */
    boolean getVictory() {
        return victory > 0;
    }

    /**
     * Checks if a given player has won this board.
     * @param p the player to check
     * @return true if p has won
     */
    boolean playerWon(int p) {
        return victory == p;
    }

    /**
     * Sets the last clicked Cell for this CellBoard. Used for AI.
     * @param i the index
     */
    void setLastClicked(int i) {
        lastClicked = i;
    }

    /**
     * Gets the index of this CellBoard.
     * @return this.index
     */
    int getIndex() {
        return index;
    }

    /**
     * Creates a copy of this CellBoard.
     * @return a new CellBoard
     */
    CellBoard copy() {
        return new CellBoard(this);
    }
}

