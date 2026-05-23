import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * A terkep feluleti panel. Tartalmazza az osszes rajzolhato elemet
 * (IDrawable) es periodikusan ujrarajzolja oket egy Swing Timer-rel.
 */
public class GamePanel extends JPanel {

    private final Game game;
    private final List<IDrawable> drawables = new ArrayList<>();
    private final Map<Node, NodeView> nodeViewMap = new HashMap<>();
    private final Map<Lane, LaneView> laneViewMap = new HashMap<>();
    private final List<LaneView> allLaneViews = new ArrayList<>();
    private final List<SnowPlowView> plowViews = new ArrayList<>();
    private final List<VehicleView> vehicleViews = new ArrayList<>();
    private javax.swing.Timer timer;

    private static final int TIMER_MS = 100;

    public GamePanel(Game game) {
        this.game = game;
        setBackground(new Color(40, 45, 50));
        setPreferredSize(new Dimension(800, 600));
        initViews();
        startTimer();
    }

    private void initViews() {
        City city = game.getCity();
        List<Node> nodes = city.getNodes();

        int[][] coords = computeNodeCoordinates(nodes);

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
                            drawables.add(vv);
                        }
                    }
                    SnowPlow plow = lane.getSnowPlow();
                    if (plow != null && seenPlows.add(plow)) {
                        SnowPlowView spv = new SnowPlowView(plow, laneViewMap);
                        plowViews.add(spv);
                        drawables.add(spv);
                    }
                }
            }
        }
    }

    private int[][] computeNodeCoordinates(List<Node> nodes) {
        int n = nodes.size();
        int[][] coords = new int[n][2];

        int panelW = 800;
        int panelH = 600;
        int margin = 80;

        if (n == 0) return coords;
        if (n == 1) {
            coords[0] = new int[]{panelW / 2, panelH / 2};
            return coords;
        }

        int cols = (int) Math.ceil(Math.sqrt(n * 1.5));
        int rows = (int) Math.ceil((double) n / cols);

        double xStep = (double) (panelW - 2 * margin) / Math.max(1, cols - 1);
        double yStep = (double) (panelH - 2 * margin) / Math.max(1, rows - 1);

        for (int i = 0; i < n; i++) {
            int col = i % cols;
            int row = i / cols;
            int jitter = (row % 2 == 1) ? (int)(xStep * 0.3) : 0;
            coords[i][0] = margin + (int) (col * xStep) + jitter;
            coords[i][1] = margin + (int) (row * yStep);
        }

        return coords;
    }

    private void startTimer() {
        timer = new javax.swing.Timer(TIMER_MS, e -> repaint());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (IDrawable d : drawables) {
            d.draw(g2d);
        }

        drawLegend(g2d);
    }

    private void drawLegend(Graphics2D g) {
        int lx = 10;
        int ly = getHeight() - 110;
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.setColor(new Color(200, 200, 200));

        String[] labels = {"Tiszta", "Vekony ho", "Vastag ho", "Jeges", "Tort jeg"};
        Color[] colors = {
            new Color(160, 160, 160),
            new Color(173, 216, 230),
            Color.WHITE,
            new Color(0, 200, 220),
            new Color(0, 200, 220)
        };

        for (int i = 0; i < labels.length; i++) {
            g.setColor(colors[i]);
            g.fillRect(lx, ly + i * 18, 14, 14);
            g.setColor(Color.BLACK);
            g.drawRect(lx, ly + i * 18, 14, 14);
            g.setColor(new Color(200, 200, 200));
            g.drawString(labels[i], lx + 20, ly + i * 18 + 12);
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
