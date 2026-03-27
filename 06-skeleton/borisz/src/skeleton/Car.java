package skeleton;

/**
 * A városban közlekedő autókat reprezentáló osztály.
 */
public class Car extends Vehicle {
    
    private Home home = new Home();
    private Workplace workplace = new Workplace();

    public void step(PathFinder pf, Lane currentLane) {
        SkeletonHelper.enterMethod("Car.Step()");
        
        boolean blocked = isBlocked();
        if (!blocked) {
            Lane nextLane = pf.getShortestPath(this, home, workplace); // Itt már Node-okat adunk át!
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