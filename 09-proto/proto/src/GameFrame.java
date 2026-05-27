import javax.swing.*;
import java.awt.*;

/**
 * A "Banducole - Városi hótakarító játék" (Hókotrók projekt) fő ablakát ({@link JFrame}) reprezentáló osztály.
 * 
 * Ez az osztály fogja össze a grafikus felhasználói felület (GUI) főbb komponenseit, 
 * úgymint a játékteret ({@link GamePanel}), az állapotsort ({@link HUDPanel}) és a 
 * bolt panelt ({@link ShopPanel}). Továbbá felelős a modell ({@link Game}) és a 
 * vezérlő ({@link GameController}) összekapcsolásáért, valamint a grafikus 
 * felület frissítésének koordinálásáért.
 * 
 */
public class GameFrame extends JFrame {

    /** A játékteret (sávok, járművek, hó) megjelenítő grafikus panel. */
    private final GamePanel gamePanel;
    
    /** Az aktuális játékos adatait és a játék állapotát (körök, pénz, lépésszám) mutató panel. */
    private final HUDPanel hudPanel;
    
    /** A fejlesztéseket és vásárlási lehetőségeket (pl. só, seprű) biztosító panel. */
    private final ShopPanel shopPanel;
    
    /** A felhasználói interakciókat (kattintások, gombnyomások) és a játéklogikát összekötő vezérlő. */
    private final GameController controller;
    
    /** A játék belső logikáját és állapotát tároló modell objektum. */
    private final Game game;

    /**
     * Létrehozza a főablakot a megadott játékmodell alapján.
     * <p>
     * Inicializálja a paneleket ({@link GamePanel}, {@link HUDPanel}, {@link ShopPanel}) és a 
     * vezérlőt ({@link GameController}), majd meghívja a grafikus elemek elrendezéséért 
     * felelős {@link #initComponents()} metódust.
     * </p>
     *
     * @param game a játék belső állapotát és logikáját tartalmazó {@link Game} modellpéldány
     */
    public GameFrame(Game game) {
        super("Banducole - Városi hótakarító játék");
        this.game = game;
        this.gamePanel = new GamePanel(game);
        this.hudPanel = new HUDPanel(game);
        this.shopPanel = new ShopPanel(game);
        this.controller = new GameController(game, this);

        initComponents();
    }

    /**
     * Inicializálja és elrendezi a főablak grafikus komponenseit.
     * 
     * Beállítja a {@link BorderLayout} elrendezést, felhelyezi a paneleket a megfelelő 
     * pozíciókba (észak, közép, kelet), valamint beregisztrálja a vezérlőt ({@link GameController}) 
     * eseménykezelőként az érintett panelekhez (pl. egérkattintások a játéktéren, gombnyomások a boltban). 
     * Végül beállítja az ablak minimális méretét, középre igazítja és láthatóvá teszi.
     * 
     */
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(hudPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        add(shopPanel, BorderLayout.EAST);

        gamePanel.addMouseListener(controller);
        shopPanel.registerActionListener(controller);
        hudPanel.setController(controller);
        shopPanel.setController(controller);

        shopPanel.update();

        pack();
        setMinimumSize(new Dimension(1350, 800));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Frissíti a felhasználói felület összes releváns komponensét.
     * 
     * Újrarajzolja a játékteret, frissíti a HUD és a bolt panel adatait, valamint 
     * meghívja az aktív busz vizuális kiválasztásáért felelős {@link #updateBusSelection()} metódust.
     * Ezt a metódust általában a vezérlő ({@link GameController}) hívja meg azután, hogy a modellben 
     * állapotváltozás (pl. egy jármű lépett, vagy kör véget ért) történt.
     * 
     */
    public void updateUI_game() {
        hudPanel.update();
        shopPanel.update();
        updateBusSelection();
        gamePanel.repaint();
    }

    /**
     * Frissíti a buszok vizuális kiválasztási állapotát a játéktéren.
     * 
     * Lekérdezi az aktuális játékost ({@link Player}). Ha az aktuális játékos egy buszvezető 
     * ({@link BusDriver}), akkor megkeresi az ő hozzárendelt buszát ({@link Bus}). Végigmegy 
     * az összes járműnézeten a játéktéren, és a {@link BusView} példányoknál beállítja 
     * a kiválasztott állapotot ({@link BusView#setSelected(boolean)}) aszerint, hogy az
     * adott nézet az éppen aktív játékos buszát ábrázolja-e.
     * 
     */
    private void updateBusSelection() {
        Player current = game.getCurrentPlayer();
        Bus activeBus = null;
        if (current instanceof BusDriver) {
            activeBus = ((BusDriver) current).getBus();
        }
        for (VehicleView vv : gamePanel.getVehicleViews()) {
            if (vv instanceof BusView) {
                BusView bv = (BusView) vv;
                bv.setSelected(bv.getVehicle() == activeBus);
            }
        }
    }

    /**
     * Visszaadja a játékteret megjelenítő panelt.
     *
     * @return a {@link GamePanel} példány
     */
    public GamePanel getGamePanel() { return gamePanel; }
    
    /**
     * Visszaadja a státuszadatokat (HUD) megjelenítő panelt.
     *
     * @return a {@link HUDPanel} példány
     */
    public HUDPanel getHudPanel() { return hudPanel; }
    
    /**
     * Visszaadja a bolt és fejlesztések paneljét.
     *
     * @return a {@link ShopPanel} példány
     */
    public ShopPanel getShopPanel() { return shopPanel; }
    
    /**
     * Visszaadja a játék belső modelljét.
     *
     * @return a {@link Game} példány
     */
    public Game getGame() { return game; }
}