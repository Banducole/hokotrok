package skeleton;

/**
 * A buszsofőr játékost reprezentáló osztály.
 * Felelős a buszok irányításáért a saját körében.
 */
public class BusDriver extends Player {

    /**
     * A buszsofőr köre.
     * A szkeleton fázisban ez a metódus szimulálja egy busz léptetését a hálózaton
     * dummy objektumok segítségével.
     */
    public void takeTurn() {
        SkeletonHelper.enterMethod("BusDriver.TakeTurn()");

        Bus dummyBus = new Bus();
        PathFinder dummyPf = new PathFinder();
        Lane dummyLane = new Lane();
        
        dummyBus.step();
        
        SkeletonHelper.exitMethod("BusDriver.TakeTurn()");
    }
}