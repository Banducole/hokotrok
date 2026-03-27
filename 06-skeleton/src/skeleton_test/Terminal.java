package skeleton_test;

public class Terminal {

    public Terminal() {
        Skeleton.logCall("Terminal::Terminal()");
    }

    public void notifyArrival(Bus bus) {
        Skeleton.logCall("Terminal::notifyArrival(bus)");
        Skeleton.enter();

        bus.arrivedAtTerminal();

        Skeleton.exit();
    }
}
