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
        lane.getState();
        
        boolean isSlippery = SkeletonHelper.askQuestion("A sáv jeges állapotban van?");
        if(isSlippery) {
            boolean canBePushed = SkeletonHelper.askQuestion("Tolható a tartalom?"); // A jeget nem tudja tolni [cite: 495, 496]
            if(!canBePushed) {
                lane.setState(new BrokenIceState());
            }
        }
        
        SkeletonHelper.exitMethod("IceBreakerHead.Clean(Lane)");
    }
}
