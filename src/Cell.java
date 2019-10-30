import java.awt.*;

public class Cell extends Rectangle {
    private Rectangle hitbox;
    private boolean filled;
    private int player; // 1 for 1, 2 for 2
    private int cx;
    private int cy;
    private int size;

    private Cell(Cell copy) {
        super(copy.cx - copy.size / 2, copy.cy - copy.size / 2, copy.size, copy.size);

        this.hitbox = new Rectangle(copy.hitbox);
        this.filled = copy.filled;
        this.player = copy.player;
        this.cx = copy.cx;
        this.cy = copy.cy;
        this.size = copy.size;
    }

    Cell(int center_x, int center_y, int size) {
        // Create draw rectangle
        super(center_x - size / 2, center_y - size / 2, size, size);

        this.cx = center_x;
        this.cy = center_y;
        this.size = size;

        // Create hitbox (or clickbox) rectangle
        size /= 0.8;
        hitbox = new Rectangle(center_x - size / 2, center_y - size / 2, size, size);
        filled = false;
        player = 0;
    }

    /**
     * Checks if this cell is filled.
     * @return this.filled
     */
    boolean filled() {
        return filled;
    }

    /**
     * Checks if this cell is filled by a player.
     * @param p the player to check
     * @return true if this.player is p
     */
    boolean player(int p) {
        return player == p;
    }

    /**
     * Fill this cell for the given player.
     * @param p the player to fill
     */
    void fill(int p) {
        filled = true;
        player = p;
    }

    /**
     * Draws this Cell.
     * @param g a Graphics2D instance
     */
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

    /**
     * Checks if a Point is contained in this Cell.
     * @param p the Point
     * @return true if p is contained in this.hitbox
     */
    @Override
    public boolean contains(Point p) {
        return hitbox.contains(p);
    }

    /**
     * Creates a copy of this cell.
     * @return a new Cell
     */
    Cell copy() {
        return new Cell(this);
    }

}
