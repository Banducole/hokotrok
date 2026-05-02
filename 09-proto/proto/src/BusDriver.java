public class BusDriver extends Player {
    private Bus bus;
    public BusDriver(Bus bus) { this.bus = bus; }
    public Bus getBus() { return bus; }
    @Override public void takeTurn() { /* prototype: commands drive bus directly */ }
}
