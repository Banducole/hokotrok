import java.util.*;

public class PathFinder {
    private final City city;
    public PathFinder(City city) { this.city = city; }

    public List<Lane> getShortestPath(Node from, Node to) {
        if (from == null || to == null) return null;
        if (from == to) return new ArrayList<>();

        Queue<Node> queue = new LinkedList<>();
        Map<Node, Node> prev = new HashMap<>();
        Map<Node, Road> roadUsed = new HashMap<>();
        queue.add(from);
        prev.put(from, null);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current == to) break;
            for (Road road : current.getConnectedRoads()) {
                Node neighbor = getOtherEnd(road, current);
                if (neighbor == null) continue;
                if (!prev.containsKey(neighbor)) {
                    prev.put(neighbor, current);
                    roadUsed.put(neighbor, road);
                    queue.add(neighbor);
                }
            }
        }

        if (!prev.containsKey(to)) return null;

        List<Road> roads = new ArrayList<>();
        Node cursor = to;
        while (prev.get(cursor) != null) {
            roads.add(0, roadUsed.get(cursor));
            cursor = prev.get(cursor);
        }

        List<Lane> result = new ArrayList<>();
        for (Road r : roads) {
            List<Lane> passable = getPassableLanes(r);
            if (!passable.isEmpty()) result.add(passable.get(0));
            else if (!r.getLanes().isEmpty()) result.add(r.getLanes().get(0));
        }
        return result;
    }

    public Node getOtherEnd(Road road, Node node) {
        if (road.getFrom() == node) return road.getTo();
        if (road.getTo()   == node) return road.getFrom();
        return null;
    }

    public List<Lane> getPassableLanes(Road road) {
        List<Lane> out = new ArrayList<>();
        for (Lane l : road.getLanes()) if (l.isPassable()) out.add(l);
        return out;
    }

    public Lane getNeighborLane(Road road, Lane lane, String direction) {
        List<Lane> lanes = road.getLanes();
        int idx = lanes.indexOf(lane);
        if (idx < 0) return null;
        if ("RIGHT".equals(direction) && idx < lanes.size() - 1) return lanes.get(idx + 1);
        if ("LEFT".equals(direction)  && idx > 0)                return lanes.get(idx - 1);
        return null;
    }
}
