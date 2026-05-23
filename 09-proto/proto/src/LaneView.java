import java.awt.*;

public class LaneView implements IDrawable {

    private final Lane lane;
    private int x1, y1, x2, y2;
    private static final int LANE_WIDTH = 36;

    public LaneView(Lane lane) {
        this.lane = lane;
    }

    public void setEndpoints(int x1, int y1, int x2, int y2) {
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
    }

    @Override
    public void draw(Graphics2D g) {
        Stroke old = g.getStroke();

        g.setStroke(new BasicStroke(LANE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        g.setColor(getStateColor());
        g.drawLine(x1, y1, x2, y2);

        g.setStroke(new BasicStroke(1));
        g.setColor(new Color(170, 175, 180));
        boolean vertical = Math.abs(y2 - y1) > Math.abs(x2 - x1);
        if (vertical) {
            int left = Math.min(x1, x2) - LANE_WIDTH / 2;
            int right = Math.max(x1, x2) + LANE_WIDTH / 2;
            int top = Math.min(y1, y2);
            int bot = Math.max(y1, y2);
            g.drawLine(left, top, left, bot);
            g.drawLine(right, top, right, bot);
        } else {
            int top = Math.min(y1, y2) - LANE_WIDTH / 2;
            int bot = Math.max(y1, y2) + LANE_WIDTH / 2;
            int left = Math.min(x1, x2);
            int right = Math.max(x1, x2);
            g.drawLine(left, top, right, top);
            g.drawLine(left, bot, right, bot);
        }

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

    private Color getStateColor() {
        LaneState st = lane.getState();
        if (st instanceof ClearState) return new Color(180, 185, 190);
        if (st instanceof ThinSnowState) return new Color(180, 210, 235);
        if (st instanceof ThickSnowState) return new Color(210, 225, 240);
        if (st instanceof IcyState) return new Color(0, 210, 230);
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
        return distToSegment(px, py) <= LANE_WIDTH / 2 + 4;
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
