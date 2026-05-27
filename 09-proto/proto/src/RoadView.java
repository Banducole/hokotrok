import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Hókotrók projektben egy teljes út ({@link Road}) grafikus megjelenítéséért felelős osztály.
 * * Ez az osztály fogja össze az úthoz tartozó sávok nézeteit ({@link LaneView}).
 * Fő feladata a sávok párhuzamos elrendezésének kiszámítása a csomópontok kezdő- és végpontjai 
 * alapján, valamint az átlós utak (felüljárók) esetén az árnyékhatás megrajzolása a 
 * vizuális mélységérzet érdekében.
 */
public class RoadView implements IDrawable {

    /** Az úthoz tartozó modellpéldány. */
    private final Road road;
    
    /** Az úthoz tartozó sávok grafikus nézeteinek listája. */
    private final List<LaneView> laneViews = new ArrayList<>();
    
    /** Jelzi, hogy az út átlós elrendezésű-e (felüljáróként jelenik-e meg). */
    private boolean diagonal;

    /** Az árnyék eltolásának mértéke pixelben az átlós utaknál. */
    private static final int SHADOW_OFFSET = 6;
    
    /** Az árnyék színe (félig átlátszó fekete). */
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 60);

    /**
     * Létrehozza az út nézetét a megadott modellpéldány és a csomópontok 
     * nézeteit tartalmazó térkép alapján. Automatikusan kiszámítja és 
     * inicializálja az úthoz tartozó sávok nézeteit.
     *
     * @param road a megjelenítendő út ({@link Road}) modellje
     * @param nodeMap a csomópontokat és azok nézeteit összerendelő térkép a koordinátákhoz
     */
    public RoadView(Road road, Map<Node, NodeView> nodeMap) {
        this.road = road;
        initLaneViews(nodeMap);
    }

    /**
     * Kiszámítja és létrehozza a sávok nézeteit az út kezdő- és végpontja alapján.
     * * Meghatározza, hogy az út átlós-e (ha X és Y irányban is jelentős az elmozdulás). 
     * Ezután a haladási irányra merőleges normálvektor segítségével kiszámítja az egyes 
     * sávok párhuzamos eltolását, és pontosan beállítja a sávnézetek ({@link LaneView}) 
     * végpontjait úgy, hogy azok egymás mellett helyezkedjenek el.
     *
     * @param nodeMap a csomópont-nézet összerendeléseket tartalmazó térkép
     */
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

    /**
     * Kirajzolja az utat a megadott grafikus kontextusra.
     * * Ha az út átlós, először egy árnyékot rajzol alá (felüljáró hatás), majd 
     * meghívja az összes hozzá tartozó sávnézet rajzoló metódusát.
     *
     * @param g a {@link Graphics2D} objektum, amire a rajzolás történik
     */
    @Override
    public void draw(Graphics2D g) {
        if (diagonal) {
            drawShadow(g);
        }
        for (LaneView lv : laneViews) {
            lv.draw(g);
        }
    }

    /**
     * Megrajzolja az átlós utak (felüljárók) alatti árnyékot.
     * * Félig átlátszó, a sávokhoz képest eltolt, vastag vonalakkal 
     * vizuális mélységet ad az útnak, kiemelve azt a háttérből.
     *
     * @param g a {@link Graphics2D} kontextus
     */
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

    /**
     * Visszaadja, hogy az út átlós (felüljáró) elrendezésű-e.
     *
     * @return true, ha az út átlósan helyezkedik el
     */
    public boolean isDiagonal() { return diagonal; }
    
    /**
     * Visszaadja az úthoz tartozó sávok nézeteinek listáját.
     *
     * @return a sávok nézeteit ({@link LaneView}) tartalmazó lista
     */
    public List<LaneView> getLaneViews() { return laneViews; }
    
    /**
     * Visszaadja a nézethez tartozó út modelljét.
     *
     * @return a megjelenített {@link Road} objektum
     */
    public Road getRoad() { return road; }
}