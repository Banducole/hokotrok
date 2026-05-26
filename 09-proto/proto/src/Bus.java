/**
 * Busz jarmű, amely ket vegallomas kozott kozlekedik.
 * <p>
 * A busz mozgasat a {@code Busz_Lep} parancs iranyitja kozvetlenul
 * (nem autonóm). Minden vegallomasi erkezesnel meghivodik az
 * {@link #arrivedAtTerminal()} metodus, amely noveli a forduloszamlalot
 * es megforditja az iranyt (start ↔ celvegallomas csere).
 * </p>
 */
public class Bus extends Vehicle {

    /** Az egyik vegallomas, ahonnan a jarat indul. */
    private Terminal terminalStart;

    /** A masik vegallomas, ahova a jarat tart. */
    private Terminal terminalEnd;

    /** Az eddig teljesitett teljes fordulok szama. */
    private int completedRounds = 0;

    /**
     * Beallitja az induló vegallomast.
     *
     * @param t a kezdo vegallomas
     */
    public void setTerminalStart(Terminal t) { this.terminalStart = t; }

    /**
     * Beallitja a cel vegallomast.
     *
     * @param t a cel vegallomas
     */
    public void setTerminalEnd(Terminal t)   { this.terminalEnd = t; }

    /** @return az indulo vegallomas */
    public Terminal getTerminalStart() { return terminalStart; }

    /** @return a cel vegallomas */
    public Terminal getTerminalEnd()   { return terminalEnd; }

    /**
     * A busz nem mozdít autonoman – a {@code Busz_Lep} parancs vezérli.
     *
     * @param random nincs hatassal (a busz nem hasznáalja)
     */
    @Override
    public void step(boolean random) {
        // blokkolt allapot csokkentest City.executeStep() vegzi
    }

    /**
     * Vegallomasi erkezest kezeli: noveli a forduloszamlalot
     * es megcsereli a start es cel vegallomast.
     */
    public void arrivedAtTerminal() {
        completedRounds++;
        Terminal tmp = terminalStart;
        terminalStart = terminalEnd;
        terminalEnd = tmp;
        Logger.action(this, "Megerkezett vegallomashoz, fordulok: " + completedRounds);
    }

    /**
     * Visszaadja az eddig teljesitett fordulok szamat.
     *
     * @return teljesitett fordulok szama
     */
    public int getCompletedRounds() { return completedRounds; }

    /**
     * Megadja, hogy a busz elerte-e a kívánt forduloszamot.
     *
     * @param expected az elvart fordulok szama
     * @return {@code true}, ha a busz legalább ennyit teljesített
     */
    public boolean isRouteComplete(int expected) { return completedRounds >= expected; }
}
