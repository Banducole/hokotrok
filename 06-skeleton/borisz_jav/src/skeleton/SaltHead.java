package skeleton;

/**
 * A sószóró fejet (SaltHead) reprezentáló osztály.
 * Ez a takarítófej sót szór az útra, amivel felolvasztja a havat és a jeget, 
 * de működéséhez sóra (mint fogyóeszközre) van szüksége.
 */
public class SaltHead implements CleanerHead {

    /**
     * Visszaadja, hogy a sószóró fej működőképes-e (van-e benne elegendő só).
     * @return Igaz, ha a fej használható állapotban van.
     */
    @Override
    public boolean isOperational() {
        SkeletonHelper.enterMethod("SaltHead.IsOperational()");
        boolean op = SkeletonHelper.askQuestion("Van elég só a sószóróban?");
        SkeletonHelper.exitMethod("SaltHead.IsOperational()");
        return op;
    }

    /**
     * Elvégzi az adott sáv sózását.
     * Meghívja a sáv applySalt metódusát, ami elindítja a hó és jég olvadását.
     * @param lane A sózandó (takarítandó) sáv.
     */
    @Override
    public void clean(Lane lane) {
        SkeletonHelper.enterMethod("SaltHead.Clean(Lane)");
        lane.applySalt();
        SkeletonHelper.exitMethod("SaltHead.Clean(Lane)");
    }
}