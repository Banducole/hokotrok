import java.util.List;

/**
 * A játékpálya és a kezdeti állapot felépítéséért felelős statikus segédosztály.
 * * Ez az osztály végzi el a város ({@link City}) teljes inicializálását. Feladatai 
 * közé tartozik a csomópontok rácsának ({@link Node}) és az azokat összekötő utak 
 * ({@link Road}) létrehozása, a sávok ({@link Lane}) időjárási viszonyainak 
 * beállítása, valamint a járművek (autók és buszok) és a játékosok 
 * (takarítók és buszvezetők) elhelyezése a pályán.
 */
public class MapBuilder {

    /** A pálya csomópont-rácsának oszlopszáma. */
    private static final int COLS = 5;
    
    /** A pálya csomópont-rácsának sorszáma. */
    private static final int ROWS = 2;

    /**
     * Felépíti a teljes játékot a megadott játékosnevek és szerepkörök alapján.
     * * A folyamat során először létrejön a rácshálózat és az útrendszer, majd az 
     * extra utak és sávok (pl. egy speciális átkötés két csomópont között). Ezután 
     * a rendszer elhelyezi az autókat, beállítja a hó- és jégállapotokat, lerakja 
     * a buszokat a megfelelő (járható) sávokra, végül létrehozza a játékosokat.
     * A takarítók automatikusan kapnak egy hókotrót egy szabad sávon, míg a 
     * buszvezetők megkapják a számukra legenerált buszt.
     *
     * @param names     a játékosok neveit tartalmazó tömb
     * @param isCleaner logikai tömb, ahol az igaz érték takarítót, a hamis buszvezetőt jelöl
     * @return a teljesen inicializált {@link Game} objektum
     */
    public static Game buildGame(String[] names, boolean[] isCleaner) {
        Game game = new Game();
        City city = game.getCity();

        Node[][] grid = createGrid(city);
        Road[][] hRoads = createHorizontalRoads(grid, city);
        Road[][] vRoads = createVerticalRoads(grid, city);

        Road wToWRoad = connectNodes(grid[0][2], grid[1][0]);
        addLanesToRoad(wToWRoad, 1);

        placeCars(city, hRoads, vRoads, grid);
        setSnowStates(hRoads, vRoads);

        List<Bus> buses = createBuses(grid, hRoads);
        int busIdx = 0;

        for (int i = 0; i < names.length; i++) {
            if (isCleaner[i]) {
                CleanerPlayer cp = new CleanerPlayer(Constants.INITIAL_CLEANER_BALANCE);
                cp.setName(names[i]);
                SnowPlow plow = new SnowPlow(cp);
                cp.addPlow(plow);
                plow.changeHead(new ThrowHead());
                plow.getHead().refuel(20);
                Lane lane = findFreeLane(city);
                if (lane != null) {
                    plow.setCurrentLane(lane);
                    lane.accept(plow);
                }
                game.addPlayer(cp);
            } else {
                Bus bus = busIdx < buses.size() ? buses.get(busIdx++) : createFallbackBus(grid);
                BusDriver bd = new BusDriver(bus);
                bd.setName(names[i]);
                game.addPlayer(bd);
            }
        }

        return game;
    }

    /**
     * Létrehoz egy alapértelmezett tesztjátékot előre definiált játékosokkal.
     * Ezt a metódust akkor használjuk, ha a felhasználó a beállításoknál 
     * nem erősíti meg a saját neveit.
     *
     * @return az alapértelmezett beállításokkal felépített {@link Game} objektum
     */
    public static Game buildSampleGame() {
        return buildGame(
            new String[]{"Kovacs Peter", "Nagy Imre", "Szabo Bela", "Molnar Andrea"},
            new boolean[]{true, false, false, true}
        );
    }

    /**
     * Létrehozza a város csomópontjait ({@link Node}) és elhelyezi őket egy 2D tömbben.
     * Definiálja a speciális csomópontokat is, mint a {@link Home}, {@link Terminal} 
     * és {@link Workplace}.
     *
     * @param city a város, amihez a csomópontokat hozzáadjuk
     * @return a csomópontokat tartalmazó 2D rács
     */
    private static Node[][] createGrid(City city) {
        Node[][] grid = new Node[ROWS][COLS];

        // 2 db távolabb lévő Terminal
        grid[0][0] = new Home();
        grid[0][1] = new Intersection();
        grid[0][2] = new Intersection();
        grid[0][3] = new Intersection(); 
        grid[0][4] = new Terminal();     

        grid[1][0] = new Terminal();     
        grid[1][1] = new Intersection();
        grid[1][2] = new Intersection(); 
        grid[1][3] = new Intersection(); 
        grid[1][4] = new Workplace();

        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                city.addNode(grid[r][c]);

        return grid;
    }

    /**
     * Létrehozza a vízszintes irányú utakat a rácspontok között, és ellátja 
     * őket 2-2 sávval.
     *
     * @param grid a csomópontok rácsa
     * @param city a város objektuma
     * @return a vízszintes utakat tartalmazó 2D tömb
     */
    private static Road[][] createHorizontalRoads(Node[][] grid, City city) {
        Road[][] hRoads = new Road[ROWS][COLS - 1];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS - 1; c++) {
                hRoads[r][c] = connectNodes(grid[r][c], grid[r][c + 1]);
                addLanesToRoad(hRoads[r][c], 2);
            }
        }
        return hRoads;
    }

    /**
     * Létrehozza a függőleges irányú utakat a rácspontok között. 
     * Az utolsó oszlopban lévő utak 4 sávot kapnak, a többi 2 sávot.
     *
     * @param grid a csomópontok rácsa
     * @param city a város objektuma
     * @return a függőleges utakat tartalmazó 2D tömb
     */
    private static Road[][] createVerticalRoads(Node[][] grid, City city) {
        Road[][] vRoads = new Road[ROWS - 1][COLS];
        for (int r = 0; r < ROWS - 1; r++) {
            for (int c = 0; c < COLS; c++) {
                vRoads[r][c] = connectNodes(grid[r][c], grid[r + 1][c]);
                int laneCount = (c == COLS - 1) ? 4 : 2;
                
                addLanesToRoad(vRoads[r][c], laneCount);
            }
        }
        return vRoads;
    }

    /**
     * Elhelyez 3 autót a pálya különböző előre definiált sávjain, és 
     * beállítja számukra a kiindulási otthont és a cél munkahelyet.
     *
     * @param city   a város objektuma
     * @param hRoads a vízszintes utak tömbje
     * @param vRoads a függőleges utak tömbje
     * @param grid   a csomópontok rácsa
     */
    private static void placeCars(City city, Road[][] hRoads, Road[][] vRoads, Node[][] grid) {
        Home home = (Home) grid[0][0];
        Workplace workplace = (Workplace) grid[1][4];

        // 3 autó kerül a pályára különböző sávokra
        placeCarOnLane(city, vRoads[0][0].getLanes().get(0), home, workplace);
        placeCarOnLane(city, hRoads[0][0].getLanes().get(0), home, workplace);
        placeCarOnLane(city, hRoads[1][3].getLanes().get(0), home, workplace);
    }

    /**
     * Segédmetódus egyetlen autó adott sávra történő elhelyezéséhez.
     *
     * @param city      a város objektuma
     * @param lane      a célsáv, ahova az autó kerül
     * @param home      az autóhoz rendelt otthon csomópont
     * @param workplace az autóhoz rendelt munkahely csomópont
     */
    private static void placeCarOnLane(City city, Lane lane, Home home, Workplace workplace) {
        Car car = new Car(city);
        car.setHome(home);
        car.setWorkplace(workplace);
        car.setCurrentLane(lane);
        lane.accept(car);
    }

    /**
     * Beállítja az úthálózat adott sávjainak kezdeti időjárási és 
     * felületi állapotait (pl. vékony hó, mély hó, jég, zúzalék).
     *
     * @param hRoads a vízszintes utak tömbje
     * @param vRoads a függőleges utak tömbje
     */
    private static void setSnowStates(Road[][] hRoads, Road[][] vRoads) {
        hRoads[0][0].getLanes().get(0).setState(new ThinSnowState(1));
        hRoads[0][0].getLanes().get(1).setState(new ThickSnowState(4));

        vRoads[0][2].getLanes().get(0).setState(new IcyState());
        vRoads[0][2].getLanes().get(1).setState(new IcyState());

        vRoads[0][3].getLanes().get(0).setState(new ThinSnowState(2));

        hRoads[0][3].getLanes().get(0).setState(new IcyState());
        hRoads[0][3].getLanes().get(1).setState(new BrokenIceState());

        hRoads[1][0].getLanes().get(0).setState(new ThickSnowState(5));
        hRoads[1][0].getLanes().get(1).setState(new ThinSnowState(1));

        hRoads[1][3].getLanes().get(0).setState(new ThinSnowState(1));
        hRoads[1][3].getLanes().get(1).setState(new IcyState());

        vRoads[0][0].getLanes().get(0).setState(new ThinSnowState(1));

        hRoads[0][1].getLanes().get(0).setState(new ThinSnowState(2));
        hRoads[0][1].getLanes().get(0).setRocky(true);
    }

    /**
     * Létrehozza és elhelyezi a buszokat a pályán.
     * Kifejezetten figyel arra, hogy a buszok induláskor átjárható 
     * (pl. BrokenIceState vagy ThinSnowState) sávokra kerüljenek, 
     * elkerülve a mély hót, ami hibát okozna.
     *
     * @param grid   a csomópontok rácsa a terminálok eléréséhez
     * @param hRoads a vízszintes utak tömbje a megfelelő sávok kiválasztásához
     * @return a létrehozott és elhelyezett buszokat tartalmazó lista
     */

    private static java.util.List<Bus> createBuses(Node[][] grid, Road[][] hRoads) {
        java.util.List<Bus> buses = new java.util.ArrayList<>();

        Terminal t1 = (Terminal) grid[0][4];
        Terminal t2 = (Terminal) grid[1][0];

        Bus bus1 = new Bus();
        bus1.setTerminalStart(t1);
        bus1.setTerminalEnd(t2);

        Lane busLane1 = hRoads[0][3].getLanes().get(1);
        bus1.setCurrentLane(busLane1);
        busLane1.accept(bus1);
        buses.add(bus1);

        Bus bus2 = new Bus();
        bus2.setTerminalStart(t2);
        bus2.setTerminalEnd(t1);

        Lane busLane2 = hRoads[1][0].getLanes().get(1);
        bus2.setCurrentLane(busLane2);
        busLane2.accept(bus2);
        buses.add(bus2);

        return buses;
    }

    /**
     * Létrehoz egy "tartalék" buszt abban az esetben, ha több buszvezető 
     * játékos van, mint ahány dedikált busz hely lett kijelölve.
     *
     * @param grid a csomópontok rácsa
     * @return egy új, sávra még nem helyezett busz
     */
    private static Bus createFallbackBus(Node[][] grid) {
        Bus bus = new Bus();
        bus.setTerminalStart((Terminal) grid[0][4]);
        bus.setTerminalEnd((Terminal) grid[1][0]);
        return bus;
    }

    /**
     * Megkeresi a város első olyan sávját, amelyen sem jármű, sem 
     * hókotró nem tartózkodik jelenleg. Ezt használjuk a takarító 
     * játékosok kezdőpontjának kijelöléséhez.
     *
     * @param city a város objektuma
     * @return az első szabad sáv, vagy null, ha nincs ilyen
     */
    private static Lane findFreeLane(City city) {
        for (Node n : city.getNodes()) {
            for (Road r : n.getConnectedRoads()) {
                for (Lane l : r.getLanes()) {
                    if (l.getSnowPlow() == null && l.getVehicles().isEmpty()) return l;
                }
            }
        }
        return null;
    }

    /**
     * Összeköt két csomópontot egy új úttal.
     *
     * @param from a kiindulási csomópont
     * @param to   a cél csomópont
     * @return a létrehozott és csatlakoztatott út
     */
    private static Road connectNodes(Node from, Node to) {
        Road road = new Road();
        road.setFrom(from);
        road.setTo(to);
        from.addRoad(road);
        to.addRoad(road);
        return road;
    }

    /**
     * Hozzáad a megadott úthoz egy meghatározott számú új sávot.
     *
     * @param road  az út, amihez sávokat adunk
     * @param count a hozzáadandó sávok száma
     */
    private static void addLanesToRoad(Road road, int count) {
        for (int i = 0; i < count; i++) {
            road.addLane(new Lane());
        }
    }
}