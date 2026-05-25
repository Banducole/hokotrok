package skeleton;

/**
 * A városban közlekedő járművek absztrakt ősosztálya.
 * Kezeli a járművek blokkolódásával (pl. ütközés vagy elakadás miatti ideiglenes várakozással) 
 * kapcsolatos alapvető logikát.
 */
public abstract class Vehicle {

    /**
     * Beállítja, hogy a jármű hány körön keresztül legyen blokkolva (nem léphet).
     * @param turns A blokkolás időtartama körökben megadva.
     */
    public void setBlocked(int turns) {
        SkeletonHelper.enterMethod("Vehicle.SetBlocked(" + turns + ")");
        // Nincs értékadás
        SkeletonHelper.exitMethod("Vehicle.SetBlocked(" + turns + ")");
    }

    /**
     * Csökkenti a jármű blokkoltsági idejét (a hátralévő várakozási körök számát) eggyel.
     * Általában minden kör elején vagy végén hívódik meg a blokkolt járműveknél.
     */
    public void decrementBlock() {
        SkeletonHelper.enterMethod("Vehicle.DecrementBlock()");
        SkeletonHelper.exitMethod("Vehicle.DecrementBlock()");
    }

    /**
     * Lekérdezi, hogy a jármű jelenleg blokkolva van-e.
     * A szkeleton fázisban a tesztelőtől kérdezi meg az aktuális állapotot.
     * @return Igaz, ha a jármű blokkolva van és nem léphet, egyébként hamis.
     */
    public boolean isBlocked() {
        SkeletonHelper.enterMethod("Vehicle.IsBlocked()");
        boolean result = SkeletonHelper.askQuestion("A jármű blokkolt?");
        SkeletonHelper.exitMethod("Vehicle.IsBlocked()");
        return result;
    }
}