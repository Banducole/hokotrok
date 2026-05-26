import java.util.List;

/**
 * Személyautó jármű, amely autonóm módon ingázik otthon és munkahely között.
 * <p>
 * A {@link PathFinder} BFS-keresésével határozza meg a következő sávot.
 * Ha a célsáv átjárhatatlan, megpróbál szomszéd sávra váltani; ha ez sem
 * sikerül, blokkolt állapotba kerül. Csúszós sávon ütközhet más járművel.
 * </p>
 */
public class Car extends Vehicle {

    /** Az autó lakóhelye (ingázás egyik végpontja). */
    private Home home;

    /** Az autó munkahelye (ingázás másik végpontja). */
    private Workplace workplace;

    /** BFS-alapú útvonalkereső a városon belül. */
    private final PathFinder pathFinder;

    /** Az aktuális úticél csomópont (home vagy workplace). */
    private Node currentTarget;

    /**
     * Létrehozza az autót a megadott városhoz kötött útvonalkeresővel.
     *
     * @param city a város, amelyen belül az autó közlekedik
     */
    public Car(City city) {
        this.pathFinder = new PathFinder();
    }

    /**
     * Beállítja az autó lakóhelyét.
     *
     * @param home a lakóhely csomópont
     */
    public void setHome(Home home) { this.home = home; }

    /**
     * Beállítja az autó munkahelyét és kezdeti úticélját (ha még nincs cél).
     *
     * @param workplace a munkahely csomópont
     */
    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
        if (currentTarget == null) currentTarget = workplace;
    }

    /** @return az autó lakóhelye */
    public Home getHome() { return home; }

    /** @return az autó munkahelye */
    public Workplace getWorkplace() { return workplace; }

    /**
     * Egy körlépés végrehajtása: blokkolt állapot kezelése, akadály megkerülése,
     * következő sávra lépés, csúszás és ütközés ellenőrzése.
     *
     * @param random ha {@code true}, a csúszás véletlen; ha {@code false}, determinisztikus
     */
    @Override
    public void step(boolean random) {
        if (isBlocked()) {
            decrementBlock();
            Logger.action(this, "blokkolt, nem tud lepni, maradek blokk: " + blockedTurns);
            return;
        }

        if (currentLane != null && currentLane.getState() instanceof ThickSnowState) {
            Logger.action(this, "Vastag ho miatt nem tud lepni");
            return;
        }

        /* Ha az aktuális sáv átjárhatatlan, próbáljon szomszéd sávra váltani */
        // if (currentLane != null && !currentLane.getState().isPassable()) {
        //     if (switchPassableLane()) {
        //         Logger.action(this, "Akadalyt kikerulte, savot valtott, uj pozicio: " + Logger.name(currentLane));
        //         checkSlip(random);
        //         return;
        //     } else {
        //         blockedTurns = 1;
        //         Logger.action(this, "Elakadt, nincs jarhato szomszed sav");
        //         return;
        //     }
        // }

        Lane nextLane = getNextLane();

        if (nextLane == null) {
            Logger.action(this, "Nincs ut a cel fele");
            return;
        }

        if (nextLane.isPassable()) {
            if (currentLane != null) currentLane.removeVehicle(this);
            nextLane.accept(this);
            currentLane = nextLane;
            Logger.action(this, "Sikeres lepes, uj pozicio: " + Logger.name(currentLane));
        } else {
            // if (!switchPassableLane()) {
                blockedTurns = 1;
                Logger.action(this, "Elakadt, nincs jarhato szomszed sav");
                return;
            // }
            // Logger.action(this, "Akadalyt kikerulte, savot valtott, uj pozicio: " + Logger.name(currentLane));
        }

        checkSlip(random);
    }

    /**
     * Ellenőrzi a csúszást és az esetleges ütközést az aktuális sávon.
     * Ha a sáv csúszós és az ütközés bekövetkezik, megkeresi az első
     * másik járművet és ütközési eseményt vált ki.
     *
     * @param random ha {@code true}, az ütközés véletlen (50%); ha {@code false}, biztos
     */
    private void checkSlip(boolean random) {
        if (currentLane.isSlippery()) {
            boolean collide = !random;
            if (random) collide = Math.random() < 0.5;
            if (collide) {
                Logger.action(this, "megcsuszott a csuszos savon");
                List<Vehicle> here = currentLane.getVehicles();
                for (Vehicle other : here) {
                    if (other != this) {
                        meetVehicle(other);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Meghatározza a következő sávot az útvonalkereső segítségével.
     * Ha az autó eléri az aktuális célt, megfordítja az irányt.
     *
     * @return a következő sáv, vagy {@code null} ha nincs elérhető útvonal
     */
    public Lane getNextLane() {
        if (currentLane == null || currentLane.getRoad() == null) {
            return null;
        }
        
        Node arrivalNode = currentLane.getRoad().getTo();
        if (arrivalNode == null) {
            return null;
        }

        /* Célcsere: ha megérkeztünk az egyik végponthoz, fordulunk */
        if (home != null && arrivalNode.equals(home)) {
            currentTarget = workplace;
        }
        else if (workplace != null && arrivalNode.equals(workplace)) {
            currentTarget = home;
            System.out.println("xdadsadasdadasdasdasdadsasdad");
        }

        if (currentTarget == null){
            return null;
        } 
        List<Lane> path = pathFinder.getShortestPath(arrivalNode, currentTarget);

        if (path != null && !path.isEmpty()){
            return path.get(0);
        }
        return null;
    }
}
