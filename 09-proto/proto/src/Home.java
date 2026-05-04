/**
 * Lakóhelyet (otthont) reprezentáló csomópont.
 * <p>
 * A {@link Car} járművek innen indulnak és ide térnek vissza.
 * A {@link Car#setHome(Home)} hívásával rendelik hozzá az autóhoz.
 * </p>
 */
public class Home extends Node {

    /**
     * Hozzáad egy utat ehhez a lakóhelyhez.
     *
     * @param road a csatlakoztatandó út
     */
    @Override
    public void addRoad(Road road) { super.addRoad(road); }
}
