import java.awt.*;
import java.util.Map;

public class BusView extends VehicleView {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 12;

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
        drawBlocked(g, Math.max(WIDTH, HEIGHT));
    }
}
