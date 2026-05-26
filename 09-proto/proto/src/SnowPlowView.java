import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class SnowPlowView implements IDrawable {

    // --- KÉPEK BETÖLTÉSE ---
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

    // A játék eredeti logikai mérete (hibakezeléshez)
    private static final int LOGIC_WIDTH = 22;
    private static final int LOGIC_HEIGHT = 14;

    // --- AZ ÚJ KÉP FIX MÉRETE (Állítsd be, hogy beférjen az útra!) ---
    // Mivel a hókotró "álló" (függőleges) helyzetben lesz, a WIDTH a kisebb, a HEIGHT a nagyobb.
    private static final int IMAGE_WIDTH = 16;  // A hókotró szélessége (hogy elférjen a sávban)
    private static final int IMAGE_HEIGHT = 32; // A hókotró hosszúsága

    private final SnowPlow snowPlow;
    private int x, y;
    private float visX, visY;
    private boolean selected;
    private final Map<Lane, LaneView> laneViewMap;
    private List<SnowPlowView> allPlowViews;
    private List<LaneView> diagonalLaneViews;

    private float[] animPX, animPY;
    private int animPLen;
    private float animT;
    private boolean animating;
    private static final float ANIM_STEP = 0.1f;

    public SnowPlowView(SnowPlow snowPlow, Map<Lane, LaneView> laneViewMap) {
        this.snowPlow = snowPlow;
        this.laneViewMap = laneViewMap;
    }

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
    }

    private static Node sharedNode(Road a, Road b) {
        if (a.getFrom() == b.getFrom() || a.getFrom() == b.getTo()) return a.getFrom();
        if (a.getTo()   == b.getFrom() || a.getTo()   == b.getTo()) return a.getTo();
        return null;
    }

    public boolean isAnimating() { return animating; }

    public void setDiagonalLaneViews(List<LaneView> views) { this.diagonalLaneViews = views; }

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

    @Override
    public void draw(Graphics2D g) {
        Composite oldComp = g.getComposite();
        if (isUnderDiagonal()) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        int ix = (int) visX, iy = (int) visY;

        // --- HÓKOTRÓ KÉPÉNEK KIRAJZOLÁSA ---
        if (plowImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.translate(ix, iy); // Eltolás a hókotró közepére
            
            // FORGATÁS JAVÍTÁSA: 180 fokkal (Math.PI) megforgatjuk, hogy a lefelé néző hókotró felfelé nézzen.
            // (Ha még mindig rossz irányba néz, cseréld ki a Math.PI -t 0 -ra vagy -Math.PI/2 -re!)
            g2d.rotate(Math.PI);
            
            // Kép kirajzolása a fix megadott méretekkel
            g2d.drawImage(plowImage, -IMAGE_WIDTH / 2, -IMAGE_HEIGHT / 2, IMAGE_WIDTH, IMAGE_HEIGHT, null);
            
            // --- FEJ KÉPÉNEK RÁRAJZOLÁSA ---
            BufferedImage currentHeadImage = getHeadImage();
            if (currentHeadImage != null) {
                int headSize = 14; // A tisztítófej ikonjának mérete
                // A fejet az Y tengelyen eltoljuk az autó orráig (a tolólaphoz)
                g2d.drawImage(currentHeadImage, -headSize / 2, -IMAGE_HEIGHT / 2, headSize, headSize, null);
            }
            g2d.dispose();
            
            // Ha valamiért nincs meg a fej kép, de a hókotró megvan, az eredeti betűt rajzoljuk ki
            if (currentHeadImage == null) {
                drawHeadLabel(g, ix, iy);
            }

            // --- ZÖLD KIJELÖLŐ KERET ---
            if (selected) {
                g.setColor(new Color(0, 200, 0));
                g.setStroke(new BasicStroke(2));
                // A keret pontosan a manuálisan beállított IMAGE_WIDTH / IMAGE_HEIGHT méreteket öleli körbe
                g.drawRect(ix - IMAGE_WIDTH / 2 - 3, iy - IMAGE_HEIGHT / 2 - 3, IMAGE_WIDTH + 6, IMAGE_HEIGHT + 6);
                g.setStroke(new BasicStroke(1));
            }

        } else {
            // VÉSZHELYZETI RAJZOLÁS (Ha egyáltalán nincs meg a hókotró kép)
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

    private void drawHeadLabel(Graphics2D g, int ix, int iy) {
        String headLabel = getHeadLabel();
        if (!headLabel.isEmpty()) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 12)); 
            FontMetrics fm = g.getFontMetrics();
            g.drawString(headLabel, ix - fm.stringWidth(headLabel) / 2, iy + fm.getAscent() / 2 - 2);
        }
    }

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

    public void setAllPlowViews(List<SnowPlowView> views) { this.allPlowViews = views; }
    public void setSelected(boolean selected) { this.selected = selected; }
    public boolean isSelected() { return selected; }
    public SnowPlow getSnowPlow() { return snowPlow; }

    public boolean containsPoint(int px, int py) {
        int ix = (int) visX, iy = (int) visY;
        // JAVÍTVA: Itt is az új IMAGE_WIDTH és IMAGE_HEIGHT méreteket használjuk a kattintás érzékeléséhez!
        return px >= ix - IMAGE_WIDTH / 2 - 4 && px <= ix + IMAGE_WIDTH / 2 + 4
            && py >= iy - IMAGE_HEIGHT / 2 - 4 && py <= iy + IMAGE_HEIGHT / 2 + 4;
    }
}