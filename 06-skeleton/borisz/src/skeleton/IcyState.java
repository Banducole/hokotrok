package skeleton;

public class IcyState implements LaneState {
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        SkeletonHelper.enterMethod("IcyState.OnSnowAdded(lane, amount)");
        SkeletonHelper.exitMethod("IcyState.OnSnowAdded(lane, amount)");
    }

    @Override
    public void onIceFormed(Lane lane) {
        SkeletonHelper.enterMethod("IcyState.OnIceFormed(lane)");
        SkeletonHelper.exitMethod("IcyState.OnIceFormed(lane)");
    }
}
