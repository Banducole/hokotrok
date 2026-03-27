package skeleton_test;

public class PathFinder {

    public PathFinder() {
        Skeleton.logCall("PathFinder::PathFinder()");
    }

    public PathFinder(City city) {
        Skeleton.logCall("PathFinder::PathFinder(city)");
    }

    public Lane getShortestPath() {
        Skeleton.logCall("PathFinder::getShortestPath(home, workplace)");
        Skeleton.logReturn("lane");
        return new Lane();
    }

    public Lane switchPassableLane(Lane current) {
        Skeleton.logCall("PathFinder::switchPassableLane(lane)");
        boolean found = Skeleton.askBool("Van járható szomszéd sáv?");
        if (found) {
            Skeleton.logReturn("lane2");
            return new Lane();
        }
        Skeleton.logReturn("null");
        return null;
    }

    public Lane getNeighborLane() {
        Skeleton.logCall("PathFinder::getNeighborLane(road, lane, RIGHT)");
        Skeleton.logReturn("lane2");
        return new Lane();
    }
}
