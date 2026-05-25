/**
 * Hókotró jármű, amely cserélhető fejekkel takarítja a sávokat.
 * <p>
 * A hókotró egy sávon áll ({@code currentLane}), és minden lépésben
 * a felszerelt fejjel ({@link CleanerHead}) takarít. Ha a takarítás
 * állapotváltozást okoz, a tulajdonos {@link CleanerPlayer} kifizetést kap.
 * A hókotró célsávra léptethető a {@link #setTargetLane(Lane)} és
 * {@link #step()} kombinációval.
 * </p>
 */
public class SnowPlow {

    /** Az aktuális sáv, amelyen a hókotró áll. */
    private Lane currentLane;

    /** A következő körben elfoglalni kívánt sáv, vagy {@code null} ha nem mozog. */
    private Lane targetLane = null;

    /** A felszerelt takarítófej, vagy {@code null} ha nincs fej. */
    private CleanerHead head;

    /** A hókotró tulajdonosa (kifizetéseket ő kapja). */
    private CleanerPlayer owner;

    /** A hókotró vételára (állandó, a {@link Constants}-ból). */
    private static final int PRICE = Constants.PRICE_SNOWPLOW;

    /**
     * Létrehozza a hókotró tulajdonos nélkül (kézi inicializáláshoz).
     */
    public SnowPlow() {}

    /**
     * Létrehozza a hókotró a megadott tulajdonossal.
     *
     * @param owner a hókotró tulajdonosa
     */
    public SnowPlow(CleanerPlayer owner) { this.owner = owner; }

    /**
     * Egy körlépés végrehajtása: mozgás a célsávra (ha van), majd takarítás.
     * <p>
     * Ha a takarítás érdemi állapotváltozást okoz, a tulajdonos kifizetést kap.
     * Ha nincs elegendő üzemanyag, hibaüzenetet naplóz.
     * </p>
     */
    public void step() {
        if (targetLane != null) {
            if (currentLane != null) currentLane.setSnowPlow(null);
            currentLane = targetLane;
            currentLane.accept(this);
            targetLane = null;
        }
        if (head != null && head.isOperational()) {
            LaneState before = currentLane.getState();
            boolean wasNonClear = !(before instanceof ClearState) || currentLane.isRocky();
            head.clean(currentLane);
            LaneState after = currentLane.getState();
            handlePayment(before, after, wasNonClear);
        } else if (head != null) {
            Logger.error(this, "Nincs elegendo uzemanyag a takaritashoz");
        }
    }

    /**
     * Kifizetés kezelése a takarítás eredménye alapján.
     * Csak akkor fizet, ha érdemi állapotváltozás történt.
     *
     * @param before      az állapot takarítás előtt
     * @param after       az állapot takarítás után
     * @param wasNonClear igaz, ha a sáv nem volt tiszta takarítás előtt
     */
    private void handlePayment(LaneState before, LaneState after, boolean wasNonClear) {
        if (owner != null && wasNonClear && !sameKind(before, after)) {
            owner.receivePayment(Constants.PAYMENT_PER_LANE_CLEANED);
        } else if (owner != null && !wasNonClear) {
            Logger.action(this, "A sav mar tiszta, nincs takaritas, nincs fizetseg");
        } else if (owner != null && head instanceof RockHead && currentLane.isRocky()) {
            /* RockHead nem változtat sávállapotot, de a szórás díjazott */
            owner.receivePayment(Constants.PAYMENT_PER_LANE_CLEANED);
        } else if (owner != null && head instanceof SaltHead && !sameKind(before, after)) {
            owner.receivePayment(Constants.PAYMENT_PER_LANE_CLEANED);
        }
    }

    /**
     * Megadja, hogy két állapot azonos típusú-e.
     *
     * @param a az első állapot
     * @param b a második állapot
     * @return {@code true}, ha azonos osztályú a két állapot
     */
    private boolean sameKind(LaneState a, LaneState b) {
        return a.getClass() == b.getClass();
    }

    /**
     * Lecseréli a felszerelt fejet az újra.
     *
     * @param newHead az új fej
     */
    public void changeHead(CleanerHead newHead) { this.head = newHead; }

    /** @return az aktuális sáv */
    public Lane getCurrentLane() { return currentLane; }

    /** @param l az új aktuális sáv */
    public void setCurrentLane(Lane l) { this.currentLane = l; }

    /** @return a felszerelt fej, vagy {@code null} */
    public CleanerHead getHead() { return head; }

    /** @param lane a következő körben elfoglalni kívánt sáv */
    public void setTargetLane(Lane lane) { this.targetLane = lane; }

    /** @return a tulajdonos CleanerPlayer, vagy {@code null} */
    public CleanerPlayer getOwner() { return owner; }

    /** @param owner az új tulajdonos */
    public void setOwner(CleanerPlayer owner) { this.owner = owner; }

    /** @return a hókotró vételára */
    public int getPrice() { return PRICE; }
}
