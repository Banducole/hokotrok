package skeleton;

/**
 * Vékony hó állapotot reprezentáló osztály. 
 * A város autói egy vékonyabb hórétegben még tudnak közlekedni.
 * Ha elég sok autó egymás után letapossa a havat, az adott útszakasz jégpáncéllá válik.
 */
public class ThinSnowState implements LaneState {

    /**
     * Hó hozzáadásakor lefutó logika.
     * Ha a hó mennyisége elér egy bizonyos határt, az állapot vastag havasra (ThickSnowState) vált.
     * @param lane A sáv, amire a hó esik.
     * @param amount A leeső hó mennyisége.
     */
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        SkeletonHelper.enterMethod("ThinSnowState.OnSnowAdded(lane, " + amount + ")");
        boolean reachesThick = SkeletonHelper.askQuestion("Elérte a hóvastagság a határt (Vastag hóhoz)?");
        if (reachesThick) {
            SkeletonHelper.enterMethod("Lane.SetState(ThickSnowState)");
            SkeletonHelper.exitMethod("Lane.SetState(ThickSnowState)");
        }
        SkeletonHelper.exitMethod("ThinSnowState.OnSnowAdded(lane, " + amount + ")");
    }

    /**
     * Jégképződéskor lefutó logika.
     * A vékony hó letaposása jeges állapotba (IcyState) viszi a sávot.
     * @param lane A sáv, ahol a jég képződik.
     */
    @Override
    public void onIceFormed(Lane lane) {
        SkeletonHelper.enterMethod("ThinSnowState.OnIceFormed(lane)");
        SkeletonHelper.enterMethod("Lane.SetState(IcyState)");
        SkeletonHelper.exitMethod("Lane.SetState(IcyState)");
        SkeletonHelper.exitMethod("ThinSnowState.OnIceFormed(lane)");
    }
}