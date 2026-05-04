/**
 * Egy sáv aktuális állapotát leíró interfész (State minta).
 * <p>
 * Minden állapot (ClearState, ThinSnowState, ThickSnowState, IcyState,
 * BrokenIceState) implementálja ezt az interfészt, és maga dönti el,
 * hogyan reagál hóesésre, sóra, jégképződésre, illetve milyen
 * járhatósági és csúszóssági tulajdonságokkal rendelkezik.
 * </p>
 */
public interface LaneState {

    /**
     * Hóesés hatása az adott állapotra.
     *
     * @param lane   az érintett sáv, amelynek állapota esetleg megváltozhat
     * @param amount az érkező hó mennyisége egységekben
     */
    void onSnowAdded(Lane lane, int amount);

    /**
     * Só felhordásának hatása az adott állapotra.
     *
     * @param lane az érintett sáv, amelynek állapota esetleg megváltozhat
     */
    void onSaltApplied(Lane lane);

    /**
     * Jégképződési esemény hatása az adott állapotra
     * (elegendő áthaladás után hívódik meg).
     *
     * @param lane az érintett sáv
     */
    void onIceFormed(Lane lane);

    /**
     * Megadja, hogy az állapotban lévő sáv átjárható-e járművek számára.
     *
     * @return {@code true}, ha a sáv átjárható
     */
    boolean isPassable();

    /**
     * Megadja, hogy az állapotban lévő sáv csúszós-e.
     * A tényleges csúszósságot a {@link Lane#isSlippery()} határozza meg,
     * figyelembe véve a zúzalékborítást is.
     *
     * @return {@code true}, ha az állapot önmagában csúszóssá teszi a sávot
     */
    boolean isSlippery();

    /**
     * Megadja, hogy az állapotban lévő hó/jég tolható-e szomszéd sávba
     * (pl. söprőfejjel vagy hányófejjel).
     *
     * @return {@code true}, ha az állapot tartalma áttolható
     */
    boolean canBePushed();

    /**
     * Visszaadja a sávon lévő hó/jég vastagságát egységekben.
     *
     * @return hóvastagság (0, ha a sáv tiszta)
     */
    int getSnowThickness();
}
