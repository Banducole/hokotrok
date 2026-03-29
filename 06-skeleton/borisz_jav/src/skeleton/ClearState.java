package skeleton;

/**
 * Tiszta sáv állapota. Ha ide hó esik, az állapota vékony havasra (ThinSnowState) vált[cite: 156].
 */
public class ClearState implements LaneState {
    
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        SkeletonHelper.enterMethod("ClearState.OnSnowAdded(lane, " + amount + ")");
        // A tiszta sávra eső hó hatására az állapot vékony havasra vált.
        lane.setState(new ThinSnowState());
        SkeletonHelper.exitMethod("ClearState.OnSnowAdded(lane, " + amount + ")");
    }

    @Override
    public void onIceFormed(Lane lane) {
        SkeletonHelper.enterMethod("ClearState.OnIceFormed(lane)");
        // Tiszta sávon nem tud jég képződni
        SkeletonHelper.exitMethod("ClearState.OnIceFormed(lane)");
    }
}
