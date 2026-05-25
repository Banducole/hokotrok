package skeleton;

/**
 * A hóhányó fejet (ThrowHead) reprezentáló osztály.
 * Ez a fej oldalra, de messzire (akár az út menti területekre) szórja a felgyülemlett havat.
 */
public class ThrowHead implements CleanerHead {
    
    /**
     * Visszaadja, hogy a hányófej működőképes-e.
     * @return Igaz, ha a fej használható állapotban van.
     */
    @Override
    public boolean isOperational() {
        SkeletonHelper.enterMethod("ThrowHead.IsOperational()");
        boolean op = SkeletonHelper.askQuestion("A hányófej működőképes?");
        SkeletonHelper.exitMethod("ThrowHead.IsOperational()");
        return op;
    }

    /**
     * Elvégzi a sáv takarítását a hányófejjel.
     * Ha a takarítás sikeres, a sáv állapotát tiszta (ClearState) állapotra állítja.
     * @param lane A takarítandó sáv.
     */
    @Override
    public void clean(Lane lane) {
        SkeletonHelper.enterMethod("ThrowHead.Clean(Lane)");
        lane.getState();
        
        boolean cleaned = SkeletonHelper.askQuestion("A  hányófej sikeresen megtisztította?");
        if(cleaned) {
            lane.setState("ClearState");
        }
        
        SkeletonHelper.exitMethod("ThrowHead.Clean(Lane)");
    }
}