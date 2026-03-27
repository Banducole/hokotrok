package skeleton;

/**
 * A járműveket (autók, buszok) összefogó ősosztály.
 */
public abstract class Vehicle {
    protected int blockedTurns = 0;

    public void setBlocked(int turns) {
        SkeletonHelper.enterMethod("Vehicle.SetBlocked(" + turns + ")");
        this.blockedTurns = turns;
        SkeletonHelper.exitMethod("Vehicle.SetBlocked(" + turns + ")");
    }

    public void decrementBlock() {
        SkeletonHelper.enterMethod("Vehicle.DecrementBlock()");
        if (blockedTurns > 0) {
            blockedTurns--;
        }
        SkeletonHelper.exitMethod("Vehicle.DecrementBlock()");
    }

    public boolean isBlocked() {
        SkeletonHelper.enterMethod("Vehicle.IsBlocked()");
        boolean result = SkeletonHelper.askQuestion("A jármű blokkolt?");
        SkeletonHelper.exitMethod("Vehicle.IsBlocked()");
        return result;
    }
}