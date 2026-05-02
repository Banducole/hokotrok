public class SaltHead extends CleanerHead {
    private int saltAmount = 0;

    public SaltHead() { this.price = Constants.PRICE_SALT_HEAD; }

    @Override
    public void clean(Lane lane) {
        SnowPlow plow = lane.getSnowPlow();
        if (isOperational()) {
            lane.applySalt();
            saltAmount--;
            Logger.action(plow, "sozva, olvadasi folyamat elindult, maradek so: " + saltAmount);
        } else {
            Logger.error(plow, "Nincs elegendo Salt a takaritashoz");
        }
    }

    @Override public boolean isOperational() { return saltAmount > 0; }
    @Override public void refuel(int amount) { saltAmount += amount; }
    @Override public String fuelKind() { return "Salt"; }
    @Override public int    fuelLevel() { return saltAmount; }
}
