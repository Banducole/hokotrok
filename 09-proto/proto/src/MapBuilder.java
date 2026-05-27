import java.util.List;

public class MapBuilder {

    private static final int COLS = 5;
    private static final int ROWS = 2;

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

        // JAVÍTÁS: Átadjuk a hRoads-t is, hogy biztosan jó sávra tegyük a buszokat
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

    public static Game buildSampleGame() {
        return buildGame(
            new String[]{"Kovacs Peter", "Nagy Imre", "Szabo Bela", "Molnar Andrea"},
            new boolean[]{true, false, false, true}
        );
    }

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

    private static void placeCars(City city, Road[][] hRoads, Road[][] vRoads, Node[][] grid) {
        Home home = (Home) grid[0][0];
        Workplace workplace = (Workplace) grid[1][4];

        // 3 autó kerül a pályára különböző sávokra
        placeCarOnLane(city, vRoads[0][0].getLanes().get(0), home, workplace);
        placeCarOnLane(city, hRoads[0][0].getLanes().get(0), home, workplace);
        placeCarOnLane(city, hRoads[1][3].getLanes().get(0), home, workplace);
    }

    private static void placeCarOnLane(City city, Lane lane, Home home, Workplace workplace) {
        Car car = new Car(city);
        car.setHome(home);
        car.setWorkplace(workplace);
        car.setCurrentLane(lane);
        lane.accept(car);
    }

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

    // JAVÍTÁS: A buszokat explicit járható sávra helyezzük
    private static java.util.List<Bus> createBuses(Node[][] grid, Road[][] hRoads) {
        java.util.List<Bus> buses = new java.util.ArrayList<>();

        Terminal t1 = (Terminal) grid[0][4];
        Terminal t2 = (Terminal) grid[1][0];

        Bus bus1 = new Bus();
        bus1.setTerminalStart(t1);
        bus1.setTerminalEnd(t2);
        // A hRoads[0][3] 1-es sávja BrokenIceState (átjárható)
        Lane busLane1 = hRoads[0][3].getLanes().get(1);
        bus1.setCurrentLane(busLane1);
        busLane1.accept(bus1);
        buses.add(bus1);

        Bus bus2 = new Bus();
        bus2.setTerminalStart(t2);
        bus2.setTerminalEnd(t1);
        // A hRoads[1][0] 1-es sávja ThinSnowState (átjárható). A 0-s sáv ThickSnowState lenne, ami láthatatlanná tenné!
        Lane busLane2 = hRoads[1][0].getLanes().get(1);
        bus2.setCurrentLane(busLane2);
        busLane2.accept(bus2);
        buses.add(bus2);

        return buses;
    }

    private static Bus createFallbackBus(Node[][] grid) {
        Bus bus = new Bus();
        bus.setTerminalStart((Terminal) grid[0][4]);
        bus.setTerminalEnd((Terminal) grid[1][0]);
        return bus;
    }

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

    private static Road connectNodes(Node from, Node to) {
        Road road = new Road();
        road.setFrom(from);
        road.setTo(to);
        from.addRoad(road);
        to.addRoad(road);
        return road;
    }

    private static void addLanesToRoad(Road road, int count) {
        for (int i = 0; i < count; i++) {
            road.addLane(new Lane());
        }
    }
}