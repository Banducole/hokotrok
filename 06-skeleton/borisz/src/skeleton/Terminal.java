package skeleton;

/**
 * A buszok végállomását reprezentáló csomópont.
 */
public class Terminal extends Node {
    
    /**
     * Értesítést kap, ha egy busz megérkezett a végállomásra.
     * @param bus A megérkezett busz.
     */
    public void notifyArrival(Bus bus) {
        SkeletonHelper.enterMethod("Terminal.NotifyArrival(Bus)");
        bus.arrivedAtTerminal();
        SkeletonHelper.exitMethod("Terminal.NotifyArrival(Bus)");
    }
}