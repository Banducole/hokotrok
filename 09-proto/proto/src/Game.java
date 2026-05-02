import java.util.ArrayList;
import java.util.List;

public class Game {
    private City city = new City();
    private List<Player> players = new ArrayList<>();
    private int roundNumber = 0;

    public City getCity() { return city; }
    public List<Player> getPlayers() { return players; }
    public int getRoundNumber() { return roundNumber; }

    public void addPlayer(Player p) { players.add(p); }

    public void nextRound() {
        for (Player p : players) p.takeTurn();
        city.executeStep();
        roundNumber++;
    }

    public boolean isGameOver() {
        for (Node node : city.getNodes()) {
            for (Road road : node.getConnectedRoads()) {
                for (Lane lane : road.getLanes()) {
                    for (Vehicle v : lane.getVehicles()) {
                        if (!v.isBlocked()) return false;
                    }
                }
            }
        }
        return true;
    }
}
