public class IcyState implements LaneState {
    private int iceThickness;
    public IcyState() { this.iceThickness = 1; }

    @Override
    public void onSnowAdded(Lane lane, int amount) { iceThickness += amount; }

    @Override
    public void onSaltApplied(Lane lane) {
        lane.setState(new ClearState());
        Logger.action(lane, "so hatasara ClearState-re valtozott");
    }
    @Override public void onIceFormed(Lane lane)  {}
    @Override public boolean isPassable()   { return true;  }
    @Override public boolean isSlippery()   { return true;  }  // overridden by Lane.isSlippery via rocky
    @Override public boolean canBePushed()  { return false; }
    @Override public int getSnowThickness() { return iceThickness; }
}
