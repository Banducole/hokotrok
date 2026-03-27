package skeleton_test;

public class Vehicle {

    public Vehicle() {
        Skeleton.logCall("Vehicle::Vehicle()");
    }

    public void step() {
        Skeleton.logCall("Vehicle::step()");
        Skeleton.enter();

        boolean blocked = isBlocked();
        if (blocked) {
            decrementBlock();
            Skeleton.exit();
            return;
        }

        PathFinder pf = new PathFinder();
        Lane nextLane = pf.getShortestPath();

        boolean passable = nextLane.isPassable();
        if (passable) {
            nextLane.accept(this);

            boolean slippery = nextLane.isSlipperyRoadWithVehicles();
            if (slippery) {
                meetVehicle();
            }
        } else {
            Lane neighbor = pf.switchPassableLane(nextLane);
            if (neighbor != null) {
                neighbor.accept(this);

                boolean slippery = neighbor.isSlipperyRoadWithVehicles();
                if (slippery) {
                    meetVehicle();
                }
            } else {
                setBlocked(1);
            }
        }

        Skeleton.exit();
    }

    public void meetVehicle() {
        Skeleton.logCall("Vehicle::meetVehicle()");
        Skeleton.enter();

        // mindkét jármű blokkol
        setBlocked(1);
        Vehicle other = new Vehicle();
        other.setBlocked(1);

        Skeleton.exit();
    }

    public boolean isBlocked() {
        Skeleton.logCall("Vehicle::isBlocked()");
        boolean result = Skeleton.askBool("A jármű blokkolt? (blockedTurns > 0)");
        Skeleton.logReturn(String.valueOf(result));
        return result;
    }

    public void decrementBlock() {
        Skeleton.logCall("Vehicle::decrementBlock()  [blockedTurns--]");
    }

    public void setBlocked(int turns) {
        Skeleton.logCall("Vehicle::setBlocked(" + turns + ")  [blockedTurns=" + turns + "]");
    }
}

// ─────────────────────────────────────────────────────────────────────────────

class Car extends Vehicle {

    public Car() {
        Skeleton.logCall("Car::Car()");
    }
}

// ─────────────────────────────────────────────────────────────────────────────

class Bus extends Vehicle {

    public Bus() {
        Skeleton.logCall("Bus::Bus()");
    }

    @Override
    public void step() {
        Skeleton.logCall("Bus::step()");
        Skeleton.enter();

        boolean blocked = isBlocked();
        if (blocked) {
            decrementBlock();
            Skeleton.exit();
            return;
        }

        Lane nextLane = new PathFinder().getShortestPath();
        boolean passable = nextLane.isPassable();

        if (passable) {
            nextLane.accept(this);

            boolean slippery = nextLane.isSlipperyRoadWithVehicles();
            if (slippery) {
                meetVehicle();
                Skeleton.exit();
                return;
            }

            boolean routeComplete = isRouteComplete();
            if (routeComplete) {
                Terminal t = new Terminal();
                t.notifyArrival(this);
            }
        } else {
            setBlocked(1);
        }

        Skeleton.exit();
    }

    public boolean isRouteComplete() {
        Skeleton.logCall("Bus::isRouteComplete()");
        boolean result = Skeleton.askBool("A busz teljesítette az útvonalat?");
        Skeleton.logReturn(String.valueOf(result));
        return result;
    }

    public void arrivedAtTerminal() {
        Skeleton.logCall("Bus::arrivedAtTerminal()  [completedRounds++]");
    }
}
