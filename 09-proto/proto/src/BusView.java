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
        updatePosition();
        g.setColor(new Color(50, 100, 220));
        g.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
        g.setColor(new Color(30, 70, 170));
        g.drawRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);

        if (selected) {
            g.setColor(new Color(0, 200, 0));
            g.setStroke(new BasicStroke(2));
            g.drawRect(x - WIDTH / 2 - 3, y - HEIGHT / 2 - 3, WIDTH + 6, HEIGHT + 6);
            g.setStroke(new BasicStroke(1));
        }

        drawBlocked(g, Math.max(WIDTH, HEIGHT));
    }

    public void setSelected(boolean selected) { this.selected = selected; }
    public boolean isSelected() { return selected; }
}
