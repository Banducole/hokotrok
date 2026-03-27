package skeleton_test;

public class Road {

    public Road() {
        Skeleton.logCall("Road::Road()");
    }

    public Road(Intersection node1, Intersection node2) {
        Skeleton.logCall("Road::Road(node1, node2)");
    }

    public void applySnow(int amount) {
        Skeleton.logCall("Road::applySnow(" + amount + ")");
        Skeleton.enter();

        Lane lane = new Lane();
        lane.addSnow(amount);

        Skeleton.exit();
    }
}
