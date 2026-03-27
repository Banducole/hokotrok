package skeleton;

/**
 * A sószóró fej által kiszórt só egy idő után elolvasztja a havat és a jeget.
 */
public class SaltHead implements Head {
    private int salt = 5;

    @Override
    public boolean isOperational() {
        SkeletonHelper.enterMethod("SaltHead.IsOperational()");
        boolean op = SkeletonHelper.askQuestion("Van elég só a sószóróban?");
        SkeletonHelper.exitMethod("SaltHead.IsOperational()");
        return op;
    }

    @Override
    public void clean(Lane lane) {
        SkeletonHelper.enterMethod("SaltHead.Clean(Lane)");
        lane.applySalt();
        SkeletonHelper.exitMethod("SaltHead.Clean(Lane)");
    }
}