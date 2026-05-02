import java.util.ArrayList;
import java.util.List;

public class Road {
    private Node from;
    private Node to;
    private final List<Lane> lanes = new ArrayList<>();

    public List<Lane> getLanes() { return lanes; }

    public void addLane(Lane lane) {
        lanes.add(lane);
        lane.setRoad(this);
    }

    public void applySnow(int amount) {
        for (Lane l : lanes) l.addSnow(amount);
    }

    public Node getFrom() { return from; }
    public Node getTo()   { return to; }
    public void setFrom(Node from) { this.from = from; }
    public void setTo(Node to)     { this.to = to; }
}
