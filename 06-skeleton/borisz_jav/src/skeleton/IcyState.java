package skeleton;

/**
 * A jeges sávállapotot reprezentáló osztály.
 * Ebben az állapotban a sáv rendkívül csúszós, ami növeli a balesetek (megcsúszás) veszélyét.
 */
public class IcyState implements LaneState {
    
    /**
     * Hó hozzáadásakor lefutó logika.
     * Jégre eső hó esetén a jég megmarad, a hó felhalmozódhat rajta.
     * @param lane A sáv, amire a hó esik.
     * @param amount A leeső hó mennyisége.
     */
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        SkeletonHelper.enterMethod("IcyState.OnSnowAdded(lane, amount)");
        SkeletonHelper.exitMethod("IcyState.OnSnowAdded(lane, amount)");
    }

    /**
     * Jégképződéskor lefutó logika.
     * Mivel a sáv már jeges, ez a metódus jellemzően nem változtat az állapoton.
     * @param lane A sáv, ahol a jég képződik.
     */
    @Override
    public void onIceFormed(Lane lane) {
        SkeletonHelper.enterMethod("IcyState.OnIceFormed(lane)");
        SkeletonHelper.exitMethod("IcyState.OnIceFormed(lane)");
    }
}