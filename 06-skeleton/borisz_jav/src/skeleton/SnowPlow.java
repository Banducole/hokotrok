package skeleton;

public class SnowPlow {
    // NINCS belső változó
    // NINCS paraméteres konstruktor (töröltük a public SnowPlow(Head head, CleanerPlayer owner) részt)
    private CleanerHead currentHead = new ThrowHead();

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
            dummyLane.cleanWith(this);
        }
        SkeletonHelper.exitMethod("SnowPlow.Step()");
    }

    public CleanerHead getHead() {
        SkeletonHelper.enterMethod("SnowPlow.GetHead()");
        
        String input = SkeletonHelper.askString("Milyen fej van a hókotrón? [t=throw, sw=sweep, i=ice, s=salt, d=dragon]:");
        CleanerHead dummyHead;
        
        switch (input) {
            case "sw":
            case "sweep":
                dummyHead = new SweepHead();
                break;
            case "i":
            case "ice":
                dummyHead = new IceBreakerHead();
                break;
            case "s":
            case "salt":
                dummyHead = new SaltHead();
                break;
            case "d":
            case "dragon":
                dummyHead = new DragonHead();
                break;
            case "t":
            case "throw":
            default:
                dummyHead = new ThrowHead();
                break;
        }
        
        SkeletonHelper.exitMethod("SnowPlow.GetHead()");
        return dummyHead;
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