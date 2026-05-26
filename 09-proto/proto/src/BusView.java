import java.awt.*;
import java.util.Map;

public class BusView extends VehicleView {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 12;
    private boolean selected;

    public BusView(Bus bus, Map<Lane, LaneView> laneViewMap) {
        super(bus, laneViewMap);
    }

    @Override
    public void draw(Graphics2D g) {
        Composite oldComp = applyBridgeAlpha(g);
        int ix = (int) visX, iy = (int) visY;
        g.setColor(new Color(50, 100, 220));
        g.fillRect(ix - WIDTH / 2, iy - HEIGHT / 2, WIDTH, HEIGHT);
        g.setColor(new Color(30, 70, 170));
        g.drawRect(ix - WIDTH / 2, iy - HEIGHT / 2, WIDTH, HEIGHT);

        if (selected) {
            g.setColor(new Color(0, 200, 0));
            g.setStroke(new BasicStroke(2));
            g.drawRect(ix - WIDTH / 2 - 3, iy - HEIGHT / 2 - 3, WIDTH + 6, HEIGHT + 6);
            g.setStroke(new BasicStroke(1));
        }

        drawBlocked(g, Math.max(WIDTH, HEIGHT));

        Lane lane = vehicle.getCurrentLane();
        if (lane != null && lane.getState() instanceof ThickSnowState) {
            int size = Math.max(WIDTH, HEIGHT);
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(2));
            g.drawRect(ix - size / 2 - 2, iy - size / 2 - 2, size + 4, size + 4);
            g.drawLine(ix - size / 2, iy - size / 2, ix + size / 2, iy + size / 2);
            g.drawLine(ix + size / 2, iy - size / 2, ix - size / 2, iy + size / 2);
            g.setStroke(new BasicStroke(1));
        }
        g.setComposite(oldComp);
    }

    public void setSelected(boolean selected) { this.selected = selected; }
    public boolean isSelected() { return selected; }
}
