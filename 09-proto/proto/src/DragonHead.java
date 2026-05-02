public class DragonHead extends CleanerHead {
    private int keroseneAmount = 0;

    public DragonHead() { this.price = Constants.PRICE_DRAGON_HEAD; }

    @Override
    public void clean(Lane lane) {
        SnowPlow plow = lane.getSnowPlow();
        if (isOperational()) {
            lane.setRocky(false);
            lane.setState(new ClearState());
            keroseneAmount--;
            Logger.action(plow, "Sarkanyfej takaritott. Maradek Kerosene: " + keroseneAmount);
            Logger.action(lane, "allapota ClearState-re valtozott");
        } else {
            Logger.error(plow, "Nincs elegendo Kerosene a takaritashoz");
        }
    }

    @Override public boolean isOperational() { return keroseneAmount > 0; }
    @Override public void refuel(int amount) { keroseneAmount += amount; }
    @Override public String fuelKind() { return "Kerosene"; }
    @Override public int    fuelLevel() { return keroseneAmount; }
}
