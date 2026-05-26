import java.awt.*;
import java.util.List;
import java.util.Map;

public abstract class VehicleView implements IDrawable {

    protected final Vehicle vehicle;
    protected int x, y;
    protected float visX, visY;
    protected Map<Lane, LaneView> laneViewMap;
    protected List<SnowPlowView> allPlowViews;
    protected List<LaneView> diagonalLaneViews;

    private float[] animPX, animPY;
    private int animPLen;
    private float animT;
    private boolean animating;
    private static final float ANIM_STEP = 0.1f;

    public VehicleView(Vehicle vehicle, Map<Lane, LaneView> laneViewMap) {
        this.vehicle = vehicle;
        this.laneViewMap = laneViewMap;
    }

    public void setAllPlowViews(List<SnowPlowView> views) { this.allPlowViews = views; }
    public void setDiagonalLaneViews(List<LaneView> views) { this.diagonalLaneViews = views; }

    protected boolean isUnderDiagonal() {
        if (diagonalLaneViews == null) return false;
        Lane currentLane = vehicle.getCurrentLane();
        for (LaneView lv : diagonalLaneViews) {
            if (lv.getLane() == currentLane) return false;
        }
        int ix = (int) visX, iy = (int) visY;
        for (LaneView lv : diagonalLaneViews) {
            if (lv.containsPoint(ix, iy)) return true;
        }
        return false;
    }

    protected Composite applyBridgeAlpha(Graphics2D g) {
        Composite old = g.getComposite();
        if (isUnderDiagonal()) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        return old;
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

    protected void updatePosition() {
        Lane lane = vehicle.getCurrentLane();
        if (lane == null) return;
        LaneView lv = laneViewMap.get(lane);
        if (lv == null) return;
        int idx = Math.max(0, lane.getVehicles().indexOf(vehicle));
        int plowCount = countPlowsOnLane(lane);
        int total = lane.getVehicles().size() + plowCount;
        java.awt.Point p = lv.getEntityPosition(idx, total);
        x = p.x; y = p.y;
    }

    private int countPlowsOnLane(Lane lane) {
        if (allPlowViews == null) return lane.getSnowPlow() != null ? 1 : 0;
        int count = 0;
        for (SnowPlowView spv : allPlowViews)
            if (spv.getSnowPlow().getCurrentLane() == lane) count++;
        return count;
    }

    protected void drawBlocked(Graphics2D g, int size) {
        if (vehicle.isBlocked()) {
            int ix = (int) visX, iy = (int) visY;
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(2));
            g.drawRect(ix - size / 2 - 2, iy - size / 2 - 2, size + 4, size + 4);
            g.drawLine(ix - size / 2, iy - size / 2, ix + size / 2, iy + size / 2);
            g.drawLine(ix + size / 2, iy - size / 2, ix - size / 2, iy + size / 2);
        }
    }

    public Vehicle getVehicle() { return vehicle; }
}
