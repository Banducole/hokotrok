import javax.swing.*;

/**
 * A Hókotrók (Banducole - Városi hótakarító játék) alkalmazás belépési pontja.
 * 
 * Ez az osztály tartalmazza a {@code main} metódust, amely elindítja a grafikus 
 * felhasználói felületet (GUI) a Swing eseménykezelő szálán (EDT). Felelős a 
 * rendszer natív kinézetének (Look and Feel) beállításáért, a játékosokat 
 * konfiguráló ablak ({@link PlayerSetupDialog}) megjelenítéséért, valamint a 
 * kiválasztott beállítások alapján a játékmodell ({@link Game}) és a főablak 
 * ({@link GameFrame}) inicializálásáért.
 * 
 */
public class Main {

    /**
     * Az alkalmazás fő indító metódusa.
     * 
     * A futás a következő lépésekből áll:
     * 
     * A grafikus felület inicializálásának ütemezése a {@link SwingUtilities#invokeLater(Runnable)} segítségével.
     * A rendszer alapértelmezett megjelenésének (System LookAndFeel) beállítása a natív élmény érdekében.
     * A játékos-beállító dialógus ({@link PlayerSetupDialog}) megnyitása és a felhasználói interakció megvárása.
     * A dialógus eredményétől függően a játékmodell létrehozása: ha a felhasználó megerősítette a beállításokat, 
     * akkor a {@link MapBuilder#buildGame(java.util.List, java.util.List)} hívódik meg, ellenkező esetben 
     * egy alapértelmezett tesztpálya ({@link MapBuilder#buildSampleGame()}) épül fel.
     * A fő játékablak ({@link GameFrame}) példányosítása és megjelenítése.
     *
     * @param args parancssori argumentumok (a jelenlegi implementáció nem használja)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            PlayerSetupDialog dialog = new PlayerSetupDialog(null);
            dialog.setVisible(true);

            Game game;
            if (dialog.isConfirmed()) {
                game = MapBuilder.buildGame(dialog.getPlayerNames(), dialog.getIsCleaner());
            } else {
                game = MapBuilder.buildSampleGame();
            }

            new GameFrame(game);
        });
    }
}