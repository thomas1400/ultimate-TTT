import java.awt.*;

public class Button {
    /**
     * A Button for use with Graphics2D, with a black outline and text.
     */

    private Rectangle outline;
    private String text;
    private int size;
    private int cx;
    private int cy;

    public Button(String text, int size, int cx, int cy, int width, int height) {
        outline = new Rectangle(cx-width/2, cy-height/2, width, height);
        this.text = text;
        this.size = size;
        this.cx = cx;
        this.cy = cy;
    }

    /**
     * Draws the button.
     * @param g2d a Graphics2D instance
     */
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fill(outline);
        g2d.setColor(Color.BLACK);
        g2d.draw(outline);
        Game.renderString(g2d, text, size, 1, cx, cy);
    }

    /**
     * Checks if a point is contained in the button.
     * @param p a Point
     * @return true if Point is inside outline
     */
    public boolean contains(Point p) {
        return outline.contains(p);
    }
}
