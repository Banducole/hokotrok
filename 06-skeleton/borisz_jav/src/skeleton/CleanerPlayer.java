package skeleton;

/**
 * A takarító játékosokat reprezentáló osztály.
 * Célja a hókotrók irányítása, fejlesztése (új fejek/üzemanyag vásárlása), valamint új gépek beszerzése.
 */
public class CleanerPlayer extends Player {

    /**
     * A játékos lépése, amely során egy hókotrót egy cél sáv felé irányít, majd lépteti azt.
     * @param sp A mozgatni kívánt hókotró.
     * @param targetLane A sáv, amire a hókotrót irányítjuk.
     */
    public void takeTurn(SnowPlow sp, Lane targetLane) {
        SkeletonHelper.enterMethod("CleanerPlayer.TakeTurn()");
        sp.setTargetLane(targetLane);
        sp.step();
        SkeletonHelper.exitMethod("CleanerPlayer.TakeTurn()");
    }

    /**
     * Fizetség fogadása sikeres takarítás után.
     * @param amount A kapott fizetség összege.
     */
    public void receivePayment(int amount) {
        SkeletonHelper.enterMethod("CleanerPlayer.ReceivePayment(amount)");
        System.out.println("  [LOG] Takarító fizetséget kapott.");
        SkeletonHelper.exitMethod("CleanerPlayer.ReceivePayment(amount)");
    }

    /**
     * Új kotrófej vásárlása és felszerelése egy adott hókotróra.
     * @param sp A hókotró, amelyik megkapja az új fejet.
     * @param newHead A megvásárolt új fej.
     */
    public void buyHead(SnowPlow sp, CleanerHead newHead) {
        SkeletonHelper.enterMethod("CleanerPlayer.BuyHead(SnowPlow, Head)");
        boolean hasEnoughBalance = SkeletonHelper.askQuestion("Van elegendő egyenleg az új fejre?");
        if (hasEnoughBalance) {
            System.out.println("  [LOG] Egyenleg csökkent.");
            sp.changeHead(newHead);
        }
        SkeletonHelper.exitMethod("CleanerPlayer.BuyHead(SnowPlow, Head)");
    }

    /**
     * Üzemanyag vagy fogyóeszköz vásárlása a hókotró jelenlegi fejébe.
     * @param sp A hókotró, amelynek fejét fel akarjuk tölteni.
     * @param amount A vásárolt üzemanyag mennyisége.
     */
    public void buyFuel(SnowPlow sp, int amount) {
        SkeletonHelper.enterMethod("CleanerPlayer.BuyFuel(SnowPlow, amount)");
        boolean hasEnoughBalance = SkeletonHelper.askQuestion("Van elegendő egyenleg az üzemanyagra?");
        if (hasEnoughBalance) {
            CleanerHead currentHead = sp.getHead();
            System.out.println("  [LOG] Egyenleg csökkent.");
            currentHead.refuel(amount); 
        }
        SkeletonHelper.exitMethod("CleanerPlayer.BuyFuel(SnowPlow, amount)");
    }

    /**
     * Új hókotró vásárlása és kihelyezése a pályára.
     * Az új gép egy meglévő gép aktuális pozíciójában (sávjában) jelenik meg.
     * @param lastPlow A már pályán lévő hókotró, aminek a helyére az újat tesszük.
     */
    public void buyNewPlow(SnowPlow lastPlow) {
        SkeletonHelper.enterMethod("CleanerPlayer.BuyNewPlow(SnowPlow)");
        boolean hasEnoughBalance = SkeletonHelper.askQuestion("Van elegendő egyenleg új hókotróra?");
        if (hasEnoughBalance) {
            Lane dummyLane = lastPlow.getCurrentLane();
            SnowPlow newPlow = new SnowPlow(); 
            dummyLane.accept(newPlow);
            System.out.println("  [LOG] Egyenleg csökkent, új kotró kihelyezve.");
        }
        SkeletonHelper.exitMethod("CleanerPlayer.BuyNewPlow(SnowPlow)");
    }
}