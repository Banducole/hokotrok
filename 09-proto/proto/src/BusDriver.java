/**
 * Buszsofor jatekos, aki egy adott buszt irányit.
 * <p>
 * Prototipus implementacio: a busz mozgasat a {@code Busz_Lep} tesztszkript-
 * parancs vegzi kozvetlenul, ezert a {@link #takeTurn()} metodus ures.
 * A {@link BusDriver} letrehozasa es a jatekhoz adasa elegendo ahhoz,
 * hogy a keretrendszer szamonkepjen a buszon.
 * </p>
 */
public class BusDriver extends Player {

    /** Az iranyitott busz. */
    private Bus bus;

    /**
     * Letrehozza a buszsofőrt a megadott buszhoz rendelve.
     *
     * @param bus az irányitando busz
     */
    public BusDriver(Bus bus) { this.bus = bus; }

    /**
     * Visszaadja az irányitott buszt.
     *
     * @return a hozzarendelt busz
     */
    public Bus getBus() { return bus; }

    /**
     * Korlépés – a busz mozgasat kozvetlenül a parancsertelmező vegzi,
     * ezert ez a metodus ures marad.
     */
    @Override public void takeTurn() { /* a Busz_Lep parancs vezeti kozvetlenul */ }
}
