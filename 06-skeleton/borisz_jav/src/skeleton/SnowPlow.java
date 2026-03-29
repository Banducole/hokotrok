package skeleton;

public class SnowPlow {
    // NINCS belső változó
    // NINCS paraméteres konstruktor (töröltük a public SnowPlow(Head head, CleanerPlayer owner) részt)

    public void setTargetLane(Lane lane) {
        SkeletonHelper.enterMethod("SnowPlow.SetTargetLane(Lane)");
        SkeletonHelper.exitMethod("SnowPlow.SetTargetLane(Lane)");
    }

    public void step() {
        SkeletonHelper.enterMethod("SnowPlow.Step()");
        Lane dummyLane = new Lane();
        boolean passable = dummyLane.isPassable();
        
        boolean entersAnyway = SkeletonHelper.askQuestion("A hókotró rálép a sávra?");
        if (entersAnyway) {
            dummyLane.accept(this);
        }
        SkeletonHelper.exitMethod("SnowPlow.Step()");
    }

    public CleanerHead getHead() {
        SkeletonHelper.enterMethod("SnowPlow.GetHead()");
        SkeletonHelper.exitMethod("SnowPlow.GetHead()");
        return new ThrowHead(); 
    }

    public CleanerPlayer getOwner() {
        SkeletonHelper.enterMethod("SnowPlow.GetOwner()");
        SkeletonHelper.exitMethod("SnowPlow.GetOwner()");
        return new CleanerPlayer(); 
    }
    
    public void changeHead(CleanerHead newHead) {
        SkeletonHelper.enterMethod("SnowPlow.ChangeHead(Head)");
        System.out.println("  [LOG] Hókotró feje lecserélve.");
        SkeletonHelper.exitMethod("SnowPlow.ChangeHead(Head)");
    }

    public Lane getCurrentLane() {
        SkeletonHelper.enterMethod("SnowPlow.GetCurrentLane()");
        SkeletonHelper.exitMethod("SnowPlow.GetCurrentLane()");
        return new Lane(); 
    }
}