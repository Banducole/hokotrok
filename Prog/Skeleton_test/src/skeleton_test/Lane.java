package skeleton_test;

public class Lane {

    public Lane() {
        Skeleton.logCall("Lane::Lane()");
    }

    public Lane(String state) {
        Skeleton.logCall("Lane::Lane(" + state + ")");
    }

    public void addSnow(int amount) {
        Skeleton.logCall("Lane::addSnow(" + amount + ")");
        Skeleton.enter();

        boolean hasSalt = Skeleton.askBool("Van-e só a sávon? (saltCountdown > 0)");
        if (hasSalt) {
            Skeleton.logCall("Lane::saltCountdown--");
            Skeleton.exit();
            return;
        }

        // Állapot megkérdezése és átadás a state-nek
        LaneState state = new LaneState();
        state.onSnowAdded(this, amount);

        Skeleton.exit();
    }

    public void registerPassage(Vehicle vehicle) {
        Skeleton.logCall("Lane::registerPassage(vehicle)");
        Skeleton.enter();

        boolean threshold = Skeleton.askBool("Elérte a passageCount a küszöbértéket?");
        if (threshold) {
            formIce();
        }

        Skeleton.exit();
    }

    public void formIce() {
        Skeleton.logCall("Lane::formIce()");
        Skeleton.enter();

        LaneState state = new LaneState();
        state.onIceFormed(this);

        Skeleton.exit();
    }

    public boolean accept(Vehicle vehicle) {
        Skeleton.logCall("Lane::accept(vehicle)");
        Skeleton.enter();

        registerPassage(vehicle);

        Skeleton.exit();
        return true;
    }

    public boolean acceptPlow(SnowPlow plow) {
        Skeleton.logCall("Lane::acceptPlow(plow)");
        Skeleton.enter();

        cleanWith(plow);

        Skeleton.exit();
        return true;
    }

    public void cleanWith(SnowPlow plow) {
        Skeleton.logCall("Lane::cleanWith(plow)");
        Skeleton.enter();

        Head head = plow.getHead();
        boolean operational = head.isOperational();
        if (!operational) {
            Skeleton.exit();
            return;
        }

        head.clean(this);

        CleanerPlayer owner = plow.getOwner();
        owner.receivePayment(0);

        Skeleton.exit();
    }

    public void applySalt() {
        Skeleton.logCall("Lane::applySalt()");
        Skeleton.enter();

        LaneState state = new LaneState();
        state.onSaltApplied(this);

        Skeleton.exit();
    }

    public boolean isPassable() {
        Skeleton.logCall("Lane::isPassable()");
        boolean result = Skeleton.askBool("A sáv járható?");
        Skeleton.logReturn(String.valueOf(result));
        return result;
    }

    public boolean isSlipperyRoadWithVehicles() {
        Skeleton.logCall("Lane::slipperyRoadWithVehicles()");
        boolean result = Skeleton.askBool("A sáv csúszós és van rajta másik jármű?");
        Skeleton.logReturn(String.valueOf(result));
        return result;
    }

    public LaneState getState() {
        Skeleton.logCall("Lane::getState()");
        return new LaneState();
    }

    public void setState(LaneState state) {
        Skeleton.logCall("Lane::setState(" + state.getName() + ")");
    }

    public void setBlocked(int turns) {
        Skeleton.logCall("Lane::setBlocked(" + turns + ")");
    }
}
