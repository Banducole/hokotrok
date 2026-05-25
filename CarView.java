import java.awt.*;
import java.util.Map;

public class CarView extends VehicleView {

    private static final int SIZE = 12;

    public CarView(Car car, Map<Lane, LaneView> laneViewMap) {
        super(car, laneViewMap);
    }

    @Override
    public void draw(Graphics2D g) {
        updatePosition();
        g.setColor(new Color(220, 190, 0));
        g.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        g.setColor(new Color(180, 150, 0));
        g.drawRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        drawBlocked(g, SIZE);
    }
}
