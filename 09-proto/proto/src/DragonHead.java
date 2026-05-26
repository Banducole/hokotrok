/**
 * Sarkányfej – kerozinnal eléget mindent a sávon, ClearState-re állítva.
 * <p>
 * A legerősebb takarítófej: bármilyen hó-, jég- vagy zúzalékállapotot
 * eltávolít egyetlen lépésben. Hasznáaltához {@link Kerosene} üzemanyag
 * szükséges; minden takarítási lépésnél egy egységet fogyaszt.
 * </p>
 */
public class DragonHead extends CleanerHead {

    /** Az aktuálisan rendelkezésre álló kerozin mennyisége egységekben. */
    private int keroseneAmount = 0;

    /**
     * Létrehozza a sárkányfejet és beállítja az árát.
     */
    public DragonHead() { this.price = Constants.PRICE_DRAGON_HEAD; }

    /**
     * Takarítás: kerozinnal eltávolít minden hót, jeget és zúzalékot,
     * ClearState-re állítva a sávot. Ha nincs kerozin, hibát jelez.
     *
     * @param lane a takarítandó sáv
     */
    @Override
    public void clean(Lane lane) {
        SnowPlow plow = lane.getSnowPlow();
        if (isOperational()) {
            lane.setState(new ClearState());
            keroseneAmount--;
            Logger.action(plow, "Sarkanyfej takaritott. Maradek Kerosene: " + keroseneAmount);
            Logger.action(lane, "allapota ClearState-re valtozott");
        } else {
            Logger.error(plow, "Nincs elegendo Kerosene a takaritashoz");
        }
    }

    /**
     * Megadja, hogy van-e elegendő kerozin a takarításhoz.
     *
     * @return {@code true}, ha legalább 1 egység kerozin rendelkezésre áll
     */
    @Override public boolean isOperational() { return keroseneAmount > 0; }

    /**
     * Feltölti a kerozinkészletet a megadott mennyiséggel.
     *
     * @param amount a hozzáadandó kerozin egységekben
     */
    @Override public void refuel(int amount) { keroseneAmount = Math.min(keroseneAmount + amount, Constants.DRAGON_HEAD_CAPACITY); }

    /** @return {@code "Kerosene"} */
    @Override public String fuelKind()  { return "Kerosene"; }

    /** @return az aktuális kerozinkészlet egységekben */
    @Override public int    fuelLevel() { return keroseneAmount; }

    @Override public int    fuelCapacity() { return Constants.DRAGON_HEAD_CAPACITY; }
}
