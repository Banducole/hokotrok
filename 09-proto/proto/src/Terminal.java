public class Terminal extends Node {
    public void notifyArrival(Bus bus) { bus.arrivedAtTerminal(); }

    @Override
    public void addRoad(Road road) { super.addRoad(road); }
}
