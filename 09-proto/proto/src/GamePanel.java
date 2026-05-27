import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * A játéktér grafikus megjelenítéséért felelős panel a Hókotrók projektben.
 * 
 * A {@link JPanel} leszármazottjaként ez az osztály végzi a város ({@link City}) 
 * elemeinek – csomópontok ({@link Node}), utak ({@link Road}), sávok ({@link Lane}), 
 * járművek ({@link Vehicle}) és hókotrók ({@link SnowPlow}) – vizuális reprezentációját.
 * Kezeli a grafikus elemek ({@link IDrawable}) helyes sorrendben történő kirajzolását, 
 * az animációk időzítését, valamint a kattintások helyének azonosítását (hit-testing).
 * 
 */
public class GamePanel extends JPanel {

    /** A játék belső logikáját és állapotát tároló modell objektum. */
    private final Game game;
    
    /** A kirajzolandó grafikus elemek listája, a rajzolási sorrendnek megfelelően. */
    private final List<IDrawable> drawables = new ArrayList<>();
    
    /** A csomópontokat ({@link Node}) és azok nézeteit ({@link NodeView}) összerendelő térkép. */
    private final Map<Node, NodeView> nodeViewMap = new HashMap<>();
    
    /** A sávokat ({@link Lane}) és azok nézeteit ({@link LaneView}) összerendelő térkép. */
    private final Map<Lane, LaneView> laneViewMap = new HashMap<>();
    
    /** Az összes sáv nézetének ({@link LaneView}) listája a gyorsabb iteráció érdekében. */
    private final List<LaneView> allLaneViews = new ArrayList<>();
    
    /** A hókotrók nézeteit ({@link SnowPlowView}) tartalmazó lista. */
    private final List<SnowPlowView> plowViews = new ArrayList<>();
    
    /** A járművek (autók és buszok) nézeteit ({@link VehicleView}) tartalmazó lista. */
    private final List<VehicleView> vehicleViews = new ArrayList<>();
    
    /** Az animációkat vezérlő Swing időzítő. */
    private javax.swing.Timer timer;

    /** Az animációs időzítő frissítési gyakorisága milliszekundumban. */
    private static final int TIMER_MS = 50;
    
    /** A rács margója pixelben (távolság a panel szélétől). */
    private static final int NODE_MARGIN = 90;
    
    /** A csomópontok közötti vízszintes távolság pixelben. */
    private static final int H_SPACING = 210;
    
    /** A csomópontok közötti függőleges távolság pixelben. */
    private static final int V_SPACING = 300;

    /**
     * Létrehozza a játéktér panelt a megadott játékmodell alapján.
     * 
     * Beállítja a háttérszínt és az alapértelmezett méretet, majd meghívja
     * a nézetek inicializálását végző {@link #initViews()} és az animációt
     * elindító {@link #startTimer()} metódusokat.
     *
     * @param game a játék belső állapotát és logikáját tartalmazó {@link Game} modellpéldány
     */
    public GamePanel(Game game) {
        this.game = game;
        setBackground(new Color(210, 218, 225));
        setPreferredSize(new Dimension(1050, 650));
        initViews();
        startTimer();
    }

    /**
     * Inicializálja a játéktér összes grafikus nézetét és felépíti a rajzolási sorrendet.
     * 
     * A metódus először kiszámítja a csomópontok rácskoordinátáit, majd létrehozza
     * a {@link NodeView} és {@link RoadView} (valamint a bennük lévő {@link LaneView})
     * példányokat. Gondoskodik a megfelelő "Z-index" szerinti rétegzésről:
     * először az egyenes utak, majd az átlós utak, utána a csomópontok, végül a 
     * járművek és hókotrók kerülnek a {@link #drawables} listába.
     * 
     */
    private void initViews() {
        City city = game.getCity();
        List<Node> nodes = city.getNodes();

        int[][] coords = computeGridCoordinates(nodes);

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            NodeView nv = new NodeView(node, coords[i][0], coords[i][1]);
            nodeViewMap.put(node, nv);
        }

        for (NodeView nv : nodeViewMap.values()) {
            nv.computeSize(nodeViewMap);
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
                }
            }
        }

        List<LaneView> diagonalLanes = new ArrayList<>();
        for (RoadView rv : roadViews) {
            if (!rv.isDiagonal()) {
                drawables.add(rv);
            } else {
                diagonalLanes.addAll(rv.getLaneViews());
            }
        }
        for (RoadView rv : roadViews) {
            if (rv.isDiagonal()) drawables.add(rv);
        }
        for (NodeView nv : nodeViewMap.values()) {
            drawables.add(nv);
        }

        collectVehiclesAndPlows(diagonalLanes);
    }

    /**
     * Összegyűjti a sávokon található járműveket és hókotrókat, majd létrehozza a nézeteiket.
     * 
     * A metódus bejárja a város összes sávját, példányosítja a {@link CarView}, 
     * {@link BusView} és {@link SnowPlowView} objektumokat. A hókotrókat a járművek után 
     * adja a rajzolási listához, hogy mindig felül jelenjenek meg. Végül átadja a nézeteknek 
     * a szükséges referenciákat (pl. átlós sávok és más hókotrók listája) az elcsúsztatások 
     * (ütközéselkerülés) számításához.
     * 
     *
     * @param diagonalLanes az átlós sávok nézeteit tartalmazó lista
     */
    private void collectVehiclesAndPlows(List<LaneView> diagonalLanes) {
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

        // Járművek előbb, hókotrók utóbb – így a hókotrók mindig felül rajzolódnak
        drawables.addAll(vehicleViews);
        drawables.addAll(plowViews);

        // Minden nézet megkapja a teljes hókotró-listát az elcsúsztatás számításához
        for (VehicleView vv : vehicleViews) {
            vv.setAllPlowViews(plowViews);
            vv.setDiagonalLaneViews(diagonalLanes);
        }
        for (SnowPlowView spv : plowViews) {
            spv.setAllPlowViews(plowViews);
            spv.setDiagonalLaneViews(diagonalLanes);
        }
    }

    /**
     * Kiszámítja a csomópontok statikus, 5 oszlopos rácsra illeszkedő koordinátáit.
     *
     * @param nodes a város csomópontjait tartalmazó lista
     * @return egy 2D tömb, ahol a {@code coords[i][0]} az X, a {@code coords[i][1]} az Y koordináta
     */
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

    /**
     * Elindítja a játéktér animációs időzítőjét.
     * 
     * A {@link javax.swing.Timer} {@value #TIMER_MS} milliszekundumonként meghívja 
     * a járművek és hókotrók {@code animTick()} metódusát a sima mozgás érdekében, 
     * majd újrarajzoltatja ({@code repaint()}) a panelt.
     * 
     */
    private void startTimer() {
        timer = new javax.swing.Timer(TIMER_MS, e -> {
            for (VehicleView vv : vehicleViews) vv.animTick();
            for (SnowPlowView spv : plowViews) spv.animTick();
            repaint();
        });
        timer.start();
    }

    /**
     * Megvizsgálja, hogy van-e folyamatban lévő animáció a játéktéren.
     *
     * @return {@code true}, ha legalább egy jármű vagy hókotró éppen animációt hajt végre, egyébként {@code false}
     */
    public boolean isAnimating() {
        for (VehicleView vv : vehicleViews) if (vv.isAnimating()) return true;
        for (SnowPlowView spv : plowViews) if (spv.isAnimating()) return true;
        return false;
    }

    /**
     * A panel tényleges kirajzolását végző metódus.
     * 
     * Bekapcsolja az élsimítást (anti-aliasing) a szebb megjelenés érdekében, 
     * majd végigiterál a {@link #drawables} listán, és minden elemet kirajzol.
     *
     * @param g a {@link Graphics} kontextus
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (IDrawable d : drawables) {
            d.draw(g2d);
        }
    }

    /** @return a játék belső modellje ({@link Game}) */
    public Game getGame() { return game; }
    
    /** @return a sávok modell-nézet összerendelését tartalmazó térkép */
    public Map<Lane, LaneView> getLaneViewMap() { return laneViewMap; }
    
    /** @return az összes sáv nézetét ({@link LaneView}) tartalmazó lista */
    public List<LaneView> getAllLaneViews() { return allLaneViews; }
    
    /** @return a hókotrók nézeteit ({@link SnowPlowView}) tartalmazó lista */
    public List<SnowPlowView> getPlowViews() { return plowViews; }
    
    /** @return a járművek nézeteit ({@link VehicleView}) tartalmazó lista */
    public List<VehicleView> getVehicleViews() { return vehicleViews; }

    /**
     * Megkeresi azt a sávnézetet, amelyik tartalmazza a megadott képernyőkoordinátát.
     * Ezt jellemzően az egérkattintások azonosítására (hit-testing) használja a vezérlő.
     *
     * @param px a keresett X koordináta
     * @param py a keresett Y koordináta
     * @return a koordinátát tartalmazó {@link LaneView}, vagy {@code null}, ha nincs ilyen
     */
    public LaneView findLaneViewAt(int px, int py) {
        for (LaneView lv : allLaneViews) {
            if (lv.containsPoint(px, py)) return lv;
        }
        return null;
    }

    /**
     * Megkeresi azt a hókotrónézetet, amelyik tartalmazza a megadott képernyőkoordinátát.
     *
     * @param px a keresett X koordináta
     * @param py a keresett Y koordináta
     * @return a koordinátát tartalmazó {@link SnowPlowView}, vagy {@code null}, ha nincs ilyen
     */
    public SnowPlowView findPlowViewAt(int px, int py) {
        for (SnowPlowView spv : plowViews) {
            if (spv.containsPoint(px, py)) return spv;
        }
        return null;
    }

    /**
     * Hozzáad egy új, kirajzolható grafikus elemet a panelhez.
     *
     * @param d a hozzáadandó {@link IDrawable} példány
     */
    public void addDrawable(IDrawable d) {
        drawables.add(d);
    }
}