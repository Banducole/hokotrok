package skeleton;

/**
 * A söprő fej oldalra tolja a havat, közvetlenül a hókotró nyomvonala mellé.
 */
public class SweepHead implements Head {
    @Override
    public boolean isOperational() {
        SkeletonHelper.enterMethod("SweepHead.IsOperational()");
        boolean op = SkeletonHelper.askQuestion("A söprőfej működőképes?");
        SkeletonHelper.exitMethod("SweepHead.IsOperational()");
        return op;
    }

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


