import java.util.*;

/**
 * A varost reprezentalo osztaly, amely csompontokat es utakat tart szamon.
 * <p>
 * A varos felelos a jarmuvek es hokotrok korleptetesert ({@link #executeStep()}),
 * valamint a varosszintu hoesesert ({@link #applySnowfall(int)}).
 * Minden ut csak egyszer kerul feldolgozasra, duplikalt bejaras elkerulesere
 * a mar latogatott utakat nyilvantartja.
 * </p>
 */
public class City {

    /** A varosban levo osszes csomopont listaja. */
    private final List<Node> nodes = new ArrayList<>();

    /**
     * Visszaadja a varos osszes csomopontinak listajat.
     *
     * @return a csompontok listaja
     */
    public List<Node> getNodes() { return nodes; }

    /**
     * Hozzaad egy csompontot a varoshoz.
     *
     * @param n a hozzaadando csomopont
     */
    public void addNode(Node n)  { nodes.add(n); }

    /**
     * Egy korlepes vegrehajtasa: eloszor az osszes jarmu, majd az osszes hokotro lep.
     * Minden ut es jarmupeldany csak egyszer kerül feldolgozasra.
     */
    public void executeStep() {
        Set<Vehicle> vehicles = new LinkedHashSet<>();
        Set<Road> visited = new HashSet<>();

        /* Az osszes sav jarmuveit osszegyujtjuk */
        for (Node node : nodes) {
            for (Road road : node.getConnectedRoads()) {
                if (visited.contains(road)) continue;
                visited.add(road);
                for (Lane lane : road.getLanes()) {
                    vehicles.addAll(lane.getVehicles());
                }
            }
        }

        /* Jarmuvek lepnek – a hokotrok kizarolag manualis lepeskor takaritanak */
        for (Vehicle v : vehicles) v.step(false);
    }

    /**
     * Varosszintu hoeses: minden ut osszes savjara alkalmazza a megadott ho mennyiseget.
     * Minden ut csak egyszer kap hót (duplikalt csomopont-referenciaktol fuggetlenul).
     *
     * @param amount az egesz varosban eso ho mennyisege egysegekben
     */
    public void applySnowfall(int amount) {
        Set<Road> visited = new HashSet<>();
        for (Node node : nodes) {
            for (Road road : node.getConnectedRoads()) {
                if (!visited.contains(road)) {
                    road.applySnow(amount);
                    visited.add(road);
                }
            }
        }
    }
}
