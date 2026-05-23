import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Felso statusz sav. Minden jatekos nevet, tipusat, egyenleget/korszamot jeleníti meg.
 * Az aktualis jatekos zold kerettel kiemelve.
 */
public class HUDPanel extends JPanel {

    private final Game game;

    private static final int PANEL_HEIGHT = 60;

    public HUDPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(0, PANEL_HEIGHT));
        setBackground(new Color(30, 35, 40));
    }

    public void update() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<Player> players = game.getPlayers();
        if (players.isEmpty()) return;

        int currentIdx = game.getCurrentPlayerIndex();
        int x = 15;

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2d.setColor(new Color(180, 180, 180));
        g2d.drawString("Kor: " + game.getRoundNumber(), x, 15);

        g2d.setFont(new Font("SansSerif", Font.BOLD, 13));
        int cardWidth = 180;
        int cardHeight = 40;
        int y = 18;

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            int cx = 15 + i * (cardWidth + 10);

            if (i == currentIdx) {
                g2d.setColor(new Color(0, 180, 0));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(cx - 2, y - 2, cardWidth + 4, cardHeight + 4, 8, 8);
                g2d.setStroke(new BasicStroke(1));
            }

            g2d.setColor(new Color(55, 60, 70));
            g2d.fillRoundRect(cx, y, cardWidth, cardHeight, 6, 6);

            g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2d.setColor(Color.WHITE);
            String name = p.getName() != null ? p.getName() : "Jatekos" + (i + 1);
            g2d.drawString(name, cx + 8, y + 16);

            g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2d.setColor(new Color(180, 200, 220));
            String info;
            if (p instanceof CleanerPlayer) {
                CleanerPlayer cp = (CleanerPlayer) p;
                info = "Takarito | $" + cp.getBalance() + " | " + cp.getPlows().size() + " hokotro";
            } else if (p instanceof BusDriver) {
                BusDriver bd = (BusDriver) p;
                info = "Sofor | " + bd.getBus().getCompletedRounds() + " fordulo";
            } else {
                info = "Jatekos";
            }
            g2d.drawString(info, cx + 8, y + 32);
        }
    }
}
