import java.util.ArrayList;
import java.util.List;

/**
 * A közlekedési hálózat csomópontjainak absztrakt ősosztálya.
 * <p>
 * Konkrét altípusok: {@link Intersection}, {@link Terminal},
 * {@link Home}, {@link Workplace}. Minden csomópont összeköttetésben
 * állhat több úttal ({@link Road}), amelyeken a járművek haladnak.
 * </p>
 */
public abstract class Node {

    /** Az ehhez a csomóponthoz csatlakozó utak listája. */
    protected final List<Road> connectedRoads = new ArrayList<>();

    /**
     * Visszaadja a csomóponthoz csatlakozó összes utat.
     *
     * @return a csatlakozó utak listája (módosítható referencia)
     */
    public List<Road> getConnectedRoads() { return connectedRoads; }

    /**
     * Hozzáad egy utat a csomóponthoz.
     *
     * @param road a hozzáadandó út
     */
    public void addRoad(Road road) { connectedRoads.add(road); }
}
