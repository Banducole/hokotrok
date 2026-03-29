package skeleton;

/**
 * A gazdaság élénkítése céljából minél előbb le kell takarítani az utakat. 
 * Ehhez hókotrók állnak rendelkezésre.
 * Egy hókotró egyszerre csak egy sávot tud letakarítani.
 */
public class SnowPlow {
    private Head currentHead;
    private Lane targetLane;
    private CleanerPlayer owner;

    /**
     * Hókotró konstruktora.
     * @param head A rászerelt kotrófej.
     * @param owner A hókotrót birtokló takarító.
     */
    public SnowPlow(Head head, CleanerPlayer owner) {
        this.currentHead = head;
        this.owner = owner;
    }

    /**
     * Beállítja a cél sávot a hókotró számára.
     * @param lane A cél sáv.
     */
    public void setTargetLane(Lane lane) {
        SkeletonHelper.enterMethod("SnowPlow.SetTargetLane(Lane)");
        this.targetLane = lane;
        SkeletonHelper.exitMethod("SnowPlow.SetTargetLane(Lane)");
    }

    /**
     * A hókotró lépése. Megpróbál rálépni a cél sávra.
     */
    public void step() {
        SkeletonHelper.enterMethod("SnowPlow.Step()");
        Lane dummyLane = new Lane();
        boolean passable = dummyLane.isPassable(); 
        
        boolean entersAnyway = SkeletonHelper.askQuestion("A hókotró rálép a sávra?");
        if (entersAnyway) {
            dummyLane.accept(this);
            dummyLane.cleanWith(this);
        }
        SkeletonHelper.exitMethod("SnowPlow.Step()");
    }

    public Head getHead() {
        SkeletonHelper.enterMethod("SnowPlow.GetHead()");
        SkeletonHelper.exitMethod("SnowPlow.GetHead()");
        return currentHead;
    }

    public CleanerPlayer getOwner() {
        SkeletonHelper.enterMethod("SnowPlow.GetOwner()");
        SkeletonHelper.exitMethod("SnowPlow.GetOwner()");
        return owner;
    }
    
 // A SnowPlow osztályba ezek a metódusok kellenek még a CleanerPlayer miatt:

    public void changeHead(Head newHead) {
        SkeletonHelper.enterMethod("SnowPlow.ChangeHead(Head)");
        this.currentHead = newHead;
        System.out.println("  [LOG] Hókotró feje lecserélve.");
        SkeletonHelper.exitMethod("SnowPlow.ChangeHead(Head)");
    }

    public Lane getCurrentLane() {
        SkeletonHelper.enterMethod("SnowPlow.GetCurrentLane()");
        SkeletonHelper.exitMethod("SnowPlow.GetCurrentLane()");
        return this.targetLane; // Szkeleton szinten elég a targetLane-t visszaadni
    }
    
}