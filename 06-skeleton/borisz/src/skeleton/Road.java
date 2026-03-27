package skeleton;

/**
 * Egy utat reprezentáló osztály. 
 * Az utak többsávosak is lehetnek. Az utak nem csak szintben kereszteződhetnek, 
 * vannak hidak és alagutak is.
 */
public class Road {
    private Lane lane; // A szkeleton teszteléséhez egy sáv

    public Road(Lane lane) {
        this.lane = lane;
    }

    /**
     * Továbbítja a havazás eseményét a sávoknak.
     * @param amount A lehulló hó mennyisége.
     */
    public void applySnow(int amount) {
        SkeletonHelper.enterMethod("Road.ApplySnow(" + amount + ")");
        lane.addSnow(amount);
        SkeletonHelper.exitMethod("Road.ApplySnow(" + amount + ")");
    }
}