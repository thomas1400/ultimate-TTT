import java.awt.*;
import java.util.Arrays;

class CellBoard {
    private int index;
    private int[] center;
    private int[][][] board_lines;
    private Cell[] cells;

    CellBoard(MainBoard mb, int i) {
        index = i;

        // Calculate center of board from index and main board center
        center = new int[2];
        center[0] = mb.getCenter()[0] + (index % 3 - 1) * mb.getSize() / 3;
        center[1] = mb.getCenter()[1] + (index / 3 - 1) * mb.getSize() / 3;

        int size = (int) (mb.getSize() / 3.0 * 0.9);

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
        int cell_size = (int)(size / 3 * 0.8);
        for (int k = 0; k < 9; k++) {
            int cell_center_x = center[0] + (k % 3 - 1) * size / 3;
            int cell_center_y = center[1] + (k / 3 - 1) * size / 3;
            cells[k] = new Cell(cell_center_x, cell_center_y , cell_size);
        }
    }

    void draw(Graphics2D g) {
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

    boolean checkClick(Point clickPt, boolean player) {
        for (Cell c : cells) {
            if (c.contains(clickPt) && !c.filled()) {
                c.fill(player);
                return true;
            }
        }
        return false;
    }
}

