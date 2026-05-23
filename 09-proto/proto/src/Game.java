import java.util.ArrayList;
import java.util.List;

/**
 * A jatek fo vezerloosztalya.
 * <p>
 * A {@link Game} tartja szamon a varost ({@link City}), a jatekosokat
 * ({@link Player}) es a korszamlalot. Minden korban eloszor a jatekosok
 * lepnek ({@link Player#takeTurn()}), majd a varos vegrehajtja a fizikai
 * lepest ({@link City#executeStep()}).
 * </p>
 */
public class Game {

    /** A szimulalt varos, amely az utakat, savokat es jarmueket tartalmazza. */
    private City city = new City();

    /** A regisztralt jatekosok listaja (takarito jatekosok es buszsoforok). */
    private List<Player> players = new ArrayList<>();

    /** Az eltelt korok szama. */
    private int roundNumber = 0;

    /** Az aktuálisan soron lévő játékos indexe. */
    private int currentPlayerIndex = 0;

    /**
     * Visszaadja a varost.
     *
     * @return a szimulalt varos
     */
    public City getCity() { return city; }

    /**
     * Visszaadja az osszes regisztralt jatekost.
     *
     * @return a jatekosok listaja
     */
    public List<Player> getPlayers() { return players; }

    /**
     * Visszaadja az aktualis korszamot.
     *
     * @return az eltelt korok szama
     */
    public int getRoundNumber() { return roundNumber; }

    /**
     * Hozzaad egy jatekost a jatekhoz.
     *
     * @param p a hozzaadando jatekos
     */
    public void addPlayer(Player p) { players.add(p); }

    /**
     * Egy teljes kor vegrehajtasa: minden jatekos lep, majd a varos fizikai lepese.
     */
    public void nextRound() {
        for (Player p : players) p.takeTurn();
        city.executeStep();
        roundNumber++;
    }

    /**
     * Megvizsgalja, hogy a jatek veget ert-e.
     * A jatek akkor er veget, ha az osszes jarmű blokkolt allapotban van.
     *
     * @return {@code true}, ha minden jarmu blokkolt
     */
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

    /**
     * Visszaadja az aktuálisan soron lévő játékost.
     *
     * @return az aktuális játékos, vagy {@code null} ha nincs játékos
     */
    public Player getCurrentPlayer() {
        if (players.isEmpty()) return null;
        return players.get(currentPlayerIndex);
    }

    /**
     * Visszaadja az aktuális játékos indexét.
     *
     * @return az index
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Továbblép a következő játékosra. Ha az utolsó játékos is lépett,
     * visszaáll az elsőre és végrehajtja a kört (hóesés, járművek léptetése).
     */
    public void nextPlayer() {
        if (players.isEmpty()) return;
        city.applySnowfall(1);
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
            city.executeStep();
            roundNumber++;
        }
    }
}
