import java.awt.*;

/**
 * Egy sáv ({@link Lane}) grafikus megjelenítéséért felelős osztály a Hókotrók projektben.
 * 
 * Megvalósítja az {@link IDrawable} interfészt. Feladata a sáv vizuális állapotának
 * (pl. tiszta, havas, jeges) megfelelő színű kirajzolása, a sáv széleinek (szegélyvonalak)
 * megrajzolása, valamint a speciális állapotok – mint a zúzalék ({@code isRocky()}) vagy 
 * a feltört jég ({@link BrokenIceState}) – extra textúráinak megjelenítése. Emellett 
 * felelős a rajta elhelyezkedő entitások elrendezéséért és az egérkattintások 
 * azonosításáért is.
 * 
 */
public class LaneView implements IDrawable {

    /** A nézethez tartozó sáv ({@link Lane}) modellpéldánya. */
    private final Lane lane;
    
    /** A sáv kezdőpontjának X koordinátája. */
    private int x1;
    /** A sáv kezdőpontjának Y koordinátája. */
    private int y1;
    /** A sáv végpontjának X koordinátája. */
    private int x2;
    /** A sáv végpontjának Y koordinátája. */
    private int y2;
    
    /** A sáv fix grafikus vastagsága pixelben. */
    private static final int LANE_WIDTH = 36;

    /**
     * Létrehozza a sáv nézetét a megadott modellpéldány alapján.
     *
     * @param lane a megjelenítendő {@link Lane} objektum
     */
    public LaneView(Lane lane) {
        this.lane = lane;
    }

    /**
     * Beállítja a sáv rajzolásához szükséges kezdő- és végpontok képernyőkoordinátáit.
     *
     * @param x1 a kezdőpont X koordinátája
     * @param y1 a kezdőpont Y koordinátája
     * @param x2 a végpont X koordinátája
     * @param y2 a végpont Y koordinátája
     */
    public void setEndpoints(int x1, int y1, int x2, int y2) {
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
    }

    /**
     * Kirajzolja a sávot a megadott grafikus kontextusra.
     * 
     * A rajzolás három fő lépésből áll:
     * A vastag alapsáv megrajzolása a sáv állapotától ({@link LaneState}) függő színnel.
     * A vékony, szürke szegélyvonalak megrajzolása a sáv mindkét oldalán (normálvektoros eltolással).
     * Az "extrák" kirajzolása: zúzalék pöttyök, ha a sáv kavicsos, illetve repedések, ha az állapota {@link BrokenIceState}.
     *
     * @param g a {@link Graphics2D} objektum, amire a rajzolás történik
     */
    @Override
    public void draw(Graphics2D g) {
        Stroke old = g.getStroke();

        g.setStroke(new BasicStroke(LANE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        g.setColor(getStateColor());
        g.drawLine(x1, y1, x2, y2);

        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(170, 175, 180));
        
        double dx = x2 - x1;
        double dy = y2 - y1;
        double len = Math.sqrt(dx * dx + dy * dy);
        
        if (len > 0) {
            // Merőleges normálvektor kiszámítása az eltoláshoz
            double nx = -dy / len;
            double ny = dx / len;
            int ox = (int) Math.round(nx * LANE_WIDTH / 2.0);
            int oy = (int) Math.round(ny * LANE_WIDTH / 2.0);

            // A két párhuzamos szegélyvonal megrajzolása
            g.drawLine(x1 + ox, y1 + oy, x2 + ox, y2 + oy);
            g.drawLine(x1 - ox, y1 - oy, x2 - ox, y2 - oy);
        }

        // 3. Extrák (zúzalék és repedések) megrajzolása
        if (lane.isRocky()) {
            g.setColor(new Color(120, 80, 30, 180));
            drawDots(g);
        }

        if (lane.getState() instanceof BrokenIceState) {
            g.setColor(new Color(0, 80, 120, 120));
            drawCracks(g);
        }

        g.setStroke(old);
    }

    /**
     * Kirajzolja a zúzalékot (apró pöttyöket) a sáv hosszában, 
     * ha a sáv kaviccsal/zúzalékkal van felszórva.
     *
     * @param g a {@link Graphics2D} kontextus
     */
    private void drawDots(Graphics2D g) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len < 1) return;
        int steps = Math.max(4, (int) (len / 15));
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            int px = (int) (x1 + t * dx);
            int py = (int) (y1 + t * dy);
            g.fillOval(px - 3, py - 3, 6, 6);
        }
    }

    /**
     * Kirajzolja a jégrepedéseket a sáv hosszában, amit a 
     * feltört jég ({@link BrokenIceState}) állapothoz használunk.
     *
     * @param g a {@link Graphics2D} kontextus
     */
    private void drawCracks(Graphics2D g) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len < 1) return;
        int segments = Math.max(3, (int) (len / 30));
        boolean vertical = Math.abs(dy) > Math.abs(dx);
        for (int i = 0; i < segments; i++) {
            double t = (double) i / segments + 0.05;
            int cx = (int) (x1 + t * dx);
            int cy = (int) (y1 + t * dy);
            int spread = LANE_WIDTH / 3;
            if (vertical) {
                g.drawLine(cx - spread, cy - spread / 2, cx + spread, cy + spread / 2);
                g.drawLine(cx - spread / 2, cy + spread, cx + spread / 2, cy - spread);
            } else {
                g.drawLine(cx - spread / 2, cy - spread, cx + spread / 2, cy + spread);
                g.drawLine(cx + spread, cy - spread / 2, cx - spread, cy + spread / 2);
            }
        }
    }

    /**
     * Visszaadja a sáv állapotának megfelelő kitöltési színt.
     *
     * @return a sáv aktuális állapotát ({@link LaneState}) reprezentáló {@link Color}
     */
    private Color getStateColor() {
        LaneState st = lane.getState();
        if (st instanceof ClearState) return new Color(180, 185, 190);
        if (st instanceof ThinSnowState) return new Color(180, 210, 235);
        if (st instanceof ThickSnowState) return new Color(210, 225, 240);
        if (st instanceof IcyState) return new Color(0, 210, 230);
        if (st instanceof BrokenIceState) return new Color(0, 200, 220);
        return Color.GRAY;
    }

    /**
     * Kiszámítja egy jármű vagy hókotró pontos pozícióját a sávon belül úgy,
     * hogy több entitás esetén azok ne fedjék át teljesen egymást.
     *
     * @param idx   az aktuális entitás sorszáma a sávon
     * @param total a sávon lévő összes entitás száma
     * @return a kiszámított képernyőkoordináta egy {@link Point} objektumban
     */
    public Point getEntityPosition(int idx, int total) {
        int cx = getCenterX();
        int cy = getCenterY();
        if (total <= 1) return new Point(cx, cy);

        double dx = x2 - x1;
        double dy = y2 - y1;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len < 1) return new Point(cx, cy);

        double nx = dx / len;
        double ny = dy / len;

        double step = 16.0;
        double offset = (idx - (total - 1) / 2.0) * step;

        return new Point((int)(cx + nx * offset), (int)(cy + ny * offset));
    }

    /** @return a nézethez tartozó modell ({@link Lane}) */
    public Lane getLane() { return lane; }
    
    /** @return a sáv középpontjának X koordinátája */
    public int getCenterX() { return (x1 + x2) / 2; }
    
    /** @return a sáv középpontjának Y koordinátája */
    public int getCenterY() { return (y1 + y2) / 2; }
    
    /** @return a sáv kezdőpontjának X koordinátája */
    public int getX1() { return x1; }
    
    /** @return a sáv kezdőpontjának Y koordinátája */
    public int getY1() { return y1; }
    
    /** @return a sáv végpontjának X koordinátája */
    public int getX2() { return x2; }
    
    /** @return a sáv végpontjának Y koordinátája */
    public int getY2() { return y2; }

    /**
     * Megvizsgálja, hogy egy adott képernyőkoordináta a sáv grafikus területén belülre esik-e.
     * Ezt az egérkattintások azonosítására (hit-testing) használjuk.
     *
     * @param px a vizsgálandó X koordináta
     * @param py a vizsgálandó Y koordináta
     * @return {@code true}, ha a pont a sávhoz tartozik, egyébként {@code false}
     */
    public boolean containsPoint(int px, int py) {
        return distToSegment(px, py) <= LANE_WIDTH / 2 + 4;
    }

    /**
     * Kiszámítja egy adott pont és a sáv (mint vonalszakasz) közötti legrövidebb távolságot.
     *
     * @param px a pont X koordinátája
     * @param py a pont Y koordinátája
     * @return a távolság pixelben
     */
    private double distToSegment(int px, int py) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double lenSq = dx * dx + dy * dy;
        if (lenSq == 0) return Math.hypot(px - x1, py - y1);
        double t = Math.max(0, Math.min(1, ((px - x1) * dx + (py - y1) * dy) / lenSq));
        double projX = x1 + t * dx;
        double projY = y1 + t * dy;
        return Math.hypot(px - projX, py - projY);
    }
}