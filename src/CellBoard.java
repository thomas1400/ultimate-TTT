import java.awt.*;
import java.util.Arrays;

class CellBoard {
    private int index;
    private int[] center;
    private int[][][] board_lines;

    CellBoard(MainBoard mb, int i) {
        this.index = i;

        this.center = new int[2];
        this.center[0] = mb.getCenter()[0] + (index % 3 - 1) * mb.getSize() / 3;
        this.center[1] = mb.getCenter()[1] + (index / 3 - 1) * mb.getSize() / 3;

        int size = (int) (mb.getSize() / 3.0 * 0.9);

        this.board_lines = new int[4][2][2]; // 4 lines with 2 points of 2 coordinates each
        this.board_lines[0] = new int[][]{
                {this.center[0] - size / 6, this.center[1] - size / 2},
                {this.center[0] - size / 6, this.center[1] + size / 2}}; // vertical left
        this.board_lines[1] = new int[][]{
                {this.center[0] + size / 6, this.center[1] - size / 2},
                {this.center[0] + size / 6, this.center[1] + size / 2}};
        this.board_lines[2] = new int[][]{
                {this.center[0] - size / 2, this.center[1] - size / 6},
                {this.center[0] + size / 2, this.center[1] - size / 6}};
        this.board_lines[3] = new int[][]{
                {this.center[0] - size / 2, this.center[1] + size / 6},
                {this.center[0] + size / 2, this.center[1] + size / 6}};
    }

    void draw(Graphics2D g) {
        for (int[][] board_line : this.board_lines) {
            int x1 = board_line[0][0];
            int y1 = board_line[0][1];
            int x2 = board_line[1][0];
            int y2 = board_line[1][1];

            g.drawLine(x1, y1, x2, y2);
        }
    }
}
