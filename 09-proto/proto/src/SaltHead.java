/**
 * Sofej – sot alkalmaz a savra, felolvasztva a hót es jeget.
 * <p>
 * Hasznalatahoz {@link Salt} uzemanyag szukseges. Minden takaritasi lepesnel
 * egy egység sot használ el, es {@link Lane#applySalt()} hívásával beallítja
 * a sóvédelmet a sávon. Ha nincs elegendő só, hibát jelez.
 * </p>
 */
public class SaltHead extends CleanerHead {

    /** Az aktuálisan rendelkezésre álló só mennyisége egységekben. */
    private int saltAmount = 0;

    /**
     * Létrehozza a sófejet és beállítja az árát.
     */
    public SaltHead() { this.price = Constants.PRICE_SALT_HEAD; }

    /**
     * Takarítás: sót alkalmaz a sávra, egy egységet felhasználva.
     * Ha nincs elegendő só, hibaüzenetet naplóz.
     *
     * @param lane a takarítandó sáv
     */
    @Override
    public void clean(Lane lane) {
        SnowPlow plow = lane.getSnowPlow();
        if (isOperational()) {
            lane.applySalt();
            saltAmount--;
            Logger.action(plow, "sozva, olvadasi folyamat elindult, maradek so: " + saltAmount);
        } else {
            Logger.error(plow, "Nincs elegendo Salt a takaritashoz");
        }
    }

    /**
     * Megadja, hogy van-e elegendő só a takarításhoz.
     *
     * @return {@code true}, ha legalább 1 egység só rendelkezésre áll
     */
    @Override public boolean isOperational() { return saltAmount > 0; }

    /**
     * Feltölti a sókészletet a megadott mennyiséggel.
     *
     * @param amount a hozzáadandó só mennyisége egységekben
     */
    @Override public void refuel(int amount) { saltAmount = Math.min(saltAmount + amount, Constants.SALT_HEAD_CAPACITY); }

    /** @return {@code "Salt"} */
    @Override public String fuelKind()  { return "Salt"; }

    /** @return az aktuális sókészlet egységekben */
    @Override public int    fuelLevel() { return saltAmount; }
}
