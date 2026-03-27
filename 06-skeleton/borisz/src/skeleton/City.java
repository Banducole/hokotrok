package skeleton;

/**
 * Zúzmaraváros reprezentációja. A várost váratlanul érte a havazás, amely 
 * folyamatosan tart, és a letakarított utakat is újra belepi.
 * A városban találhatóak az utak, és itt kezeljük a globális időjárási eseményeket.
 */
public class City {
    private Road road; // A szkeleton teszteléséhez elegendő egyetlen út referenciája

    public City(Road road) {
        this.road = road;
    }

    /**
     * Elindítja a havazást a városban.
     * SD-07: Havazás - sima sávra esik hó.
     * @param amount A lehulló hó mennyisége.
     */
    public void applySnowfall(int amount) {
        SkeletonHelper.enterMethod("City.ApplySnowfall(" + amount + ")");
        road.applySnow(amount);
        SkeletonHelper.exitMethod("City.ApplySnowfall(" + amount + ")");
    }
}
