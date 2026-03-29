package skeleton;

/**
 * Egy utcaszakasz egyetlen sávját reprezentáló osztály.
 * A sávok állapotát most már dummy objektumokkal szimuláljuk a szkeleton fázisban.
 */
public class Lane {
    
    // KIVÉVE: private LaneState state = new ClearState();

    public boolean isPassable() {
        SkeletonHelper.enterMethod("Lane.IsPassable()");
        boolean result = SkeletonHelper.askQuestion("A következő sáv járható?");
        SkeletonHelper.exitMethod("Lane.IsPassable()");
        return result;
    }

    public void accept(Vehicle vehicle) {
        SkeletonHelper.enterMethod("Lane.Accept(Vehicle)");
        
        registerPassage(vehicle);
        boolean slipperyAndOccupied = slipperyRoadWithVehicles();
        
        if (slipperyAndOccupied) {
            System.out.println("  [LOG] Jármű megcsúszott és ütközött!");
            vehicle.setBlocked(1); 
        }
        
        SkeletonHelper.exitMethod("Lane.Accept(Vehicle)");
    }

    public boolean slipperyRoadWithVehicles() {
        SkeletonHelper.enterMethod("Lane.SlipperyRoadWithVehicles()");
        boolean result = SkeletonHelper.askQuestion("A sáv csúszós és van rajta másik jármű?");
        SkeletonHelper.exitMethod("Lane.SlipperyRoadWithVehicles()");
        return result;
    }
    
    public void accept(SnowPlow sp) {
        SkeletonHelper.enterMethod("Lane.Accept(SnowPlow)");
        cleanWith(sp);
        SkeletonHelper.exitMethod("Lane.Accept(SnowPlow)");
    }

    public void cleanWith(SnowPlow sp) {
        SkeletonHelper.enterMethod("Lane.CleanWith(SnowPlow)");
        
        Head head = sp.getHead();
        if (head.isOperational()) {
            head.clean(this);
            
            boolean needToPay = SkeletonHelper.askQuestion("A takarítás sikeres volt, jár érte fizetség?");
            if (needToPay) {
                CleanerPlayer owner = sp.getOwner();
                owner.receivePayment(100);
            }
        }
        
        SkeletonHelper.exitMethod("Lane.CleanWith(SnowPlow)");
    }

    public void getState() {
        SkeletonHelper.enterMethod("Lane.GetState()");
        SkeletonHelper.exitMethod("Lane.GetState()");
    }

    public void setState(String state) {
        SkeletonHelper.enterMethod("Lane.SetState(" + state + ")");
        System.out.println("  [LOG] Sáv állapota megváltozott: " + state);
        SkeletonHelper.exitMethod("Lane.SetState(" + state + ")");
    }
    
    public void setState(LaneState newState) {
        SkeletonHelper.enterMethod("Lane.SetState(" + newState.getClass().getSimpleName() + ")");
        // Nincs értékadás, nem mentjük el az állapotot!
        SkeletonHelper.exitMethod("Lane.SetState(" + newState.getClass().getSimpleName() + ")");
    }

    public void addSnow(int amount) {
        SkeletonHelper.enterMethod("Lane.AddSnow(" + amount + ")");
        
        boolean hasSalt = SkeletonHelper.askQuestion("Van-e aktív só a sávon?");
        if (!hasSalt) {
            // A szekvencia fenntartásához lokális dummy állapotot használunk
            LaneState dummyState = new ClearState();
            dummyState.onSnowAdded(this, amount);
        } else {
            System.out.println("  [LOG] A só felolvasztotta a havat, az állapot nem változott.");
        }
        
        SkeletonHelper.exitMethod("Lane.AddSnow(" + amount + ")");
    }

    public void formIce() {
        SkeletonHelper.enterMethod("Lane.FormIce()");
        // Lokális dummy állapot a hívási lánchoz
        LaneState dummyState = new ThinSnowState();
        dummyState.onIceFormed(this);
        SkeletonHelper.exitMethod("Lane.FormIce()");
    }

    public void registerPassage(Vehicle vehicle) {
        SkeletonHelper.enterMethod("Lane.RegisterPassage(Vehicle)");
        
        boolean thresholdReached = SkeletonHelper.askQuestion("Elérte az áthaladások száma a jégképződési küszöbértéket?");
        if (thresholdReached) {
            formIce();
        }
        
        SkeletonHelper.exitMethod("Lane.RegisterPassage(Vehicle)");
    }
    
    public void applySalt() {
        SkeletonHelper.enterMethod("Lane.ApplySalt()");
        System.out.println("  [LOG] state.OnSaltApplied(lane) meghívódik.");
        boolean melted = SkeletonHelper.askQuestion("A jég teljesen felolvadt a sótól?");
        if (melted) {
            setState(new ClearState());
        }
        SkeletonHelper.exitMethod("Lane.ApplySalt()");
    }

    public void setBlocked(int turns) {
        SkeletonHelper.enterMethod("Lane.SetBlocked(" + turns + ")");
        SkeletonHelper.exitMethod("Lane.SetBlocked(" + turns + ")");
    }
}