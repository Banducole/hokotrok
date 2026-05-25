import java.util.ArrayList;
import java.util.List;

/**
 * Egy utat reprezentalo osztaly, amely ket csomopont kozott halad
 * es tobb savbol ({@link Lane}) allhat.
 * <p>
 * Az ut iranyitott: {@code from} csomopontinol indul, {@code to} csomopontinal
 * vegzodik. A savok az uton belul parhuzamosan futnak; jarmu csak az egyik
 * sav mentén haladhat. Hoeses az ut osszes savjat erinti.
 * </p>
 */
public class Road {

    /** Az ut kezdocsomopointja. */
    private Node from;

    /** Az ut vegcsomopointja. */
    private Node to;

    /** Az uthoz tartozo savok rendezett listaja. */
    private final List<Lane> lanes = new ArrayList<>();

    /**
     * Visszaadja az uthoz tartozo osszes savot.
     *
     * @return a savok listaja (modosithato referencia)
     */
    public List<Lane> getLanes() { return lanes; }

    /**
     * Hozzaad egy savot az uthoz es beallitja a sav visszamutato referenciat.
     *
     * @param lane a hozzaadando sav
     */
    public void addLane(Lane lane) {
        lanes.add(lane);
        lane.setRoad(this);
    }

    /**
     * Hoeseskor meghivodo metodus – az ut osszes savjara alkalmazza a hót.
     *
     * @param amount az egysegnyi ho mennyisege
     */
    public void applySnow(int amount) {
        for (Lane l : lanes) l.addSnow(amount);
    }

    /**
     * Visszaadja az ut kezdocsomopointjat.
     *
     * @return a kezdocsomopont
     */
    public Node getFrom() { return from; }

    /**
     * Visszaadja az ut vegcsomopointjat.
     *
     * @return a vegcsomopont
     */
    public Node getTo()   { return to; }

    /**
     * Beallitja az ut kezdocsomopointjat.
     *
     * @param from a kezdocsomopont
     */
    public void setFrom(Node from) { this.from = from; }

    /**
     * Beallitja az ut vegcsomopointjat.
     *
     * @param to a vegcsomopont
     */
    public void setTo(Node to)     { this.to = to; }
}
