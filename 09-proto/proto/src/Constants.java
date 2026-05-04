/**
 * Játék szintű konstansok gyűjteménye.
 * <p>
 * Tartalmazza az összes hóvastagsági küszöböt, árat, kifizetést
 * és egyéb numerikus paramétert, amelyek a szimuláció logikáját vezérlik.
 * A konstansok itt tartva egyszerűen módosítható a játék egyensúlya
 * anélkül, hogy több osztályban kellene változtatni.
 * </p>
 */
public class Constants {

    /** Hóvastagság határa, amely felett ThinSnowState helyett ThickSnowState keletkezik. */
    public static final int THICK_SNOW_THRESHOLD = 3;

    /** Ennyi áthaladás után fagy be egy sáv (IcyState). */
    public static final int PASSAGE_THRESHOLD    = 3;

    /** Ennyi kört véd a só a sávon (hány hóesést old fel automatikusan). */
    public static final int SALT_COUNTDOWN_INIT  = 3;

    /** Ennyi hóvastagságot olvaszt el egyszerre a só ThickSnowState-ben. */
    public static final int SNOW_MELT_AMOUNT     = 2;

    /** Új hókotró vételára (CleanerPlayer egyenlegéből levonva). */
    public static final int PRICE_SNOWPLOW        = 500;

    /** Söprőfej (SweepHead) vételára. */
    public static final int PRICE_SWEEP_HEAD      = 100;

    /** Hányófej (ThrowHead) vételára. */
    public static final int PRICE_THROW_HEAD      = 200;

    /** Jégtörőfej (IceBreakerHead) vételára. */
    public static final int PRICE_ICEBREAKER_HEAD = 150;

    /** Sófej (SaltHead) vételára. */
    public static final int PRICE_SALT_HEAD       = 150;

    /** Sárkányfej (DragonHead) vételára. */
    public static final int PRICE_DRAGON_HEAD     = 300;

    /** Zúzalékfej (RockHead) vételára. */
    public static final int PRICE_ROCK_HEAD       = 200;

    /** Egy egység só ára (SaltHead feltöltéséhez). */
    public static final int PRICE_SALT_UNIT       = 10;

    /** Egy egység kerozin ára (DragonHead feltöltéséhez). */
    public static final int PRICE_KEROSENE_UNIT   = 20;

    /** Egy egység zúzalék ára (RockHead feltöltéséhez). */
    public static final int PRICE_ROCK_UNIT       = 5;

    /** Ütközés után ennyi körig marad blokkolt egy jármű. */
    public static final int BLOCK_TURNS_ON_COLLISION = 2;

    /** Egy sáv sikeres takarításáért kapott kifizetés. */
    public static final int PAYMENT_PER_LANE_CLEANED = 50;

    /** RockHead maximális zúzaléktartaléka (egységben). */
    public static final int ROCK_HEAD_CAPACITY      = 10;

    /** Segédosztály, nem példányosítható. */
    private Constants() {}
}
