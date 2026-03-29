package skeleton;

public class Road {
    // NINCS belső változó
    // NINCS paraméteres konstruktor

    public void applySnow(int amount) {
        SkeletonHelper.enterMethod("Road.ApplySnow(" + amount + ")");
        Lane dummyLane = new Lane(); // Üres konstruktorral hívjuk
        dummyLane.addSnow(amount);
        SkeletonHelper.exitMethod("Road.ApplySnow(" + amount + ")");
    }
}