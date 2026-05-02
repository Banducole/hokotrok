public abstract class Player {
    protected String name;
    public abstract void takeTurn();
    public String getName() { return name; }
    public void   setName(String n) { this.name = n; }
}
