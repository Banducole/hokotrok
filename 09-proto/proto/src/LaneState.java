public interface LaneState {
    void onSnowAdded(Lane lane, int amount);
    void onSaltApplied(Lane lane);
    void onIceFormed(Lane lane);
    boolean isPassable();
    boolean isSlippery();
    boolean canBePushed();
    int getSnowThickness();
}
