import java.util.*;

public class City {
    private final List<Node> nodes = new ArrayList<>();

    public List<Node> getNodes() { return nodes; }
    public void addNode(Node n)  { nodes.add(n); }

    public void executeStep() {
        Set<Vehicle> vehicles = new LinkedHashSet<>();
        Set<SnowPlow> plows   = new LinkedHashSet<>();
        Set<Road> visited = new HashSet<>();
        for (Node node : nodes) {
            for (Road road : node.getConnectedRoads()) {
                if (visited.contains(road)) continue;
                visited.add(road);
                for (Lane lane : road.getLanes()) {
                    vehicles.addAll(lane.getVehicles());
                    if (lane.getSnowPlow() != null) plows.add(lane.getSnowPlow());
                }
            }
        }
        for (Vehicle v : vehicles) v.step(false);
        for (SnowPlow p : plows)   p.step();
    }

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
