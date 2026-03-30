package skeleton;

/**
 * Egy utcaszakasz egyetlen sávját reprezentáló osztály.
 * A sávok rendelkeznek állapotokkal (pl. tiszta, vékony hó, jég),
 * amelyek befolyásolják a járművek haladását.
 */
public class Lane {
    
    /**
     * Ellenőrzi, hogy a sáv járható-e a normál járművek számára.
     * @return Igaz, ha a sávra rá lehet hajtani.
     */
    public boolean isPassable() {
        SkeletonHelper.enterMethod("Lane.IsPassable()");
        boolean result = SkeletonHelper.askQuestion("A következő sáv járható?");
        SkeletonHelper.exitMethod("Lane.IsPassable()");
        return result;
    }

    /**
     * Elfogadja a sávra rálépő járművet (legyen az autó vagy busz) és regisztrálja az áthaladást.
     * @param vehicle A sávra érkező jármű.
     */
    public void accept(Vehicle vehicle) {
        SkeletonHelper.enterMethod("Lane.Accept(Vehicle)");
        
        registerPassage(vehicle);
        boolean slipperyAndOccupied = slipperyRoadWithVehicles();
        
        if (slipperyAndOccupied) {
            System.out.println("  [LOG] Jármű megcsúszott és ütközött!");
            // Ha ütközik, mindkét jármű blokkolódik
            vehicle.setBlocked(1); 
        }
        
        SkeletonHelper.exitMethod("Lane.Accept(Vehicle)");
    }


    /**
     * Ellenőrzi, hogy a sáv csúszós-e és van-e rajta másik jármű, amivel ütközni lehet.
     * @return Igaz, ha ütközés történik.
     */
    public boolean slipperyRoadWithVehicles() {
        SkeletonHelper.enterMethod("Lane.SlipperyRoadWithVehicles()");
        boolean result = SkeletonHelper.askQuestion("A sáv csúszós és van rajta másik jármű?");
        SkeletonHelper.exitMethod("Lane.SlipperyRoadWithVehicles()");
        return result;
    }
    
 // --- Új metódusok a Lane osztályba ---

    /**
     * Elfogadja a sávra rálépő hókotrót.
     * @param sp A sávra érkező hókotró.
     */
    public void accept(SnowPlow sp) {
        SkeletonHelper.enterMethod("Lane.Accept(SnowPlow)");
        //cleanWith(sp);
        SkeletonHelper.exitMethod("Lane.Accept(SnowPlow)");
    }

    /**
     * Meghívja a hókotró takarítási logikáját a sávon.
     * @param sp A takarítást végző hókotró.
     */
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
    
 // --- Új attribútumok és metódusok a Lane osztályba ---
    private LaneState state = new ClearState(); // Alapértelmezett állapot

    /**
     * Beállítja a sáv új állapotát.
     * @param newState Az új állapot (pl. ThinSnowState).
     */
    public void setState(LaneState newState) {
        SkeletonHelper.enterMethod("Lane.SetState(" + newState.getClass().getSimpleName() + ")");
        this.state = newState;
        SkeletonHelper.exitMethod("Lane.SetState(" + newState.getClass().getSimpleName() + ")");
    }

    /**
     * Hó hozzáadása a sávhoz (időjárási esemény).
     * @param amount Hó mennyisége.
     */
    public void addSnow(int amount) {
        SkeletonHelper.enterMethod("Lane.AddSnow(" + amount + ")");
        
        boolean hasSalt = SkeletonHelper.askQuestion("Van-e aktív só a sávon?");
        if (!hasSalt) {
            state.onSnowAdded(this, amount);
        } else {
            System.out.println("  [LOG] A só felolvasztotta a havat, az állapot nem változott.");
        }
        
        SkeletonHelper.exitMethod("Lane.AddSnow(" + amount + ")");
    }

    /**
     * Jég képződése a sávon (letaposás miatt).
     */
    public void formIce() {
        SkeletonHelper.enterMethod("Lane.FormIce()");
        state.onIceFormed(this);
        SkeletonHelper.exitMethod("Lane.FormIce()");
    }

    /**
     * Regisztrálja egy jármű áthaladását, ami növelheti a hó letaposottságát.
     * @param vehicle Az áthaladó jármű.
     */
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