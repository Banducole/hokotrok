package skeleton_test;

// ── UC-00 ────────────────────────────────────────────────────────────────────
class UC00 {
    static void run() {
        System.out.println("--- UC-00: Skeleton inicializáció ---");
        City city1      = new City();
        Intersection n1 = new Intersection();
        Intersection n2 = new Intersection();
        Road road1      = new Road(n1, n2);
        Lane lane1      = new Lane("ClearState");
        PathFinder pf1  = new PathFinder(city1);
        Car car1        = new Car();
        CleanerPlayer cp1 = new CleanerPlayer();
        SnowPlow sp1    = new SnowPlow();
        sp1.setOwner(cp1);
        DragonHead head1 = new DragonHead();
        sp1.setHead(head1);
        Skeleton.testResult(true);
    }
}

// ── UC-01 ────────────────────────────────────────────────────────────────────
class UC01 {
    static void run() {
        System.out.println("--- UC-01: Autó mozog – normál eset ---");
        Car car = new Car();
        car.step();
        Skeleton.testResult(true);
    }
}

// ── UC-02 ────────────────────────────────────────────────────────────────────
class UC02 {
    static void run() {
        System.out.println("--- UC-02: Jármű elakad – nincs járható szomszéd ---");
        Car car = new Car();
        car.step();
        Skeleton.testResult(true);
    }
}

// ── UC-03 ────────────────────────────────────────────────────────────────────
class UC03 {
    static void run() {
        System.out.println("--- UC-03: Jármű megcsúszik és ütközik ---");
        Car car1 = new Car();
        car1.step();
        Skeleton.testResult(true);
    }
}

// ── UC-04 ────────────────────────────────────────────────────────────────────
class UC04 {
    static void run() {
        System.out.println("--- UC-04: Hókotró takarít – sáv már tiszta ---");
        CleanerPlayer cp = new CleanerPlayer();
        cp.takeTurn();
        Skeleton.testResult(true);
    }
}

// ── UC-05 ────────────────────────────────────────────────────────────────────
class UC05 {
    static void run() {
        System.out.println("--- UC-05: Hókotró takarít – fej nem működőképes ---");
        CleanerPlayer cp = new CleanerPlayer();
        cp.takeTurn();
        Skeleton.testResult(true);
    }
}

// ── UC-06 ────────────────────────────────────────────────────────────────────
class UC06 {
    static void run() {
        System.out.println("--- UC-06: Hókotró áthalad vastag hóban ---");
        CleanerPlayer cp = new CleanerPlayer();
        cp.takeTurn();
        Skeleton.testResult(true);
    }
}

// ── UC-07 ────────────────────────────────────────────────────────────────────
class UC07 {
    static void run() {
        System.out.println("--- UC-07: Havazás – sima sávra esik hó ---");
        City city = new City();
        city.applySnowfall(2);
        Skeleton.testResult(true);
    }
}

// ── UC-08 ────────────────────────────────────────────────────────────────────
class UC08 {
    static void run() {
        System.out.println("--- UC-08: Jégképződés vékony havas sávon ---");
        Lane lane = new Lane("ThinSnowState");
        Car car   = new Car();
        lane.registerPassage(car);
        Skeleton.testResult(true);
    }
}

// ── UC-09 ────────────────────────────────────────────────────────────────────
class UC09 {
    static void run() {
        System.out.println("--- UC-09: Söprő fejjel takarítás ---");
        SnowPlow sp   = new SnowPlow();
        SweepHead h   = new SweepHead();
        sp.setHead(h);
        Lane lane = new Lane("ThinSnowState");
        lane.cleanWith(sp);
        Skeleton.testResult(true);
    }
}

// ── UC-10 ────────────────────────────────────────────────────────────────────
class UC10 {
    static void run() {
        System.out.println("--- UC-10: Hányó fejjel takarítás ---");
        SnowPlow sp   = new SnowPlow();
        ThrowHead h   = new ThrowHead();
        sp.setHead(h);
        Lane lane = new Lane("ThinSnowState");
        lane.cleanWith(sp);
        Skeleton.testResult(true);
    }
}

// ── UC-11 ────────────────────────────────────────────────────────────────────
class UC11 {
    static void run() {
        System.out.println("--- UC-11: Jégtörő fejjel takarítás ---");
        SnowPlow sp       = new SnowPlow();
        IceBreakerHead h  = new IceBreakerHead();
        sp.setHead(h);
        Lane lane = new Lane("IcyState");
        lane.cleanWith(sp);
        Skeleton.testResult(true);
    }
}

// ── UC-12 ────────────────────────────────────────────────────────────────────
class UC12 {
    static void run() {
        System.out.println("--- UC-12: Sószóróval takarítás ---");
        SnowPlow sp  = new SnowPlow();
        SaltHead h   = new SaltHead();
        sp.setHead(h);
        Lane lane = new Lane("IcyState");
        lane.cleanWith(sp);
        Skeleton.testResult(true);
    }
}

// ── UC-13 ────────────────────────────────────────────────────────────────────
class UC13 {
    static void run() {
        System.out.println("--- UC-13: Sárkányfejjel takarítás ---");
        SnowPlow sp   = new SnowPlow();
        DragonHead h  = new DragonHead();
        sp.setHead(h);
        Lane lane = new Lane("ThickSnowState");
        lane.cleanWith(sp);
        Skeleton.testResult(true);
    }
}

// ── UC-14 ────────────────────────────────────────────────────────────────────
class UC14 {
    static void run() {
        System.out.println("--- UC-14: Fejcsere ---");
        CleanerPlayer cp = new CleanerPlayer();
        SnowPlow sp      = new SnowPlow();
        ThrowHead newH   = new ThrowHead();
        cp.buyHead(sp, newH);
        Skeleton.testResult(true);
    }
}

// ── UC-15 ────────────────────────────────────────────────────────────────────
class UC15 {
    static void run() {
        System.out.println("--- UC-15: Hajtóanyag vásárlás ---");
        CleanerPlayer cp = new CleanerPlayer();
        SnowPlow sp      = new SnowPlow();
        SaltHead h       = new SaltHead();
        sp.setHead(h);
        cp.buyFuel(sp, 5);
        Skeleton.testResult(true);
    }
}

// ── UC-16 ────────────────────────────────────────────────────────────────────
class UC16 {
    static void run() {
        System.out.println("--- UC-16: Új hókotró vásárlás ---");
        CleanerPlayer cp  = new CleanerPlayer();
        SnowPlow lastPlow = new SnowPlow();
        cp.buyNewPlow(lastPlow);
        Skeleton.testResult(true);
    }
}

// ── UC-17 ────────────────────────────────────────────────────────────────────
class UC17 {
    static void run() {
        System.out.println("--- UC-17: Busz végállomásra érkezik ---");
        Bus bus = new Bus();
        bus.step();
        Skeleton.testResult(true);
    }
}

// ── UC-18 ────────────────────────────────────────────────────────────────────
class UC18 {
    static void run() {
        System.out.println("--- UC-18: Jármű blokkolt – nem lép ---");
        Car car = new Car();
        car.step();
        Skeleton.testResult(true);
    }
}

// ── UC-19 ────────────────────────────────────────────────────────────────────
class UC19 {
    static void run() {
        System.out.println("--- UC-19: Jármű átvált szomszéd sávra ---");
        Car car = new Car();
        car.step();
        Skeleton.testResult(true);
    }
}

// ── UC-20 ────────────────────────────────────────────────────────────────────
class UC20 {
    static void run() {
        System.out.println("--- UC-20: Havazás – sózott sávra esik hó ---");
        City city = new City();
        city.applySnowfall(2);
        Skeleton.testResult(true);
    }
}

// ── UC-21 ────────────────────────────────────────────────────────────────────
class UC21 {
    static void run() {
        System.out.println("--- UC-21: Havazás – vékony havas sávra esik hó ---");
        City city = new City();
        city.applySnowfall(1);
        Skeleton.testResult(true);
    }
}
