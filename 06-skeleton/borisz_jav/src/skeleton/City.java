package skeleton;

/**
 * Zúzmaraváros reprezentációja.
 * A város összefogja az utakat és csomópontokat, valamint kezeli a globális időjárási eseményeket (pl. havazás).
 */
public class City {

    /**
     * Elindítja a havazást a városban, továbbítva az eseményt a városban található utak felé.
     * @param amount A lehulló hó mennyisége.
     */
    public void applySnowfall(int amount) {
        SkeletonHelper.enterMethod("City.ApplySnowfall(" + amount + ")");
        Road dummyRoad = new Road(); // Üres konstruktorral hívjuk
        dummyRoad.applySnow(amount);
        SkeletonHelper.exitMethod("City.ApplySnowfall(" + amount + ")");
    }
}