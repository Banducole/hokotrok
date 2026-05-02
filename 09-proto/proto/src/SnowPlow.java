public class SnowPlow {
    private Lane currentLane;
    private Lane targetLane = null;
    private CleanerHead head;
    private CleanerPlayer owner;
    private final int price = Constants.PRICE_SNOWPLOW;

    public SnowPlow() {}
    public SnowPlow(CleanerPlayer owner) { this.owner = owner; }

    public void step() {
        if (targetLane != null) {
            if (currentLane != null) currentLane.setSnowPlow(null);
            currentLane = targetLane;
            currentLane.accept(this);
            targetLane = null;
        }
        if (head != null && head.isOperational()) {
            LaneState before = currentLane.getState();
            boolean wasNonClear = !(before instanceof ClearState) || currentLane.isRocky();
            head.clean(currentLane);
            LaneState after = currentLane.getState();
            // Pay only if something meaningful happened
            if (owner != null && wasNonClear && !sameKind(before, after)) {
                owner.receivePayment(Constants.PAYMENT_PER_LANE_CLEANED);
            } else if (owner != null && !wasNonClear) {
                Logger.action(this, "A sav mar tiszta, nincs takaritas, nincs fizetseg");
            } else if (head instanceof RockHead && currentLane.isRocky()) {
                // RockHead doesn't change state; reward modestly (or skip)
                if (owner != null) owner.receivePayment(Constants.PAYMENT_PER_LANE_CLEANED);
            } else if (head instanceof SaltHead) {
                // SaltHead may have changed state via applySalt; reward if it did
                if (owner != null && !sameKind(before, after)) {
                    owner.receivePayment(Constants.PAYMENT_PER_LANE_CLEANED);
                }
            }
        } else if (head != null) {
            Logger.error(this, "Nincs elegendo uzemanyag a takaritashoz");
        }
    }

    private boolean sameKind(LaneState a, LaneState b) {
        return a.getClass() == b.getClass();
    }

    public void changeHead(CleanerHead newHead) { this.head = newHead; }
    public Lane getCurrentLane() { return currentLane; }
    public void setCurrentLane(Lane l) { this.currentLane = l; }
    public CleanerHead getHead() { return head; }
    public void setTargetLane(Lane lane) { this.targetLane = lane; }
    public CleanerPlayer getOwner() { return owner; }
    public void setOwner(CleanerPlayer owner) { this.owner = owner; }
    public int getPrice() { return price; }
}
