/**
 * Tiszta (havas és jégmentes) sávállapot.
 * <p>
 * Ebben az állapotban a sáv teljes mértékben átjárható és nem csúszós.
 * Hóesés hatására {@link ThinSnowState}-re vált, só és jégképződés
 * hatástalan. Ez az alapértelmezett kezdőállapot minden sáv esetén.
 * </p>
 */
public class ClearState implements LaneState {

    /**
     * Hóesés hatása: ClearState-ből ThinSnowState keletkezik.
     *
     * @param lane   az érintett sáv
     * @param amount az érkező hó mennyisége egységekben
     */
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        lane.setState(new ThinSnowState(amount));
    }

    /**
     * Só hatástalan tiszta sávon – nem változtatja az állapotot.
     *
     * @param lane az érintett sáv
     */
    @Override public void onSaltApplied(Lane lane) {}

    /**
     * Jégképződés hatástalan tiszta sávon – nem változtatja az állapotot.
     *
     * @param lane az érintett sáv
     */
    @Override public void onIceFormed(Lane lane)  {}

    /** @return {@code true} – a tiszta sáv mindig átjárható */
    @Override public boolean isPassable()   { return true;  }

    /** @return {@code false} – a tiszta sáv nem csúszós */
    @Override public boolean isSlippery()   { return false; }

    /** @return {@code false} – nincs tolható tartalom */
    @Override public boolean canBePushed()  { return false; }

    /** @return {@code 0} – nincs hó a sávon */
    @Override public int getSnowThickness() { return 0; }
}
