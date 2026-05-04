/**
 * Játékosok absztrakt ősosztálya.
 * <p>
 * Két konkrét típus létezik: {@link CleanerPlayer} (takarító játékos,
 * aki hókotrókat irányít) és {@link BusDriver} (buszsofor).
 * Minden játékos rendelkezik névvel és képes körlépést végrehajtani.
 * </p>
 */
public abstract class Player {

    /** A játékos azonosító neve (a Logger által is használt). */
    protected String name;

    /**
     * A játékos körlépése – altípusok implementálják a saját logikájukat.
     */
    public abstract void takeTurn();

    /**
     * Visszaadja a játékos nevét.
     *
     * @return játékos neve
     */
    public String getName() { return name; }

    /**
     * Beállítja a játékos nevét.
     *
     * @param n az új név
     */
    public void setName(String n) { this.name = n; }
}
