/**
 * Vékony hótakaróval fedett sávállapot.
 * <p>
 * A sáv még átjárható, de elegendő hóesés után {@link ThickSnowState}-re,
 * elegendő áthaladás után pedig {@link IcyState}-re vált. Só hatására
 * azonnal {@link ClearState}-re tisztul.
 * </p>
 */
public class ThinSnowState implements LaneState {

    /** A sávon lévő hó jelenlegi vastagsága egységekben. */
    private int snowThickness;

    /**
     * Létrehozza az állapotot a megadott kezdeti hóvastagsággal.
     *
     * @param initialThickness a kezdeti hóvastagság egységekben
     */
    public ThinSnowState(int initialThickness) { this.snowThickness = initialThickness; }

    /**
     * Hóesés hatása: növeli a hóvastagságot; ha eléri a küszöböt,
     * ThickSnowState-re vált.
     *
     * @param lane   az érintett sáv
     * @param amount az érkező hó mennyisége egységekben
     */
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        snowThickness += amount;
        if (snowThickness >= Constants.THICK_SNOW_THRESHOLD) {
            lane.setState(new ThickSnowState(snowThickness));
            Logger.action(lane, "allapota ThickSnowState-re valtozott");
        }
    }

    /**
     * Só hatása: azonnal ClearState-re állítja a sávot.
     *
     * @param lane az érintett sáv
     */
    @Override
    public void onSaltApplied(Lane lane) {
        lane.setState(new ClearState());
        Logger.action(lane, "so hatasara ClearState-re valtozott");
    }

    /**
     * Jégképződés hatása: elegendő áthaladás után IcyState keletkezik.
     *
     * @param lane az érintett sáv
     */
    @Override
    public void onIceFormed(Lane lane) {
        lane.setState(new IcyState());
        Logger.action(lane, "allapota IcyState-re valtozott");
    }

    /** @return {@code true} – a vékony hó még átjárható */
    @Override public boolean isPassable()   { return true;  }

    /** @return {@code false} – vékony hó nem teszi csúszóssá a sávot */
    @Override public boolean isSlippery()   { return false; }

    /** @return {@code true} – a vékony hó tolható szomszéd sávba */
    @Override public boolean canBePushed()  { return true;  }

    /**
     * Visszaadja az aktuális hóvastagságot.
     *
     * @return hóvastagság egységekben
     */
    @Override public int getSnowThickness() { return snowThickness; }
}
