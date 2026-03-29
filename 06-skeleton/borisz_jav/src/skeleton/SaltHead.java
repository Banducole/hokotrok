package skeleton;

public class SaltHead implements Head {
    // KIVÉVE: private int salt = 5;

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