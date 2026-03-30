package skeleton;

/**
 * A hókotró járművet reprezentáló osztály.
 * A hókotrókat a takarító játékosok (CleanerPlayer) irányítják. 
 * Képesek különböző takarítófejekkel (CleanerHead) megtisztítani a sávokat.
 */
public class SnowPlow {
    
    private CleanerHead currentHead = new ThrowHead();

    /**
     * Beállítja a hókotró célját (melyik sáv felé haladjon).
     * @param lane A célzott sáv.
     */
    public void setTargetLane(Lane lane) {
        SkeletonHelper.enterMethod("SnowPlow.SetTargetLane(Lane)");
        SkeletonHelper.exitMethod("SnowPlow.SetTargetLane(Lane)");
    }

    /**
     * A hókotró léptetése egy körben.
     * Megpróbál rálépni a célzott sávra, és ha rálépett, azonnal el is kezdi a takarítást.
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

    /**
     * Visszaadja a hókotrón jelenleg lévő takarítófejet.
     * A szkeleton fázisban ez a metódus megkérdezi a tesztelőt, hogy milyen fejet szimuláljon.
     * @return Az aktuális takarítófej (CleanerHead) objektuma.
     */
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

    /**
     * Visszaadja a hókotrót birtokló és irányító játékost.
     * @return A tulajdonos játékos (CleanerPlayer) objektuma.
     */
    public CleanerPlayer getOwner() {
        SkeletonHelper.enterMethod("SnowPlow.GetOwner()");
        SkeletonHelper.exitMethod("SnowPlow.GetOwner()");
        return new CleanerPlayer(); 
    }
    
    /**
     * Lecseréli a hókotró jelenlegi fejét egy új fejre.
     * @param newHead Az új takarítófej.
     */
    public void changeHead(CleanerHead newHead) {
        SkeletonHelper.enterMethod("SnowPlow.ChangeHead(Head)");
        System.out.println("  [LOG] Hókotró feje lecserélve.");
        SkeletonHelper.exitMethod("SnowPlow.ChangeHead(Head)");
    }

    /**
     * Visszaadja azt a sávot, amelyen a hókotró jelenleg tartózkodik.
     * @return Az aktuális sáv (Lane).
     */
    public Lane getCurrentLane() {
        SkeletonHelper.enterMethod("SnowPlow.GetCurrentLane()");
        SkeletonHelper.exitMethod("SnowPlow.GetCurrentLane()");
        return new Lane(); 
    }
}