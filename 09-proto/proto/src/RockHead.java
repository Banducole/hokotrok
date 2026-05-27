/**
 * Zúzalékfej – csúszásmentesítő kavicsot szór a sávra.
 * <p>
 * Hasznáaltához {@link Rock} üzemanyag szükséges; minden takarítási lépésnél
 * egy egységet fogyaszt. A zúzalék {@code rocky=true} állapotot állít be
 * a sávon, ami felülírja a csúszósságot ({@link Lane#isSlippery()}).
 * Maximális kapacitása {@link Constants#ROCK_HEAD_CAPACITY} egység.
 * </p>
 */
public class RockHead extends CleanerHead {

    /** Az aktuálisan rendelkezésre álló zúzalék mennyisége egységekben. */
    private int rockAmount = 0;

    /** A fej maximális zúzaléktartaléka egységekben. */
    private static final int ROCK_LIMIT = Constants.ROCK_HEAD_CAPACITY;

    /**
     * Létrehozza a zúzalékfejet és beállítja az árát.
     */
    public RockHead() { this.price = Constants.PRICE_ROCK_HEAD; }

    /**
     * Takarítás: zúzalékot szór a sávra, egy egységet felhasználva.
     * Ha nincs elegendő zúzalék, hibát jelez.
     *
     * @param lane a kezelendő sáv
     */
    @Override
    public void clean(Lane lane) {
        SnowPlow plow = lane.getSnowPlow();
        if (isOperational()) {
            lane.applyRock();
            rockAmount--;
            Logger.action(plow, "Zuzalek szorasa sikeres, maradek Rock: " + rockAmount);
        } else {
            Logger.error(plow, "Nincs elegendo Rock a takaritashoz");
        }
    }

    /**
     * Megadja, hogy van-e elegendő zúzalék a szóráshoz.
     *
     * @return {@code true}, ha legalább 1 egység zúzalék rendelkezésre áll
     */
    @Override public boolean isOperational() { return rockAmount > 0; }

    /**
     * Feltölti a zúzalékkészletet, maximum a kapacitásig.
     *
     * @param amount a hozzáadandó zúzalék mennyisége egységekben
     */
    @Override
    public void refuel(int amount) {
        rockAmount = Math.min(rockAmount + amount, ROCK_LIMIT);
    }

    /** @return {@code "Rock"} */
    @Override public String fuelKind()  { return "Rock"; }

    /** @return az aktuális zúzalékkészlet egységekben */
    @Override public int    fuelLevel() { return rockAmount; }

    @Override public int    fuelCapacity() { return ROCK_LIMIT; }
}
