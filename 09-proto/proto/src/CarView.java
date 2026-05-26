import java.awt.*;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class CarView extends VehicleView {

    private static BufferedImage carImage;

    static {
        try {
            carImage = ImageIO.read(CarView.class.getResource("/images/auto.png"));
        } catch (Exception e) {
            System.err.println("Hiba az auto.png betöltésekor!");
            e.printStackTrace();
        }
    }

    private static final int LOGIC_SIZE = 12; 
    
    private static final int IMAGE_SIZE = 28; 

    public CarView(Car car, Map<Lane, LaneView> laneViewMap) {
        super(car, laneViewMap);
    }

    @Override
    public void draw(Graphics2D g) {
        Composite oldComp = applyBridgeAlpha(g);
        int ix = (int) visX, iy = (int) visY;
        double angle = getLaneAngle();

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(ix, iy);
        g2d.rotate(angle);

        if (carImage != null) {
            g2d.drawImage(carImage, -IMAGE_SIZE / 2, -IMAGE_SIZE / 2, IMAGE_SIZE, IMAGE_SIZE, null);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(-LOGIC_SIZE / 2, -LOGIC_SIZE / 2, LOGIC_SIZE, LOGIC_SIZE);
        }
        g2d.dispose();

        drawBlocked(g, LOGIC_SIZE);
        g.setComposite(oldComp);
    }
}