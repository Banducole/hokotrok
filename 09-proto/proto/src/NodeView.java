import java.awt.*;
import java.util.Map;

public class NodeView implements IDrawable {

    private final Node node;
    private int x;
    private int y;

    private int width = 60;
    private int height = 40;

    private static final int MIN_WIDTH = 60;
    private static final int MIN_HEIGHT = 40;
    private static final double LANE_SPACING = 38.0;
    private static final int PADDING = 10;

    public NodeView(Node node, int x, int y) {
        this.node = node;
        this.x = x;
        this.y = y;
    }

    public void computeSize(Map<Node, NodeView> nodeMap) {
        double maxW = MIN_WIDTH;
        double maxH = MIN_HEIGHT;

        for (Road road : node.getConnectedRoads()) {
            Node other = road.getFrom() == node ? road.getTo() : road.getFrom();
            NodeView otherView = nodeMap.get(other);
            if (otherView == null) continue;

            double dx = otherView.getX() - x;
            double dy = otherView.getY() - y;
            double len = Math.sqrt(dx * dx + dy * dy);
            if (len < 1) continue;

            double nx = -dy / len;
            double ny = dx / len;

            int laneCount = road.getLanes().size();
            double totalSpread = laneCount * LANE_SPACING;

            double hSpread = Math.abs(nx) * totalSpread + PADDING;
            double vSpread = Math.abs(ny) * totalSpread + PADDING;

            maxW = Math.max(maxW, hSpread);
            maxH = Math.max(maxH, vSpread);
        }

        this.width = (int) maxW;
        this.height = (int) maxH;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(160, 165, 170));
        g.fillRect(x - width / 2, y - height / 2, width, height);
        g.setColor(new Color(130, 135, 140));
        g.drawRect(x - width / 2, y - height / 2, width, height);

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
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
