/**
 * Vastag hótakaróval fedett, átjárhatatlan sávállapot.
 * <p>
 * A sáv nem járható; járművek nem léphetnek rá. Söprőfejjel vagy
 * hányófejjel a hó áttoható szomszéd sávba. Elegendő só csökkenti
 * a vastagságot; ha a vastagság a küszöb alá süllyed, {@link ThinSnowState}-re vált.
 * Zúzalékszórás hatástalan, mert a hó maga betemeti a kavicsot.
 * </p>
 */
public class ThickSnowState implements LaneState {

    /** A sávon lévő hó jelenlegi vastagsága egységekben. */
    private int snowThickness;

    /**
     * Létrehozza az állapotot a megadott kezdeti hóvastagsággal.
     *
     * @param initialThickness a kezdeti hóvastagság egységekben
     */
    public ThickSnowState(int initialThickness) { this.snowThickness = initialThickness; }

    /**
     * Hóesés hatása: növeli a hóvastagságot.
     *
     * @param lane   az érintett sáv
     * @param amount az érkező hó mennyisége egységekben
     */
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        snowThickness += amount;
    }

    /**
     * Só hatása: csökkenti a hóvastagságot; ha a küszöb alá süllyed,
     * ThinSnowState-re vált.
     *
     * @param lane az érintett sáv
     */
    @Override
    public void onSaltApplied(Lane lane) {
        snowThickness -= Constants.SNOW_MELT_AMOUNT;
        if (snowThickness < Constants.THICK_SNOW_THRESHOLD) {
            lane.setState(new ThinSnowState(Math.max(snowThickness, 1)));
            Logger.action(lane, "so hatasara ThinSnowState-re valtozott");
        }
    }

    /**
     * Jégképződés hatástalan vastag hón – a hó megakadályozza a jégcsíkok
     * kialakulását.
     *
     * @param lane az érintett sáv
     */
    @Override public void onIceFormed(Lane lane)  { /* vastag hó alatt nem tud jég kialakulni */ }

    /** @return {@code false} – vastag hóban a sáv átjárhatatlan */
    @Override public boolean isPassable()   { return false; }

    /** @return {@code false} – vastag hó nem csúszós */
    @Override public boolean isSlippery()   { return false; }

    /** @return {@code true} – a vastag hó áttoható szomszéd sávba */
    @Override public boolean canBePushed()  { return true;  }

    /**
     * Visszaadja az aktuális hóvastagságot.
     *
     * @return hóvastagság egységekben
     */
    @Override public int getSnowThickness() { return snowThickness; }
}
