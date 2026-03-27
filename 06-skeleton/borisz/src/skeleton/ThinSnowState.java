package skeleton;

/**
 * Vékony hó állapot. A város autói egy vékonyabb hórétegben még tudnak közlekedni.
 * Ha elég sok autó egymás után letapossa a havat, az adott útszakasz jégpáncéllá válik[cite: 163].
 */
public class ThinSnowState implements LaneState {

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

    @Override
    public void onIceFormed(Lane lane) {
        SkeletonHelper.enterMethod("ThinSnowState.OnIceFormed(lane)");
        // A vékony hó letaposása IcyState-be (jégpáncél) viszi a sávot.
        SkeletonHelper.enterMethod("Lane.SetState(IcyState)");
        SkeletonHelper.exitMethod("Lane.SetState(IcyState)");
        SkeletonHelper.exitMethod("ThinSnowState.OnIceFormed(lane)");
    }
}
