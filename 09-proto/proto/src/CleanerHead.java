/**
 * Hokotro-fejek absztrakt ososztálya.
 * <p>
 * Minden fej egy adott takaritasi strategiat valosit meg (Sweep, Throw,
 * IceBreaker, Salt, Dragon, Rock). A fejek egy reszenek uzemanyag kell
 * a mukodéshez (pl. {@link SaltHead}, {@link DragonHead}, {@link RockHead});
 * masok (pl. {@link SweepHead}, {@link ThrowHead}, {@link IceBreakerHead})
 * uzemanyag nelkul is mukodnek.
 * </p>
 */
public abstract class CleanerHead {

    /** A fej vételara (CleanerPlayer egyenlegéből vonodik le). */
    protected int price;

    /**
     * Takaritasi logika vegrehajtasa az adott savon.
     * Az altipus donti el, hogyan kezeli az allapotot.
     *
     * @param lane a takaritando sav
     */
    public abstract void clean(Lane lane);

    /**
     * Megadja, hogy a fej uzemkész-e (van-e elegendo uzemanyag).
     *
     * @return {@code true}, ha a fej kepez takaritast vegezni
     */
    public abstract boolean isOperational();

    /**
     * Visszaadja a fej vetelarat.
     *
     * @return ara penznembens
     */
    public int getPrice() { return price; }

    /**
     * Feltolti az uzemanyagot – altipusok felulirjak, ha van uzemanyaguk.
     * Alaptertelmezes: semmi sem tortenik (uzemanyag nelkuli fejeknel).
     *
     * @param amount a hozzaadando uzemanyag mennyisege egysegekben
     */
    public void refuel(int amount) { /* uzemanyag nelkuli fej, feltoltes hatastalan */ }

    /**
     * Az uzemanyag tipusanak neve (pl. "Salt", "Kerosene", "Rock"),
     * vagy ures string, ha a fej nem használ uzemanyagot.
     *
     * @return az uzemanyag tipusanak neve
     */
    public String fuelKind() { return ""; }

    /**
     * Az aktualis uzemanyagszint lekerdezese.
     * Alaptertelmezes: 0 (uzemanyag nelkuli fejeknel).
     *
     * @return az uzemanyag mennyisege egysegekben
     */
    public int fuelLevel() { return 0; }
}
