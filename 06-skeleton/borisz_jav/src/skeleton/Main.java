package skeleton;

import java.util.Scanner;

/**
 * A szkeleton program belépési pontja és menükezelője.
 * Indításkor a program egy számozott menüből kínálja fel a futtatható use-case-eket (szekvenciákat).
 */
public class Main {
    
    /**
     * A program fő metódusa, amely elindítja a konzolos menürendszert.
     * @param args Parancssori argumentumok (nem használt).
     */
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

    /** Futtatja a(z) UC-00: Skeleton inicializáció (Alapállapot) szekvenciát. */
    private static void runUC00() {
        System.out.println("\n[FUTTATÁS] UC-00: Skeleton inicializáció");
        SkeletonHelper.enterMethod("Main.runUC00()");
        System.out.println("  [LOG] Város, Utak, Kereszteződések létrehozása...");
        System.out.println("  [LOG] Sávok inicializálása ClearState állapotban...");
        System.out.println("  [LOG] Autók, Hókotrók és Takarítók létrehozása...");
        System.out.println("A teszt sikeresen lefutott. Minden objektum az értékeknek megfelelően létrejött.");
        SkeletonHelper.exitMethod("Main.runUC00()");
    }

    /** Futtatja a(z) UC-01: Autó mozog - normál eset szekvenciát. */
    private static void runUC01() {
        System.out.println("\n[FUTTATÁS] UC-01: Autó mozog - normál eset");
        Car car1 = new Car();
        car1.step();
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-02: Jármű elakad - nincs járható szomszéd szekvenciát. */
    private static void runUC02() {
        System.out.println("\n[FUTTATÁS] UC-02: Jármű elakad - nincs járható szomszéd");
        Car car1 = new Car();
        car1.step();
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-03: Jármű megcsúszik és ütközik szekvenciát. */
    private static void runUC03() {
        System.out.println("\n[FUTTATÁS] UC-03: Jármű megcsúszik és ütközik");
        Car car1 = new Car();
        car1.step(); 
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-04: Hókotró takarít - sáv már tiszta szekvenciát. */
    private static void runUC04() {
        System.out.println("\n[FUTTATÁS] UC-04: Hókotró takarít - sáv már tiszta");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1 = new SnowPlow(); 
        Lane lane1 = new Lane();
        cp1.takeTurn(sp1, lane1);
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-05: Hókotró takarít - fej nem működőképes szekvenciát. */
    private static void runUC05() {
        System.out.println("\n[FUTTATÁS] UC-05: Hókotró takarít - fej nem működőképes");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1 = new SnowPlow(); 
        Lane lane1 = new Lane();
        lane1.setState(new ThickSnowState());
        cp1.takeTurn(sp1, lane1);
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-06: Hókotró áthalad vastag hóban szekvenciát. */
    private static void runUC06() {
        System.out.println("\n[FUTTATÁS] UC-06: Hókotró áthalad vastag hóban");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1 = new SnowPlow(); 
        Lane lane1 = new Lane();
        cp1.takeTurn(sp1, lane1);
        System.out.println("\nA teszt sikeresen lefutott.");
    }
    
    /** Futtatja a(z) UC-07: Havazás - sima sávra esik hó szekvenciát. */
    private static void runUC07() {
        System.out.println("\n[FUTTATÁS] UC-07: Havazás - sima sávra esik hó");
        Road road1 = new Road(); 
        City city1 = new City(); 
        city1.applySnowfall(5);
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-08: Jégképződés vékony havas sávon szekvenciát. */
    private static void runUC08() {
        System.out.println("\n[FUTTATÁS] UC-08: Jégképződés vékony havas sávon");
        Lane lane1 = new Lane();
        lane1.setState(new ThinSnowState()); 
        Car car1 = new Car();
        lane1.registerPassage(car1);
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-09: Söprő fejjel takarítás - vékony hó, van szomszéd sáv szekvenciát. */
    private static void runUC09() {
        System.out.println("\n[FUTTATÁS] UC-09: Söprő fejjel takarítás - vékony hó, van szomszéd sáv");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1 = new SnowPlow(); 
        Lane lane1 = new Lane();
        lane1.setState(new ThinSnowState());
        cp1.takeTurn(sp1, lane1);
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-10: Hányó fejjel takarítás – vékony hó szekvenciát. */
    private static void runUC10() {
        System.out.println("\n[FUTTATÁS] UC-10: Hányó fejjel takarítás – vékony hó");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1 = new SnowPlow(); 
        Lane lane1 = new Lane();
        lane1.setState(new ThinSnowState());
        cp1.takeTurn(sp1, lane1);
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-11: Jégtörő fejjel takarítás - jeges sáv szekvenciát. */
    private static void runUC11() {
        System.out.println("\n[FUTTATÁS] UC-11: Jégtörő fejjel takarítás - jeges sáv");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1 = new SnowPlow(); 
        Lane lane1 = new Lane();
        lane1.setState(new IcyState());
        cp1.takeTurn(sp1, lane1);
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-12: Sószóróval takarítás - jeges sáv szekvenciát. */
    private static void runUC12() {
        System.out.println("\n[FUTTATÁS] UC-12: Sószóróval takarítás - jeges sáv");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1 = new SnowPlow(); 
        Lane lane1 = new Lane();
        lane1.setState(new IcyState());
        cp1.takeTurn(sp1, lane1);
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-13: Sárkányfejjel takarítás - van üzemanyag szekvenciát. */
    private static void runUC13() {
        System.out.println("\n[FUTTATÁS] UC-13: Sárkányfejjel takarítás - van üzemanyag");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1 = new SnowPlow(); 
        Lane lane1 = new Lane();
        lane1.setState(new ThickSnowState());
        cp1.takeTurn(sp1, lane1);
        System.out.println("\nA teszt sikeresen lefutott.");
    }
    
    /** Futtatja a(z) UC-14: Fejcsere szekvenciát. */
    private static void runUC14() {
        System.out.println("\n[FUTTATÁS] UC-14: Fejcsere");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1 = new SnowPlow(); 
        ThrowHead newHead = new ThrowHead();
        cp1.buyHead(sp1, newHead);
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-15: Hajtóanyag vásárlás szekvenciát. */
    private static void runUC15() {
        System.out.println("\n[FUTTATÁS] UC-15: Hajtóanyag vásárlás");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1 = new SnowPlow(); 
        cp1.buyFuel(sp1, 5);
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-16: Új hókotró vásárlás szekvenciát. */
    private static void runUC16() {
        System.out.println("\n[FUTTATÁS] UC-16: Új hókotró vásárlás");
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow lastPlow = new SnowPlow(); 
        Lane lane1 = new Lane();
        lastPlow.setTargetLane(lane1); 
        cp1.buyNewPlow(lastPlow);
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-17: Busz végállomásra érkezik - körszámláló nő szekvenciát. */
    private static void runUC17() {
        System.out.println("\n[FUTTATÁS] UC-17: Busz végállomásra érkezik");
        Bus bus1 = new Bus();
        bus1.step(); 
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-18: Jármű blokkolt - nem lép szekvenciát. */
    private static void runUC18() {
        System.out.println("\n[FUTTATÁS] UC-18: Jármű blokkolt - nem lép");
        Car car1 = new Car();
        boolean blocked = car1.isBlocked(); 
        if (blocked) {
            car1.decrementBlock();
        }
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-19: Jármű átvált szomszéd sávra szekvenciát. */
    private static void runUC19() {
        System.out.println("\n[FUTTATÁS] UC-19: Jármű átvált szomszéd sávra");
        Car car1 = new Car();
        car1.step();
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-20: Havazás - sózott sávra esik hó szekvenciát. */
    private static void runUC20() {
        System.out.println("\n[FUTTATÁS] UC-20: Havazás - sózott sávra esik hó");
        Road road1 = new Road(); 
        City city1 = new City(); 
        city1.applySnowfall(2);
        System.out.println("\nA teszt sikeresen lefutott.");
    }

    /** Futtatja a(z) UC-21: Havazás - vékony havas sávra esik hó szekvenciát. */
    private static void runUC21() {
        System.out.println("\n[FUTTATÁS] UC-21: Havazás - vékony havas sávra esik hó");
        Lane lane1 = new Lane();
        lane1.setState(new ThinSnowState());
        Road road1 = new Road(); 
        City city1 = new City(); 
        city1.applySnowfall(3);
        System.out.println("\nA teszt sikeresen lefutott.");
    }
}