public class ClearState implements LaneState {
    @Override
    public void onSnowAdded(Lane lane, int amount) {
        lane.setState(new ThinSnowState(amount));
    }
    @Override public void onSaltApplied(Lane lane) {}
    @Override public void onIceFormed(Lane lane)  {}
    @Override public boolean isPassable()   { return true;  }
    @Override public boolean isSlippery()   { return false; }
    @Override public boolean canBePushed()  { return false; }
    @Override public int getSnowThickness() { return 0; }
}
