import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.imageio.ImageIO;

public class BusView extends VehicleView {

    private static BufferedImage busImage;

    // Logikai és képi méretek
    private static final int LOGIC_WIDTH = 20;
    private static final int LOGIC_HEIGHT = 12;
    private static final int IMAGE_WIDTH = 34;
    private static final int IMAGE_HEIGHT = 15;

    // Színek kiszervezve konstansokba az egyszerűbb módosíthatóságért
    private static final Color FALLBACK_FILL_COLOR = new Color(50, 100, 220);
    private static final Color FALLBACK_BORDER_COLOR = new Color(30, 70, 170);
    private static final Color SELECTION_COLOR = new Color(0, 200, 0);
    private static final Color SNOW_WARNING_COLOR = Color.RED;

    private boolean selected;

    static {
        try {
            var resource = BusView.class.getResource("/images/busz.png");
            if (resource != null) {
                busImage = ImageIO.read(resource);
            } else {
                System.err.println("Hiba: Nem található a /images/busz.png erőforrás!");
            }
        } catch (Exception e) {
            System.err.println("Hiba a busz.png betöltésekor!");
            e.printStackTrace();
        }
    }

    public BusView(Bus bus, Map<Lane, LaneView> laneViewMap) {
        super(bus, laneViewMap);
    }

    @Override
    public void draw(Graphics2D g) {
        Composite oldComp = applyBridgeAlpha(g);
        int ix = (int) visX;
        int iy = (int) visY;

        if (busImage != null) {
            g.drawImage(
                busImage, 
                ix - (IMAGE_WIDTH / 2), 
                iy - (IMAGE_HEIGHT / 2), 
                IMAGE_WIDTH, 
                IMAGE_HEIGHT, 
                null
            );
        } else {
            g.setColor(FALLBACK_FILL_COLOR);
            g.fillRect(ix - (LOGIC_WIDTH / 2), iy - (LOGIC_HEIGHT / 2), LOGIC_WIDTH, LOGIC_HEIGHT);
            
            g.setColor(FALLBACK_BORDER_COLOR);
            g.drawRect(ix - (LOGIC_WIDTH / 2), iy - (LOGIC_HEIGHT / 2), LOGIC_WIDTH, LOGIC_HEIGHT);
        }

        if (selected) {
            g.setColor(SELECTION_COLOR);
            g.setStroke(new BasicStroke(2));
            g.drawRect(
                ix - (IMAGE_WIDTH / 2) - 3, 
                iy - (IMAGE_HEIGHT / 2) - 3, 
                IMAGE_WIDTH + 6, 
                IMAGE_HEIGHT + 6
            );
            g.setStroke(new BasicStroke(1));
        }

        drawBlocked(g, Math.max(LOGIC_WIDTH, LOGIC_HEIGHT));

        Lane lane = vehicle.getCurrentLane();
        if (lane != null && lane.getState() instanceof ThickSnowState) {
            g.setColor(SNOW_WARNING_COLOR);
            g.setStroke(new BasicStroke(2));

            int boxX = ix - (IMAGE_WIDTH / 2) - 2;
            int boxY = iy - (IMAGE_HEIGHT / 2) - 2;
            int boxWidth = IMAGE_WIDTH + 4;
            int boxHeight = IMAGE_HEIGHT + 4;

            g.drawRect(boxX, boxY, boxWidth, boxHeight);
            
            g.drawLine(boxX, boxY, boxX + boxWidth, boxY + boxHeight);
            g.drawLine(boxX + boxWidth, boxY, boxX, boxY + boxHeight);
            
            g.setStroke(new BasicStroke(1));
        }

        g.setComposite(oldComp);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}