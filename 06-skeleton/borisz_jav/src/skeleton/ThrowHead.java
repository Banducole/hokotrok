package skeleton;

/**
 * A hányó fej oldalra, de messzire szórja a havat.
 */
public class ThrowHead implements CleanerHead {
    
    @Override
    public boolean isOperational() {
        SkeletonHelper.enterMethod("ThrowHead.IsOperational()");
        boolean op = SkeletonHelper.askQuestion("A hányófej működőképes?");
        SkeletonHelper.exitMethod("ThrowHead.IsOperational()");
        return op;
    }

    @Override
    public void clean(Lane lane) {
        SkeletonHelper.enterMethod("ThrowHead.Clean(Lane)");
        lane.getState();
        
        boolean cleaned = SkeletonHelper.askQuestion("A  hányófej sikeresen megtisztította?");
        if(cleaned) {
            lane.setState("ClearState");
        }
        
        SkeletonHelper.exitMethod("ThrowHead.Clean(Lane)");
    }
}
