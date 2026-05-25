/**
 * Munkahelyet reprezentáló csomópont.
 * <p>
 * A {@link Car} járművek célja; az autók a {@link Home} és
 * a {@link Workplace} között ingáznak. A {@link Car#setWorkplace(Workplace)}
 * hívásával rendelik hozzá az autóhoz.
 * </p>
 */
public class Workplace extends Node {

    /**
     * Hozzáad egy utat ehhez a munkahelyhez.
     *
     * @param road a csatlakoztatandó út
     */
    @Override
    public void addRoad(Road road) { super.addRoad(road); }
}
