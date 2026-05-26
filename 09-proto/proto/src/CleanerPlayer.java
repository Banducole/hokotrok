import java.util.ArrayList;
import java.util.List;

/**
 * Takarító játékos, aki hókotrókat irányít és felszereléseket vásárol.
 * <p>
 * A {@link CleanerPlayer} egyenleggel rendelkezik, amelyből hókotrókat
 * ({@link SnowPlow}), fejeket ({@link CleanerHead}) és üzemanyagot vásárolhat.
 * Takarítás után a hókotróktól kifizetést kaphat ({@link #receivePayment(int)}).
 * </p>
 */
public class CleanerPlayer extends Player {

    /** A játékos aktuális pénzegyenlege. */
    private int balance;

    /** A játékos tulajdonában lévő hókotró lista. */
    private final List<SnowPlow> plows = new ArrayList<>();

    /**
     * Létrehozza a takarító játékost a megadott kezdő egyenleggel.
     *
     * @param initialBalance a kezdő egyenleg
     */
    public CleanerPlayer(int initialBalance) { this.balance = initialBalance; }

    /**
     * Körlépés – a játékos hókotróit a parancsértelmező irányítja közvetlenül.
     */
    @Override public void takeTurn() { /* parancsértelmező vezérli */ }

    /** @return az aktuális egyenleg */
    public int getBalance() { return balance; }

    /** @return a játékos hókotróinak listája */
    public List<SnowPlow> getPlows() { return plows; }

    /**
     * Hozzáad egy hókotró a játékos flottájához és beállítja a tulajdonost.
     *
     * @param plow a hozzáadandó hókotró
     */
    public void addPlow(SnowPlow plow) { plows.add(plow); plow.setOwner(this); }

    /**
     * Fejet vásárol egy hókotróra, levonva az árát az egyenlegből.
     * Ha nincs elegendő egyenleg, hibaüzenetet naplóz.
     *
     * @param plow    a felszerelni kívánt hókotró
     * @param newHead a megvásárolni kívánt fej
     */
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

    /**
     * Üzemanyagot vásárol egy hókotró fejébe, levonva a költséget az egyenlegből.
     * Ha nincs elegendő egyenleg vagy nincs fej, hibaüzenetet naplóz.
     *
     * @param plow     a feltöltendő hókotró
     * @param fuelType az üzemanyag típusa (ár meghatározásához)
     * @param amount   a feltöltendő mennyiség egységekben
     */
    public void buyFuel(SnowPlow plow, FuelType fuelType, int amount) {
        CleanerHead head = plow.getHead();
        if (head == null) {
            Logger.error(this, "A hokotrora nincs fej szerelve");
            return;
        }
        if (head.fuelCapacity() > 0 && head.fuelLevel() >= head.fuelCapacity()) {
            Logger.error(this, "Az uzemanyag tartaly tele van");
            return;
        }
        int cost = fuelType.getPrice() * amount;
        if (balance >= cost) {
            balance -= cost;
            head.refuel(amount);
            Logger.action(this, "Toltes sikeres, maradek egyenleg: " + balance);
        } else {
            Logger.error(this, "Nincs eleg penz a tolteshez");
        }
    }

    /**
     * Új hókotró vásárlása. Az új hókotró először az ugyanazon az úton lévő
     * szabad sávra kerül; ha nincs, a városban keres szabad sávot.
     * Ha sehol nincs szabad sáv, hibát naplóz, de a vásárlás sikeres.
     *
     * @param lastPlow a referencia hókotró (pozíció meghatározásához)
     * @param newName  az új hókotró szimbolikus neve
     * @param city     a város (szabad sáv kereséséhez)
     * @return az új hókotró, vagy {@code null} ha nincs elegendő egyenleg
     */
    public SnowPlow buyNewPlow(SnowPlow lastPlow, String newName, City city) {
        SnowPlow newPlow = new SnowPlow(this);
        Logger.register(newPlow, newName);
        int price = newPlow.getPrice();

        if (balance < price) {
            Logger.error(this, "Nincs eleg penz hokotro vasarlashoz");
            return null;
        }

        balance -= price;
        plows.add(newPlow);
        placePlow(newPlow, lastPlow, city);
        Logger.action(this, "vasarlas sikeres, uj hokotro letrejott, egyenleg: " + balance);
        return newPlow;
    }

    /**
     * Megkeresi az új hókotrónak megfelelő szabad sávot és ráállítja.
     * Előnyben részesíti az ugyanazon az úton lévő szabad sávokat,
     * majd a város többi szabad sávját.
     *
     * @param newPlow  az elhelyezendő hókotró
     * @param lastPlow a referencia hókotró (azonos útján keres először)
     * @param city     a város (fallback kereséshez)
     */
    private void placePlow(SnowPlow newPlow, SnowPlow lastPlow, City city) {
        Lane targetLane = findFreeOnSameRoad(lastPlow);

        /* Ha az úton nincs szabad sáv, a városban keres */
        if (targetLane == null && city != null) {
            targetLane = findFreeInCity(city);
        }

        if (targetLane != null) {
            newPlow.setCurrentLane(targetLane);
            targetLane.accept(newPlow);
        } else {
            Logger.error(this, "Nincs szabad sav az uj hokotronak a varosban!");
        }
    }

    /**
     * Megkeresi az első szabad sávot a referencia hókotró útján.
     *
     * @param lastPlow a referencia hókotró
     * @return szabad sáv, vagy {@code null} ha nincs
     */
    private Lane findFreeOnSameRoad(SnowPlow lastPlow) {
        if (lastPlow == null || lastPlow.getCurrentLane() == null) return null;
        Road road = lastPlow.getCurrentLane().getRoad();
        if (road == null) return null;
        for (Lane l : road.getLanes()) {
            if (l.getSnowPlow() == null) return l;
        }
        return null;
    }

    /**
     * Megkeresi az első szabad sávot a városban.
     *
     * @param city a keresési terület
     * @return szabad sáv, vagy {@code null} ha nincs
     */
    private Lane findFreeInCity(City city) {
        for (Node n : city.getNodes()) {
            for (Road r : n.getConnectedRoads()) {
                for (Lane l : r.getLanes()) {
                    if (l.getSnowPlow() == null) return l;
                }
            }
        }
        return null;
    }

    /**
     * Kifizetést fogad el (takarítás díja), növelve az egyenleget.
     *
     * @param amount a kapott összeg
     */
    public void receivePayment(int amount) {
        balance += amount;
        Logger.action(this, "penzt szerzett: " + amount + ", egyenleg: " + balance);
    }
}
