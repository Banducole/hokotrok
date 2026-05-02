import java.util.List;

public class Car extends Vehicle {
    private Home home;
    private Workplace workplace;
    private final PathFinder pathFinder;
    private Node currentTarget;

    public Car(City city) {
        this.pathFinder = new PathFinder(city);
    }

    public void setHome(Home home) { this.home = home; }
    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
        if (currentTarget == null) currentTarget = workplace;
    }
    public Home getHome() { return home; }
    public Workplace getWorkplace() { return workplace; }

    @Override
    public void step(boolean random) {
        if (isBlocked()) {
            decrementBlock();
            Logger.action(this, "blokkolt, nem tud lepni, maradek blokk: " + blockedTurns);
            return;
        }

        // If current lane is impassable (e.g. ThickSnow before us), try to switch to a parallel lane
        if (currentLane != null && !currentLane.getState().isPassable()) {
            if (switchPassableLane()) {
                Logger.action(this, "Akadalyt kikerulte, savot valtott, uj pozicio: " + Logger.name(currentLane));
                // After switching, still check slipperiness on the new lane
                if (currentLane.isSlippery()) {
                    boolean collide = !random;
                    if (random) collide = Math.random() < 0.5;
                    if (collide) {
                        Logger.action(this, "megcsuszott a csuszos savon");
                        for (Vehicle other : currentLane.getVehicles()) {
                            if (other != this) {
                                meetVehicle(other);
                                return;
                            }
                        }
                    }
                }
                return;
            } else {
                blockedTurns = 1;
                Logger.action(this, "Elakadt, nincs jarhato szomszed sav");
                return;
            }
        }

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
            if (!switchPassableLane()) {
                blockedTurns = 1;
                Logger.action(this, "Elakadt, nincs jarhato szomszed sav");
                return;
            }
            Logger.action(this, "Akadalyt kikerulte, savot valtott, uj pozicio: " + Logger.name(currentLane));
        }

        // Slip + collision check
        if (currentLane.isSlippery()) {
            boolean collide = !random; // random=false -> deterministic collision
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

    public Lane getNextLane() {
        if (currentLane == null || currentLane.getRoad() == null) return null;
        Node arrivalNode = currentLane.getRoad().getTo();
        if (arrivalNode == null) return null;

        if (home != null && arrivalNode == home)              currentTarget = workplace;
        else if (workplace != null && arrivalNode == workplace) currentTarget = home;

        if (currentTarget == null) return null;
        List<Lane> path = pathFinder.getShortestPath(arrivalNode, currentTarget);
        if (path != null && !path.isEmpty()) return path.get(0);
        return null;
    }
}
