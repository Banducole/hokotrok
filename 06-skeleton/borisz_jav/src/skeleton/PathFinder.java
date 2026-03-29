package skeleton;

/**
 * Az útvonalkeresésért felelős osztály.
 */
public class PathFinder {
    /**
     * Visszaadja a legrövidebb utat (a következő sávot) a kezdő és célállomás között.
     * @param car Az autó, aminek az utat keressük.
     * @param start Kezdőpont (pl. lakás)
     * @param end Végpont (pl. munkahely)
     * @return A következő sáv, amire lépnie kell.
     */
	public Lane getShortestPath(Vehicle vehicle, Node start, Node end) {
        String startType = start.getClass().getSimpleName();
        String endType = end.getClass().getSimpleName();
        
        SkeletonHelper.enterMethod("PathFinder.GetShortestPath(Vehicle, " + startType + ", " + endType + ")");
        
        // A skeletonban dedikáltan visszaadunk egy mock sávot
        SkeletonHelper.exitMethod("PathFinder.GetShortestPath(Vehicle, " + startType + ", " + endType + ")");
        return new Lane(); 
    }
    
    public Lane getNeighborLane(Lane currentLane, String direction) {
        SkeletonHelper.enterMethod("PathFinder.GetNeighborLane(lane, " + direction + ")");
        SkeletonHelper.exitMethod("PathFinder.GetNeighborLane(lane, " + direction + ")");
        return new Lane(); // Dummy sáv a teszthez
    }
    
    public boolean switchPassableLane(Lane currentLane) {
        SkeletonHelper.enterMethod("PathFinder.SwitchPassableLane(lane)");
        boolean hasNeighbor = SkeletonHelper.askQuestion("Van járható szomszéd sáv?");
        SkeletonHelper.exitMethod("PathFinder.SwitchPassableLane(lane)");
        return hasNeighbor;
    }
    
}
