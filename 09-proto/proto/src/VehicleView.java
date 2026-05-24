import java.awt.*;
import java.util.Map;

/**
 * Jarmuvek absztrakt view osossztalya.
 * A poziciot a sav LaneView kozeppontjabol szamitja.
 */
public abstract class VehicleView implements IDrawable {

    protected final Vehicle vehicle;
    protected int x;
    protected int y;
    protected Map<Lane, LaneView> laneViewMap;

    public VehicleView(Vehicle vehicle, Map<Lane, LaneView> laneViewMap) {
        this.vehicle = vehicle;
        this.laneViewMap = laneViewMap;
    }

    protected void updatePosition() {
        Lane lane = vehicle.getCurrentLane();
        if (lane == null) return;
        LaneView lv = laneViewMap.get(lane);
        if (lv == null) return;
        int idx = Math.max(0, lane.getVehicles().indexOf(vehicle));
        int total = lane.getVehicles().size() + (lane.getSnowPlow() != null ? 1 : 0);
        java.awt.Point p = lv.getEntityPosition(idx, total);
        x = p.x;
        y = p.y;
    }

    protected void drawBlocked(Graphics2D g, int size) {
        if (vehicle.isBlocked()) {
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(2));
            g.drawRect(x - size / 2 - 2, y - size / 2 - 2, size + 4, size + 4);
            g.drawLine(x - size / 2, y - size / 2, x + size / 2, y + size / 2);
            g.drawLine(x + size / 2, y - size / 2, x - size / 2, y + size / 2);
        }
    }

    public Vehicle getVehicle() { return vehicle; }
}
