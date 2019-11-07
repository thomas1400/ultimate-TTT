import java.awt.*;
import java.util.ArrayList;

class MainBoard {
    /**
     * Holds the game state of Ultimate TTT.
     *
     * Contains an array of CellBoards, which each contain one smaller board.
     */
    private CellBoard[] cells;
    private int[] center;
    private int size; // width and height

    private int[][][] boardLines;

    private int activeBoard;
    private int lastClicked;
    private int moveCount;

    private MainBoard(MainBoard copy) {
        this.size = copy.size;
        this.center = copy.center;
        this.cells = new CellBoard[copy.cells.length];
        for (int i = 0; i < cells.length; i++) {
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
        this.activeBoard = copy.activeBoard;
        this.lastClicked = copy.lastClicked;
        this.moveCount = copy.moveCount;
    }

    MainBoard(int[] center, int size) {
        this.size = size;
        this.center = center;
        cells = new CellBoard[9];
        for (int i = 0; i < 9; i++) {
            cells[i] = new CellBoard(this, i);
        }

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

        activeBoard = -1;
    }

    /**
     * Gets the array of CellBoard
     * @return this.cells
     */
    CellBoard[] getCells() {
        return cells;
    }

    /**
     * Gets the center of this board.
     * @return this.center
     */
    int[] getCenter() {
        return center;
    }

    /**
     * Gets the size of this board.
     * @return this.size
     */
    int getSize() {
        return size;
    }

    /**
     * Draws the board lines and calls draw on each CellBoard.
     * @param g a Graphics2D instance
     */
    void draw(Graphics2D g) {
        for (CellBoard cb : cells) {
            cb.draw(g, activeBoard);
        }
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke((float) (size / 100.0)));
        for (int[][] board_line : boardLines) {
            int x1 = board_line[0][0];
            int y1 = board_line[0][1];
            int x2 = board_line[1][0];
            int y2 = board_line[1][1];

            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * Checks if a click falls within each of this board's CellBoards.
     * @param clickPt the Point of the click
     * @param player the current player
     * @return true if the click was a valid move
     */
    boolean checkClick(Point clickPt, int player) {
        for (CellBoard cb : cells) {
            int check = cb.checkClick(clickPt, player, activeBoard);
            if (check != -1) {
                lastClicked = cb.getIndex();
                moveCount += 1;
                if (cells[check].getVictory()) {
                    activeBoard = -1;
                } else {
                    activeBoard = check;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the current player has won.
     * Minimizes the search space by using the index of the last click.
     * @param player the current player
     * @return true if the player has won
     */
    boolean checkVictory(int player) {
        // check all three in a rows. check for victories by last player played using last_clicked index
        int x = lastClicked % 3;
        int y = lastClicked / 3;

        // check horizontals
        for (int j = 0; j < 3; j++) {
            int index = (j * 3) + x;
            if (!cells[index].playerWon(player)) {
                break;
            }
            if (j == 2) {
                return true;
            }
        }

        // check verticals
        for (int j = 0; j < 3; j++) {
            int index = (y * 3) + j;
            if (!cells[index].playerWon(player)) {
                break;
            }
            if (j == 2) {
                return true;
            }
        }

        // check diagonal
        if (x == y) {
            for (int j = 0; j < 3; j++) {
                int index = (j * 3) + j;
                if (!cells[index].playerWon(player)) {
                    break;
                }
                if (j == 2) {
                    return true;
                }
            }
        }

        // check anti-diagonal
        if (x + y == 2) {
            for (int j = 0; j < 3; j++) {
                int index = (j * 3) + (2 - j);
                if (!cells[index].playerWon(player)) {
                    break;
                }
                if (j == 2) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the board is full.
     * @return true if the number of moves is 81
     */
    boolean checkFull() {
        return moveCount == 81;
    }

    /**
     * Ends the game. Sets the active board to an unused value.
     */
    void endGame() {
        activeBoard = -2;
    }

    /**
     * Gets all possible children of this board.
     *
     * A child is a copy of this board with one of the possible moves made.
     * @param player the current player
     * @return ArrayList of Moves, the children
     */
    ArrayList<Move> getChildren(int player) {
        ArrayList<Move> children = new ArrayList<>();
        Move newmove;
        MainBoard newmb;
        for (int cb = 0; cb < this.cells.length; cb++) {
            if (activeBoard == this.cells[cb].getIndex() || (activeBoard < 0 && !this.cells[cb].getVictory())) {
                for (int cell = 0; cell < 9; cell++) {
                    if (this.cells[cb].checkMove(cell)) {
                        newmb = new MainBoard(this);
                        newmb.makeMove(cb, cell, player);
                        newmb.cells[cb].checkVictory(player);
                        newmove = new Move(newmb, cb, cell, player, newmb.heuristic());
                        children.add(newmove);
                    }
                }
            }
        }
        return children;
    }

    /**
     * Makes a move on this board without a click. Used for AI.
     * @param cb the CellBoard index
     * @param cell the Cell index
     * @param player the current player
     */
    public void makeMove(int cb, int cell, int player) {
        cells[cb].makeMove(cell, player);
        lastClicked = cb;
        cells[cb].setLastClicked(cell);
        cells[cb].checkVictory(player);
        activeBoard = cell;
        if (cells[activeBoard].getVictory()) {
            activeBoard = -1;
        }
    }

    /**
     * Numerical representation of the 'score' of this board.
     * @return number of boards won by P1 - number of boards won by P2
     */
    int heuristic() {
        int h = 0;
        for (CellBoard cb : cells) {
            if (cb.playerWon(1)) {
                h += 1;
            }
            if (cb.playerWon(2)){
                h -= 1;
            }
        }
        if (this.checkVictory(1)) {
            h += 10;
        }
        if (this.checkVictory(2)) {
            h -= 10;
        }
        return h;
    }

}
