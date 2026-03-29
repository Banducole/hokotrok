package skeleton;

/**
 * A takarító játékosokat reprezentáló osztály.
 * A takarítók a hókotrókat irányítják, cserélgetik a fejeket és vásárolják a segédeszközöket.
 */
public class CleanerPlayer {
    private int balance = 500; // Kezdeti egyenleg [cite: 106]

    /**
     * A takarító játékos köre. Ebben a metódusban kerül beállításra a cél sáv,
     * és elindítja a hókotró léptetését.
     * @param sp A takarítóhoz tartozó hókotró.
     * @param targetLane A sáv, amit meg szeretne tisztítani.
     */
    public void takeTurn(SnowPlow sp, Lane targetLane) {
        SkeletonHelper.enterMethod("CleanerPlayer.TakeTurn()");
        
        sp.setTargetLane(targetLane);
        sp.step();
        
        SkeletonHelper.exitMethod("CleanerPlayer.TakeTurn()");
    }

    /**
     * A hókotrók minden egység letisztított útszakaszért pénzt kapnak, 
     * amelyből különféle segédeszközöket vásárolhatnak.
     * @param amount A kapott fizetség mennyisége.
     */
    public void receivePayment(int amount) {
        SkeletonHelper.enterMethod("CleanerPlayer.ReceivePayment(amount)");
        this.balance += amount;
        System.out.println("  [LOG] Takarító egyenlege nőtt, jelenlegi: " + this.balance);
        SkeletonHelper.exitMethod("CleanerPlayer.ReceivePayment(amount)");
    }
    
 // --- Új metódusok a CleanerPlayer osztályba ---

    /**
     * Új kotrófej vásárlása és felszerelése.
     * SD-16: Fejcsere [cite: 532-544].
     * @param sp A hókotró, amire az új fejet szereljük.
     * @param newHead A megvásárolni kívánt új fej.
     */
    public void buyHead(SnowPlow sp, Head newHead) {
        SkeletonHelper.enterMethod("CleanerPlayer.BuyHead(SnowPlow, Head)");
        
        boolean hasEnoughBalance = SkeletonHelper.askQuestion("Van elegendő egyenleg (pl. 200) az új fejre?");
        if (hasEnoughBalance) {
            this.balance -= 200; // Mockolt ár
            System.out.println("  [LOG] Egyenleg csökkent: " + this.balance);
            sp.changeHead(newHead);
        }
        
        SkeletonHelper.exitMethod("CleanerPlayer.BuyHead(SnowPlow, Head)");
    }

    /**
     * Üzemanyag (vagy só) vásárlása az aktuális fejhez.
     * SD-17: Hajtóanyag vásárlás [cite: 546-563].
     * @param sp A hókotró, aminek a fejét fel akarjuk tölteni.
     * @param amount A vásárolt mennyiség.
     */
    public void buyFuel(SnowPlow sp, int amount) {
        SkeletonHelper.enterMethod("CleanerPlayer.BuyFuel(SnowPlow, amount)");
        
        boolean hasEnoughBalance = SkeletonHelper.askQuestion("Van elegendő egyenleg (pl. 100) az üzemanyagra?");
        if (hasEnoughBalance) {
            Head currentHead = sp.getHead();
            this.balance -= 100;
            System.out.println("  [LOG] Egyenleg csökkent: " + this.balance);
            currentHead.refuel(amount); // Ehhez a Head interfészt is bővíteni kell egy refuel metódussal!
        }
        
        SkeletonHelper.exitMethod("CleanerPlayer.BuyFuel(SnowPlow, amount)");
    }

    /**
     * A legdrágább segédeszköz az új hókotró.
     * SD-18: Új hókotró vásárlás [cite: 568-585].
     * @param lastPlow Egy meglévő hókotró, aminek a sávjára az újat helyezzük.
     */
    public void buyNewPlow(SnowPlow lastPlow) {
        SkeletonHelper.enterMethod("CleanerPlayer.BuyNewPlow(SnowPlow)");
        
        boolean hasEnoughBalance = SkeletonHelper.askQuestion("Van elegendő egyenleg (pl. 500) új hókotróra?");
        if (hasEnoughBalance) {
            Lane currentLane = lastPlow.getCurrentLane();
            SnowPlow newPlow = new SnowPlow(new ThrowHead(), this); // Létrehozás alapértelmezett fejjel
            currentLane.accept(newPlow);
            this.balance -= 500;
            System.out.println("  [LOG] Egyenleg csökkent: " + this.balance);
        }
        
        SkeletonHelper.exitMethod("CleanerPlayer.BuyNewPlow(SnowPlow)");
    }
    
}
