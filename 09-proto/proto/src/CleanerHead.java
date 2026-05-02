public abstract class CleanerHead {
    protected int price;

    public abstract void clean(Lane lane);
    public abstract boolean isOperational();

    public int getPrice() { return price; }

    /** Default no-op refuel - subclasses with consumables override. */
    public void refuel(int amount) {}

    /** Returns name of fuel type the head consumes, or "" for none. */
    public String fuelKind() { return ""; }
    public int   fuelLevel() { return 0; }
}
