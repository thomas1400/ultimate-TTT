import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;

class MainBoard {
    private CellBoard[] cells;
    private int[] center;
    private int size; // width and height
    private int[][][] board_lines;
    private int active_board;

    MainBoard(int[] center, int size) {
        this.size = size;
        this.center = center;
        cells = new CellBoard[9];
        for (int i = 0; i < 9; i++) {
            cells[i] = new CellBoard(this, i);
        }

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

        active_board = -1;
    }

    CellBoard[] getCells() {
        return cells;
    }

    int[] getCenter() {
        return center;
    }

    int getSize() {
        return size;
    }

    void draw(Graphics2D g) {
        for (CellBoard cb : cells) {
            cb.draw(g, active_board);
        }
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke((float) (size / 100.0)));
        for (int[][] board_line : board_lines) {
            int x1 = board_line[0][0];
            int y1 = board_line[0][1];
            int x2 = board_line[1][0];
            int y2 = board_line[1][1];

            g.drawLine(x1, y1, x2, y2);
        }
    }

    boolean checkClick(Point clickPt, boolean player) {
        for (CellBoard cb : cells) {
            int check = cb.checkClick(clickPt, player, active_board);
            if (check != -1) {
                if (cells[check].isWon()) {
                    active_board = -1;
                } else {
                    active_board = check;
                }
                return true;
            }
        }
        return false;
    }

    // TODO: Check victory (write victory algorithm)
    boolean checkVictory() {
        return false;
    }

}
