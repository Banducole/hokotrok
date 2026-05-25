package skeleton;

/**
 * Az útvonalkeresésért felelős osztály.
 * Segítségével a járművek megtalálják a legrövidebb utat a céljuk felé, illetve a szomszédos sávokat.
 */
public class PathFinder {
    
    /**
     * Visszaadja a legrövidebb úton következő sávot a kezdő és célállomás között.
     * @param vehicle A jármű (pl. autó vagy busz), aminek az utat keressük.
     * @param start Kezdőpont (pl. lakás vagy végállomás).
     * @param end Végpont (pl. munkahely vagy végállomás).
     * @return A következő sáv, amire a járműnek lépnie kell.
     */
    public Lane getShortestPath(Vehicle vehicle, Node start, Node end) {
        String startType = start.getClass().getSimpleName();
        String endType = end.getClass().getSimpleName();
        
        SkeletonHelper.enterMethod("PathFinder.GetShortestPath(Vehicle, " + startType + ", " + endType + ")");
        
        // A skeletonban dedikáltan visszaadunk egy mock sávot
        SkeletonHelper.exitMethod("PathFinder.GetShortestPath(Vehicle, " + startType + ", " + endType + ")");
        return new Lane(); 
    }
    
    /**
     * Visszaadja az adott sáv melletti (szomszédos) sávot egy adott irányban.
     * @param currentLane A jelenlegi sáv, ahol a jármű vagy hókotró tartózkodik.
     * @param direction A keresett szomszéd iránya.
     * @return A szomszédos sáv objektuma.
     */
    public Lane getNeighborLane(Lane currentLane, String direction) {
        SkeletonHelper.enterMethod("PathFinder.GetNeighborLane(lane, " + direction + ")");
        SkeletonHelper.exitMethod("PathFinder.GetNeighborLane(lane, " + direction + ")");
        return new Lane();
    }
    
    /**
     * Megvizsgálja, hogy van-e járható szomszédos sáv az akadály kikerüléséhez, 
     * és rákérdez a tesztelőtől, hogy sikeres-e a sávváltás.
     * @param currentLane A jelenlegi (esetleg blokkolt) sáv.
     * @return Igaz, ha van járható szomszédos sáv, amire a jármű áttérhet.
     */
    public boolean switchPassableLane(Lane currentLane) {
        SkeletonHelper.enterMethod("PathFinder.SwitchPassableLane(lane)");
        boolean hasNeighbor = SkeletonHelper.askQuestion("Van járható szomszéd sáv?");
        SkeletonHelper.exitMethod("PathFinder.SwitchPassableLane(lane)");
        return hasNeighbor;
    }
}