package skeleton;

/**
 * A városban közlekedő buszokat reprezentáló osztály.
 * A buszok két végállomás (Terminal) között közlekednek meghatározott útvonalon.
 */
public class Bus extends Vehicle {

    /**
     * A busz léptetése egy szimulációs körben.
     * Megkeresi a következő sávot a végállomása felé, és megpróbál rálépni.
     * @param pf Az útvonalkereső (PathFinder) objektum.
     * @param currentLane A sáv, amelyen a busz jelenleg áll.
     */
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

    /**
     * Lekérdezi a tesztelőtől, hogy a busz elérte-e a célját (végállomást).
     * @return Igaz, ha a busz teljesítette az aktuális útvonalát.
     */
    public boolean isRouteComplete() {
        SkeletonHelper.enterMethod("Bus.IsRouteComplete()");
        boolean result = SkeletonHelper.askQuestion("A busz teljesítette az útvonalat (végállomásra ért)?");
        SkeletonHelper.exitMethod("Bus.IsRouteComplete()");
        return result;
    }

    /**
     * Értesíti a buszt, hogy sikeresen megérkezett a végállomásra.
     * A valós működésben ez növelné a befejezett körök számát.
     */
    public void arrivedAtTerminal() {
        SkeletonHelper.enterMethod("Bus.ArrivedAtTerminal()");
        System.out.println("  [LOG] Busz befejezett egy kört.");
        SkeletonHelper.exitMethod("Bus.ArrivedAtTerminal()");
    }
}