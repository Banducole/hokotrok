import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HUDPanel extends JPanel {

    private final Game game;
    private GameController controller;
    private static final int PANEL_HEIGHT = 80;

    public HUDPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(0, PANEL_HEIGHT));
        setBackground(new Color(55, 60, 68));
    }

    public void setController(GameController controller) { this.controller = controller; }

    public void update() { repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<Player> players = game.getPlayers();
        if (players.isEmpty()) return;

        int currentIdx = game.getCurrentPlayerIndex();
        int totalWidth = getWidth();
        int cardWidth = Math.min(320, (totalWidth - 20) / players.size() - 10);
        int cardHeight = 60;
        int y = 10;

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            int cx = 10 + i * (cardWidth + 10);

            if (i == currentIdx) {
                g2d.setColor(new Color(0, 200, 0));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(cx - 3, y - 3, cardWidth + 6, cardHeight + 6, 10, 10);
                g2d.setStroke(new BasicStroke(1));
            }

            g2d.setColor(new Color(45, 50, 58));
            g2d.fillRoundRect(cx, y, cardWidth, cardHeight, 8, 8);

            String name = p.getName() != null ? p.getName() : "Jatekos" + (i + 1);
            String type = (p instanceof CleanerPlayer) ? " (Tak.)" : " (Busz.)";

            g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2d.setColor(Color.WHITE);
            g2d.drawString(name + type, cx + 10, y + 20);

            if (p instanceof CleanerPlayer) {
                CleanerPlayer cp = (CleanerPlayer) p;
                String info = "Penz: " + cp.getBalance();

                if (i == currentIdx && controller != null) {
                    SnowPlow sel = controller.getSelectedPlow();
                    if (sel != null && sel.getOwner() == cp) {
                        CleanerHead head = sel.getHead();
                        if (head instanceof SaltHead) {
                            info += " | So: " + head.fuelLevel();
                        } else if (head instanceof DragonHead) {
                            info += " | Kerozin: " + head.fuelLevel();
                        } else if (head instanceof RockHead) {
                            info += " | Zuzalek: " + head.fuelLevel();
                        }
                    }
                }

                g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2d.setColor(new Color(200, 210, 220));
                g2d.drawString(info, cx + 10, y + 40);

            } else if (p instanceof BusDriver) {
                Bus bus = ((BusDriver) p).getBus();
                String status = null;
                if (bus.isBlocked()) {
                    status = "Kimarad egy korbol - Karambol";
                } else {
                    Lane busLane = bus.getCurrentLane();
                    if (busLane != null && busLane.getState() instanceof ThickSnowState) {
                        status = "Elakadt a vastag hoban";
                    }
                }
                if (status != null) {
                    g2d.setFont(new Font("SansSerif", Font.ITALIC, 11));
                    g2d.setColor(new Color(255, 160, 60));
                    g2d.drawString(status, cx + 10, y + 40);
                }
            }
        }
    }
}
