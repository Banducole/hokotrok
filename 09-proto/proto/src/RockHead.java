public class RockHead extends CleanerHead {
    private int rockAmount = 0;
    private final int rockLimit = Constants.ROCK_HEAD_CAPACITY;

    public RockHead() { this.price = Constants.PRICE_ROCK_HEAD; }

    @Override
    public void clean(Lane lane) {
        SnowPlow plow = lane.getSnowPlow();
        if (isOperational()) {
            lane.applyRock();
            rockAmount--;
            Logger.action(plow, "Zuzalek szorasa sikeres, maradek Rock: " + rockAmount);
        } else {
            Logger.error(plow, "Nincs elegendo Rock a takaritashoz");
        }
    }

    @Override public boolean isOperational() { return rockAmount > 0; }
    @Override
    public void refuel(int amount) {
        rockAmount = Math.min(rockAmount + amount, rockLimit);
    }
    @Override public String fuelKind() { return "Rock"; }
    @Override public int    fuelLevel() { return rockAmount; }
}
