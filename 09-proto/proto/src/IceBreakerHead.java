public class IceBreakerHead extends CleanerHead {
    public IceBreakerHead() { this.price = Constants.PRICE_ICEBREAKER_HEAD; }

    @Override
    public void clean(Lane lane) {
        SnowPlow plow = lane.getSnowPlow();
        if (lane.getState() instanceof IcyState) {
            lane.setState(new BrokenIceState());
            Logger.action(plow, "Jegtorofej takaritott");
            Logger.action(lane, "allapota BrokenIceState-re valtozott");
        } else {
            Logger.action(plow, "Jegtorofej: nem jeges sav, nincs hatas");
        }
    }

    @Override public boolean isOperational() { return true; }
}
