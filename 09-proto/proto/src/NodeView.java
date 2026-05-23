import java.awt.*;

/**
 * Csomopont megjelenito. Kis dobozt rajzol tipusbetuvel (H/W/T/I).
 */
public class NodeView implements IDrawable {

    private final Node node;
    private int x;
    private int y;

    private static final int SIZE = 30;

    public NodeView(Node node, int x, int y) {
        this.node = node;
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(60, 60, 60));
        g.fillRoundRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE, 8, 8);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        String label = getTypeLabel();
        FontMetrics fm = g.getFontMetrics();
        int tx = x - fm.stringWidth(label) / 2;
        int ty = y + fm.getAscent() / 2 - 1;
        g.drawString(label, tx, ty);
    }

    private String getTypeLabel() {
        if (node instanceof Home) return "H";
        if (node instanceof Workplace) return "W";
        if (node instanceof Terminal) return "T";
        return "I";
    }

    public Node getNode() { return node; }
    public int getX() { return x; }
    public int getY() { return y; }
}
