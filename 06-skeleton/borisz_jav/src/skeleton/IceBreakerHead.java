package skeleton;

/**
 * A jégtörő fej a jeget töri fel, de nem takarítja el azt.
 */
public class IceBreakerHead implements CleanerHead {
    @Override
    public boolean isOperational() {
        SkeletonHelper.enterMethod("IceBreakerHead.IsOperational()");
        SkeletonHelper.exitMethod("IceBreakerHead.IsOperational()");
        return true; // Alapértelmezetten mindig működik
    }

    @Override
    public void clean(Lane lane) {
        SkeletonHelper.enterMethod("IceBreakerHead.Clean(Lane)");
        
        LaneState currentState = lane.getState();
        
        if(currentState.getClass().getSimpleName().equals("IcyState")) {
            boolean canBePushed = SkeletonHelper.askQuestion("Tolható a tartalom?"); 
            if(!canBePushed) {
                lane.setState(new BrokenIceState());
            }
        }
        
        SkeletonHelper.exitMethod("IceBreakerHead.Clean(Lane)");
    }
}
