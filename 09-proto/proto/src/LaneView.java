import java.awt.*;

/**
 * Sav megjelenito. Szines teglalap az allapot szerint, zuzalek/tort jeg overlay-jel.
 */
public class LaneView implements IDrawable {

    private final Lane lane;
    private int x1, y1, x2, y2;
    private static final int LANE_WIDTH = 14;

    public LaneView(Lane lane) {
        this.lane = lane;
    }

    public void setEndpoints(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void draw(Graphics2D g) {
        Stroke old = g.getStroke();
        g.setStroke(new BasicStroke(LANE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(getStateColor());
        g.drawLine(x1, y1, x2, y2);

        if (lane.isRocky()) {
            g.setColor(new Color(139, 90, 43, 160));
            drawDots(g);
        }

        if (lane.getState() instanceof BrokenIceState) {
            g.setStroke(new BasicStroke(1));
            g.setColor(new Color(0, 80, 120, 150));
            drawCracks(g);
        }

        g.setStroke(old);
    }

    private void drawDots(Graphics2D g) {
        int cx = (x1 + x2) / 2;
        int cy = (y1 + y2) / 2;
        int dx = x2 - x1;
        int dy = y2 - y1;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len < 1) return;
        int steps = Math.max(3, (int) (len / 20));
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            int px = (int) (x1 + t * dx);
            int py = (int) (y1 + t * dy);
            g.fillOval(px - 2, py - 2, 4, 4);
        }
    }

    private void drawCracks(Graphics2D g) {
        int cx = (x1 + x2) / 2;
        int cy = (y1 + y2) / 2;
        g.drawLine(cx - 5, cy - 5, cx + 5, cy + 5);
        g.drawLine(cx - 5, cy + 5, cx + 5, cy - 5);
        g.drawLine(cx - 3, cy - 7, cx + 3, cy + 7);
    }

    private Color getStateColor() {
        LaneState st = lane.getState();
        if (st instanceof ClearState) return new Color(160, 160, 160);
        if (st instanceof ThinSnowState) return new Color(173, 216, 230);
        if (st instanceof ThickSnowState) return Color.WHITE;
        if (st instanceof IcyState) return new Color(0, 200, 220);
        if (st instanceof BrokenIceState) return new Color(0, 200, 220);
        return Color.GRAY;
    }

    public Lane getLane() { return lane; }
    public int getCenterX() { return (x1 + x2) / 2; }
    public int getCenterY() { return (y1 + y2) / 2; }
    public int getX1() { return x1; }
    public int getY1() { return y1; }
    public int getX2() { return x2; }
    public int getY2() { return y2; }

    public boolean containsPoint(int px, int py) {
        return distToSegment(px, py) <= LANE_WIDTH / 2 + 3;
    }

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
