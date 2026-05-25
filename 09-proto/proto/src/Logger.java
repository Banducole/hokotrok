import java.util.HashMap;
import java.util.Map;

/**
 * Kozponti naplozasi segedosztaly strukturalt kimenethez.
 * <p>
 * Harom uzenetfajtat tamogat:
 * <ul>
 *   <li>{@code [ACTION]} – sikeres muveletek es allapotvaltozasok</li>
 *   <li>{@code [STAT]}   – objektum allapotanak pillanatkepszeru kiirasa</li>
 *   <li>{@code [ERROR]}  – hibas vagy tilott muveletek jelzese</li>
 * </ul>
 * Az osztaly egy statikus nevterkepet tart fenn, amelyen keresztul
 * az objektumokhoz tartozo szimbolikus nevek kerdezhetok le.
 * Peldanyositas nem szuksges, minden metodus statikus.
 * </p>
 */
public class Logger {

    /** Objektum → szimbolikus nev lekepezestabla (tesztszkriptbeli azonositok). */
    private static final Map<Object, String> NAMES = new HashMap<>();

    /** Segedosztaly, nem peldanyosithato. */
    private Logger() {}

    /**
     * Regisztralja az objektumot a megadott nev alatt.
     * Ezt a nevet fogja hasznalni az osszes tovabbi log-uzenet.
     *
     * @param obj  a regisztralando objektum
     * @param name a hozzarendelni kivant szimbolikus nev
     */
    public static void register(Object obj, String name) {
        NAMES.put(obj, name);
    }

    /**
     * Visszaadja az objektum szimbolikus nevet, vagy ha nincs regisztralva,
     * az egyszerusitett osztalynevet.
     *
     * @param obj a keresett objektum ({@code null} eseten "null" stringet ad vissza)
     * @return a szimbolikus nev vagy az osztaly neve
     */
    public static String name(Object obj) {
        if (obj == null) return "null";
        String n = NAMES.get(obj);
        return n != null ? n : obj.getClass().getSimpleName();
    }

    /**
     * Kiir egy {@code [ACTION]} naplouzenet az objektumhoz.
     *
     * @param obj     a cselekvest vegzo objektum
     * @param message a naplo szovege
     */
    public static void action(Object obj, String message) {
        System.out.println("[ACTION] " + name(obj) + ": " + message);
    }

    /**
     * Kiir egy {@code [ERROR]} naplouzenet objektumhivatkozas nelkul.
     *
     * @param message a hibauzenet szovege
     */
    public static void error(String message) {
        System.out.println("[ERROR] " + message);
    }

    /**
     * Kiir egy {@code [ERROR]} naplouzenet a megadott objektumhoz rendelve.
     *
     * @param obj     az objektum, amelynel a hiba keletkezett
     * @param message a hibauzenet szovege
     */
    public static void error(Object obj, String message) {
        System.out.println("[ERROR] " + name(obj) + ": " + message);
    }

    /**
     * Kiir egy {@code [STAT]} naplouzenet az objektum allapotat leiro mezokkel.
     *
     * @param type   az objektum tipusanak neve (pl. "Car", "Lane", "SnowPlow")
     * @param obj    a statisztikaban szereplo objektum
     * @param fields a mezo=ertek parok szokozzel elvalasztva (pl. "Position=s1 Blocked=false")
     */
    public static void stat(String type, Object obj, String fields) {
        System.out.println("[STAT] " + type + " " + name(obj) + " " + fields);
    }
}
