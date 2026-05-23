import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoadView implements IDrawable {

    private final Road road;
    private final List<LaneView> laneViews = new ArrayList<>();

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
        for (LaneView lv : laneViews) {
            lv.draw(g);
        }
    }

    public List<LaneView> getLaneViews() { return laneViews; }
    public Road getRoad() { return road; }
}
