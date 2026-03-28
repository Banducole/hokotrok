package skeleton;

/**
 * A sávok állapotát (tiszta, vékony hó, vastag hó, jég) leíró interfész.
 * Lehetővé teszi az Állapot (State) tervezési minta alkalmazását.
 */
public interface LaneState {
    /**
     * Hó hozzáadásakor lefutó logika.
     * @param lane A sáv, amire a hó esik.
     * @param amount A hó mennyisége.
     */
    void onSnowAdded(Lane lane, int amount);

    /**
     * Jégképződéskor lefutó logika.
     * @param lane A sáv, ahol a jég képződik.
     */
    void onIceFormed(Lane lane);
}