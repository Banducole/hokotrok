package skeleton;

/**
 * Egy utcaszakasz egyetlen sávját reprezentáló osztály.
 * A sávok felelősek a járművek fogadásáért, a forgalom nyilvántartásáért,
 * valamint az útviszonyok (hó, jég, só) kezeléséért.
 * A sávok állapotát most már dummy objektumokkal szimuláljuk a szkeleton fázisban.
 */
public class Lane {
    
    /**
     * Lekérdezi, hogy a sáv normál járművek (pl. Car, Bus) számára járható-e.
     *
     * @return true, ha a sáv járható (nincs vastag hó vagy akadály).
     */
    public boolean isPassable() {
        SkeletonHelper.enterMethod("Lane.IsPassable()");
        boolean result = SkeletonHelper.askQuestion("A következő sáv járható?");
        SkeletonHelper.exitMethod("Lane.IsPassable()");
        return result;
    }

    /**
     * Fogad egy normál járművet a sávon. Regisztrálja az áthaladást, és ellenőrzi, 
     * hogy az útviszonyok miatt megcsúszik-e rajta a jármű, okozva ezzel ütközést.
     *
     * @param vehicle Az a jármű, ami rálépett a sávra.
     */
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

    /**
     * Megvizsgálja, hogy a sáv csúszós-e, és tartózkodik-e rajta másik jármű,
     * amivel a frissen belépő jármű ütközhet.
     *
     * @return true, ha csúszás és ütközés történik.
     */
    public boolean slipperyRoadWithVehicles() {
        SkeletonHelper.enterMethod("Lane.SlipperyRoadWithVehicles()");
        
        boolean isSlippery = SkeletonHelper.askQuestion("A sáv csúszós állapotú?");
        boolean willCrash = false;
        
        if (isSlippery) {
            willCrash = SkeletonHelper.askQuestion("Történik ütközés egy másik járművel?");
        }
        
        SkeletonHelper.exitMethod("Lane.SlipperyRoadWithVehicles()");
        
        return isSlippery && willCrash;
    }
    
    /**
     * Fogad egy hókotrót (SnowPlow) a sávon, ami azonnal el is kezdi a takarítást.
     * A hókotró számára minden sáv járható.
     *
     * @param sp A belépő hókotró jármű.
     */
    public void accept(SnowPlow sp) {
        SkeletonHelper.enterMethod("Lane.Accept(SnowPlow)");
        //cleanWith(sp);
        SkeletonHelper.exitMethod("Lane.Accept(SnowPlow)");
    }

    /**
     * Meghívja a hókotró takarítófejének (Head) működését az aktuális sávon.
     * Ha a fej működőképes, elvégzi a takarítást, majd a szkeleton rákérdez,
     * hogy jár-e érte fizetség a játékosnak.
     *
     * @param sp A takarítást végző hókotró.
     */
    public void cleanWith(SnowPlow sp) {
        SkeletonHelper.enterMethod("Lane.CleanWith(SnowPlow)");
        
        CleanerHead head = sp.getHead();
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

    /**
     * Lekérdezi a sáv aktuális állapotát (állapot objektumot).
     */
    public LaneState getState() {
        SkeletonHelper.enterMethod("Lane.GetState()");
        
        String input = SkeletonHelper.askString("Milyen állapotban van a sáv? [c=clear, tn=thin, tk=thick, i=icy, b=broken]:");
        LaneState dummyState;
        
        switch (input) {
            case "tn":
            case "thin":
                dummyState = new ThinSnowState();
                break;
            case "tk":
            case "thick":
                dummyState = new ThickSnowState();
                break;
            case "i":
            case "icy":
                dummyState = new IcyState();
                break;
            case "b":
            case "broken":
                dummyState = new BrokenIceState();
                break;
            case "c":
            case "clear":
            default:
                dummyState = new ClearState();
                break;
        }
        
        SkeletonHelper.exitMethod("Lane.GetState()");
        return dummyState;
    }

    /**
     * Beállítja a sáv állapotát egy adott string alapú azonosító alapján (szimulációs célból).
     *
     * @param state Az új állapot neve.
     */
    public void setState(String state) {
        SkeletonHelper.enterMethod("Lane.SetState(" + state + ")");
        System.out.println("  [LOG] Sáv állapota megváltozott: " + state);
        SkeletonHelper.exitMethod("Lane.SetState(" + state + ")");
    }
    
    /**
     * Átállítja a sáv fizikai állapotát (LaneState) egy új objektumra a State minta alapján.
     * A szkeletonban csak az állapotváltás tényét logoljuk.
     *
     * @param newState Az új állapotobjektum (pl. ThickSnowState).
     */
    public void setState(LaneState newState) {
        SkeletonHelper.enterMethod("Lane.SetState(" + newState.getClass().getSimpleName() + ")");
        // Nincs értékadás, nem mentjük el az állapotot!
        SkeletonHelper.exitMethod("Lane.SetState(" + newState.getClass().getSimpleName() + ")");
    }

    /**
     * Hó hozzáadása a sávhoz (havazás vagy takarítás általi áttolás miatt).
     * Ha a sáv le van sózva, a hó elolvad.
     *
     * @param amount A leesett/áttolt hó mennyisége.
     */
    public void addSnow(int amount) {
        SkeletonHelper.enterMethod("Lane.AddSnow(" + amount + ")");
        
        boolean hasSalt = SkeletonHelper.askQuestion("Van-e aktív só a sávon?");
        if (!hasSalt) {
            // A szekvencia fenntartásához lokális dummy állapotot használunk
            LaneState dummyState = this.getState();
            dummyState.onSnowAdded(this, amount);
        } else {
            System.out.println("  [LOG] A só felolvasztotta a havat, az állapot nem változott.");
        }
        
        SkeletonHelper.exitMethod("Lane.AddSnow(" + amount + ")");
    }

    /**
     * Jeget képez a sávon. Ezt a letaposott vékony hó állapota váltja ki.
     */
    public void formIce() {
        SkeletonHelper.enterMethod("Lane.FormIce()");
        // Lokális dummy állapot a hívási lánchoz
        LaneState dummyState = new ThinSnowState();
        dummyState.onIceFormed(this);
        SkeletonHelper.exitMethod("Lane.FormIce()");
    }

    /**
     * Regisztrálja egy jármű áthaladását a sávon. 
     * Megkérdezi a felhasználót, hogy az áthaladások száma elérte-e a küszöböt a jégképződéshez.
     *
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
    
    /**
     * Sót alkalmaz a sávon, ami elkezdi felolvasztani a jeget és a havat.
     * Ha a jég teljesen felolvad, a sáv tiszta (ClearState) állapotba kerül.
     */
    public void applySalt() {
        SkeletonHelper.enterMethod("Lane.ApplySalt()");
        System.out.println("  [LOG] state.OnSaltApplied(lane) meghívódik.");
        boolean melted = SkeletonHelper.askQuestion("A jég teljesen felolvadt a sótól?");
        if (melted) {
            setState(new ClearState());
        }
        SkeletonHelper.exitMethod("Lane.ApplySalt()");
    }

    /**
     * Blokkolja a sávot adott ideig (járműves ütközés miatt).
     *
     * @param turns A blokkolás ideje körökben.
     */
    public void setBlocked(int turns) {
        SkeletonHelper.enterMethod("Lane.SetBlocked(" + turns + ")");
        SkeletonHelper.exitMethod("Lane.SetBlocked(" + turns + ")");
    }
}