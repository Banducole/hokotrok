package skeleton;

public abstract class Vehicle {
    // KIVÉVE: protected int blockedTurns = 0;

    public void setBlocked(int turns) {
        SkeletonHelper.enterMethod("Vehicle.SetBlocked(" + turns + ")");
        // Nincs értékadás
        SkeletonHelper.exitMethod("Vehicle.SetBlocked(" + turns + ")");
    }

    public void decrementBlock() {
        SkeletonHelper.enterMethod("Vehicle.DecrementBlock()");
        // Nincs csökkentés, a logolás a lényeg
        SkeletonHelper.exitMethod("Vehicle.DecrementBlock()");
    }

    public boolean isBlocked() {
        SkeletonHelper.enterMethod("Vehicle.IsBlocked()");
        boolean result = SkeletonHelper.askQuestion("A jármű blokkolt?");
        SkeletonHelper.exitMethod("Vehicle.IsBlocked()");
        return result;
    }
}