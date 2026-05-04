/**
 * Jégtörőfej – {@link IcyState}-ből {@link BrokenIceState}-re alakítja a sávot.
 * <p>
 * Csak jeges sávon hat; más állapotú sávon nincs hatása.
 * A feltört jég ({@link BrokenIceState}) már tolható szomszéd sávba,
 * ellentétben a szilárd jéggel. Nem igényel üzemanyagot, mindig üzemképes.
 * </p>
 */
public class IceBreakerHead extends CleanerHead {

    /**
     * Létrehozza a jégtörőfejet és beállítja az árát.
     */
    public IceBreakerHead() { this.price = Constants.PRICE_ICEBREAKER_HEAD; }

    /**
     * Takarítás: IcyState-ből BrokenIceState-re alakítja a sávot.
     * Nem jeges sávon nincs hatása.
     *
     * @param lane a kezelendő sáv
     */
    @Override
    public void clean(Lane lane) {
        SnowPlow plow = lane.getSnowPlow();
        if (lane.getState() instanceof IcyState) {
            lane.setState(new BrokenIceState());
            Logger.action(plow, "Jegtorofej takaritott");
            Logger.action(lane, "allapota BrokenIceState-re valtozott");
        } else {
            Logger.action(plow, "Jegtorofej: nem jeges sav, nincs hatas");
        }
    }

    /**
     * A jégtörőfej mindig üzemképes, nem igényel üzemanyagot.
     *
     * @return {@code true}
     */
    @Override public boolean isOperational() { return true; }
}
