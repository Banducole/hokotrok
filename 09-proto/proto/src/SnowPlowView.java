import java.awt.*;
import java.util.List;
import java.util.Map;

public class SnowPlowView implements IDrawable {

    private final SnowPlow snowPlow;
    private int x, y;
    private float visX, visY;
    private boolean selected;
    private final Map<Lane, LaneView> laneViewMap;
    private List<SnowPlowView> allPlowViews;

    private float[] animPX, animPY;
    private int animPLen;
    private float animT;
    private boolean animating;
    private static final float ANIM_STEP = 0.1f;

    private static final int WIDTH = 22;
    private static final int HEIGHT = 14;

    public SnowPlowView(SnowPlow snowPlow, Map<Lane, LaneView> laneViewMap) {
        this.snowPlow = snowPlow;
        this.laneViewMap = laneViewMap;
    }

    public void animTick() {
        if (!animating) {
            updatePosition();
            visX = x; visY = y;
            return;
        }
        animT = Math.min(animT + ANIM_STEP, 1f);
        float scaled = animT * (animPLen - 1);
        int seg = (int) Math.min(scaled, animPLen - 2);
        float frac = scaled - seg;
        visX = animPX[seg] + frac * (animPX[seg + 1] - animPX[seg]);
        visY = animPY[seg] + frac * (animPY[seg + 1] - animPY[seg]);
        if (animT >= 1f) animating = false;
    }

    public void startAnimation(Lane oldLane, Lane newLane) {
        LaneView oldLV = laneViewMap.get(oldLane);
        LaneView newLV = laneViewMap.get(newLane);
        if (oldLV == null || newLV == null) return;

        float sX = oldLV.getCenterX(), sY = oldLV.getCenterY();
        float eX = newLV.getCenterX(), eY = newLV.getCenterY();
        Road oldRoad = oldLane.getRoad();
        Road newRoad = newLane.getRoad();

        if (oldRoad == null || newRoad == null || oldRoad == newRoad) {
            animPX = new float[]{sX, eX};
            animPY = new float[]{sY, eY};
            animPLen = 2;
        } else {
            Node shared = sharedNode(oldRoad, newRoad);
            if (shared != null) {
                float nX = (shared == oldRoad.getFrom()) ? oldLV.getX1() : oldLV.getX2();
                float nY = (shared == oldRoad.getFrom()) ? oldLV.getY1() : oldLV.getY2();
                animPX = new float[]{sX, nX, eX};
                animPY = new float[]{sY, nY, eY};
                animPLen = 3;
            } else {
                animPX = new float[]{sX, eX};
                animPY = new float[]{sY, eY};
                animPLen = 2;
            }
        }
        visX = sX; visY = sY;
        animT = 0f;
        animating = true;
    }

    private static Node sharedNode(Road a, Road b) {
        if (a.getFrom() == b.getFrom() || a.getFrom() == b.getTo()) return a.getFrom();
        if (a.getTo()   == b.getFrom() || a.getTo()   == b.getTo()) return a.getTo();
        return null;
    }

    public boolean isAnimating() { return animating; }

    @Override
    public void draw(Graphics2D g) {
        int ix = (int) visX, iy = (int) visY;

        Color plowColor = getPlowColor();
        g.setColor(plowColor);
        g.fillRect(ix - WIDTH / 2, iy - HEIGHT / 2, WIDTH, HEIGHT);

        if (selected) {
            g.setColor(new Color(0, 200, 0));
            g.setStroke(new BasicStroke(2));
            g.drawRect(ix - WIDTH / 2 - 3, iy - HEIGHT / 2 - 3, WIDTH + 6, HEIGHT + 6);
            g.setStroke(new BasicStroke(1));
        } else {
            g.setColor(plowColor.darker());
            g.drawRect(ix - WIDTH / 2, iy - HEIGHT / 2, WIDTH, HEIGHT);
        }

        String headLabel = getHeadLabel();
        if (!headLabel.isEmpty()) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 9));
            FontMetrics fm = g.getFontMetrics();
            g.drawString(headLabel, ix - fm.stringWidth(headLabel) / 2, iy + fm.getAscent() / 2 - 1);
        }
    }

    private Color getPlowColor() {
        CleanerHead head = snowPlow.getHead();
        if (head instanceof DragonHead)    return new Color(210, 60, 40);
        if (head instanceof SaltHead)      return new Color(60, 120, 200);
        if (head instanceof SweepHead)     return new Color(80, 170, 70);
        if (head instanceof IceBreakerHead) return new Color(100, 160, 200);
        if (head instanceof RockHead)      return new Color(160, 120, 70);
        if (head instanceof ThrowHead)     return new Color(220, 140, 30);
        return new Color(255, 140, 0);
    }

    private void updatePosition() {
        Lane lane = snowPlow.getCurrentLane();
        if (lane == null) return;
        LaneView lv = laneViewMap.get(lane);
        if (lv == null) return;

        int vehicleCount = lane.getVehicles().size();
        int myPlowIdx = 0, plowCount = 1;

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
        x = p.x; y = p.y;
    }

    private String getHeadLabel() {
        CleanerHead head = snowPlow.getHead();
        if (head == null) return "";
        if (head instanceof SweepHead)     return "S";
        if (head instanceof ThrowHead)     return "H";
        if (head instanceof IceBreakerHead) return "J";
        if (head instanceof SaltHead)      return "Z";
        if (head instanceof DragonHead)    return "D";
        if (head instanceof RockHead)      return "R";
        return "?";
    }

    public void setAllPlowViews(List<SnowPlowView> views) { this.allPlowViews = views; }
    public void setSelected(boolean selected) { this.selected = selected; }
    public boolean isSelected() { return selected; }
    public SnowPlow getSnowPlow() { return snowPlow; }

    public boolean containsPoint(int px, int py) {
        int ix = (int) visX, iy = (int) visY;
        return px >= ix - WIDTH / 2 - 4 && px <= ix + WIDTH / 2 + 4
            && py >= iy - HEIGHT / 2 - 4 && py <= iy + HEIGHT / 2 + 4;
    }
}
