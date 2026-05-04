/**
 * Kereszteződést reprezentáló csomópont.
 * <p>
 * Az {@link Intersection} a hálózat általános elágazási pontja,
 * ahol több út találkozhat. A {@link PathFinder} BFS-keresése
 * ilyen csomópontokon keresztül köti össze a forrást és a célt.
 * </p>
 */
public class Intersection extends Node {

    /**
     * Hozzáad egy utat ehhez a kereszteződéshez.
     *
     * @param road a csatlakoztatandó út
     */
    @Override
    public void addRoad(Road road) { super.addRoad(road); }
}
