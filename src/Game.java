import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Game extends JPanel implements MouseListener {
    private MainBoard mb;
    private boolean mouseClicked;
    private Point clickPt;
    private boolean p1Turn;
    private boolean victory;

    public static void main(String[] args) {
        // Create JFrame, Game, add Game to JFrame and set settings
        JFrame frame = new JFrame("Ultimate TTT");

        Game g = new Game();
        g.addMouseListener(g);
        g.setPreferredSize(new Dimension(600, 600));

        frame.add(g);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize instance variables
        g.mb = new MainBoard(new int[]{g.getWidth() / 2, g.getHeight() / 2}, (int) (g.getWidth() * 0.95));
        g.mouseClicked = false;
        g.p1Turn = true;
        g.victory = false;

        // Create update and repaint timer and start it
        Timer t = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.update();
                g.repaint();
            }
        });

        t.start();

        // Create WindowListener to stop timer on close, ending the process
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                t.stop();
            }
        });
    }

    private void update() {
        if (mouseClicked) {
            if (mb.checkClick(clickPt, p1Turn)) { // checkClick checks click, updates board, activeboard
                victory = mb.checkVictory();
                p1Turn = !p1Turn;
            }
            mouseClicked = false;
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.BLACK);
        mb.draw(g2d);
    }

    // Implemented mouseListener methods

    @Override
    public void mouseClicked(MouseEvent me) {
        clickPt = me.getPoint();
        mouseClicked = true;
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
