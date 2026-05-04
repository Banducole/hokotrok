import java.util.*;

/**
 * BFS-alapú útvonalkereső, amely a legrövidebb utat találja meg
 * két csomópont között a városon belül.
 * <p>
 * A {@link Car} osztály használja az autonóm navigációhoz. A keresés
 * csomópontokon ({@link Node}) és utakon ({@link Road}) keresztül halad;
 * az eredményt az egyes utakon belül elérhető legjobb sávok listájaként
 * adja vissza.
 * </p>
 */
public class PathFinder {

    /**
     * Létrehozza az útvonalkeresőt.
     * A BFS a csomópontok szomszédsági listájából dolgozik,
     * ezért külön városreferencia nem szükséges.
     */
    public PathFinder() { /* a BFS csomopontok szomszedossagabol dolgozik, varosi referencia nem kell */ }

    /**
     * Megkeresi a legrövidebb utat a két csomópont között BFS-sel.
     * Minden úthoz az első átjárható sávot választja; ha nincs átjárható,
     * az első sávot veszi fallbackként.
     *
     * @param from a kiindulási csomópont
     * @param to   a célcsomópont
     * @return a sávok listája a bejárandó sorrendben,
     *         {@code null} ha nincs útvonal, üres lista ha {@code from == to}
     */
    public List<Lane> getShortestPath(Node from, Node to) {
        if (from == null || to == null) return null;
        if (from == to) return new ArrayList<>();

        Queue<Node> queue = new LinkedList<>();
        Map<Node, Node> prev = new HashMap<>();
        Map<Node, Road> roadUsed = new HashMap<>();
        queue.add(from);
        prev.put(from, null);

        /* BFS a legrövidebb út megkereséséhez */
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

        /* Visszakövetés: az útvonal utait összegyűjtjük */
        List<Road> roads = new ArrayList<>();
        Node cursor = to;
        while (prev.get(cursor) != null) {
            roads.add(0, roadUsed.get(cursor));
            cursor = prev.get(cursor);
        }

        /* Minden úthoz kiválasztjuk az első (lehetőleg átjárható) sávot */
        List<Lane> result = new ArrayList<>();
        for (Road r : roads) {
            List<Lane> passable = getPassableLanes(r);
            if (!passable.isEmpty()) result.add(passable.get(0));
            else if (!r.getLanes().isEmpty()) result.add(r.getLanes().get(0));
        }
        return result;
    }

    /**
     * Visszaadja az út másik végcsomópontját a megadott csomóponthoz képest.
     *
     * @param road az út
     * @param node az egyik végpont
     * @return a másik végpont, vagy {@code null} ha a csomópont nem tartozik az úthoz
     */
    public Node getOtherEnd(Road road, Node node) {
        if (road.getFrom() == node) return road.getTo();
        if (road.getTo()   == node) return road.getFrom();
        return null;
    }

    /**
     * Visszaadja az úton lévő összes átjárható sávot.
     *
     * @param road a vizsgált út
     * @return az átjárható sávok listája (lehet üres)
     */
    public List<Lane> getPassableLanes(Road road) {
        List<Lane> out = new ArrayList<>();
        for (Lane l : road.getLanes()) if (l.isPassable()) out.add(l);
        return out;
    }

    /**
     * Visszaadja az adott sáv bal vagy jobb szomszéd sávját.
     *
     * @param road      az út, amelyen a sávok találhatók
     * @param lane      a kiindulási sáv
     * @param direction {@code "LEFT"} vagy {@code "RIGHT"}
     * @return a szomszéd sáv, vagy {@code null} ha nincs ilyen irányban
     */
    public Lane getNeighborLane(Road road, Lane lane, String direction) {
        List<Lane> lanes = road.getLanes();
        int idx = lanes.indexOf(lane);
        if (idx < 0) return null;
        if ("RIGHT".equals(direction) && idx < lanes.size() - 1) return lanes.get(idx + 1);
        if ("LEFT".equals(direction)  && idx > 0)                return lanes.get(idx - 1);
        return null;
    }
}
