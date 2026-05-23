/**
 * Minta-palya eloallitasa a grafikus felulet tesztelesehez.
 * A palya egy kis varost modellez csomópontokkal, utakkal es jarmuvekkel.
 */
public class MapBuilder {

    public static Game buildSampleGame() {
        Game game = new Game();
        City city = game.getCity();

        Home h1 = new Home();
        Home h2 = new Home();
        Workplace w1 = new Workplace();
        Terminal t1 = new Terminal();
        Terminal t2 = new Terminal();
        Intersection i1 = new Intersection();
        Intersection i2 = new Intersection();
        Intersection i3 = new Intersection();

        city.addNode(h1);
        city.addNode(i1);
        city.addNode(i2);
        city.addNode(w1);
        city.addNode(t1);
        city.addNode(i3);
        city.addNode(t2);
        city.addNode(h2);

        Road r1 = connectNodes(h1, i1);
        Road r2 = connectNodes(i1, i2);
        Road r3 = connectNodes(i2, w1);
        Road r4 = connectNodes(t1, i1);
        Road r5 = connectNodes(i2, i3);
        Road r6 = connectNodes(i3, t2);
        Road r7 = connectNodes(i3, h2);

        for (Road r : new Road[]{r1, r2, r3, r4, r5, r6, r7}) {
            addLanesToRoad(r, 2);
        }

        Car car1 = new Car(city);
        car1.setHome(h1);
        car1.setWorkplace(w1);
        car1.setCurrentLane(r1.getLanes().get(0));
        r1.getLanes().get(0).accept(car1);

        Car car2 = new Car(city);
        car2.setHome(h2);
        car2.setWorkplace(w1);
        car2.setCurrentLane(r7.getLanes().get(0));
        r7.getLanes().get(0).accept(car2);

        Bus bus1 = new Bus();
        bus1.setTerminalStart(t1);
        bus1.setTerminalEnd(t2);
        bus1.setCurrentLane(r4.getLanes().get(0));
        r4.getLanes().get(0).accept(bus1);

        CleanerPlayer cp = new CleanerPlayer(2000);
        cp.setName("Takarito1");
        SnowPlow plow1 = new SnowPlow(cp);
        cp.addPlow(plow1);
        plow1.setCurrentLane(r2.getLanes().get(1));
        r2.getLanes().get(1).accept(plow1);
        plow1.changeHead(new ThrowHead());

        BusDriver bd = new BusDriver(bus1);
        bd.setName("Sofor1");

        game.addPlayer(cp);
        game.addPlayer(bd);

        r2.getLanes().get(0).setState(new ThinSnowState(1));
        r3.getLanes().get(0).setState(new ThickSnowState(4));
        r5.getLanes().get(0).setState(new IcyState());
        r6.getLanes().get(1).setState(new BrokenIceState());

        return game;
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
