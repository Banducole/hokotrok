package skeleton;

/**
 * Tiszta sáv állapotát reprezentáló osztály. 
 * Ha ebbe a sávba hó esik, az állapota vékony havasra (ThinSnowState) vált.
 */
public class ClearState implements LaneState {
    
    /**
     * Hó hozzáadásakor lefutó logika.
     * A tiszta sávra eső hó hatására az állapot vékony havasra vált.
     * @param lane A sáv, amire a hó esik.
     * @param amount A leeső hó mennyisége.
     */
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        SkeletonHelper.enterMethod("ClearState.OnSnowAdded(lane, " + amount + ")");
        // A tiszta sávra eső hó hatására az állapot vékony havasra vált.
        lane.setState(new ThinSnowState());
        SkeletonHelper.exitMethod("ClearState.OnSnowAdded(lane, " + amount + ")");
    }

    /**
     * Jégképződéskor lefutó logika.
     * Tiszta sávon alapesetben nem tud jég képződni.
     * @param lane A sáv, ahol a jég próbál képződni.
     */
    @Override
    public void onIceFormed(Lane lane) {
        SkeletonHelper.enterMethod("ClearState.OnIceFormed(lane)");
        SkeletonHelper.exitMethod("ClearState.OnIceFormed(lane)");
    }
}