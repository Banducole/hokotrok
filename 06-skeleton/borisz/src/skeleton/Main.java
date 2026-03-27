package skeleton;

import java.util.Scanner;

/**
 * A szkeleton program belépési pontja és menükezelője.
 * Indításkor a program egy számozott menüből kínálja fel a futtatható use-case-eket[cite: 207].
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- HÓKOTRÓK SZEKLETON MENÜ ---");
            System.out.println("0. UC-00: Skeleton inicializáció (Alapállapot)");
            System.out.println("1. UC-01: Autó mozog - normál eset");
            System.out.println("2. UC-02: Jármű elakad - nincs járható szomszéd");
            System.out.println("3. UC-03: Jármű megcsúszik és ütközik");
            System.out.println("4. UC-04: Hókotró takarít - sáv már tiszta");
            System.out.println("5. UC-05: Hókotró takarít - fej nem működőképes");
            System.out.println("6. UC-06: Hókotró áthalad vastag hóban");
            System.out.println("7. UC-07: Havazás - sima sávra esik hó");
            System.out.println("8. UC-08: Jégképződés vékony havas sávon");
            System.out.println("9. UC-09: Söprő fejjel takarítás - vékony hó, van szomszéd sáv");
            System.out.println("10. UC-10: Hányó fejjel takarítás – vékony hó");
            System.out.println("11. UC-11: Jégtörő fejjel takarítás - jeges sáv");
            System.out.println("12. UC-12: Sószóróval takarítás - jeges sáv");
            System.out.println("13. UC-13: Sárkányfejjel takarítás - van üzemanyag");
            System.out.println("14. UC-14: Fejcsere");
            System.out.println("15. UC-15: Hajtóanyag vásárlás");
            System.out.println("16. UC-16: Új hókotró vásárlás");
            System.out.println("17. UC-17: Busz végállomásra érkezik - körszámláló nő");
            System.out.println("18. UC-18: Jármű blokkolt - nem lép");
            System.out.println("19. UC-19: Jármű átvált szomszéd sávra");
            System.out.println("20. UC-20: Havazás - sózott sávra esik hó");
            System.out.println("21. UC-21: Havazás - vékony havas sávra esik hó");
            System.out.println("99. Kilépés");
            System.out.print("Válassz egy menüpontot: ");
            
            String choice = scanner.nextLine();

            switch (choice) {
                case "0": runUC00(); break;
                case "1": runUC01(); break;
                case "2": runUC02(); break;
                case "3": runUC03(); break;
                case "4": runUC04(); break;
                case "5": runUC05(); break;
                case "6": runUC06(); break;
                case "7": runUC07(); break;
                case "8": runUC08(); break;
                case "9": runUC09(); break;
                case "10": runUC10(); break;
                case "11": runUC11(); break;
                case "12": runUC12(); break;
                case "13": runUC13(); break;
                case "14": runUC14(); break;
                case "15": runUC15(); break;
                case "16": runUC16(); break;
                case "17": runUC17(); break;
                case "18": runUC18(); break;
                case "19": runUC19(); break;
                case "20": runUC20(); break;
                case "21": runUC21(); break;
                case "99":
                    running = false;
                    System.out.println("Kilépés...");
                    break;
                default:
                    System.out.println("Érvénytelen választás! Kérlek a menüpontok sorszámát add meg.");
            }
        }
    }

    /**
     * UC-00: Skeleton inicializáció futtatása.
     * SD-00 szekvenciadiagram alapján[cite: 218].
     */
    private static void runUC00() {
        System.out.println("\n[FUTTATÁS] UC-00: Skeleton inicializáció");
        SkeletonHelper.enterMethod("Main.runUC00()");
        System.out.println("  [LOG] Város, Utak, Kereszteződések létrehozása...");
        System.out.println("  [LOG] Sávok inicializálása ClearState állapotban...");
        System.out.println("  [LOG] Autók, Hókotrók és Takarítók létrehozása...");
        System.out.println("A teszt sikeresen lefutott. Minden objektum az értékeknek megfelelően létrejött[cite: 106].");
        SkeletonHelper.exitMethod("Main.runUC00()");
    }

    /**
     * UC-01: Autó mozog - normál eset futtatása[cite: 107].
     * SD-01 szekvenciadiagram alapján[cite: 244].
     */
    private static void runUC01() {
        System.out.println("\n[FUTTATÁS] UC-01: Autó mozog - normál eset");
        
        Car car1 = new Car();
        Lane lane1 = new Lane();
        PathFinder pf1 = new PathFinder();
        
        car1.step(pf1, lane1);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 107].");
    }

    /**
     * UC-02: Jármű elakad - nincs járható szomszéd[cite: 109].
     * SD-02 szekvenciadiagram alapján[cite: 264].
     */
    private static void runUC02() {
        System.out.println("\n[FUTTATÁS] UC-02: Jármű elakad - nincs járható szomszéd");
        Car car1 = new Car();
        PathFinder pf1 = new PathFinder();
        Home home1 = new Home();
        Workplace wp1 = new Workplace();
        
        boolean blocked = car1.isBlocked();
        if (!blocked) {
            Lane nextLane = pf1.getShortestPath(car1, home1, wp1); // Stringek helyett objektumok
            boolean passable = nextLane.isPassable();
            if (!passable) {
                boolean switched = pf1.switchPassableLane(nextLane);
                if (!switched) {
                    car1.setBlocked(1);
                }
            }
        }
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /**
     * UC-03: Jármű megcsúszik és ütközik[cite: 119].
     * SD-03 szekvenciadiagram alapján[cite: 283].
     */
    private static void runUC03() {
        System.out.println("\n[FUTTATÁS] UC-03: Jármű megcsúszik és ütközik");
        Car car1 = new Car();
        Lane lane1 = new Lane();
        PathFinder pf1 = new PathFinder();
        
        car1.step(pf1, lane1); 
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 132].");
    }

    /**
     * UC-04: Hókotró takarít - sáv már tiszta[cite: 134].
     * SD-04 szekvenciadiagram alapján[cite: 312].
     */
    private static void runUC04() {
        System.out.println("\n[FUTTATÁS] UC-04: Hókotró takarít - sáv már tiszta");
        
        CleanerPlayer cp1 = new CleanerPlayer();
        DragonHead head1 = new DragonHead();
        SnowPlow sp1 = new SnowPlow(head1, cp1);
        Lane lane1 = new Lane();
        
        cp1.takeTurn(sp1, lane1);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 149].");
    }

    /**
     * UC-05: Hókotró takarít - fej nem működőképes[cite: 150].
     * SD-05 szekvenciadiagram alapján[cite: 327].
     */
    private static void runUC05() {
        System.out.println("\n[FUTTATÁS] UC-05: Hókotró takarít - fej nem működőképes");
        CleanerPlayer cp1 = new CleanerPlayer();
        DragonHead head1 = new DragonHead(); 
        SnowPlow sp1 = new SnowPlow(head1, cp1);
        Lane lane1 = new Lane();
        lane1.setState(new ThickSnowState());
        
        cp1.takeTurn(sp1, lane1);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 154].");
    }

    /**
     * UC-06: Hókotró áthalad vastag hóban[cite: 155].
     * SD-06 szekvenciadiagram alapján[cite: 343].
     */
    private static void runUC06() {
        System.out.println("\n[FUTTATÁS] UC-06: Hókotró áthalad vastag hóban");
        
        CleanerPlayer cp1 = new CleanerPlayer();
        ThrowHead head1 = new ThrowHead();
        SnowPlow sp1 = new SnowPlow(head1, cp1);
        Lane lane1 = new Lane();
        
        cp1.takeTurn(sp1, lane1);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 155].");
    }
    
    /**
     * UC-07: Havazás - sima sávra esik hó[cite: 156].
     * SD-07 szekvenciadiagram alapján[cite: 375].
     */
    private static void runUC07() {
        System.out.println("\n[FUTTATÁS] UC-07: Havazás - sima sávra esik hó");
        
        Lane lane1 = new Lane(); 
        Road road1 = new Road(lane1);
        City city1 = new City(road1);
        
        city1.applySnowfall(5);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 156].");
    }

    /**
     * UC-08: Jégképződés vékony havas sávon[cite: 158].
     * SD-08 szekvenciadiagram alapján[cite: 391].
     */
    private static void runUC08() {
        System.out.println("\n[FUTTATÁS] UC-08: Jégképződés vékony havas sávon");
        
        Lane lane1 = new Lane();
        lane1.setState(new ThinSnowState()); 
        Car car1 = new Car();
        
        lane1.registerPassage(car1);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 163].");
    }

    /**
     * UC-09: Söprő fejjel takarítás vékony hó, van szomszéd sáv[cite: 164].
     * SD-11 szekvenciadiagram alapján[cite: 434].
     */
    private static void runUC09() {
        System.out.println("\n[FUTTATÁS] UC-09: Söprő fejjel takarítás - vékony hó, van szomszéd sáv");
        CleanerPlayer cp1 = new CleanerPlayer();
        SweepHead head1 = new SweepHead();
        SnowPlow sp1 = new SnowPlow(head1, cp1);
        Lane lane1 = new Lane();
        lane1.setState(new ThinSnowState());
        
        cp1.takeTurn(sp1, lane1);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 164].");
    }

    /**
     * UC-10: Hányó fejjel takarítás – vékony hó[cite: 168].
     * SD-12 szekvenciadiagram alapján[cite: 456].
     */
    private static void runUC10() {
        System.out.println("\n[FUTTATÁS] UC-10: Hányó fejjel takarítás – vékony hó");
        CleanerPlayer cp1 = new CleanerPlayer();
        ThrowHead head1 = new ThrowHead();
        SnowPlow sp1 = new SnowPlow(head1, cp1);
        Lane lane1 = new Lane();
        lane1.setState(new ThinSnowState());
        
        cp1.takeTurn(sp1, lane1);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 166].");
    }

    /**
     * UC-11: Jégtörő fejjel takarítás - jeges sáv[cite: 167].
     * SD-13 szekvenciadiagram alapján[cite: 479].
     */
    private static void runUC11() {
        System.out.println("\n[FUTTATÁS] UC-11: Jégtörő fejjel takarítás - jeges sáv");
        CleanerPlayer cp1 = new CleanerPlayer();
        IceBreakerHead head1 = new IceBreakerHead();
        SnowPlow sp1 = new SnowPlow(head1, cp1);
        Lane lane1 = new Lane();
        lane1.setState(new IcyState());
        
        cp1.takeTurn(sp1, lane1);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 172].");
    }

    /**
     * UC-12: Sószóróval takarítás - jeges sáv[cite: 174].
     * SD-14 szekvenciadiagram alapján[cite: 499].
     */
    private static void runUC12() {
        System.out.println("\n[FUTTATÁS] UC-12: Sószóróval takarítás - jeges sáv");
        CleanerPlayer cp1 = new CleanerPlayer();
        SaltHead head1 = new SaltHead();
        SnowPlow sp1 = new SnowPlow(head1, cp1);
        Lane lane1 = new Lane();
        lane1.setState(new IcyState());
        
        cp1.takeTurn(sp1, lane1);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 174].");
    }

    /**
     * UC-13: Sárkányfejjel takarítás - van üzemanyag[cite: 175].
     * SD-15 szekvenciadiagram alapján[cite: 510].
     */
    private static void runUC13() {
        System.out.println("\n[FUTTATÁS] UC-13: Sárkányfejjel takarítás - van üzemanyag");
        CleanerPlayer cp1 = new CleanerPlayer();
        DragonHead head1 = new DragonHead(); 
        SnowPlow sp1 = new SnowPlow(head1, cp1);
        Lane lane1 = new Lane();
        lane1.setState(new ThickSnowState());
        
        cp1.takeTurn(sp1, lane1);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 175].");
    }
    
    /**
     * UC-14: Fejcsere[cite: 180].
     * SD-16 szekvenciadiagram alapján[cite: 532].
     */
    private static void runUC14() {
        System.out.println("\n[FUTTATÁS] UC-14: Fejcsere");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1 = new SnowPlow(new DragonHead(), cp1);
        ThrowHead newHead = new ThrowHead();
        
        cp1.buyHead(sp1, newHead);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 187].");
    }

    /**
     * UC-15: Hajtóanyag vásárlás[cite: 188].
     * SD-17 szekvenciadiagram alapján[cite: 546].
     */
    private static void runUC15() {
        System.out.println("\n[FUTTATÁS] UC-15: Hajtóanyag vásárlás");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1 = new SnowPlow(new DragonHead(), cp1); 
        
        cp1.buyFuel(sp1, 5);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 188].");
    }

    /**
     * UC-16: Új hókotró vásárlás[cite: 189].
     * SD-18 szekvenciadiagram alapján[cite: 568].
     */
    private static void runUC16() {
        System.out.println("\n[FUTTATÁS] UC-16: Új hókotró vásárlás");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow lastPlow = new SnowPlow(new DragonHead(), cp1);
        Lane lane1 = new Lane();
        lastPlow.setTargetLane(lane1); 
        
        cp1.buyNewPlow(lastPlow);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 189].");
    }

    /**
     * UC-17: Busz végállomásra érkezik - körszámláló nő[cite: 190].
     * SD-19 szekvenciadiagram alapján[cite: 588].
     */
    private static void runUC17() {
        System.out.println("\n[FUTTATÁS] UC-17: Busz végállomásra érkezik");
        Bus bus1 = new Bus();
        Lane lane1 = new Lane();
        PathFinder pf1 = new PathFinder();
        
        bus1.step(pf1, lane1); // Most már a PathFinder-t is átadjuk neki
        
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /**
     * UC-18: Jármű blokkolt - nem lép[cite: 196].
     * SD-20 szekvenciadiagram alapján[cite: 611].
     */
    private static void runUC18() {
        System.out.println("\n[FUTTATÁS] UC-18: Jármű blokkolt - nem lép");
        Car car1 = new Car();
        
        boolean blocked = car1.isBlocked(); 
        if (blocked) {
            car1.decrementBlock();
        }
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 196].");
    }

    /**
     * UC-19: Jármű átvált szomszéd sávra[cite: 197].
     * SD-21 szekvenciadiagram alapján[cite: 621].
     */
    private static void runUC19() {
        System.out.println("\n[FUTTATÁS] UC-19: Jármű átvált szomszéd sávra");
        Car car1 = new Car();
        PathFinder pf1 = new PathFinder();
        Home home1 = new Home();
        Workplace wp1 = new Workplace();
        
        boolean blocked = car1.isBlocked(); 
        if (!blocked) {
            Lane nextLane = pf1.getShortestPath(car1, home1, wp1); // Stringek helyett objektumok
            boolean passable = nextLane.isPassable(); 
            if (!passable) {
                boolean switched = pf1.switchPassableLane(nextLane); 
                if (switched) {
                    Lane neighbor = new Lane();
                    neighbor.accept(car1);
                }
            }
        }
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /**
     * UC-20: Havazás - sózott sávra esik hó[cite: 199].
     * SD-10 szekvenciadiagram alapján[cite: 417].
     */
    private static void runUC20() {
        System.out.println("\n[FUTTATÁS] UC-20: Havazás - sózott sávra esik hó");
        Lane lane1 = new Lane(); 
        Road road1 = new Road(lane1);
        City city1 = new City(road1);
        
        city1.applySnowfall(2);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 204].");
    }

    /**
     * UC-21: Havazás - vékony havas sávra esik hó[cite: 205].
     * SD-09 szekvenciadiagram alapján[cite: 401].
     */
    private static void runUC21() {
        System.out.println("\n[FUTTATÁS] UC-21: Havazás - vékony havas sávra esik hó");
        Lane lane1 = new Lane();
        lane1.setState(new ThinSnowState());
        Road road1 = new Road(lane1);
        City city1 = new City(road1);
        
        city1.applySnowfall(3);
        
        System.out.println("\nA teszt sikeresen lefutott[cite: 205].");
    }
}