package skeleton;

public class CleanerPlayer {
    // KIVÉVE: private int balance = 500;

    public void takeTurn(SnowPlow sp, Lane targetLane) {
        SkeletonHelper.enterMethod("CleanerPlayer.TakeTurn()");
        sp.setTargetLane(targetLane);
        sp.step();
        SkeletonHelper.exitMethod("CleanerPlayer.TakeTurn()");
    }

    public void receivePayment(int amount) {
        SkeletonHelper.enterMethod("CleanerPlayer.ReceivePayment(amount)");
        System.out.println("  [LOG] Takarító fizetséget kapott.");
        SkeletonHelper.exitMethod("CleanerPlayer.ReceivePayment(amount)");
    }

    public void buyHead(SnowPlow sp, Head newHead) {
        SkeletonHelper.enterMethod("CleanerPlayer.BuyHead(SnowPlow, Head)");
        boolean hasEnoughBalance = SkeletonHelper.askQuestion("Van elegendő egyenleg az új fejre?");
        if (hasEnoughBalance) {
            System.out.println("  [LOG] Egyenleg csökkent.");
            sp.changeHead(newHead);
        }
        SkeletonHelper.exitMethod("CleanerPlayer.BuyHead(SnowPlow, Head)");
    }

    public void buyFuel(SnowPlow sp, int amount) {
        SkeletonHelper.enterMethod("CleanerPlayer.BuyFuel(SnowPlow, amount)");
        boolean hasEnoughBalance = SkeletonHelper.askQuestion("Van elegendő egyenleg az üzemanyagra?");
        if (hasEnoughBalance) {
            Head currentHead = sp.getHead();
            System.out.println("  [LOG] Egyenleg csökkent.");
            currentHead.refuel(amount); 
        }
        SkeletonHelper.exitMethod("CleanerPlayer.BuyFuel(SnowPlow, amount)");
    }

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