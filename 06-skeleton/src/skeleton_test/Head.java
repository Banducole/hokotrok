package skeleton_test;

public class Head {

    public Head() {
        Skeleton.logCall("Head::Head()");
    }

    public boolean isOperational() {
        Skeleton.logCall("Head::isOperational()");
        boolean result = Skeleton.askBool("A fej működőképes? (van elég üzemanyag)");
        Skeleton.logReturn(String.valueOf(result));
        return result;
    }

    public void clean(Lane lane) {
        Skeleton.logCall("Head::clean(lane)");
        Skeleton.enter();

        LaneState state = lane.getState();
        state.getName(); // állapot lekérdezés naplózáshoz
        lane.setState(new LaneState("ClearState"));

        Skeleton.exit();
    }

    public void refuel(int amount) {
        Skeleton.logCall("Head::refuel(" + amount + ")");
    }

    public int getPrice() {
        Skeleton.logCall("Head::getPrice()");
        int price = Skeleton.askInt("Mi a fej ára?");
        Skeleton.logReturn(String.valueOf(price));
        return price;
    }
}

// ─────────────────────────────────────────────────────────────────────────────

class SweepHead extends Head {

    public SweepHead() {
        Skeleton.logCall("SweepHead::SweepHead()");
    }

    @Override
    public void clean(Lane lane) {
        Skeleton.logCall("SweepHead::clean(lane)");
        Skeleton.enter();

        LaneState state = lane.getState();
        boolean pushable = state.canBePushed();

        if (pushable) {
            PathFinder pf = new PathFinder();
            Lane neighbor = pf.getNeighborLane();
            neighbor.addSnow(0);
            lane.setState(new LaneState("ClearState"));
        }

        Skeleton.exit();
    }

    @Override
    public boolean isOperational() {
        Skeleton.logCall("SweepHead::isOperational()");
        Skeleton.logReturn("true");
        return true;
    }
}

// ─────────────────────────────────────────────────────────────────────────────

class ThrowHead extends Head {

    public ThrowHead() {
        Skeleton.logCall("ThrowHead::ThrowHead()");
    }

    @Override
    public void clean(Lane lane) {
        Skeleton.logCall("ThrowHead::clean(lane)");
        Skeleton.enter();

        LaneState state = lane.getState();
        boolean pushable = state.canBePushed();

        if (pushable) {
            // hányó fej – szomszéd sáv nem érintett
            lane.setState(new LaneState("ClearState"));
        }

        Skeleton.exit();
    }

    @Override
    public boolean isOperational() {
        Skeleton.logCall("ThrowHead::isOperational()");
        Skeleton.logReturn("true");
        return true;
    }
}

// ─────────────────────────────────────────────────────────────────────────────

class IceBreakerHead extends Head {

    public IceBreakerHead() {
        Skeleton.logCall("IceBreakerHead::IceBreakerHead()");
    }

    @Override
    public void clean(Lane lane) {
        Skeleton.logCall("IceBreakerHead::clean(lane)");
        Skeleton.enter();

        LaneState state = lane.getState();
        boolean slippery = state.isSlippery();
        boolean pushable  = state.canBePushed();

        if (slippery && !pushable) {
            lane.setState(new LaneState("BrokenIceState"));
        }

        Skeleton.exit();
    }

    @Override
    public boolean isOperational() {
        Skeleton.logCall("IceBreakerHead::isOperational()");
        Skeleton.logReturn("true");
        return true;
    }
}

// ─────────────────────────────────────────────────────────────────────────────

class SaltHead extends Head {

    public SaltHead() {
        Skeleton.logCall("SaltHead::SaltHead()");
    }

    @Override
    public void clean(Lane lane) {
        Skeleton.logCall("SaltHead::clean(lane)");
        Skeleton.enter();

        boolean operational = isOperational();
        if (operational) {
            lane.applySalt();
            refuel(-1); // só csökken
        }

        Skeleton.exit();
    }

    @Override
    public boolean isOperational() {
        Skeleton.logCall("SaltHead::isOperational()");
        boolean result = Skeleton.askBool("Van elég só?");
        Skeleton.logReturn(String.valueOf(result));
        return result;
    }

    @Override
    public void refuel(int amount) {
        Skeleton.logCall("SaltHead::refuel(" + amount + ")  [salt += " + amount + "]");
    }
}

// ─────────────────────────────────────────────────────────────────────────────

class DragonHead extends Head {

    public DragonHead() {
        Skeleton.logCall("DragonHead::DragonHead()");
    }

    @Override
    public void clean(Lane lane) {
        Skeleton.logCall("DragonHead::clean(lane)");
        Skeleton.enter();

        boolean operational = isOperational();
        if (operational) {
            lane.setState(new LaneState("ClearState"));
            refuel(-1); // kerozin csökken
        }

        Skeleton.exit();
    }

    @Override
    public boolean isOperational() {
        Skeleton.logCall("DragonHead::isOperational()");
        boolean result = Skeleton.askBool("Van elég kerozin?");
        Skeleton.logReturn(String.valueOf(result));
        return result;
    }

    @Override
    public void refuel(int amount) {
        Skeleton.logCall("DragonHead::refuel(" + amount + ")  [kerosene += " + amount + "]");
    }
}
