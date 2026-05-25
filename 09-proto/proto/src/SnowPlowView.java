import java.awt.*;
import java.util.List;
import java.util.Map;

public class SnowPlowView implements IDrawable {

    private final SnowPlow snowPlow;
    private int x;
    private int y;
    private boolean selected;
    private final Map<Lane, LaneView> laneViewMap;
    private List<SnowPlowView> allPlowViews;

    private static final int WIDTH = 22;
    private static final int HEIGHT = 14;

    public SnowPlowView(SnowPlow snowPlow, Map<Lane, LaneView> laneViewMap) {
        this.snowPlow = snowPlow;
        this.laneViewMap = laneViewMap;
    }

    @Override
    public void draw(Graphics2D g) {
        updatePosition();

        Color plowColor = getPlowColor();
        g.setColor(plowColor);
        g.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);

        if (selected) {
            g.setColor(new Color(0, 200, 0));
            g.setStroke(new BasicStroke(2));
            g.drawRect(x - WIDTH / 2 - 3, y - HEIGHT / 2 - 3, WIDTH + 6, HEIGHT + 6);
            g.setStroke(new BasicStroke(1));
        } else {
            g.setColor(plowColor.darker());
            g.drawRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
        }

        String headLabel = getHeadLabel();
        if (!headLabel.isEmpty()) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 9));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(headLabel, x - fm.stringWidth(headLabel) / 2, y + fm.getAscent() / 2 - 1);
        }
    }

    private Color getPlowColor() {
        CleanerHead head = snowPlow.getHead();
        if (head instanceof DragonHead) return new Color(210, 60, 40);
        if (head instanceof SaltHead) return new Color(60, 120, 200);
        if (head instanceof SweepHead) return new Color(80, 170, 70);
        if (head instanceof IceBreakerHead) return new Color(100, 160, 200);
        if (head instanceof RockHead) return new Color(160, 120, 70);
        if (head instanceof ThrowHead) return new Color(220, 140, 30);
        return new Color(255, 140, 0);
    }

    public void setAllPlowViews(List<SnowPlowView> views) {
        this.allPlowViews = views;
    }

    private void updatePosition() {
        Lane lane = snowPlow.getCurrentLane();
        if (lane == null) return;
        LaneView lv = laneViewMap.get(lane);
        if (lv == null) return;

        int vehicleCount = lane.getVehicles().size();
        int myPlowIdx = 0;
        int plowCount = 1;

        if (allPlowViews != null) {
            plowCount = 0;
            for (SnowPlowView spv : allPlowViews) {
                if (spv.getSnowPlow().getCurrentLane() == lane) {
                    if (spv == this) myPlowIdx = plowCount;
                    plowCount++;
                }
            }
        }

        java.awt.Point p = lv.getEntityPosition(vehicleCount + myPlowIdx, vehicleCount + plowCount);
        x = p.x;
        y = p.y;
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
        return px >= x - WIDTH / 2 - 4 && px <= x + WIDTH / 2 + 4
            && py >= y - HEIGHT / 2 - 4 && py <= y + HEIGHT / 2 + 4;
    }
}
