package skeleton;

/**
 * A városban közlekedő normál autókat reprezentáló osztály.
 * Az autók általában a lakás (Home) és a munkahely (Workplace) között közlekednek.
 */
public class Car extends Vehicle {

    /**
     * Az autó léptetése egy szimulációs körben.
     * Megkeresi a legrövidebb utat a célja felé, és ha a következő sáv járható, rálép.
     * * @param pf Az útvonalkereső (PathFinder) objektum.
     * @param currentLane A sáv, amelyen az autó jelenleg áll.
     */
    public void step(PathFinder pf, Lane currentLane) {
        SkeletonHelper.enterMethod("Car.Step()");
        
        boolean blocked = isBlocked();
        if (!blocked) {
            Home dummyHome = new Home();
            Workplace dummyWork = new Workplace();
            
            Lane nextLane = pf.getShortestPath(this, dummyHome, dummyWork); 
            if (nextLane != null) {
                boolean passable = nextLane.isPassable();
                if (passable) {
                    nextLane.accept(this);
                }
            }
        }
        
        SkeletonHelper.exitMethod("Car.Step()");
    }
}