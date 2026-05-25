package skeleton_test;

public class CleanerPlayer {

    public CleanerPlayer() {
        Skeleton.logCall("CleanerPlayer::CleanerPlayer()");
    }

    public void takeTurn() {
        Skeleton.logCall("CleanerPlayer::takeTurn()");
        Skeleton.enter();

        SnowPlow sp = new SnowPlow();
        sp.step();

        Skeleton.exit();
    }

    public void receivePayment(int amount) {
        Skeleton.logCall("CleanerPlayer::receivePayment(amount)  [balance += amount]");
    }

    public int getBalance() {
        Skeleton.logCall("CleanerPlayer::getBalance()");
        int result = Skeleton.askInt("Mi az egyenleg?");
        Skeleton.logReturn(String.valueOf(result));
        return result;
    }

    public boolean buyHead(SnowPlow plow, Head newHead) {
        Skeleton.logCall("CleanerPlayer::buyHead(plow, newHead)");
        Skeleton.enter();

        int balance = getBalance();
        int price   = newHead.getPrice();

        boolean enough = balance >= price;
        Skeleton.logReturn("balance >= price: " + enough);

        if (enough) {
            Skeleton.logCall("CleanerPlayer: balance -= " + price);
            plow.changeHead(newHead);
        }

        Skeleton.exit();
        return enough;
    }

    public boolean buyFuel(SnowPlow plow, int amount) {
        Skeleton.logCall("CleanerPlayer::buyFuel(plow, amount)");
        Skeleton.enter();

        int balance = getBalance();
        int price   = Skeleton.askInt("Mi az üzemanyag ára?");

        boolean enough = balance >= price;
        if (enough) {
            Skeleton.logCall("CleanerPlayer: balance -= " + price);
            plow.getHead().refuel(amount);
        }

        Skeleton.exit();
        return enough;
    }

    public boolean buyNewPlow(SnowPlow lastPlow) {
        Skeleton.logCall("CleanerPlayer::buyNewPlow(lastPlow)");
        Skeleton.enter();

        int balance  = getBalance();
        int price    = lastPlow.getPrice();
        boolean enough = balance >= price;

        if (enough) {
            Lane lane = lastPlow.getCurrentLane();
            SnowPlow newPlow = new SnowPlow();
            lane.acceptPlow(newPlow);
            Skeleton.logCall("CleanerPlayer: balance -= " + price);
            Skeleton.logCall("CleanerPlayer: plows.add(newPlow)");
        }

        Skeleton.exit();
        return enough;
    }
}
