public class ThickSnowState implements LaneState {
    private int snowThickness;
    public ThickSnowState(int initialThickness) { this.snowThickness = initialThickness; }

    @Override
    public void onSnowAdded(Lane lane, int amount) {
        snowThickness += amount;
    }
    @Override
    public void onSaltApplied(Lane lane) {
        snowThickness -= Constants.SNOW_MELT_AMOUNT;
        if (snowThickness < Constants.THICK_SNOW_THRESHOLD) {
            lane.setState(new ThinSnowState(Math.max(snowThickness, 1)));
            Logger.action(lane, "so hatasara ThinSnowState-re valtozott");
        }
    }
    @Override public void onIceFormed(Lane lane)  {}
    @Override public boolean isPassable()   { return false; }
    @Override public boolean isSlippery()   { return false; }
    @Override public boolean canBePushed()  { return true;  }
    @Override public int getSnowThickness() { return snowThickness; }
}
