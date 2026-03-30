package skeleton;

public class Bus extends Vehicle {

    public void step(PathFinder pf, Lane currentLane) {
        SkeletonHelper.enterMethod("Bus.Step()");
        
        boolean blocked = isBlocked();
        if (!blocked) {
            Terminal dummyStart = new Terminal();
            Terminal dummyEnd = new Terminal();
            
            Lane nextLane = pf.getShortestPath(this, dummyStart, dummyEnd);
            if (nextLane != null) {
                boolean passable = nextLane.isPassable();
                if (passable) {
                    // Az accept() metódus önmagában lekezeli a csúszás és ütközés kérdését!
                    nextLane.accept(this); 
                    
                    // Így itt már csak azt vizsgáljuk, hogy a végállomásra ért-e.
                    boolean routeComplete = isRouteComplete();
                    if (routeComplete) {
                        dummyEnd.notifyArrival(this);
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
        System.out.println("  [LOG] Busz befejezett egy kört.");
        SkeletonHelper.exitMethod("Bus.ArrivedAtTerminal()");
    }
}