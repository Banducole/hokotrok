import java.util.ArrayList;
import java.util.List;

public class Lane {
    private LaneState state = new ClearState();
    private final List<Vehicle> vehicles = new ArrayList<>();
    private SnowPlow snowPlow = null;
    private int saltCountdown = 0;
    private int passageCount = 0;
    private boolean rocky = false;
    private Road road;

    public void accept(Vehicle v) {
        if (state.isPassable()) {
            vehicles.add(v);
            registerPassage(v);
        }
    }

    public void accept(SnowPlow s) {
        if (snowPlow != null && snowPlow != s) {
            Logger.error(this, "A savon mar van hokotro");
        } else {
            snowPlow = s;
        }
    }

    public void addSnow(int amount) {
        if (saltCountdown > 0) {
            saltCountdown--;
            Logger.action(this, amount + " egyseg ho esett, de a so azonnal felolvasztotta");
            return;
        }
        state.onSnowAdded(this, amount);
        Logger.action(this, amount + " egyseg ho esett, Allapot: " + state.getClass().getSimpleName());
        if (rocky && state instanceof ThickSnowState) {
            rocky = false;
            Logger.action(this, "a ho betemette a zuzalekot, rocky=false");
        }
    }

    public void applyRock() {
        rocky = true;
        Logger.action(this, "zuzalek szorva, rocky=true");
    }

    public void applySalt() {
        saltCountdown = Constants.SALT_COUNTDOWN_INIT;
        state.onSaltApplied(this);
    }

    public void formIce() {
        state.onIceFormed(this);
    }

    public boolean isPassable() {
        if (!state.isPassable()) return false;
        for (Vehicle v : vehicles) {
            if (v.isBlocked()) return false;
        }
        return true;
    }

    public boolean isSlippery() {
        if (rocky) return false;
        return state.isSlippery();
    }

    public void registerPassage(Vehicle v) {
        passageCount++;
        if (passageCount >= Constants.PASSAGE_THRESHOLD) {
            formIce();
            passageCount = 0;
        }
    }

    public void cleanWith(SnowPlow plow) {
        if (plow.getHead() != null) plow.getHead().clean(this);
    }

    public void setState(LaneState newState) { this.state = newState; }
    public LaneState getState() { return state; }
    public Road getRoad() { return road; }
    public void setRoad(Road road) { this.road = road; }
    public List<Vehicle> getVehicles() { return vehicles; }
    public void removeVehicle(Vehicle v) { vehicles.remove(v); }
    public SnowPlow getSnowPlow() { return snowPlow; }
    public void setSnowPlow(SnowPlow s) { this.snowPlow = s; }
    public boolean isRocky() { return rocky; }
    public void setRocky(boolean r) { this.rocky = r; }
    public int getSaltCountdown() { return saltCountdown; }
    public int getPassageCount() { return passageCount; }
}
