import java.util.ArrayList;
import java.util.List;

/**
 * Egy forgalmi savot representalo osztaly.
 * <p>
 * A sav az ut egyik parhuzamos csikja; tobb jarmu is tartózkodhat rajta.
 * Allapota a {@link LaneState} State-minta szerint valtozhat (ClearState,
 * ThinSnowState, ThickSnowState, IcyState, BrokenIceState). A sav nyilvantartja
 * az athalado jarmuvet, az esetleg rajta allomaso hokotrot, a sokornyeket,
 * a zuzalekboritas tenyet es az athaladas-szamlalot (jegkepzodes celjabol).
 * </p>
 */
public class Lane {

    /** A sav aktualis allapota (State minta). */
    private LaneState state = new ClearState();

    /** A saven tartózkodo jarmuek listaja. */
    private final List<Vehicle> vehicles = new ArrayList<>();

    /** Az ezen a savon allomaso hokotro, vagy {@code null} ha nincs. */
    private SnowPlow snowPlow = null;

    /** Hany korig hat meg a sos vedelem (0 = mar nem hat). */
    private int saltCountdown = 0;

    /** Az athalasok szama az utolso jegkepzodes ota. */
    private int passageCount = 0;

    /** Igaz, ha a sav zuzalekkal van borítva (csuszasmentesito). */
    private boolean rocky = false;

    /** A savhoz tartozo ut visszamutato referencija. */
    private Road road;

    /**
     * Jarmu belep a savra – csak akkor kerul fel, ha a sav atjarhato.
     * Minden sikeres belépesnél regisztralja az athalást (jégképzéshez).
     *
     * @param v a belépő jármű
     */
    public void accept(Vehicle v) {
        if (state.isPassable()) {
            vehicles.add(v);
            registerPassage(v);
        }
    }

    /**
     * Hókotró belep a savra – ha már van másik hókotró a sávon, hibát jelez.
     *
     * @param s a belépő hókotró
     */
    public void accept(SnowPlow s) {
        if (snowPlow != null && snowPlow != s) {
            Logger.error(this, "A savon mar van hokotro");
        } else {
            snowPlow = s;
        }
    }

    /**
     * Hóesés hatása a sávra. Ha aktív a sós védelem, az felolvasztja a hót;
     * egyébként az aktuális állapot kezeli. Vastag hó eltünteti a zúzalékot.
     *
     * @param amount az érkező hó mennyisége egységekben
     */
    public void addSnow(int amount) {
        if (saltCountdown > 0) {
            saltCountdown--;
            Logger.action(this, amount + " egyseg ho esett, de a so azonnal felolvasztotta");
            return;
        }
        state.onSnowAdded(this, amount);
        Logger.action(this, amount + " egyseg ho esett, Allapot: " + state.getClass().getSimpleName());
        if (rocky && state instanceof ThickSnowState) {
            rocky = false;
            Logger.action(this, "a ho betemette a zuzalekot, rocky=false");
        }
    }

    /**
     * Zúzalékot szór a sávra – csúszásmentesítő hatású.
     * Hatástalan, ha a sáv már el van temetve vastag hóval.
     */
    public void applyRock() {
        rocky = true;
        Logger.action(this, "zuzalek szorva, rocky=true");
    }

    /**
     * Sót alkalmaz a sávra: beállítja a sóvisszaszámlálót és értesíti
     * az állapotot (azonnali olvadáshoz).
     */
    public void applySalt() {
        saltCountdown = Constants.SALT_COUNTDOWN_INIT;
        state.onSaltApplied(this);
    }

    /**
     * Jégképződési eseményt vált ki az állapoton (elegendő áthaladás után).
     */
    public void formIce() {
        state.onIceFormed(this);
    }

    /**
     * Megadja, hogy a sáv jelenleg átjárható-e.
     * Blokkolt jármű jelenléte is akadályozhatja az átjárhatóságot.
     *
     * @return {@code true}, ha sem az állapot, sem a rajta lévő járművek
     *         nem akadályozzák az átjárást
     */
    public boolean isPassable() {
        if (!state.isPassable()) return false;
        for (Vehicle v : vehicles) {
            if (v.isBlocked()) return false;
        }
        return true;
    }

    /**
     * Megadja, hogy a sáv csúszós-e. Zúzalék jelenléte felülírja
     * az állapot csúszósságát (kavics megtapad a jégen).
     *
     * @return {@code true}, ha a sáv csúszós és nincs zúzalék
     */
    public boolean isSlippery() {
        if (rocky) return false;
        return state.isSlippery();
    }

    /**
     * Regisztrálja az áthaladást; ha eléri a küszöbét, jégképződést indít.
     *
     * @param v az áthaladó jármű (jelenleg nem használt paraméter)
     */
    public void registerPassage(Vehicle v) {
        passageCount++;
        if (passageCount >= Constants.PASSAGE_THRESHOLD) {
            formIce();
            passageCount = 0;
        }
    }

    /**
     * A hókotró fejével megtakarítja a sávot, ha van felszerelt fej.
     *
     * @param plow a takarítást végző hókotró
     */
    public void cleanWith(SnowPlow plow) {
        if (plow.getHead() != null) plow.getHead().clean(this);
    }

    /** @param newState az új állapot */
    public void setState(LaneState newState) { this.state = newState; }

    /** @return az aktuális állapot */
    public LaneState getState() { return state; }

    /** @return az uthoz tartozó út */
    public Road getRoad() { return road; }

    /** @param road az új út referencia */
    public void setRoad(Road road) { this.road = road; }

    /** @return a sávon tartózkodó járművek listája */
    public List<Vehicle> getVehicles() { return vehicles; }

    /** @param v az eltávolítandó jármű */
    public void removeVehicle(Vehicle v) { vehicles.remove(v); }

    /** @return a sávon álló hókotró, vagy {@code null} */
    public SnowPlow getSnowPlow() { return snowPlow; }

    /** @param s az új hókotró referencia (ellenőrzés nélkül) */
    public void setSnowPlow(SnowPlow s) { this.snowPlow = s; }

    /** @return {@code true}, ha van zúzalék a sávon */
    public boolean isRocky() { return rocky; }

    /** @param r az új zúzalékállapot */
    public void setRocky(boolean r) { this.rocky = r; }

    /** @return hány körön át hat még a só */
    public int getSaltCountdown() { return saltCountdown; }

    /** @return az áthaladások száma az utolsó jégképzés óta */
    public int getPassageCount() { return passageCount; }
}
