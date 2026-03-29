package skeleton;

/**
 * A buszsofőr játékost reprezentáló osztály.
 */
public class BusDriver extends Player {

    public void takeTurn() {
        SkeletonHelper.enterMethod("BusDriver.TakeTurn()");
        
        // Dummy objektumokkal meghívjuk a busz lépését
        Bus dummyBus = new Bus();
        PathFinder dummyPf = new PathFinder();
        Lane dummyLane = new Lane();
        
        dummyBus.step(dummyPf, dummyLane);
        
        SkeletonHelper.exitMethod("BusDriver.TakeTurn()");
    }
}