/**
 * Busz végállomást reprezentáló csomópont.
 * <p>
 * A {@link Bus} járatok két {@code Terminal} között közlekednek.
 * Amikor a busz megérkezik a célállomásra, a {@link #notifyArrival(Bus)}
 * metódus értesíti, és a busz megfordítja az irányvonalát.
 * </p>
 */
public class Terminal extends Node {

    /**
     * Értesíti a buszt, hogy megérkezett a végállomásra.
     * A busz ekkor növeli a fordulószámlálóját és megcseréli
     * a kezdő- és végállomását.
     *
     * @param bus az érkező busz
     */
    public void notifyArrival(Bus bus) { bus.arrivedAtTerminal(); }

    /**
     * Hozzáad egy utat ehhez a végállomáshoz.
     *
     * @param road a csatlakoztatandó út
     */
    @Override
    public void addRoad(Road road) { super.addRoad(road); }
}
