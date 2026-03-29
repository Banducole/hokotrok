package skeleton;

public class BrokenIceState implements LaneState {
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        SkeletonHelper.enterMethod("BrokenIceState.OnSnowAdded(lane, amount)");
        SkeletonHelper.exitMethod("BrokenIceState.OnSnowAdded(lane, amount)");
    }

    @Override
    public void onIceFormed(Lane lane) {
        SkeletonHelper.enterMethod("BrokenIceState.OnIceFormed(lane)");
        SkeletonHelper.exitMethod("BrokenIceState.OnIceFormed(lane)");
    }
}
