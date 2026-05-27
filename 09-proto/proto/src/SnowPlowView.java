import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * A hókotró ({@link SnowPlow}) grafikus megjelenítéséért felelős osztály a Hókotrók projektben.
 * * Megvalósítja az {@link IDrawable} interfészt. Fő feladatai közé tartozik a hókotró 
 * és a rászerelt specifikus fejek (pl. söprőfej, sószórófej, sárkányfej) textúráinak 
 * betöltése és kirajzolása. Komplex animációs logikát tartalmaz a csomópontokon 
 * keresztüli sima mozgáshoz, számítja a megfelelő dőlésszöget a sáv iránya alapján, 
 * és kezeli az átlós utak (felüljárók) alatti áthaladás vizuális áttetszőségét.
 */
public class SnowPlowView implements IDrawable {

    // A hókotró és a különféle fejek statikus textúrái
    private static BufferedImage plowImage;
    private static BufferedImage dragonHeadImage;
    private static BufferedImage sweepHeadImage;
    private static BufferedImage throwHeadImage;
    private static BufferedImage iceBreakerHeadImage;
    private static BufferedImage rockHeadImage;
    private static BufferedImage saltHeadImage;

    static {
        try {
            plowImage = ImageIO.read(SnowPlowView.class.getResource("/images/hokotro.png"));
            dragonHeadImage = ImageIO.read(SnowPlowView.class.getResource("/images/sarkanyfej.png"));
            sweepHeadImage = ImageIO.read(SnowPlowView.class.getResource("/images/soprofej.png"));
            throwHeadImage = ImageIO.read(SnowPlowView.class.getResource("/images/hanyofej.png"));
            iceBreakerHeadImage = ImageIO.read(SnowPlowView.class.getResource("/images/jegtotro.png"));
            rockHeadImage = ImageIO.read(SnowPlowView.class.getResource("/images/kavicsszorofej.png"));
            saltHeadImage = ImageIO.read(SnowPlowView.class.getResource("/images/soszorofej.png"));
        } catch (Exception e) {
            System.err.println("Egy vagy több hókotró/fej kép nem található!");
        }
    }

    /** A játék eredeti logikai szélessége (hibakezeléshez, kép nélküli rajzoláskor). */
    private static final int LOGIC_WIDTH = 22;
    /** A játék eredeti logikai magassága (hibakezeléshez, kép nélküli rajzoláskor). */
    private static final int LOGIC_HEIGHT = 14;

    /** A hókotró képi textúrájának szélessége. */
    private static final int IMAGE_WIDTH = 16;
    /** A hókotró képi textúrájának magassága. */
    private static final int IMAGE_HEIGHT = 32;

    /** A megjelenítendő hókotró modellpéldánya. */
    private final SnowPlow snowPlow;
    
    /** A hókotró logikai célpozíciója (X koordináta). */
    private int x;
    /** A hókotró logikai célpozíciója (Y koordináta). */
    private int y;
    
    /** A hókotró aktuális, látható vizuális X koordinátája (animáció során változik). */
    private float visX;
    /** A hókotró aktuális, látható vizuális Y koordinátája (animáció során változik). */
    private float visY;
    
    /** Jelzi, hogy a hókotró éppen ki van-e választva a felhasználó által. */
    private boolean selected;
    
    /** A sávokat és nézeteiket összerendelő térkép. */
    private final Map<Lane, LaneView> laneViewMap;
    
    /** Az összes hókotró nézetének listája (az egy sávon belüli elcsúsztatás számításához). */
    private List<SnowPlowView> allPlowViews;
    
    /** Az átlós sávok nézeteinek listája (az alagút/felüljáró hatás ellenőrzéséhez). */
    private List<LaneView> diagonalLaneViews;

    // Animációhoz használt változók
    private float[] animPX, animPY;
    private int animPLen;
    private float animT;
    private boolean animating;
    private static final float ANIM_STEP = 0.1f;
    private double currentAngle = Double.NaN;

    /**
     * Létrehozza a hókotró nézetét a megadott modellpéldány és a sávnézetek alapján.
     *
     * @param snowPlow    a {@link SnowPlow} modellpéldány
     * @param laneViewMap a sávokat és nézeteiket összerendelő térkép
     */
    public SnowPlowView(SnowPlow snowPlow, Map<Lane, LaneView> laneViewMap) {
        this.snowPlow = snowPlow;
        this.laneViewMap = laneViewMap;
    }

    /**
     * Kezeli az animáció egy léptetését (képkockáját).
     * * Ha az animáció folyamatban van, frissíti a vizuális koordinátákat 
     * (visX, visY) a kiszámított útvonal interpolációja alapján. Ha nincs 
     * animáció, frissíti és felveszi a statikus pozíciót a sávon belül.
     */
    public void animTick() {
        if (!animating) {
            updatePosition();
            visX = x; visY = y;
            return;
        }
        animT = Math.min(animT + ANIM_STEP, 1f);
        float scaled = animT * (animPLen - 1);
        int seg = (int) Math.min(scaled, animPLen - 2);
        float frac = scaled - seg;
        visX = animPX[seg] + frac * (animPX[seg + 1] - animPX[seg]);
        visY = animPY[seg] + frac * (animPY[seg + 1] - animPY[seg]);
        if (animT >= 1f) animating = false;
    }

    /**
     * Elindítja a hókotró mozgási animációját egy régi sávról egy újra.
     * * Kiszámítja az útvonalat a sávok középpontjai alapján. Ha a két sáv 
     * nem ugyanahhoz az úthoz tartozik, megkeresi a közös csomópontot 
     * ({@link Node}), és azon keresztül vezet egy 3 pontos útvonalat, hogy 
     * a kanyarodás folyamatos legyen.
     *
     * @param oldLane a kiindulási sáv
     * @param newLane a cél sáv
     */
    public void startAnimation(Lane oldLane, Lane newLane) {
        LaneView oldLV = laneViewMap.get(oldLane);
        LaneView newLV = laneViewMap.get(newLane);
        if (oldLV == null || newLV == null) return;

        float sX = oldLV.getCenterX(), sY = oldLV.getCenterY();
        float eX = newLV.getCenterX(), eY = newLV.getCenterY();
        Road oldRoad = oldLane.getRoad();
        Road newRoad = newLane.getRoad();

        if (oldRoad == null || newRoad == null || oldRoad == newRoad) {
            animPX = new float[]{sX, eX};
            animPY = new float[]{sY, eY};
            animPLen = 2;
        } else {
            Node shared = sharedNode(oldRoad, newRoad);
            if (shared != null) {
                float nX = (shared == oldRoad.getFrom()) ? oldLV.getX1() : oldLV.getX2();
                float nY = (shared == oldRoad.getFrom()) ? oldLV.getY1() : oldLV.getY2();
                animPX = new float[]{sX, nX, eX};
                animPY = new float[]{sY, nY, eY};
                animPLen = 3;
            } else {
                animPX = new float[]{sX, eX};
                animPY = new float[]{sY, eY};
                animPLen = 2;
            }
        }
        visX = sX; visY = sY;
        animT = 0f;
        animating = true;
        if (oldRoad == null || newRoad == null || oldRoad != newRoad) {
            currentAngle = Math.atan2(
                animPY[animPLen - 1] - animPY[animPLen - 2],
                animPX[animPLen - 1] - animPX[animPLen - 2]
            );
        }
    }

    /**
     * Megkeresi két út közös csomópontját, ha van ilyen.
     * Ezt a kanyarodási útvonalak számításához használjuk.
     *
     * @param a az egyik út
     * @param b a másik út
     * @return a közös {@link Node}, vagy null, ha nincs
     */
    private static Node sharedNode(Road a, Road b) {
        if (a.getFrom() == b.getFrom() || a.getFrom() == b.getTo()) return a.getFrom();
        if (a.getTo()   == b.getFrom() || a.getTo()   == b.getTo()) return a.getTo();
        return null;
    }

    /** @return true, ha a hókotró éppen vizuális animációt hajt végre */
    public boolean isAnimating() { return animating; }

    /**
     * Beállítja az átlós sávok nézeteinek listáját az alatta való áthaladás 
     * vizuális kezeléséhez.
     *
     * @param views az átlós {@link LaneView} példányok listája
     */
    public void setDiagonalLaneViews(List<LaneView> views) { this.diagonalLaneViews = views; }

    /**
     * Kiszámítja a hókotró rajzolási dőlésszögét.
     * Ha animáció fut, az animációs útvonal szögét adja vissza. Ha az út 
     * vízszintes vagy függőleges, derékszögekre kerekíti a pontos illeszkedés 
     * érdekében.
     *
     * @return a dőlésszög radiánban
     */
    private double getLaneAngle() {
        Lane lane = snowPlow.getCurrentLane();
        LaneView lv = lane != null ? laneViewMap.get(lane) : null;
        boolean diagonal = false;
        if (lv != null) {
            double dx = Math.abs(lv.getX2() - lv.getX1());
            double dy = Math.abs(lv.getY2() - lv.getY1());
            double len = Math.sqrt(dx * dx + dy * dy);
            diagonal = len > 1 && dx > 0.1 * len && dy > 0.1 * len;
        }

        double raw;
        if (!Double.isNaN(currentAngle)) {
            raw = currentAngle;
        } else if (lv != null) {
            raw = Math.atan2(lv.getY2() - lv.getY1(), lv.getX2() - lv.getX1());
        } else {
            return 0;
        }

        if (diagonal) return raw;
        double deg = Math.toDegrees(raw);
        return Math.toRadians(Math.round(deg / 90.0) * 90.0);
    }

    /**
     * Ellenőrzi, hogy a hókotró vizuális pozíciója jelenleg fedésben van-e 
     * egy átlós sávval (kivéve, ha épp azon halad).
     *
     * @return true, ha egy felüljáró alatt tartózkodik
     */
    private boolean isUnderDiagonal() {
        if (diagonalLaneViews == null) return false;
        Lane currentLane = snowPlow.getCurrentLane();
        for (LaneView lv : diagonalLaneViews) {
            if (lv.getLane() == currentLane) return false;
        }
        int ix = (int) visX, iy = (int) visY;
        for (LaneView lv : diagonalLaneViews) {
            if (lv.containsPoint(ix, iy)) return true;
        }
        return false;
    }

    /**
     * Kirajzolja a hókotrót a megadott grafikus kontextusra.
     * * Kezeli a felüljáró alatti áttetszőséget, a megfelelő szögbe történő 
     * elforgatást, az alapjármű és az aktuálisan rászerelt tisztítófej 
     * ikonjának kirajzolását. Képek hiányában színes geometriai alakzatokkal 
     * és betűkódokkal helyettesíti azokat. Végül kirajzolja a kiválasztást 
     * jelző zöld keretet.
     *
     * @param g a grafikus kontextus
     */
    @Override
    public void draw(Graphics2D g) {
        Composite oldComp = g.getComposite();
        if (isUnderDiagonal()) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        int ix = (int) visX, iy = (int) visY;

        if (plowImage != null) {
            double angle = getLaneAngle();
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.translate(ix, iy);
            g2d.rotate(angle + Math.PI / 2);

            g2d.drawImage(plowImage, -IMAGE_WIDTH / 2, -IMAGE_HEIGHT / 2, IMAGE_WIDTH, IMAGE_HEIGHT, null);

            BufferedImage currentHeadImage = getHeadImage();
            if (currentHeadImage != null) {
                int headSize = 14;
                int hx = -headSize / 2;
                int hy = -IMAGE_HEIGHT / 2;
                g2d.rotate(Math.PI, hx + headSize / 2.0, hy + headSize / 2.0);
                g2d.drawImage(currentHeadImage, hx, hy, headSize, headSize, null);
                g2d.rotate(-Math.PI, hx + headSize / 2.0, hy + headSize / 2.0);
            }
            g2d.dispose();

            if (currentHeadImage == null) {
                drawHeadLabel(g, ix, iy);
            }

            if (selected) {
                g.setColor(new Color(0, 200, 0));
                g.setStroke(new BasicStroke(2));
                g.drawRect(ix - IMAGE_HEIGHT / 2 - 3, iy - IMAGE_HEIGHT / 2 - 3, IMAGE_HEIGHT + 6, IMAGE_HEIGHT + 6);
                g.setStroke(new BasicStroke(1));
            }

        } else {
            Color plowColor = getPlowColor();
            g.setColor(plowColor);
            g.fillRect(ix - LOGIC_WIDTH / 2, iy - LOGIC_HEIGHT / 2, LOGIC_WIDTH, LOGIC_HEIGHT);
            g.setColor(plowColor.darker());
            g.drawRect(ix - LOGIC_WIDTH / 2, iy - LOGIC_HEIGHT / 2, LOGIC_WIDTH, LOGIC_HEIGHT);
            drawHeadLabel(g, ix, iy);

            if (selected) {
                g.setColor(new Color(0, 200, 0));
                g.setStroke(new BasicStroke(2));
                g.drawRect(ix - LOGIC_WIDTH / 2 - 3, iy - LOGIC_HEIGHT / 2 - 3, LOGIC_WIDTH + 6, LOGIC_HEIGHT + 6);
                g.setStroke(new BasicStroke(1));
            }
        }
        
        g.setComposite(oldComp);
    }

    /**
     * Lekérdezi a hókotróra szerelt aktuális tisztítófej ({@link CleanerHead}) képét.
     *
     * @return a megfelelő {@link BufferedImage}, vagy null ha nincs/hiányzik
     */
    private BufferedImage getHeadImage() {
        CleanerHead head = snowPlow.getHead();
        if (head == null) return null;
        if (head instanceof SweepHead)     return sweepHeadImage;
        if (head instanceof ThrowHead)     return throwHeadImage;
        if (head instanceof IceBreakerHead) return iceBreakerHeadImage;
        if (head instanceof SaltHead)      return saltHeadImage;
        if (head instanceof DragonHead)    return dragonHeadImage;
        if (head instanceof RockHead)      return rockHeadImage;
        return null;
    }

    /**
     * Kirajzol egy azonosító betűt a hókotróra, ha a textúrák nem elérhetők.
     *
     * @param g  a grafikus kontextus
     * @param ix az X koordináta
     * @param iy az Y koordináta
     */
    private void drawHeadLabel(Graphics2D g, int ix, int iy) {
        String headLabel = getHeadLabel();
        if (!headLabel.isEmpty()) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 12)); 
            FontMetrics fm = g.getFontMetrics();
            g.drawString(headLabel, ix - fm.stringWidth(headLabel) / 2, iy + fm.getAscent() / 2 - 2);
        }
    }

    /**
     * Visszaadja a fallback (kép nélküli) rajzoláshoz használt színt a 
     * felszerelt fej típusa alapján.
     *
     * @return a fejhez rendelt jellemző {@link Color}
     */
    private Color getPlowColor() {
        CleanerHead head = snowPlow.getHead();
        if (head instanceof DragonHead)    return new Color(210, 60, 40);
        if (head instanceof SaltHead)      return new Color(60, 120, 200);
        if (head instanceof SweepHead)     return new Color(80, 170, 70);
        if (head instanceof IceBreakerHead) return new Color(100, 160, 200);
        if (head instanceof RockHead)      return new Color(160, 120, 70);
        if (head instanceof ThrowHead)     return new Color(220, 140, 30);
        return new Color(255, 140, 0);
    }

    /**
     * Frissíti a hókotró célpozícióját a sávján belül, figyelembe véve 
     * a többi járművet és hókotrót, hogy a vizuális átfedés elkerülhető legyen.
     */
    private void updatePosition() {
        Lane lane = snowPlow.getCurrentLane();
        if (lane == null) return;
        LaneView lv = laneViewMap.get(lane);
        if (lv == null) return;

        int vehicleCount = lane.getVehicles().size();
        int myPlowIdx = 0, plowCount = 1;

        if (allPlowViews != null) {
            plowCount = 0;
            for (SnowPlowView spv : allPlowViews) {
                if (spv.getSnowPlow().getCurrentLane() == lane) {
                    if (spv == this) myPlowIdx = plowCount;
                    plowCount++;
                }
            }
        }

        java.awt.Point p = lv.getEntityPosition(vehicleCount + myPlowIdx, vehicleCount + plowCount);
        x = p.x; y = p.y;
    }

    /**
     * Visszaad egy egykarakteres szöveget a felszerelt fej típusa alapján 
     * a fallback megjelenítéshez.
     *
     * @return a fej típusa szerinti betű (pl. "S", "D", "Z")
     */
    private String getHeadLabel() {
        CleanerHead head = snowPlow.getHead();
        if (head == null) return "";
        if (head instanceof SweepHead)     return "S";
        if (head instanceof ThrowHead)     return "H";
        if (head instanceof IceBreakerHead) return "J";
        if (head instanceof SaltHead)      return "Z";
        if (head instanceof DragonHead)    return "D";
        if (head instanceof RockHead)      return "R";
        return "?";
    }

    /**
     * Beállítja az összes hókotró nézetének referenciáját.
     * @param views a {@link SnowPlowView} példányok listája
     */
    public void setAllPlowViews(List<SnowPlowView> views) { this.allPlowViews = views; }
    
    /**
     * Beállítja a hókotró kiválasztott állapotát.
     * @param selected true, ha a hókotró ki van választva
     */
    public void setSelected(boolean selected) { this.selected = selected; }
    
    /** @return true, ha a hókotró éppen ki van választva */
    public boolean isSelected() { return selected; }
    
    /** @return a megjelenített modellpéldány */
    public SnowPlow getSnowPlow() { return snowPlow; }

    /**
     * Megvizsgálja, hogy egy adott képernyőkoordináta a hókotró vizuális 
     * területén (bounding box) belülre esik-e. Ezt egérkattintások 
     * azonosításához (hit-testing) használjuk.
     *
     * @param px a keresett X koordináta
     * @param py a keresett Y koordináta
     * @return true, ha a pont rajta van a hókotrón
     */
    public boolean containsPoint(int px, int py) {
        int ix = (int) visX, iy = (int) visY;
        return px >= ix - IMAGE_WIDTH / 2 - 4 && px <= ix + IMAGE_WIDTH / 2 + 4
            && py >= iy - IMAGE_HEIGHT / 2 - 4 && py <= iy + IMAGE_HEIGHT / 2 + 4;
    }
}