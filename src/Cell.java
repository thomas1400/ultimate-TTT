import java.awt.*;

public class Cell extends Rectangle {
    private Rectangle hitbox;
    private boolean filled;
    private int player; // 1 for 1, 2 for 2

    Cell(int center_x, int center_y, int size) {
        // Create draw rectangle
        super(center_x - size / 2, center_y - size / 2, size, size);

        // Create hitbox (or clickbox) rectangle
        size /= 0.8;
        hitbox = new Rectangle(center_x - size / 2, center_y - size / 2, size, size);
        filled = false;
        player = 0;
    }

    boolean filled() {
        return filled;
    }

    boolean player(int p) {
        return player == p;
    }

    void fill(int p) {
        filled = true;
        player = p;
    }

    void draw(Graphics2D g) {
        if (filled) {
            if (player == 1) { // if P1
                g.setColor(Color.RED);
            } else { // if P2
                g.setColor(Color.BLUE);
            }
            g.fill(this);
        }
    }

    @Override
    public boolean contains(Point p) {
        return hitbox.contains(p);
    }

}
