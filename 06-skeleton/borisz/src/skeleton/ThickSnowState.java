package skeleton;

public class ThickSnowState implements LaneState {
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        SkeletonHelper.enterMethod("ThickSnowState.OnSnowAdded(lane, amount)");
        SkeletonHelper.exitMethod("ThickSnowState.OnSnowAdded(lane, amount)");
    }

    @Override
    public void onIceFormed(Lane lane) {
        SkeletonHelper.enterMethod("ThickSnowState.OnIceFormed(lane)");
        SkeletonHelper.exitMethod("ThickSnowState.OnIceFormed(lane)");
    }
}
