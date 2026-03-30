package skeleton;

/**
 * A városban közlekedő normál autókat reprezentáló osztály.
 * Az autók általában a lakás (Home) és a munkahely (Workplace) között közlekednek.
 */
public class Car extends Vehicle {

    /**
     * Végrehajtja az autó egy szimulációs lépését (körét).
     * A metódus először ellenőrzi, hogy a jármű blokkolva van-e. Ha nincs,
     * lekéri a legrövidebb útvonal következő sávját a PathFinder segítségével.
     * Ha a célzott sáv járható, az autó rálép. Ha nem járható (pl. vastag hó miatt), 
     * az útvonaltervező megpróbál sávot váltani egy járható szomszédos sávra. 
     * Ha minden irány blokkolva van (nincs alternatíva), a jármű elakad.
     */
    public void step() {
        SkeletonHelper.enterMethod("Car.Step()");
        
        if (this.isBlocked()) {
            SkeletonHelper.exitMethod("Car.Step()");
            return;
        }
        
        PathFinder pf = new PathFinder();
        Lane nextLane = pf.getShortestPath(this, new Home(), new Workplace());
        
        if (nextLane != null) {
            if (nextLane.isPassable()) {
                nextLane.accept(this);
            } else {
                boolean hasAlternative = pf.switchPassableLane(nextLane);
                if (hasAlternative) {
                    Lane alternativeLane = new Lane();
                    alternativeLane.accept(this);
                } else {
                    this.setBlocked(1);
                }
            }
        }
        
        SkeletonHelper.exitMethod("Car.Step()");
    }
}