import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * A busz ({@link Bus}) grafikus megjelenítéséért felelős osztály a Hókotrók projektben.
 * <p>
 * A {@link VehicleView} leszármazottjaként ez az osztály végzi el a busz
 * kirajzolását a képernyőre a Java 2D API ({@link Graphics2D}) segítségével.
 * Kezeli az elforgatást a sáv iránya alapján, a képi textúra betöltését, 
 * a kiválasztott állapotot, valamint a mély hó okozta vizuális vészjelzést.
 * </p>
 */
public class BusView extends VehicleView {

    /** A busz textúráját tároló statikus kép. */
    private static BufferedImage busImage;

    /** A busz logikai szélessége pixelben, fallback (textúra nélküli) rajzolás esetén. */
    private static final int LOGIC_WIDTH = 20;
    
    /** A busz logikai magassága pixelben, fallback (textúra nélküli) rajzolás esetén. */
    private static final int LOGIC_HEIGHT = 12;
    
    /** A megjelenítendő busz textúra szélessége pixelben. */
    private static final int IMAGE_WIDTH = 34;
    
    /** A megjelenítendő busz textúra magassága pixelben. */
    private static final int IMAGE_HEIGHT = 15;

    /** A fallback téglalap kitöltési színe (kék árnyalat), ha a kép betöltése sikertelen. */
    private static final Color FALLBACK_FILL_COLOR = new Color(50, 100, 220);
    
    /** A fallback téglalap keretének színe. */
    private static final Color FALLBACK_BORDER_COLOR = new Color(30, 70, 170);
    
    /** A kiválasztott (selected) állapotot jelző keret színe. */
    private static final Color SELECTION_COLOR = new Color(0, 200, 0);
    
    /** A mély hóban (elakadva) lévő állapotot jelző vizuális figyelmeztetés színe. */
    private static final Color SNOW_WARNING_COLOR = Color.RED;

    /** Tárolja, hogy a busz aktuálisan ki van-e választva a felületen. */
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

    /**
     * Létrehozza a busz nézetét a modell és a sávok nézeteinek leképezése alapján.
     *
     * @param bus a megjelenítendő {@link Bus} modellpéldány
     * @param laneViewMap a sávokat ({@link Lane}) és azok grafikus nézeteit ({@link LaneView}) összerendelő térkép
     */
    public BusView(Bus bus, Map<Lane, LaneView> laneViewMap) {
        super(bus, laneViewMap);
    }

    /**
     * Kirajzolja a buszt a megadott grafikus kontextusra.
     * 
     * A metódus először elvégzi a transzformációkat (eltolás a vizuális koordinátákra, 
     * forgatás a sáv szögének megfelelően). Ha a textúra ({@code bus.png}) elérhető, azt 
     * rajzolja ki, ellenkező esetben a fallback téglalapot. Ezt követően kirajzolja a 
     * kiválasztást jelző zöld keretet (ha aktív), majd a vészjelző piros áthúzást, 
     * amennyiben a jármű alatti sáv állapota {@link ThickSnowState}.
     * 
     *
     * @param g a {@link Graphics2D} objektum, amire a rajzolás történik
     */
    @Override
    public void draw(Graphics2D g) {
        Composite oldComp = applyBridgeAlpha(g);
        int ix = (int) visX;
        int iy = (int) visY;
        double angle = getLaneAngle();

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(ix, iy);
        g2d.rotate(angle);

        if (busImage != null) {
            g2d.drawImage(busImage, -IMAGE_WIDTH / 2, -IMAGE_HEIGHT / 2, IMAGE_WIDTH, IMAGE_HEIGHT, null);
        } else {
            g2d.setColor(FALLBACK_FILL_COLOR);
            g2d.fillRect(-LOGIC_WIDTH / 2, -LOGIC_HEIGHT / 2, LOGIC_WIDTH, LOGIC_HEIGHT);
            g2d.setColor(FALLBACK_BORDER_COLOR);
            g2d.drawRect(-LOGIC_WIDTH / 2, -LOGIC_HEIGHT / 2, LOGIC_WIDTH, LOGIC_HEIGHT);
        }

        if (selected) {
            g2d.setColor(SELECTION_COLOR);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(-IMAGE_WIDTH / 2 - 3, -IMAGE_HEIGHT / 2 - 3, IMAGE_WIDTH + 6, IMAGE_HEIGHT + 6);
            g2d.setStroke(new BasicStroke(1));
        }
        g2d.dispose();

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

    /**
     * Beállítja a busz grafikus kiválasztási állapotát.
     *
     * @param selected {@code true}, ha a buszt ki akarjuk választani (pl. kattintás hatására), különben {@code false}
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Visszaadja a busz aktuális kiválasztási állapotát.
     *
     * @return {@code true}, ha a busz jelenleg ki van választva a felületen
     */
    public boolean isSelected() {
        return selected;
    }
}