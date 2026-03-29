package skeleton;

public class DragonHead implements CleanerHead {
    // KIVÉVE: private int kerosene = 5;

    @Override
    public boolean isOperational() {
        SkeletonHelper.enterMethod("DragonHead.IsOperational()");
        boolean op = SkeletonHelper.askQuestion("Van elég kerozin a sárkányfejben?");
        SkeletonHelper.exitMethod("DragonHead.IsOperational()");
        return op;
    }

    @Override
    public void clean(Lane lane) {
        SkeletonHelper.enterMethod("DragonHead.Clean(Lane)");
        lane.getState();
        boolean cleaned = SkeletonHelper.askQuestion("A sárkányfej megtisztította a sávot?");
        if(cleaned) {
            lane.setState(new ClearState()); // Nincs string hivatkozás
        }
        SkeletonHelper.exitMethod("DragonHead.Clean(Lane)");
    }
}