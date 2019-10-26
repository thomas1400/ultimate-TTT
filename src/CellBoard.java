import java.awt.*;

class CellBoard {
    private int index;
    private int victory; // 0 for not won, 1 for p1, 2 for p2
    private int[][][] board_lines;
    private Rectangle outline;
    private Cell[] cells;
    private int last_clicked;

    private CellBoard(CellBoard copy) {
        this.index = copy.index;
        this.victory = copy.victory;
        this.outline = new Rectangle(copy.outline);
        this.cells = new Cell[copy.cells.length];
        for (int i = 0; i < this.cells.length; i++) {
            this.cells[i] = copy.cells[i].copy();
        }
        this.last_clicked = copy.last_clicked;
        this.board_lines = new int[copy.board_lines.length]
                [copy.board_lines[0].length]
                [copy.board_lines[0][0].length];
        for (int i = 0; i < this.board_lines.length; i++) {
            for (int j = 0; j < this.board_lines[i].length; j++) {
                this.board_lines[i][j] = copy.board_lines[i][j].clone();
            }
        }
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
        board_lines = new int[4][2][2]; // 4 lines with 2 points of 2 coordinates each
        board_lines[0] = new int[][]{
                {center[0] - size / 6, center[1] - size / 2},
                {center[0] - size / 6, center[1] + size / 2}}; // vertical left
        board_lines[1] = new int[][]{
                {center[0] + size / 6, center[1] - size / 2},
                {center[0] + size / 6, center[1] + size / 2}};
        board_lines[2] = new int[][]{
                {center[0] - size / 2, center[1] - size / 6},
                {center[0] + size / 2, center[1] - size / 6}};
        board_lines[3] = new int[][]{
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
        for (int[][] board_line : board_lines) {
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

    int checkClick(Point clickPt, int player, int active) {
        if (victory != 0 || (index != active && active != -1)) {
            return -1;
        }
        for (int i = 0; i < cells.length; i++) {
            Cell c = cells[i];
            if (c.contains(clickPt) && !c.filled()) {
                last_clicked = i;
                c.fill(player);
                checkVictory(player);
                return i;
            }
        }
        return -1;
    }

    boolean checkMove(int index) {
        return !cells[index].filled();
    }

    void makeMove(int index, int player) {
        cells[index].fill(player);
    }

    void checkVictory(int player) {
        // check all three in a rows. check for victories by last player played using last_clicked index
        int x = last_clicked % 3;
        int y = last_clicked / 3;

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

    boolean getVictory() {
        return victory > 0;
    }

    boolean playerWon(int p) {
        return victory == p;
    }

    int getIndex() {
        return index;
    }

    CellBoard copy() {
        return new CellBoard(this);
    }
}

