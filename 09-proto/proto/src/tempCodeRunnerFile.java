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
    
    private static final int IMAGE_SIZE = 24; 

    private double currentAngle = 0.0;
    private double lastX = -1000.0;
    private double lastY = -1000.0;

    public CarView(Car car, Map<Lane, LaneView> laneViewMap) {
        super(car, laneViewMap);
    }

    @Override
    public void draw(Graphics2D g) {
        int ix = (int) visX, iy = (int) visY;
        
        if (lastX != -1000.0 && lastY != -1000.0) {
            double dx = visX - lastX;
            double dy = visY - lastY;
            
            if (Math.abs(dx) > 0.1 || Math.abs(dy) > 0.1) {
                currentAngle = Math.atan2(dy, dx) - (Math.PI / 2);
                
            }
        }
        lastX = visX;
        lastY = visY;

        if (carImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            
            g2d.translate(ix, iy);
            g2d.rotate(currentAngle);
            
            g2d.drawImage(carImage, -IMAGE_SIZE / 2, -IMAGE_SIZE / 2, IMAGE_SIZE, IMAGE_SIZE, null);
            
            g2d.dispose();
            
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(ix - IMAGE_SIZE / 2, iy - IMAGE_SIZE / 2, IMAGE_SIZE, IMAGE_SIZE);
        }
        
        drawBlocked(g, LOGIC_SIZE);
    }
}