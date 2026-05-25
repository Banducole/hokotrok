package skeleton;

/**
 * A vastag hó állapotát reprezentáló osztály.
 * Ebben az állapotban a sáv a normál járművek számára jellemzően akadályt jelent,
 * és csak hókotrók tudnak könnyedén áthaladni rajta.
 */
public class ThickSnowState implements LaneState {
    
    /**
     * Hó hozzáadásakor lefutó logika.
     * Mivel a sáv már vastag havas, a további hóesés nem változtatja meg az állapot típusát.
     * @param lane A sáv, amire a hó esik.
     * @param amount A leeső hó mennyisége.
     */
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        SkeletonHelper.enterMethod("ThickSnowState.OnSnowAdded(lane, amount)");
        SkeletonHelper.exitMethod("ThickSnowState.OnSnowAdded(lane, amount)");
    }

    /**
     * Jégképződéskor lefutó logika.
     * Vastag hó esetén a letaposás miatti jégképződés jellemzően nem történik meg.
     * @param lane A sáv, ahol a jég próbál képződni.
     */
    @Override
    public void onIceFormed(Lane lane) {
        SkeletonHelper.enterMethod("ThickSnowState.OnIceFormed(lane)");
        SkeletonHelper.exitMethod("ThickSnowState.OnIceFormed(lane)");
    }
}