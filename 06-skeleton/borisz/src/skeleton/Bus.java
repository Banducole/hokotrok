package skeleton;

/**
 * A városban buszok is járnak, amelyeknek két végállomás között kell megfordulniuk.
 */
public class Bus extends Vehicle {
    private int completedRounds = 0;
    private Terminal startTerminal = new Terminal();
    private Terminal endTerminal = new Terminal();

    /**
     * A busz léptetése egy szimulációs körben.
     */
    public void step(PathFinder pf, Lane currentLane) {
        SkeletonHelper.enterMethod("Bus.Step()");
        
        boolean blocked = isBlocked();
        if (!blocked) {
            // A busz a két végállomás között kér utat!
            Lane nextLane = pf.getShortestPath(this, startTerminal, endTerminal);
            
            if (nextLane != null) {
                boolean passable = nextLane.isPassable();
                if (passable) {
                    nextLane.accept(this); 
                    
                    boolean slippery = nextLane.slipperyRoadWithVehicles();
                    if (!slippery) {
                        boolean routeComplete = isRouteComplete();
                        if (routeComplete) {
                            endTerminal.notifyArrival(this);
                        }
                    }
                }
            }
        }
        
        SkeletonHelper.exitMethod("Bus.Step()");
    }

    public boolean isRouteComplete() {
        SkeletonHelper.enterMethod("Bus.IsRouteComplete()");
        boolean result = SkeletonHelper.askQuestion("A busz teljesítette az útvonalat (végállomásra ért)?");
        SkeletonHelper.exitMethod("Bus.IsRouteComplete()");
        return result;
    }

    public void arrivedAtTerminal() {
        SkeletonHelper.enterMethod("Bus.ArrivedAtTerminal()");
        completedRounds++;
        System.out.println("  [LOG] Busz befejezett köreinek száma: " + completedRounds);
        SkeletonHelper.exitMethod("Bus.ArrivedAtTerminal()");
    }
}