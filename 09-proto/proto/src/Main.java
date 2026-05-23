import javax.swing.*;

/**
 * A grafikus alkalmazas belepesi pontja.
 * Letrehozza a minta-palyat es megjeleniti a jatekot.
 * A regi Proto is elerheto marad: java -cp out Proto
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            Game game = MapBuilder.buildSampleGame();
            GameFrame frame = new GameFrame(game);

            JOptionPane.showMessageDialog(frame,
                "Udvozollek a Banducole jatekban!\n\n"
                + "Takarito jatekos:\n"
                + "  - Kattints egy hokotrodra a kijelolesehez\n"
                + "  - Kattints egy szomszedos savra a lepeshez es takaritashoz\n"
                + "  - A jobb oldali panelen vasarolhatsz fejeket es uzemanyagot\n\n"
                + "Buszsofor jatekos:\n"
                + "  - Kattints egy szomszedos savra a busz leptesehez\n\n"
                + "A 'Kovetkezo jatekos >>' gombbal adhatod at a kort.",
                "Jatekszabalyok",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
