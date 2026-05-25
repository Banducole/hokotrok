import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class GamePanel extends JPanel {

    private final Game game;
    private final List<IDrawable> drawables = new ArrayList<>();
    private final Map<Node, NodeView> nodeViewMap = new HashMap<>();
    private final Map<Lane, LaneView> laneViewMap = new HashMap<>();
    private final List<LaneView> allLaneViews = new ArrayList<>();
    private final List<SnowPlowView> plowViews = new ArrayList<>();
    private final List<VehicleView> vehicleViews = new ArrayList<>();
    private javax.swing.Timer timer;

    private static final int TIMER_MS = 50;
    private static final int NODE_MARGIN = 90;
    private static final int H_SPACING = 210;
    private static final int V_SPACING = 300;

    public GamePanel(Game game) {
        this.game = game;
        setBackground(new Color(210, 218, 225));
        setPreferredSize(new Dimension(1050, 650));
        initViews();
        startTimer();
    }

    private void initViews() {
        City city = game.getCity();
        List<Node> nodes = city.getNodes();

        int[][] coords = computeGridCoordinates(nodes);

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            NodeView nv = new NodeView(node, coords[i][0], coords[i][1]);
            nodeViewMap.put(node, nv);
        }

        Set<Road> visitedRoads = new HashSet<>();
        List<RoadView> roadViews = new ArrayList<>();
        for (Node node : nodes) {
            for (Road road : node.getConnectedRoads()) {
                if (visitedRoads.add(road)) {
                    RoadView rv = new RoadView(road, nodeViewMap);
                    roadViews.add(rv);
                    for (LaneView lv : rv.getLaneViews()) {
                        laneViewMap.put(lv.getLane(), lv);
                        allLaneViews.add(lv);
                    }
                    drawables.add(rv);
                }
            }
        }

        for (NodeView nv : nodeViewMap.values()) {
            drawables.add(nv);
        }

        collectVehiclesAndPlows();
    }

    private void collectVehiclesAndPlows() {
        Set<Vehicle> seenVehicles = new HashSet<>();
        Set<SnowPlow> seenPlows = new HashSet<>();

        for (Node node : game.getCity().getNodes()) {
            for (Road road : node.getConnectedRoads()) {
                for (Lane lane : road.getLanes()) {
                    for (Vehicle v : lane.getVehicles()) {
                        if (seenVehicles.add(v)) {
                            VehicleView vv;
                            if (v instanceof Car) {
                                vv = new CarView((Car) v, laneViewMap);
                            } else {
                                vv = new BusView((Bus) v, laneViewMap);
                            }
                            vehicleViews.add(vv);
                        }
                    }
                    SnowPlow plow = lane.getSnowPlow();
                    if (plow != null && seenPlows.add(plow)) {
                        SnowPlowView spv = new SnowPlowView(plow, laneViewMap);
                        plowViews.add(spv);
                    }
                }
            }
        }

        // Jarmuvek elobb, hokotrok utobb – igy a hokotrok mindig felul rajzolodnak
        drawables.addAll(vehicleViews);
        drawables.addAll(plowViews);

        // Minden nezet megkapja a teljes hokotro-listat az elcsusztatas szamitasahoz
        for (VehicleView vv : vehicleViews) vv.setAllPlowViews(plowViews);
        for (SnowPlowView spv : plowViews) spv.setAllPlowViews(plowViews);
    }

    private int[][] computeGridCoordinates(List<Node> nodes) {
        int n = nodes.size();
        int[][] coords = new int[n][2];

        int cols = 5;
        int rows = (int) Math.ceil((double) n / cols);

        for (int i = 0; i < n; i++) {
            int col = i % cols;
            int row = i / cols;
            coords[i][0] = NODE_MARGIN + col * H_SPACING;
            coords[i][1] = NODE_MARGIN + row * V_SPACING;
        }

        return coords;
    }

    private void startTimer() {
        timer = new javax.swing.Timer(TIMER_MS, e -> {
            for (VehicleView vv : vehicleViews) vv.animTick();
            for (SnowPlowView spv : plowViews) spv.animTick();
            repaint();
        });
        timer.start();
    }

    public boolean isAnimating() {
        for (VehicleView vv : vehicleViews) if (vv.isAnimating()) return true;
        for (SnowPlowView spv : plowViews) if (spv.isAnimating()) return true;
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (IDrawable d : drawables) {
            d.draw(g2d);
        }
    }

    public Game getGame() { return game; }
    public Map<Lane, LaneView> getLaneViewMap() { return laneViewMap; }
    public List<LaneView> getAllLaneViews() { return allLaneViews; }
    public List<SnowPlowView> getPlowViews() { return plowViews; }
    public List<VehicleView> getVehicleViews() { return vehicleViews; }

    public LaneView findLaneViewAt(int px, int py) {
        for (LaneView lv : allLaneViews) {
            if (lv.containsPoint(px, py)) return lv;
        }
        return null;
    }

    public SnowPlowView findPlowViewAt(int px, int py) {
        for (SnowPlowView spv : plowViews) {
            if (spv.containsPoint(px, py)) return spv;
        }
        return null;
    }

    public void addDrawable(IDrawable d) {
        drawables.add(d);
    }
}
