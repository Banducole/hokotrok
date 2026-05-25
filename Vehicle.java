/**
 * Jarmuek absztrakt ososztálya.
 * <p>
 * Ket konkreet altipus letezik: {@link Car} (auto, autonóm mozgással)
 * es {@link Bus} (busz, parancs altal irányitott). Minden jarmu nyilvantartja
 * az aktualis savjat es a blokkolt koreinek szamat. Utkozes utan mindket
 * jarmu {@link Constants#BLOCK_TURNS_ON_COLLISION} korre blokkolodik.
 * </p>
 */
public abstract class Vehicle {

    /** A jarmű aktuális sava (null, ha nincs savra allitva). */
    protected Lane currentLane;

    /** Hany kor van meg hatra a blokkolt allapoibol (0 = szabad). */
    protected int blockedTurns = 0;

    /**
     * A jarmű korlépése – altipusok implementaljak a sajat mozgaslogikajukat.
     *
     * @param random ha {@code true}, a csuszas/utkozés veletlenszeru;
     *               ha {@code false}, determinisztikusan bekovetkezik
     */
    public abstract void step(boolean random);

    /**
     * Ütközés kezelese ket jarmű kozott. Mindket jarmüvet blokkolt
     * allapotra allitja a konfiguralt koraszamra.
     *
     * @param other az utközési partnerjarmu
     */
    public void meetVehicle(Vehicle other) {
        this.blockedTurns  = Constants.BLOCK_TURNS_ON_COLLISION;
        other.blockedTurns = Constants.BLOCK_TURNS_ON_COLLISION;
        Logger.action(this,  "Utkozes tortent, a jarmu blokkolodott");
        Logger.action(other, "Utkozes tortent, a jarmu blokkolodott");
    }

    /**
     * Megadja, hogy a jarmű jelenleg blokkolt-e.
     *
     * @return {@code true}, ha meg van hatra blokkolt kore
     */
    public boolean isBlocked()    { return blockedTurns > 0; }

    /**
     * Visszaadja a jarmű aktualis savat.
     *
     * @return az aktualis sav, vagy {@code null}
     */
    public Lane getCurrentLane(){ return currentLane; }

    /**
     * Beallitja a jarmű aktualis savat (konyvaszetelt inicializalashoz).
     *
     * @param l az uj sav
     */
    public void setCurrentLane(Lane l){ this.currentLane = l; }

    /**
     * Visszaadja a blokkolt korek hatra levo szamat.
     *
     * @return blokkolt korek szama
     */
    public int getBlockedTurns() { return blockedTurns; }

    /**
     * Kozvetlenul beallitja a blokkolt korek szamat (tesztelesi celra).
     *
     * @param n az uj ertek
     */
    public void setBlockedTurns(int n) { this.blockedTurns = n; }

    /**
     * Csokkenti a blokkolt korek szamlalojat egyel (ha meg nem nulla).
     */
    public void decrementBlock() { if (blockedTurns > 0) blockedTurns--; }

    /**
     * Megprobálja a jarmüvet egy masik, atjarhato sávra atiranyitani
     * ugyanazon az úton belül (akadaly megkerülésehez).
     *
     * @return {@code true}, ha sikerult szabad szomszed savot talalni
     */
    public boolean switchPassableLane() {
        Road road = currentLane.getRoad();
        if (road == null) return false;
        for (Lane lane : road.getLanes()) {
            if (lane != currentLane && lane.isPassable()) {
                currentLane.removeVehicle(this);
                currentLane = lane;
                /* kozvetlenul hozzaadjuk, athaladas regisztracio nelkul */
                lane.getVehicles().add(this);
                return true;
            }
        }
        return false;
    }
}
