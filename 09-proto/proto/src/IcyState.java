/**
 * Jeges sávállapot – a sáv átjárható, de csúszós.
 * <p>
 * Elegendő áthaladás után ({@link ThinSnowState#onIceFormed}) keletkezik.
 * Só hatására azonnal {@link ClearState}-re olvad. A jég nem tolható
 * szomszéd sávba (szemben a {@link BrokenIceState}-szel). Zúzalék
 * szórásával a csúszósság megszüntethető ({@link Lane#isSlippery()} ellenőrzi).
 * </p>
 */
public class IcyState implements LaneState {

    /** A jég vastagsága egységekben (hóeséskor növekszik). */
    private int iceThickness;

    /**
     * Létrehozza a jeges állapotot 1 egységnyi kezdeti vastagságával.
     */
    public IcyState() { this.iceThickness = 1; }

    /**
     * Hóesés hatása: növeli a jégvastagságot (a hó ráfagy a jégre).
     *
     * @param lane   az érintett sáv
     * @param amount az érkező hó/jég mennyisége egységekben
     */
    @Override
    public void onSnowAdded(Lane lane, int amount) { iceThickness += amount; }

    /**
     * Só hatása: azonnal ClearState-re olvasztja a jégréteget.
     *
     * @param lane az érintett sáv
     */
    @Override
    public void onSaltApplied(Lane lane) {
        lane.setState(new ClearState());
        Logger.action(lane, "so hatasara ClearState-re valtozott");
    }

    /**
     * Jégképződés hatástalan – a sáv már jeges állapotban van.
     *
     * @param lane az érintett sáv
     */
    @Override public void onIceFormed(Lane lane)  { /* már jeges, nincs további hatás */ }

    /** @return {@code true} – jeges sáv átjárható */
    @Override public boolean isPassable()   { return true;  }

    /**
     * @return {@code true} – a jég csúszóssá teszi a sávot
     *         (a tényleges csúszósságot {@link Lane#isSlippery()} határozza meg,
     *          figyelembe véve a zúzalékot is)
     */
    @Override public boolean isSlippery()   { return true;  }

    /** @return {@code false} – a szilárd jég nem tolható szomszéd sávba */
    @Override public boolean canBePushed()  { return false; }

    /**
     * Visszaadja az aktuális jégvastagságot.
     *
     * @return jégvastagság egységekben
     */
    @Override public int getSnowThickness() { return iceThickness; }
}
