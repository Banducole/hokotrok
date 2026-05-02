public class ThinSnowState implements LaneState {
    private int snowThickness;
    public ThinSnowState(int initialThickness) { this.snowThickness = initialThickness; }

    @Override
    public void onSnowAdded(Lane lane, int amount) {
        snowThickness += amount;
        if (snowThickness >= Constants.THICK_SNOW_THRESHOLD) {
            lane.setState(new ThickSnowState(snowThickness));
            Logger.action(lane, "allapota ThickSnowState-re valtozott");
        }
    }
    @Override
    public void onSaltApplied(Lane lane) {
        lane.setState(new ClearState());
        Logger.action(lane, "so hatasara ClearState-re valtozott");
    }
    @Override
    public void onIceFormed(Lane lane) {
        lane.setState(new IcyState());
        Logger.action(lane, "allapota IcyState-re valtozott");
    }
    @Override public boolean isPassable()   { return true;  }
    @Override public boolean isSlippery()   { return false; }
    @Override public boolean canBePushed()  { return true;  }
    @Override public int getSnowThickness() { return snowThickness; }
}
