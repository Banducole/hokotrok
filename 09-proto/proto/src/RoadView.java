import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoadView implements IDrawable {

    private final Road road;
    private final List<LaneView> laneViews = new ArrayList<>();
    private boolean diagonal;

    private static final int SHADOW_OFFSET = 6;
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 60);

    public RoadView(Road road, Map<Node, NodeView> nodeMap) {
        this.road = road;
        initLaneViews(nodeMap);
    }

    private void initLaneViews(Map<Node, NodeView> nodeMap) {
        NodeView fromView = nodeMap.get(road.getFrom());
        NodeView toView = nodeMap.get(road.getTo());
        if (fromView == null || toView == null) return;

        int fx = fromView.getX();
        int fy = fromView.getY();
        int tx = toView.getX();
        int ty = toView.getY();

        double dx = tx - fx;
        double dy = ty - fy;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len < 1) return;

        double absDx = Math.abs(dx);
        double absDy = Math.abs(dy);
        diagonal = absDx > 0.1 * len && absDy > 0.1 * len;

        double nx = -dy / len;
        double ny = dx / len;

        int laneCount = road.getLanes().size();
        double laneSpacing = 38.0;
        double totalWidth = laneCount * laneSpacing;
        double startOffset = -totalWidth / 2.0 + laneSpacing / 2.0;

        for (int i = 0; i < laneCount; i++) {
            Lane lane = road.getLanes().get(i);
            LaneView lv = new LaneView(lane);
            double offset = startOffset + i * laneSpacing;
            int lx1 = (int) (fx + nx * offset);
            int ly1 = (int) (fy + ny * offset);
            int lx2 = (int) (tx + nx * offset);
            int ly2 = (int) (ty + ny * offset);
            lv.setEndpoints(lx1, ly1, lx2, ly2);
            laneViews.add(lv);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (diagonal) {
            drawShadow(g);
        }
        for (LaneView lv : laneViews) {
            lv.draw(g);
        }
    }

    private void drawShadow(Graphics2D g) {
        Stroke old = g.getStroke();
        g.setColor(SHADOW_COLOR);
        for (LaneView lv : laneViews) {
            g.setStroke(new BasicStroke(40, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine(
                lv.getX1() + SHADOW_OFFSET, lv.getY1() + SHADOW_OFFSET,
                lv.getX2() + SHADOW_OFFSET, lv.getY2() + SHADOW_OFFSET
            );
        }
        g.setStroke(old);
    }

    public boolean isDiagonal() { return diagonal; }
    public List<LaneView> getLaneViews() { return laneViews; }
    public Road getRoad() { return road; }
}
