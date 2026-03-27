package skeleton_test;

public class SnowPlow {

    public SnowPlow() {
        Skeleton.logCall("SnowPlow::SnowPlow()");
    }

    public void step() {
        Skeleton.logCall("SnowPlow::step()");
        Skeleton.enter();

        Lane targetLane = new Lane();
        targetLane.acceptPlow(this);

        Skeleton.exit();
    }

    public Head getHead() {
        Skeleton.logCall("SnowPlow::getHead()");
        Skeleton.logReturn("head");
        return new Head();
    }

    public void setHead(Head head) {
        Skeleton.logCall("SnowPlow::setHead(head)");
    }

    public void changeHead(Head newHead) {
        Skeleton.logCall("SnowPlow::changeHead(newHead)");
    }

    public CleanerPlayer getOwner() {
        Skeleton.logCall("SnowPlow::getOwner()");
        Skeleton.logReturn("owner");
        return new CleanerPlayer();
    }

    public void setOwner(CleanerPlayer owner) {
        Skeleton.logCall("SnowPlow::setOwner(owner)");
    }

    public Lane getCurrentLane() {
        Skeleton.logCall("SnowPlow::getCurrentLane()");
        Skeleton.logReturn("currentLane");
        return new Lane();
    }

    public int getPrice() {
        Skeleton.logCall("SnowPlow::getPrice()");
        Skeleton.logReturn("500");
        return 500;
    }
}
