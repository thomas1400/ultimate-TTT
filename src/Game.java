import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

public class Game extends JPanel implements MouseListener {
    /**
     * Driving class for the Ultimate TTT game.
     */
    private MainBoard mb;
    private Point clickPt;
    private GameAgent ga;

    private boolean mouseClicked;
    private boolean gameStarted;

    private int gameType;
    private int currentPlayer;
    private int victory;

    private Button onePButton;
    private Button twoPButton;

    public static void main(String[] args) {
        // Create JFrame, Game, add Game to JFrame and set settings
        JFrame frame = new JFrame("Ultimate TTT");

        Game g = new Game();
        g.addMouseListener(g);
        g.setPreferredSize(new Dimension(600, 750));

        frame.add(g);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize Game instance variables.
        g.mb = new MainBoard(new int[]{g.getWidth() / 2, g.getHeight() / 2}, (int) (g.getWidth() * 0.95));
        g.ga = new GameAgent();

        g.mouseClicked = false;
        g.gameStarted = false;

        g.gameType = 0;
        g.currentPlayer = 1;
        g.victory = 0;

        g.onePButton = new Button("1 Player", 60,
                g.getWidth()/2, g.getHeight()/2 - 75, g.getWidth()/2, 100);
        g.twoPButton = new Button("2 Players", 60,
                g.getWidth()/2, g.getHeight()/2 + 75, g.getWidth()/2, 100);

        // Create update and repaint timer and start it.
        Timer t = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.update();
                g.repaint();
            }
        });

        t.start();

        // Create WindowListener to stop timer on close, ending the process.
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                t.stop();
            }
        });
    }

    /**
     * Renders a String in the window using Graphics2D.
     * @param g a Graphics2D instance
     * @param text the String to render
     * @param size the font size
     * @param outline the width of the outline
     * @param x the center x of the text
     * @param y the center y of the text
     */
    public static void renderString(Graphics2D g, String text, int size, int outline, int x, int y) {

        Graphics2D g2d = (Graphics2D) g.create();

        Font f = new Font("Cambria", Font.PLAIN, size);
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        g2d.setFont(f);

        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout tl = new TextLayout(text, g2d.getFont(), frc);

        int descent = (int) tl.getDescent();
        int ascent = (int) tl.getAscent();

        int center_x = x - (int) tl.getBounds().getCenterX();
        int center_y = y - (2 * descent - ascent) / 2;

        AffineTransform at = new AffineTransform();
        at.translate(center_x, center_y);
        Shape text_shape = tl.getOutline(null);

        g2d.transform(at);
        g2d.setStroke(new BasicStroke(outline, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(Color.WHITE);
        g2d.draw(text_shape);

        g2d.setColor(Color.BLACK);
        g2d.fill(text_shape);

        g2d.dispose();
    }

    /**
     * Updates the game.
     * Checks for clicks, processes clicks and makes moves, and checks for victory.
     */
    private void update() {
        if (mouseClicked) {
            if (!gameStarted) {
                if (onePButton.contains(clickPt)) {
                    gameStarted = true;
                    gameType = 1;
                }
                if (twoPButton.contains(clickPt)) {
                    gameStarted = true;
                    gameType = 2;
                }
                mouseClicked = false;
            } else {
                if (mb.checkClick(clickPt, currentPlayer)) { // checkClick checks click, updates board, activeboard
                    if (mb.checkVictory(currentPlayer)) {
                        victory = currentPlayer;
                        mb.endGame();
                    }
                    if (gameType == 1 && victory == 0) { // if 1 player: make computer move, check victory
                        Move AIMove = ga.alphabetaMove(mb, 4, 2);
                        mb.makeMove(AIMove.getCellBoard(), AIMove.getCell(), 2);
                        if (mb.checkVictory(2)) {
                            victory = 2;
                            mb.endGame();
                        }
                    }
                    if (gameType == 2) { // if 2 player: change currentPlayer
                        currentPlayer = 3 - currentPlayer;
                    }
                }
                mouseClicked = false;
            }
        }
    }

    /**
     * Paints game components/menus.
     * @param graphics a Graphics instance
     */
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        renderString(g2d, "Ultimate Tic-Tac-Toe",
                60, 0, getWidth() / 2, (getHeight() - getWidth()) / 4);

        if (!gameStarted) {
            onePButton.draw(g2d);
            twoPButton.draw(g2d);
        } else {
            mb.draw(g2d);

            if (victory == 0) {
                if (gameType == 1) {
                    renderString(g2d, "Playing Against AI",
                            60, 6, getWidth() / 2, getWidth() + 3 * (getHeight() - getWidth()) / 4);
                } else {
                    renderString(g2d, "Player " + currentPlayer + "'s Turn",
                            60, 6, getWidth() / 2, getWidth() + 3 * (getHeight() - getWidth()) / 4);
                }
            } else {
                renderString(g2d, "Player " + victory + " Wins!",
                        60, 6, getWidth() / 2, getWidth() + 3 * (getHeight() - getWidth()) / 4);
            }
        }

    }

    // Implemented mouseListener methods

    /**
     * Records click point upon mouse click.
     * @param me MouseEvent
     */
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
