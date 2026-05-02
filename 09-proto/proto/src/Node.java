import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    protected final List<Road> connectedRoads = new ArrayList<>();

    public List<Road> getConnectedRoads() { return connectedRoads; }
    public void addRoad(Road road) { connectedRoads.add(road); }
}
