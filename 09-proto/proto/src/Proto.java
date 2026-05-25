import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * A prototipus fo belepepontja es parancsertelmezo osztaly.
 * <p>
 * Soronkent olvassa a standard bemenetet, es minden sort egy jatekbeli
 * paranccsal felelteti meg (pl. {@code Csomopont}, {@code Ut}, {@code Auto_Lep}).
 * Az osszes letrehozott objektumot egy kozponti nyilvantartasban ({@code registry})
 * tarolia, ahonnan nev szerint lekerdezhetok. A {@link Game} peldany
 * fogja ossze a varost es a jatekosokat.
 * </p>
 */
public class Proto {

    /** A jatek allapotot tartalmazo fo objektum. */
    private final Game game = new Game();

    /**
     * Szimbolikus nev -&gt; objektum lekepezés a tesztszkriptbeli azonositokhoz.
     * Minden letrehozott entitas (csomopont, ut, sav, jarmu, hokotro, jatekos) itt regisztralodik.
     */
    private final Map<String, Object> registry = new HashMap<>();

    /**
     * A program belepepontja: letrehoz egy {@link Proto} peldanyt es futtatja.
     *
     * @param args parancssori argumentumok (nem hasznaltak)
     * @throws Exception bemenet-olvasasi hiba eseten
     */
    public static void main(String[] args) throws Exception {
        new Proto().run();
    }

    /**
     * A fo futtatasi hurok: soronkent olvassa a standard bemenetet,
     * kihagyja az ures sorokat es a kommenteket (#), es feldolgozza a parancsokat.
     *
     * @throws Exception bemenet-olvasasi hiba eseten
     */
    public void run() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = br.readLine()) != null) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
            try {
                handle(trimmed);
            } catch (Exception e) {
                Logger.error("Parancs feldolgozasi hiba: " + trimmed + " (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")");
            }
        }
    }

    /**
     * Egy parancssor feldolgozasa: tokenekre bontja, es a parancs neve alapjan
     * a megfelelo kezelomedtodushoz iranyitja.
     *
     * @param line a feldolgozando parancssor (nem ures, nem komment)
     */
    private void handle(String line) {
        String[] tok = line.split("\\s+");
        String cmd = tok[0];
        switch (cmd) {
            case "Csomopont":      doCsomopont(tok); break;
            case "Ut":             doUt(tok); break;
            case "Sav":            doSav(tok); break;
            case "Jarmu":          doJarmu(tok); break;
            case "Hokotro":        doHokotro(tok); break;
            case "Kapcsol":        doKapcsol(tok); break;
            case "Hozzaad_Sav":    doHozzaadSav(tok); break;
            case "Raallit":        doRaallit(tok); break;
            case "Felszerel":      doFelszerel(tok); break;
            case "Toltes":         doToltes(tok); break;
            case "Allapot":        doAllapot(tok); break;
            case "Uticel_Auto":    doUticelAuto(tok); break;
            case "Uticel_Busz":    doUticelBusz(tok); break;
            case "Ho":             doHo(tok); break;
            case "Hokotro_Lep":    doHokotroLep(tok); break;
            case "Auto_Lep":       doAutoLep(tok); break;
            case "Busz_Lep":       doBuszLep(tok); break;
            case "Allapot_Auto":   doAllapotAuto(tok); break;
            case "Takarito":       doTakarito(tok); break;
            case "Buszsofor":      doBuszsofor(tok); break;
            case "Vesz_Fej":       doVeszFej(tok); break;
            case "Vesz_Hokotro":   doVeszHokotro(tok); break;
            case "Stat":           doStat(tok); break;
            default: Logger.error("Ismeretlen parancs: " + line);
        }
    }

    // ---------- segedmetodusok ----------

    /**
     * Visszaad egy regisztralt objektumot a megadott nevvel es tipussal.
     * Hibanal RuntimeException-t dob, amelyet a hivo elkaphat.
     *
     * @param <T>   a vart tipus
     * @param name  a keresett szimbolikus nev
     * @param klass a vart Java osztaly
     * @return a megtalalt objektum
     * @throws RuntimeException ha az objektum nem letezik vagy tipushiba van
     */
    @SuppressWarnings("unchecked")
    private <T> T get(String name, Class<T> klass) {
        Object o = registry.get(name);
        if (o == null) throw new RuntimeException("Nem letezo objektum: " + name);
        if (!klass.isInstance(o)) throw new RuntimeException("Tipushiba: " + name + " nem " + klass.getSimpleName());
        return (T) o;
    }

    /**
     * Regisztralja az objektumot a nyilvantartasban es a Loggerben.
     *
     * @param name a szimbolikus nev
     * @param obj  a tarolando objektum
     */
    private void put(String name, Object obj) {
        registry.put(name, obj);
        Logger.register(obj, name);
    }

    // ---------- parancskezelo metodusok ----------

    /**
     * {@code Csomopont <nev> <tipus>} – letrehoz egy csompontot (Home, Workplace,
     * Intersection vagy Terminal) es hozzaadja a varoshoz.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=nev, [2]=tipus
     */
    private void doCsomopont(String[] t) {
        String name = t[1], type = t[2];
        Node n;
        switch (type) {
            case "Intersection": n = new Intersection(); break;
            case "Terminal":     n = new Terminal();     break;
            case "Home":         n = new Home();         break;
            case "Workplace":    n = new Workplace();    break;
            default: Logger.error("Ismeretlen csomopont tipus: " + type); return;
        }
        put(name, n);
        game.getCity().addNode(n);
        Logger.action(n, "letrejott (" + type + ")");
    }

    /**
     * {@code Ut <nev>} – letrehoz egy urat.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=nev
     */
    private void doUt(String[] t) {
        Road r = new Road();
        put(t[1], r);
        Logger.action(r, "letrejott");
    }

    /**
     * {@code Sav <nev>} – letrehoz egy savot.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=nev
     */
    private void doSav(String[] t) {
        Lane l = new Lane();
        put(t[1], l);
        Logger.action(l, "letrejott");
    }

    /**
     * {@code Jarmu <nev> <tipus>} – letrehoz egy jarmut (Car vagy Bus).
     *
     * @param t tokenek tombje: [0]=parancs, [1]=nev, [2]=tipus
     */
    private void doJarmu(String[] t) {
        String name = t[1], type = t[2];
        Vehicle v;
        switch (type) {
            case "Car": v = new Car(game.getCity()); break;
            case "Bus": v = new Bus(); break;
            default: Logger.error("Ismeretlen jarmu tipus: " + type); return;
        }
        put(name, v);
        Logger.action(v, "letrejott (" + type + ")");
    }

    /**
     * {@code Hokotro <nev>} – letrehoz egy hókotrot.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=nev
     */
    private void doHokotro(String[] t) {
        SnowPlow p = new SnowPlow();
        put(t[1], p);
        Logger.action(p, "letrejott");
    }

    /**
     * {@code Kapcsol <ut> <csomp1> <csomp2>} – az utat osszekapcsolja ket csomponttal.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=ut, [2]=from, [3]=to
     */
    private void doKapcsol(String[] t) {
        Road r = get(t[1], Road.class);
        Node a = get(t[2], Node.class);
        Node b = get(t[3], Node.class);
        r.setFrom(a);
        r.setTo(b);
        a.addRoad(r);
        b.addRoad(r);
        Logger.action(r, "kapcsolva: " + Logger.name(a) + " <-> " + Logger.name(b));
    }

    /**
     * {@code Hozzaad_Sav <ut> <sav>} – hozzaadja a savot az uthoz.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=ut, [2]=sav
     */
    private void doHozzaadSav(String[] t) {
        Road r = get(t[1], Road.class);
        Lane l = get(t[2], Lane.class);
        r.addLane(l);
        Logger.action(r, "uj sav hozzaadva: " + Logger.name(l));
    }

    /**
     * {@code Raallit <jarmu|hokotro> <sav>} – kozvetlenul raallitja a jarmut vagy hokotrot
     * a megadott savra (inicializalashoz, atmenet-regisztracio nelkul).
     *
     * @param t tokenek tombje: [0]=parancs, [1]=objektum neve, [2]=sav neve
     */
    private void doRaallit(String[] t) {
        Object obj = registry.get(t[1]);
        Lane lane = get(t[2], Lane.class);
        if (obj instanceof Vehicle) {
            Vehicle v = (Vehicle) obj;
            v.setCurrentLane(lane);
            lane.getVehicles().add(v);
            Logger.action(v, "raallitva " + Logger.name(lane) + " savra");
        } else if (obj instanceof SnowPlow) {
            SnowPlow p = (SnowPlow) obj;
            p.setCurrentLane(lane);
            lane.setSnowPlow(p);
            Logger.action(p, "raallitva " + Logger.name(lane) + " savra");
        } else {
            Logger.error("Raallit: ismeretlen tipus: " + t[1]);
        }
    }

    /**
     * {@code Felszerel <hokotro> <fejtipus>} – felszereli a hokotrot a megadott fejjel.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=hokotro, [2]=fejtipus
     */
    private void doFelszerel(String[] t) {
        SnowPlow plow = get(t[1], SnowPlow.class);
        String type = t[2];
        CleanerHead head;
        switch (type) {
            case "Sweep":       head = new SweepHead();       break;
            case "Throw":       head = new ThrowHead();       break;
            case "IceBreaker":  head = new IceBreakerHead();  break;
            case "Salt":        head = new SaltHead();        break;
            case "Dragon":      head = new DragonHead();      break;
            case "Rock":        head = new RockHead();        break;
            default: Logger.error("Ismeretlen fej tipus: " + type); return;
        }
        plow.changeHead(head);
        Logger.action(plow, "felszerelve: " + type + "Head");
    }

    /**
     * {@code Toltes <hokotro> <uzemanyag> <mennyiseg>} – feltolti a hokotro fejeben
     * levo uzemanyagkeszletet.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=hokotro, [2]=uzemanyagtipus, [3]=mennyiseg
     */
    private void doToltes(String[] t) {
        SnowPlow plow = get(t[1], SnowPlow.class);
        String type = t[2];
        int amount = Integer.parseInt(t[3]);
        CleanerHead head = plow.getHead();
        if (head == null) { Logger.error(plow, "Nincs felszerelt fej"); return; }
        if (!type.equals(head.fuelKind())) {
            Logger.error(plow, "A felszerelt fej (" + head.getClass().getSimpleName()
                + ") nem fogad el " + type + " tipust");
            return;
        }
        head.refuel(amount);
        Logger.action(plow, "feltoltve " + amount + " egyseg " + type);
    }

    /**
     * {@code Allapot <sav> <allapottipus>} – kozvetlenul beallitja a sav allapotát.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=sav, [2]=allapottipus
     */
    private void doAllapot(String[] t) {
        Lane lane = get(t[1], Lane.class);
        String type = t[2];
        LaneState state;
        switch (type) {
            case "ClearState":      state = new ClearState(); break;
            case "ThinSnowState":   state = new ThinSnowState(1); break;
            case "ThickSnowState":  state = new ThickSnowState(Constants.THICK_SNOW_THRESHOLD); break;
            case "IcyState":        state = new IcyState(); break;
            case "BrokenIceState":  state = new BrokenIceState(); break;
            default: Logger.error("Ismeretlen allapot: " + type); return;
        }
        lane.setState(state);
        Logger.action(lane, "allapota beallitva: " + type);
    }

    /**
     * {@code Uticel_Auto <auto> <home> <workplace>} – beallitja az auto uticelját.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=auto, [2]=home, [3]=workplace
     */
    private void doUticelAuto(String[] t) {
        Car c = get(t[1], Car.class);
        Home h = get(t[2], Home.class);
        Workplace w = get(t[3], Workplace.class);
        c.setHome(h);
        c.setWorkplace(w);
        Logger.action(c, "uticel beallitva: " + Logger.name(h) + " -> " + Logger.name(w));
    }

    /**
     * {@code Uticel_Busz <busz> <terminal1> <terminal2>} – beallitja a busz vegallomásait.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=busz, [2]=terminal1, [3]=terminal2
     */
    private void doUticelBusz(String[] t) {
        Bus b = get(t[1], Bus.class);
        Terminal t1 = get(t[2], Terminal.class);
        Terminal t2 = get(t[3], Terminal.class);
        b.setTerminalStart(t1);
        b.setTerminalEnd(t2);
        Logger.action(b, "uticel beallitva: " + Logger.name(t1) + " <-> " + Logger.name(t2));
    }

    /**
     * {@code Ho <sav> <mennyiseg>} – a megadott mennyisegu hot adja a savhoz.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=sav, [2]=mennyiseg
     */
    private void doHo(String[] t) {
        Lane lane = get(t[1], Lane.class);
        int amount = Integer.parseInt(t[2]);
        lane.addSnow(amount);
    }

    /**
     * {@code Hokotro_Lep <hokotro> -> <sav>} – a hokotrot a cel savra lepeti, majd takarit.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=hokotro, [2]="-&gt;", [3]=sav
     */
    private void doHokotroLep(String[] t) {
        SnowPlow plow = get(t[1], SnowPlow.class);
        Lane lane = get(t[3], Lane.class);
        plow.setTargetLane(lane);
        plow.step();
    }

    /**
     * {@code Auto_Lep <auto> <random>} – az autot egy korlepesre utasitja.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=auto, [2]=random (true/false)
     */
    private void doAutoLep(String[] t) {
        Car c = get(t[1], Car.class);
        boolean random = Boolean.parseBoolean(t[2]);
        c.step(random);
    }

    /**
     * {@code Busz_Lep <busz> <ut> <sav> <random>} – a buszt a megadott savra lepeti.
     * Ellenorzi az ut-sav osszetartozast, a blokkolt allapotot, az atjarhatosagot
     * es a vegallomasi erkezest.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=busz, [2]=ut, [3]=sav, [4]=random
     */
    private void doBuszLep(String[] t) {
        Bus bus = get(t[1], Bus.class);
        Road road = get(t[2], Road.class);
        Lane lane = get(t[3], Lane.class);
        boolean random = Boolean.parseBoolean(t[4]);

        if (!road.getLanes().contains(lane)) {
            Logger.error(bus, "Ervenytelen lepes, az ut/sav nem szomszedos");
            return;
        }

        if (bus.isBlocked()) {
            bus.decrementBlock();
            Logger.action(bus, "blokkolt, nem tud lepni, maradek blokk: " + bus.getBlockedTurns());
            return;
        }

        Lane prev = bus.getCurrentLane();
        if (lane.isPassable()) {
            if (prev != null) prev.removeVehicle(bus);
            lane.accept(bus);
            bus.setCurrentLane(lane);
            Logger.action(bus, "Sikeres lepes, uj pozicio: " + Logger.name(lane));
        } else {
            if (!bus.switchPassableLane()) {
                bus.setBlockedTurns(1);
                Logger.action(bus, "Elakadt vastag hoban");
                return;
            }
            Logger.action(bus, "Akadalyt kikerulte, uj pozicio: " + Logger.name(bus.getCurrentLane()));
        }

        /* Vegallomasi erkezest ellenorizzuk */
        Node arrival = road.getTo();
        if (arrival instanceof Terminal && arrival == bus.getTerminalEnd()) {
            ((Terminal) arrival).notifyArrival(bus);
        }

        /* Csuszas es utkozés ellenorzése */
        if (lane.isSlippery()) {
            boolean collide = !random;
            if (random) collide = Math.random() < 0.5;
            if (collide) {
                Logger.action(bus, "megcsuszott a csuszos savon");
                for (Vehicle other : lane.getVehicles()) {
                    if (other != bus) {
                        bus.meetVehicle(other);
                        return;
                    }
                }
            }
        }
    }

    /**
     * {@code Allapot_Auto <jarmu> Blocked=<n>} – kozvetlenul beallitja a jarmu
     * blokkolt allapotat (tesztelesi celra).
     *
     * @param t tokenek tombje: [0]=parancs, [1]=jarmu, [2]="Blocked=&lt;n&gt;"
     */
    private void doAllapotAuto(String[] t) {
        Vehicle v = get(t[1], Vehicle.class);
        String kv = t[2];
        if (kv.startsWith("Blocked=")) {
            int n = Integer.parseInt(kv.substring("Blocked=".length()));
            v.setBlockedTurns(n);
            Logger.action(v, "blokkolt allapot beallitva: " + n);
        } else {
            Logger.error("Ismeretlen tulajdonsag: " + kv);
        }
    }

    /**
     * {@code Takarito <nev> <egyenleg>} – letrehoz egy takarito jatekost.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=nev, [2]=kezdo egyenleg
     */
    private void doTakarito(String[] t) {
        String name = t[1];
        int balance = Integer.parseInt(t[2]);
        CleanerPlayer cp = new CleanerPlayer(balance);
        cp.setName(name);
        put(name, cp);
        game.addPlayer(cp);
        Logger.action(cp, "letrejott takarito jatekos, kezdo egyenleg: " + balance);
    }

    /**
     * {@code Buszsofor <busz>} – letrehoz egy buszsofőrt a megadott buszhoz.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=busz neve
     */
    private void doBuszsofor(String[] t) {
        Bus bus = get(t[1], Bus.class);
        BusDriver bd = new BusDriver(bus);
        bd.setName("driver_" + Logger.name(bus));
        registry.put("driver_" + t[1], bd);
        Logger.register(bd, "driver_" + t[1]);
        game.addPlayer(bd);
        Logger.action(bd, "buszsofor letrejott");
    }

    /**
     * {@code Vesz_Fej <jatekos> <hokotro> <fejtipus>} – a jatekos fejet vasarol
     * a megadott hokotrohoz.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=jatekos, [2]=hokotro, [3]=fejtipus
     */
    private void doVeszFej(String[] t) {
        CleanerPlayer cp = get(t[1], CleanerPlayer.class);
        SnowPlow plow = get(t[2], SnowPlow.class);
        String type = t[3];
        CleanerHead head;
        switch (type) {
            case "Sweep":       head = new SweepHead();       break;
            case "Throw":       head = new ThrowHead();       break;
            case "IceBreaker":  head = new IceBreakerHead();  break;
            case "Salt":        head = new SaltHead();        break;
            case "Dragon":      head = new DragonHead();      break;
            case "Rock":        head = new RockHead();        break;
            default: Logger.error("Ismeretlen fej tipus: " + type); return;
        }
        cp.buyHead(plow, head);
    }

    /**
     * {@code Vesz_Hokotro <jatekos> <ref-hokotro> <uj-nev>} – a jatekos uj hokotrot
     * vasarol; az uj hokotro a referencia hokotro kozelebe kerul.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=jatekos, [2]=ref-hokotro, [3]=uj nev (opcionalis)
     */
    private void doVeszHokotro(String[] t) {
        CleanerPlayer cp = get(t[1], CleanerPlayer.class);
        SnowPlow last = get(t[2], SnowPlow.class);
        String newName = t.length > 3 ? t[3] : "hk" + (cp.getPlows().size() + 2);
        SnowPlow created = cp.buyNewPlow(last, newName, game.getCity());
        if (created != null) registry.put(newName, created);
    }

    /**
     * {@code Stat <tipus> <nev>} – kinyomtatja az adott objektum allapotat
     * strukturalt {@code [STAT]} formaban.
     * Tamogatott tipusok: Lane, Car, Bus, SnowPlow, CleanerPlayer.
     *
     * @param t tokenek tombje: [0]=parancs, [1]=tipus, [2]=objektum neve
     */
    private void doStat(String[] t) {
        String type = t[1];
        String name = t[2];
        Object o = registry.get(name);
        if (o == null) { Logger.error("Stat: nem letezo objektum: " + name); return; }

        switch (type) {
            case "Lane": {
                Lane l = (Lane) o;
                String fields = "State=" + l.getState().getClass().getSimpleName()
                    + " Thickness=" + l.getState().getSnowThickness()
                    + " Slippery=" + l.isSlippery()
                    + " IsPassable=" + l.isPassable()
                    + " Rocky=" + l.isRocky()
                    + " SaltCountdown=" + l.getSaltCountdown()
                    + " PassageCount=" + l.getPassageCount();
                Logger.stat("Lane", l, fields);
                break;
            }
            case "Car": {
                Car c = (Car) o;
                String fields = "Position=" + Logger.name(c.getCurrentLane())
                    + " Blocked=" + c.isBlocked()
                    + " BlockedTurns=" + c.getBlockedTurns();
                Logger.stat("Car", c, fields);
                break;
            }
            case "Bus": {
                Bus b = (Bus) o;
                String fields = "Position=" + Logger.name(b.getCurrentLane())
                    + " Blocked=" + b.isBlocked()
                    + " BlockedTurns=" + b.getBlockedTurns()
                    + " CompletedRounds=" + b.getCompletedRounds();
                Logger.stat("Bus", b, fields);
                break;
            }
            case "SnowPlow": {
                SnowPlow p = (SnowPlow) o;
                String headName = p.getHead() == null ? "null" : p.getHead().getClass().getSimpleName();
                String fuel = "";
                if (p.getHead() != null && !p.getHead().fuelKind().isEmpty()) {
                    fuel = " Fuel_" + p.getHead().fuelKind() + "=" + p.getHead().fuelLevel();
                }
                String fields = "Position=" + Logger.name(p.getCurrentLane())
                    + " Head=" + headName + fuel;
                Logger.stat("SnowPlow", p, fields);
                break;
            }
            case "CleanerPlayer": {
                CleanerPlayer cp = (CleanerPlayer) o;
                String fields = "Balance=" + cp.getBalance() + " Plows=" + cp.getPlows().size();
                Logger.stat("CleanerPlayer", cp, fields);
                break;
            }
            default: Logger.error("Ismeretlen Stat tipus: " + type);
        }
    }
}
