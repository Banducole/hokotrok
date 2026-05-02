import java.util.ArrayList;
import java.util.List;

public class CleanerPlayer extends Player {
    private int balance;
    private final List<SnowPlow> plows = new ArrayList<>();

    public CleanerPlayer(int initialBalance) { this.balance = initialBalance; }

    @Override public void takeTurn() {}

    public int getBalance() { return balance; }
    public List<SnowPlow> getPlows() { return plows; }
    public void addPlow(SnowPlow plow) { plows.add(plow); plow.setOwner(this); }

    public void buyHead(SnowPlow plow, CleanerHead newHead) {
        int price = newHead.getPrice();
        if (balance >= price) {
            balance -= price;
            plow.changeHead(newHead);
            Logger.action(this, "Fej vasarlas sikeres, maradek egyenleg: " + balance);
        } else {
            Logger.error(this, "Nincs eleg penz a fej vasarlashoz");
        }
    }

    public void buyFuel(SnowPlow plow, FuelType fuelType, int amount) {
        int cost = fuelType.getPrice() * amount;
        if (balance >= cost) {
            CleanerHead head = plow.getHead();
            if (head != null) {
                balance -= cost;
                head.refuel(amount);
                Logger.action(this, "Toltes sikeres, maradek egyenleg: " + balance);
            } else {
                Logger.error(this, "A hokotrora nincs fej szerelve");
            }
        } else {
            Logger.error(this, "Nincs eleg penz a tolteshez");
        }
    }

    public SnowPlow buyNewPlow(SnowPlow lastPlow, String newName) {
        SnowPlow newPlow = new SnowPlow(this);
        Logger.register(newPlow, newName);
        int price = newPlow.getPrice();
        if (balance >= price) {
            balance -= price;
            plows.add(newPlow);
            if (lastPlow != null) {
                Lane targetLane = lastPlow.getCurrentLane();
                if (targetLane != null) {
                    newPlow.setCurrentLane(targetLane);
                    targetLane.accept(newPlow);
                }
            }
            Logger.action(this, "vasarlas sikeres, uj hokotro letrejott, egyenleg: " + balance);
            return newPlow;
        } else {
            Logger.error(this, "Nincs eleg penz hokotro vasarlashoz");
            return null;
        }
    }

    public void receivePayment(int amount) {
        balance += amount;
        Logger.action(this, "penzt szerzett: " + amount + ", egyenleg: " + balance);
    }
}
