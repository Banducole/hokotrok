package skeleton;

/**
 * Egy hókotróra egyszerre egyfajta kotrófej szerelhető, de a fejek cserélgethetők.
 * Ez az interfész fogja össze a különböző kotrófejeket.
 */
public interface CleanerHead {
    
    /**
     * Visszaadja, hogy a fej működőképes-e (pl. van-e benne üzemanyag).
     * @return Igaz, ha a fej használható állapotban van.
     */
    boolean isOperational();
    
    /**
     * Elvégzi a sáv takarítását az adott fej sajátosságainak megfelelően.
     * @param lane A takarítandó sáv.
     */
    void clean(Lane lane);
    
    /**
     * Újratölti a fejet a szükséges üzemanyaggal vagy fogyóeszközzel (pl. só, kerozin).
     * @param amount A betöltött anyag mennyisége.
     */
    default void refuel(int amount) {
        SkeletonHelper.enterMethod("Head.Refuel(" + amount + ")");
        SkeletonHelper.exitMethod("Head.Refuel(" + amount + ")");
    }
}