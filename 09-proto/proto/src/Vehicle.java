public abstract class Vehicle {
    protected Lane currentLane;
    protected int blockedTurns = 0;

    public abstract void step(boolean random);

    public void meetVehicle(Vehicle other) {
        this.blockedTurns  = Constants.BLOCK_TURNS_ON_COLLISION;
        other.blockedTurns = Constants.BLOCK_TURNS_ON_COLLISION;
        Logger.action(this,  "Utkozes tortent, a jarmu blokkolodott");
        Logger.action(other, "Utkozes tortent, a jarmu blokkolodott");
    }

    public boolean isBlocked()    { return blockedTurns > 0; }
    public Lane    getCurrentLane(){ return currentLane; }
    public void    setCurrentLane(Lane l){ this.currentLane = l; }
    public int     getBlockedTurns() { return blockedTurns; }
    public void    setBlockedTurns(int n) { this.blockedTurns = n; }
    public void    decrementBlock() { if (blockedTurns > 0) blockedTurns--; }

    public boolean switchPassableLane() {
        Road road = currentLane.getRoad();
        if (road == null) return false;
        for (Lane lane : road.getLanes()) {
            if (lane != currentLane && lane.isPassable()) {
                currentLane.removeVehicle(this);
                currentLane = lane;
                lane.getVehicles().add(this); // bypass passage registration
                return true;
            }
        }
        return false;
    }
}
