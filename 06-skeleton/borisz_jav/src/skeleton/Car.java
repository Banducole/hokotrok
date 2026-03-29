package skeleton;

public class Car extends Vehicle {
    // KIVÉVE: private Home home; private Workplace workplace;

    public void step(PathFinder pf, Lane currentLane) {
        SkeletonHelper.enterMethod("Car.Step()");
        
        boolean blocked = isBlocked();
        if (!blocked) {
            // Lokális dummy objektumok a szekvencia kedvéért
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