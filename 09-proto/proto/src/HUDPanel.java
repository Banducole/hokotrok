import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A játék státuszadatainak (HUD - Heads-Up Display) megjelenítéséért felelős grafikus panel a Hókotrók projektben.
 * <p>
 * A {@link JPanel} leszármazottjaként ez az osztály rajzolja ki a képernyő tetején
 * látható információs sávot. Itt jelennek meg a játékosok ({@link Player}) kártyái,
 * az aktuális játékos vizuális kiemelése, valamint a hozzájuk tartozó részletes adatok:
 * takarítók ({@link CleanerPlayer}) esetén a pénzösszeg és a kiválasztott hókotró üzemanyagszintje,
 * buszvezetők ({@link BusDriver}) esetén pedig a teljesített körök száma és a busz állapota
 * (pl. karambol vagy elakadás mély hóban).
 * </p>
 */
public class HUDPanel extends JPanel {

    /** A játék belső logikáját és állapotát tároló modell objektum. */
    private final Game game;
    
    /** A felhasználói interakciókat és a játéklogikát összekötő vezérlő. */
    private GameController controller;
    
    /** A HUD panel fix magassága pixelben. */
    private static final int PANEL_HEIGHT = 80;

    /**
     * Létrehozza a státusz panelt a megadott játékmodell alapján.
     * <p>
     * Beállítja a panel preferált méretét ({@value #PANEL_HEIGHT} pixel magasság)
     * és a sötétszürke háttérszínt.
     * </p>
     *
     * @param game a játék belső állapotát és logikáját tartalmazó {@link Game} modellpéldány
     */
    public HUDPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(0, PANEL_HEIGHT));
        setBackground(new Color(55, 60, 68));
    }

    /**
     * Beállítja a játékvezérlőt, amellyel a panel képes lekérdezni a specifikus
     * interakciós adatokat (pl. az aktuálisan kiválasztott hókotrót).
     *
     * @param controller a {@link GameController} példány
     */
    public void setController(GameController controller) { 
        this.controller = controller; 
    }

    /**
     * Frissíti a HUD panelt (újrarajzolást kér a Swing-től).
     */
    public void update() { 
        repaint(); 
    }

    /**
     * A panel tényleges kirajzolását végző metódus.
     *
     * Végigiterál a játékosok listáján, és mindenkinek rajzol egy információs "kártyát".
     * Az éppen soron lévő játékost zöld kerettel emeli ki. A kártyákon megjeleníti
     * a játékos nevét és típusát (takarító vagy buszvezető). Ezen felül dinamikusan 
     * lekérdezi és kiírja a típusfüggő adatokat: 
     * 
     * {@link CleanerPlayer}: Aktuális egyenleg, illetve a kiválasztott hókotró speciális fejének ({@link SaltHead}, {@link DragonHead}, {@link RockHead}) üzemanyagszintje.
     * {@link BusDriver}: A busz ({@link Bus}) által befejezett körök száma, valamint az esetleges akadályoztatás ({@link ThickSnowState} miatti elakadás vagy karambol).
     * 
     * @param g a {@link Graphics} kontextus
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<Player> players = game.getPlayers();
        if (players.isEmpty()) return;

        int currentIdx = game.getCurrentPlayerIndex();
        int totalWidth = getWidth();
        int cardWidth = Math.min(320, (totalWidth - 20) / players.size() - 10);
        int cardHeight = 60;
        int y = 10;

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            int cx = 10 + i * (cardWidth + 10);

            if (i == currentIdx) {
                g2d.setColor(new Color(0, 200, 0));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(cx - 3, y - 3, cardWidth + 6, cardHeight + 6, 10, 10);
                g2d.setStroke(new BasicStroke(1));
            }

            g2d.setColor(new Color(45, 50, 58));
            g2d.fillRoundRect(cx, y, cardWidth, cardHeight, 8, 8);

            String name = p.getName() != null ? p.getName() : "Játékos " + (i + 1);
            String type = (p instanceof CleanerPlayer) ? " (Tak.)" : " (Busz.)";

            g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2d.setColor(Color.WHITE);
            g2d.drawString(name + type, cx + 10, y + 20);

            if (p instanceof CleanerPlayer) {
                CleanerPlayer cp = (CleanerPlayer) p;
                String info = "Pénz: " + cp.getBalance();

                if (i == currentIdx && controller != null) {
                    SnowPlow sel = controller.getSelectedPlow();
                    if (sel != null && sel.getOwner() == cp) {
                        CleanerHead head = sel.getHead();
                        if (head instanceof SaltHead) {
                            info += " | Só: " + head.fuelLevel();
                        } else if (head instanceof DragonHead) {
                            info += " | Kerozin: " + head.fuelLevel();
                        } else if (head instanceof RockHead) {
                            info += " | Zúzalék: " + head.fuelLevel();
                        }
                    }
                }

                g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2d.setColor(new Color(200, 210, 220));
                g2d.drawString(info, cx + 10, y + 40);

            } else if (p instanceof BusDriver) {
                Bus bus = ((BusDriver) p).getBus();
                
                String status = "Teljesített körök: " + bus.getCompletedRounds();
                
                if (bus.isBlocked()) {
                    status += " | Karambol (kimarad)";
                } else {
                    Lane busLane = bus.getCurrentLane();
                    if (busLane != null && busLane.getState() instanceof ThickSnowState) {
                        status += " | Elakadt";
                    }
                }
                
                g2d.setFont(new Font("SansSerif", Font.ITALIC, 11));
                g2d.setColor(new Color(255, 180, 60));
                g2d.drawString(status, cx + 10, y + 40);
            }
        }
    }
}