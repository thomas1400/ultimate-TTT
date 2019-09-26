import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;

class MainBoard {
    private CellBoard[] cells;
    private int[] center;
    private int size; // width and height
    private int[][][] board_lines;

    MainBoard(int[] center, int size) {
        this.size = size;
        this.center = center;
        this.cells = new CellBoard[9];
        for (int i = 0; i < 9; i++) {
            cells[i] = new CellBoard(this, i);
        }

        this.board_lines = new int[4][2][2]; // 4 lines with 2 points of 2 coordinates each
        this.board_lines[0] = new int[][]{
                {this.center[0]-size/6, this.center[1]-size/2},
                {this.center[0]-size/6, this.center[1]+size/2}}; // vertical left
        this.board_lines[1] = new int[][]{
                {this.center[0]+size/6, this.center[1]-size/2},
                {this.center[0]+size/6, this.center[1]+size/2}};
        this.board_lines[2] = new int[][]{
                {this.center[0]-size/2, this.center[1]-size/6},
                {this.center[0]+size/2, this.center[1]-size/6}};
        this.board_lines[3] = new int[][]{
                {this.center[0]-size/2, this.center[1]+size/6},
                {this.center[0]+size/2, this.center[1]+size/6}};
    }

    CellBoard[] getCells() { return cells; }
    int[] getCenter() { return this.center; }
    int getSize() { return size; }

    void draw(Graphics2D g) {
        for (CellBoard cb : this.cells) {
            cb.draw(g);
        }
        g.setStroke(new BasicStroke((float) (this.size/100.0)));
        for (int[][] board_line : this.board_lines) {
            int x1 = board_line[0][0];
            int y1 = board_line[0][1];
            int x2 = board_line[1][0];
            int y2 = board_line[1][1];

            g.drawLine(x1, y1, x2, y2);
        }
    }
}
