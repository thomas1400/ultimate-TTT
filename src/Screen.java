import javax.swing.*;
import java.awt.*;

public class Screen extends Canvas {
    private MainBoard game;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ultimate TTT");
        Screen canvas = new Screen();
        canvas.setSize(600, 600);
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
        canvas.game = new MainBoard(new int[]{canvas.getWidth()/2, canvas.getHeight()/2}, canvas.getWidth());
    }

    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        this.game.draw(g);
    }
}
