import java.awt.*;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Az autó ({@link Car}) grafikus megjelenítéséért felelős osztály a Hókotrók projektben.
 * 
 * A {@link VehicleView} leszármazottjaként ez az osztály végzi el az autó
 * kirajzolását a képernyőre a Java 2D API ({@link Graphics2D}) segítségével.
 * Kezeli a megfelelő textúra betöltését, az elforgatást a sáv iránya alapján,
 * valamint a fallback (alapértelmezett geometriai) rajzolást, ha a kép nem elérhető.
 * 
 */
public class CarView extends VehicleView {

    /** Az autó textúráját tároló statikus kép. */
    private static BufferedImage carImage;

    static {
        try {
            carImage = ImageIO.read(CarView.class.getResource("/images/auto.png"));
        } catch (Exception e) {
            System.err.println("Hiba az auto.png betöltésekor!");
            e.printStackTrace();
        }
    }

    /** Az autó logikai mérete (szélessége és magassága) pixelben, fallback rajzolás esetén. */
    private static final int LOGIC_SIZE = 12; 
    
    /** A megjelenítendő autó textúra mérete (szélessége és magassága) pixelben. */
    private static final int IMAGE_SIZE = 28; 

    /**
     * Létrehozza az autó nézetét a modell és a sávok nézeteinek leképezése alapján.
     *
     * @param car         a megjelenítendő {@link Car} modellpéldány
     * @param laneViewMap a sávokat ({@link Lane}) és azok grafikus nézeteit ({@link LaneView}) összerendelő térkép
     */
    public CarView(Car car, Map<Lane, LaneView> laneViewMap) {
        super(car, laneViewMap);
    }

    /**
     * Kirajzolja az autót a megadott grafikus kontextusra.
     * 
     * A metódus kezeli a transzformációkat: az autó pozíciójába tolja a koordináta-rendszert,
     * majd elforgatja azt a sáv szögének megfelelően (mivel a kép alapból jobbra néz, a 
     * beépített szögkalkuláció megfelelő, további szögkorrekcióra nincs szükség). Ha a 
     * textúra ({@code auto.png}) sikeresen betöltődött, azt rajzolja ki, ellenkező esetben 
     * egy fekete négyzetet (fallback). Végül meghívja a blokkolt állapot esetleges vizuális 
     * jelzését végző metódust.
     * 
     *
     * @param g a {@link Graphics2D} objektum, amire a rajzolás történik
     */
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