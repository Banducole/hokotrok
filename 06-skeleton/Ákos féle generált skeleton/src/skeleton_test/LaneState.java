package skeleton_test;

public class LaneState {

    public LaneState() {
        Skeleton.logCall("LaneState::LaneState()");
    }

    public LaneState(String name) {
        Skeleton.logCall("LaneState::LaneState(" + name + ")");
    }

    public void onSnowAdded(Lane lane, int amount) {
        Skeleton.logCall("LaneState::onSnowAdded(lane, " + amount + ")");
        Skeleton.enter();

        lane.setState(new LaneState("ThinSnowState"));

        Skeleton.exit();
    }

    public void onIceFormed(Lane lane) {
        Skeleton.logCall("LaneState::onIceFormed(lane)");
        Skeleton.enter();

        lane.setState(new LaneState("IcyState"));

        Skeleton.exit();
    }

    public void onSaltApplied(Lane lane) {
        Skeleton.logCall("LaneState::onSaltApplied(lane)");
        Skeleton.enter();

        boolean melted = Skeleton.askBool("A jég teljesen felolvadt?");
        if (melted) {
            lane.setState(new LaneState("ClearState"));
        }

        Skeleton.exit();
    }

    public boolean isPassable() {
        Skeleton.logCall("LaneState::isPassable()");
        boolean result = Skeleton.askBool("Az állapot járható?");
        Skeleton.logReturn(String.valueOf(result));
        return result;
    }

    public boolean isSlippery() {
        Skeleton.logCall("LaneState::isSlippery()");
        boolean result = Skeleton.askBool("Az állapot csúszós?");
        Skeleton.logReturn(String.valueOf(result));
        return result;
    }

    public boolean canBePushed() {
        Skeleton.logCall("LaneState::canBePushed()");
        boolean result = Skeleton.askBool("A sáv tartalma tolható?");
        Skeleton.logReturn(String.valueOf(result));
        return result;
    }

    public String getName() {
        return Skeleton.askString("Mi az állapot neve? (pl. ClearState, ThinSnowState, ThickSnowState, IcyState, BrokenIceState)");
    }
}
