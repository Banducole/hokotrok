package skeleton;

/**
 * A söprő fej (SweepHead) a havat oldalra tolja, közvetlenül a hókotró nyomvonala mellé (a szomszédos sávra).
 */
public class SweepHead implements CleanerHead {
    
    /**
     * Visszaadja, hogy a söprőfej működőképes-e.
     * @return Igaz, ha a fej használható állapotban van.
     */
    @Override
    public boolean isOperational() {
        SkeletonHelper.enterMethod("SweepHead.IsOperational()");
        boolean op = SkeletonHelper.askQuestion("A söprőfej működőképes?");
        SkeletonHelper.exitMethod("SweepHead.IsOperational()");
        return op;
    }

    /**
     * Elvégzi a sáv takarítását a söprőfejjel.
     * Ha a sáv tartalma tolható (hó), akkor egy bizonyos mennyiséget átrak a szomszédos sávba, 
     * majd a jelenlegi sávot tiszta (ClearState) állapotba helyezi.
     * @param lane A takarítandó sáv.
     */
    @Override
    public void clean(Lane lane) {
        SkeletonHelper.enterMethod("SweepHead.Clean(Lane)");
        lane.getState();
        
        boolean canBePushed = SkeletonHelper.askQuestion("A sáv tartalma tolható (hó)?");
        if(canBePushed) {
            PathFinder pf = new PathFinder();
            Lane neighbor = pf.getNeighborLane(lane, "RIGHT");
            if (neighbor != null) {
                neighbor.addSnow(3); // Mennyiség átadása
            }
            lane.setState(new ClearState());
        }
        
        SkeletonHelper.exitMethod("SweepHead.Clean(Lane)");
    }
}