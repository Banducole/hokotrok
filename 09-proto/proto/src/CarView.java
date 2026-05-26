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

        if (carImage != null) {
            g.drawImage(carImage, ix - (IMAGE_SIZE / 2), iy - (IMAGE_SIZE / 2), IMAGE_SIZE, IMAGE_SIZE, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(ix - LOGIC_SIZE / 2, iy - LOGIC_SIZE / 2, LOGIC_SIZE, LOGIC_SIZE);
        }

        drawBlocked(g, LOGIC_SIZE);
        g.setComposite(oldComp);
    }
}