package skeleton;

/**
 * A feltört jeget reprezentáló sávállapot.
 * Jégtörő fej használata után a jeges sáv ebbe az állapotba kerül.
 */
public class BrokenIceState implements LaneState {
    
    /**
     * Hó hozzáadásakor lefutó logika.
     * Feltört jégen a hó a normál sávokhoz hasonlóan felhalmozódik.
     * * @param lane A sáv, amire a hó esik.
     * @param amount A leeső hó mennyisége.
     */
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        SkeletonHelper.enterMethod("BrokenIceState.OnSnowAdded(lane, amount)");
        SkeletonHelper.exitMethod("BrokenIceState.OnSnowAdded(lane, amount)");
    }

    /**
     * Jégképződéskor lefutó logika.
     * * @param lane A sáv, ahol a jég képződik.
     */
    @Override
    public void onIceFormed(Lane lane) {
        SkeletonHelper.enterMethod("BrokenIceState.OnIceFormed(lane)");
        SkeletonHelper.exitMethod("BrokenIceState.OnIceFormed(lane)");
    }
}