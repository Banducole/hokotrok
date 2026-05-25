package skeleton;

/**
 * A sárkányfejet (DragonHead) reprezentáló osztály.
 * Ez a fej képes a vastag hó és a jég felolvasztására is, de működéséhez kerozinra van szüksége.
 */
public class DragonHead implements CleanerHead {

    /**
     * Visszaadja, hogy a sárkányfej működőképes-e (van-e benne elegendő kerozin).
     * @return Igaz, ha a fej használható állapotban van.
     */
    @Override
    public boolean isOperational() {
        SkeletonHelper.enterMethod("DragonHead.IsOperational()");
        boolean op = SkeletonHelper.askQuestion("Van elég kerozin a sárkányfejben?");
        SkeletonHelper.exitMethod("DragonHead.IsOperational()");
        return op;
    }

    /**
     * Elvégzi az adott sáv takarítását (olvasztását) a sárkányfejjel.
     * @param lane A takarítandó sáv.
     */
    @Override
    public void clean(Lane lane) {
        SkeletonHelper.enterMethod("DragonHead.Clean(Lane)");
        lane.getState();
        boolean cleaned = SkeletonHelper.askQuestion("A sárkányfej megtisztította a sávot?");
        if(cleaned) {
            lane.setState(new ClearState());
        }
        SkeletonHelper.exitMethod("DragonHead.Clean(Lane)");
    }
}