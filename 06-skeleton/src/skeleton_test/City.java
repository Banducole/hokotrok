package skeleton_test;

public class City {

    public City() {
        Skeleton.logCall("City::City()");
    }

    public void applySnowfall(int amount) {
        Skeleton.logCall("City::applySnowfall(" + amount + ")");
        Skeleton.enter();

        Road road = new Road();
        road.applySnow(amount);

        Skeleton.exit();
    }
}
