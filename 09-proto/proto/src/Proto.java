import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Proto {
    private final Game game = new Game();
    private final Map<String, Object> registry = new HashMap<>();

    public static void main(String[] args) throws Exception {
        new Proto().run();
    }

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

    // ---------- helpers ----------
    @SuppressWarnings("unchecked")
    private <T> T get(String name, Class<T> klass) {
        Object o = registry.get(name);
        if (o == null) throw new RuntimeException("Nem letezo objektum: " + name);
        if (!klass.isInstance(o)) throw new RuntimeException("Tipushiba: " + name + " nem " + klass.getSimpleName());
        return (T) o;
    }

    private void put(String name, Object obj) {
        registry.put(name, obj);
        Logger.register(obj, name);
    }

    // ---------- commands ----------
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

    private void doUt(String[] t) {
        Road r = new Road();
        put(t[1], r);
        Logger.action(r, "letrejott");
    }

    private void doSav(String[] t) {
        Lane l = new Lane();
        put(t[1], l);
        Logger.action(l, "letrejott");
    }

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

    private void doHokotro(String[] t) {
        SnowPlow p = new SnowPlow();
        put(t[1], p);
        Logger.action(p, "letrejott");
    }

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

    private void doHozzaadSav(String[] t) {
        Road r = get(t[1], Road.class);
        Lane l = get(t[2], Lane.class);
        r.addLane(l);
        Logger.action(r, "uj sav hozzaadva: " + Logger.name(l));
    }

    private void doRaallit(String[] t) {
        Object obj = registry.get(t[1]);
        Lane lane = get(t[2], Lane.class);
        if (obj instanceof Vehicle) {
            Vehicle v = (Vehicle) obj;
            v.setCurrentLane(lane);
            lane.getVehicles().add(v); // initialisation, no passage registration
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

    private void doFelszerel(String[] t) {
        SnowPlow plow = get(t[1], SnowPlow.class);
        String type = t[2];
        CleanerHead head;
        switch (type) {
            case "Sweep":       head = new SweepHead(); break;
            case "Throw":       head = new ThrowHead(); break;
            case "IceBreaker":  head = new IceBreakerHead(); break;
            case "Salt":        head = new SaltHead(); break;
            case "Dragon":      head = new DragonHead(); break;
            case "Rock":        head = new RockHead(); break;
            default: Logger.error("Ismeretlen fej tipus: " + type); return;
        }
        plow.changeHead(head);
        Logger.action(plow, "felszerelve: " + type + "Head");
    }

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

    private void doUticelAuto(String[] t) {
        Car c = get(t[1], Car.class);
        Home h = get(t[2], Home.class);
        Workplace w = get(t[3], Workplace.class);
        c.setHome(h);
        c.setWorkplace(w);
        Logger.action(c, "uticel beallitva: " + Logger.name(h) + " -> " + Logger.name(w));
    }

    private void doUticelBusz(String[] t) {
        Bus b = get(t[1], Bus.class);
        Terminal t1 = get(t[2], Terminal.class);
        Terminal t2 = get(t[3], Terminal.class);
        b.setTerminalStart(t1);
        b.setTerminalEnd(t2);
        Logger.action(b, "uticel beallitva: " + Logger.name(t1) + " <-> " + Logger.name(t2));
    }

    private void doHo(String[] t) {
        Lane lane = get(t[1], Lane.class);
        int amount = Integer.parseInt(t[2]);
        lane.addSnow(amount);
    }

    private void doHokotroLep(String[] t) {
        SnowPlow plow = get(t[1], SnowPlow.class);
        Lane lane = get(t[3], Lane.class);
        plow.setTargetLane(lane);
        plow.step();
    }

    private void doAutoLep(String[] t) {
        Car c = get(t[1], Car.class);
        boolean random = Boolean.parseBoolean(t[2]);
        c.step(random);
    }

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

        Node arrival = road.getTo();
        if (arrival instanceof Terminal && arrival == bus.getTerminalEnd()) {
            ((Terminal) arrival).notifyArrival(bus);
        }

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

    private void doAllapotAuto(String[] t) {
        Vehicle v = get(t[1], Vehicle.class);
        // Format: Allapot_Auto auto1 Blocked=2
        String kv = t[2];
        if (kv.startsWith("Blocked=")) {
            int n = Integer.parseInt(kv.substring("Blocked=".length()));
            v.setBlockedTurns(n);
            Logger.action(v, "blokkolt allapot beallitva: " + n);
        } else {
            Logger.error("Ismeretlen tulajdonsag: " + kv);
        }
    }

    private void doTakarito(String[] t) {
        String name = t[1];
        int balance = Integer.parseInt(t[2]);
        CleanerPlayer cp = new CleanerPlayer(balance);
        cp.setName(name);
        put(name, cp);
        game.addPlayer(cp);
        Logger.action(cp, "letrejott takarito jatekos, kezdo egyenleg: " + balance);
    }

    private void doBuszsofor(String[] t) {
        Bus bus = get(t[1], Bus.class);
        BusDriver bd = new BusDriver(bus);
        bd.setName("driver_" + Logger.name(bus));
        // register under the bus name + suffix to avoid clash
        registry.put("driver_" + t[1], bd);
        Logger.register(bd, "driver_" + t[1]);
        game.addPlayer(bd);
        Logger.action(bd, "buszsofor letrejott");
    }

    private void doVeszFej(String[] t) {
        CleanerPlayer cp = get(t[1], CleanerPlayer.class);
        SnowPlow plow = get(t[2], SnowPlow.class);
        String type = t[3];
        CleanerHead head;
        switch (type) {
            case "Sweep":       head = new SweepHead(); break;
            case "Throw":       head = new ThrowHead(); break;
            case "IceBreaker":  head = new IceBreakerHead(); break;
            case "Salt":        head = new SaltHead(); break;
            case "Dragon":      head = new DragonHead(); break;
            case "Rock":        head = new RockHead(); break;
            default: Logger.error("Ismeretlen fej tipus: " + type); return;
        }
        cp.buyHead(plow, head);
    }

    private void doVeszHokotro(String[] t) {
        CleanerPlayer cp = get(t[1], CleanerPlayer.class);
        SnowPlow last = get(t[2], SnowPlow.class);
        String newName = t.length > 3 ? t[3] : "hk" + (cp.getPlows().size() + 2);
        // generate a name: hk<n> where n is next index
        SnowPlow created = cp.buyNewPlow(last, newName);
        if (created != null) registry.put(newName, created);
    }

    private void doStat(String[] t) {
        String type = t[1];
        String name = t[2];
        Object o = registry.get(name);
        if (o == null) { Logger.error("Stat: nem letezo objektum: " + name); return; }

        switch (type) {
            case "Lane": {
                Lane l = (Lane) o;
                String fields = "State=" + l.getState().getClass().getSimpleName()
                    + " SnowThickness=" + l.getState().getSnowThickness()
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
