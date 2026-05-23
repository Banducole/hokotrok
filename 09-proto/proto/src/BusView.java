import java.awt.*;
import java.util.Map;

/**
 * Busz megjelenito. Kek kis teglalap, blokkoltnal piros X.
 */
public class BusView extends VehicleView {

    private static final int WIDTH = 16;
    private static final int HEIGHT = 10;

    public BusView(Bus bus, Map<Lane, LaneView> laneViewMap) {
        super(bus, laneViewMap);
    }

    @Override
    public void draw(Graphics2D g) {
        updatePosition();
        g.setColor(new Color(50, 100, 220));
        g.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.drawRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
        drawBlocked(g, Math.max(WIDTH, HEIGHT));
    }
}
