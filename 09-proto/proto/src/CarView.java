import java.awt.*;
import java.util.Map;

/**
 * Auto megjelenito. Sarga kis negyzet, blokkoltnal piros X.
 */
public class CarView extends VehicleView {

    private static final int SIZE = 10;

    public CarView(Car car, Map<Lane, LaneView> laneViewMap) {
        super(car, laneViewMap);
    }

    @Override
    public void draw(Graphics2D g) {
        updatePosition();
        g.setColor(new Color(230, 200, 0));
        g.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        drawBlocked(g, SIZE);
    }
}
