import javax.swing.*;

/**
 * A grafikus alkalmazas belepesi pontja.
 * Letrehozza a minta-palyat es megjeleníti a jatekot.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = MapBuilder.buildSampleGame();
            new GameFrame(game);
        });
    }
}
