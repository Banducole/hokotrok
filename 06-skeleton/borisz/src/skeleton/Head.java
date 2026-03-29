package skeleton;

/**
 * Egy hókotróra egyszerre egyfajta kotrófej szerelhető, de a fejek cserélgethetők.
 * Ez az interfész fogja össze a különböző kotrófejeket.
 */
public interface Head {
    /**
     * Visszaadja, hogy a fej működőképes-e (pl. van-e benne üzemanyag).
     */
    boolean isOperational();
    
    /**
     * Elvégzi a sáv takarítását az adott fej sajátosságainak megfelelően.
     */
    void clean(Lane lane);
    
    default void refuel(int amount) {
        SkeletonHelper.enterMethod("Head.Refuel(" + amount + ")");
        SkeletonHelper.exitMethod("Head.Refuel(" + amount + ")");
    }
}
