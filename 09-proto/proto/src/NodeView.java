import java.awt.*;

public class NodeView implements IDrawable {

    private final Node node;
    private int x;
    private int y;

    private static final int WIDTH = 60;
    private static final int HEIGHT = 40;

    public NodeView(Node node, int x, int y) {
        this.node = node;
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(160, 165, 170));
        g.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
        g.setColor(new Color(130, 135, 140));
        g.drawRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);

        g.setColor(new Color(60, 60, 60));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        String label = getTypeLabel();
        FontMetrics fm = g.getFontMetrics();
        int tx = x - fm.stringWidth(label) / 2;
        int ty = y + fm.getAscent() / 2 - 2;
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
