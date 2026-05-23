import java.awt.*;
import java.util.Map;

/**
 * Hokotro megjelenito. Narancssarga negyzet fejbetujellel, kijeloltnel zold keret.
 */
public class SnowPlowView implements IDrawable {

    private final SnowPlow snowPlow;
    private int x;
    private int y;
    private boolean selected;
    private final Map<Lane, LaneView> laneViewMap;

    private static final int SIZE = 14;

    public SnowPlowView(SnowPlow snowPlow, Map<Lane, LaneView> laneViewMap) {
        this.snowPlow = snowPlow;
        this.laneViewMap = laneViewMap;
    }

    @Override
    public void draw(Graphics2D g) {
        updatePosition();
        g.setColor(new Color(255, 140, 0));
        g.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);

        if (selected) {
            g.setColor(new Color(0, 200, 0));
            g.setStroke(new BasicStroke(2));
            g.drawRect(x - SIZE / 2 - 2, y - SIZE / 2 - 2, SIZE + 4, SIZE + 4);
            g.setStroke(new BasicStroke(1));
        } else {
            g.setColor(Color.BLACK);
            g.drawRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        }

        String headLabel = getHeadLabel();
        if (!headLabel.isEmpty()) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 9));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(headLabel, x - fm.stringWidth(headLabel) / 2, y + fm.getAscent() / 2 - 1);
        }
    }

    private void updatePosition() {
        Lane lane = snowPlow.getCurrentLane();
        if (lane == null) return;
        LaneView lv = laneViewMap.get(lane);
        if (lv == null) return;
        x = lv.getCenterX() + 4;
        y = lv.getCenterY() + 4;
    }

    private String getHeadLabel() {
        CleanerHead head = snowPlow.getHead();
        if (head == null) return "";
        if (head instanceof SweepHead) return "S";
        if (head instanceof ThrowHead) return "H";
        if (head instanceof IceBreakerHead) return "J";
        if (head instanceof SaltHead) return "Z";
        if (head instanceof DragonHead) return "D";
        if (head instanceof RockHead) return "R";
        return "?";
    }

    public void setSelected(boolean selected) { this.selected = selected; }
    public boolean isSelected() { return selected; }
    public SnowPlow getSnowPlow() { return snowPlow; }

    public boolean containsPoint(int px, int py) {
        return px >= x - SIZE / 2 - 3 && px <= x + SIZE / 2 + 3
            && py >= y - SIZE / 2 - 3 && py <= y + SIZE / 2 + 3;
    }
}
