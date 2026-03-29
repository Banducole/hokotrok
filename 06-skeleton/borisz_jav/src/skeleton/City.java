package skeleton;

public class City {
    // NINCS belső változó
    // NINCS paraméteres konstruktor

    public void applySnowfall(int amount) {
        SkeletonHelper.enterMethod("City.ApplySnowfall(" + amount + ")");
        Road dummyRoad = new Road(); // Üres konstruktorral hívjuk
        dummyRoad.applySnow(amount);
        SkeletonHelper.exitMethod("City.ApplySnowfall(" + amount + ")");
    }
}