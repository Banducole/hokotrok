public class ThrowHead extends CleanerHead {
    public ThrowHead() { this.price = Constants.PRICE_THROW_HEAD; }

    @Override
    public void clean(Lane lane) {
        SnowPlow plow = lane.getSnowPlow();
        if (lane.getState().canBePushed()) {
            lane.setRocky(false);
            lane.setState(new ClearState());
            Logger.action(plow, "Hanyofej takaritott");
            Logger.action(lane, "allapota ClearState-re valtozott");
        } else {
            Logger.action(plow, "Hanyofej: nincs mit takaritani");
        }
    }

    @Override public boolean isOperational() { return true; }
}
