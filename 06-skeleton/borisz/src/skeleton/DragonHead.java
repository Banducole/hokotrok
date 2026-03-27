package skeleton;

/**
 * A sárkány fej egy gázturbinával egyszerűen elolvasztja a havat és a jeget a hókotró előtt.
 * Működéséhez biokerozint kell vásárolni.
 */
public class DragonHead implements Head {
    private int kerosene = 5; // [cite: 106]

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
        
        boolean cleaned = SkeletonHelper.askQuestion("A sárkányfej megtisztította a sávot (volt mit tisztítani)?");
        if(cleaned) {
            lane.setState("ClearState");
        }
        
        SkeletonHelper.exitMethod("DragonHead.Clean(Lane)");
    }
}
