import javax.swing.*;
import java.awt.*;

/**
 * Modális dialógusablak a játékosok beállítására a játék indítása előtt.
 *
 * A {@link JDialog} leszármazottjaként ez az osztály biztosítja a grafikus 
 * felületet a játékosok számának (2, 3 vagy 4), nevüknek, valamint a 
 * szerepkörüknek (takarító/hókotró vagy buszvezető) kiválasztására.
 * A megadott adatokat a játék indításakor a modell és a pálya felépítéséhez 
 * használja fel a rendszer.
 * 
 */
public class PlayerSetupDialog extends JDialog {

    /** A kiválasztott játékosok száma (alapértelmezetten 4). */
    private int playerCount = 4;
    
    /** A játékosok nevét bekérő szövegmezők tömbje. */
    private JTextField[] nameFields;
    
    /** A takarító (hókotró) szerepkört kiválasztó rádiógombok tömbje. */
    private JRadioButton[] cleanerRadios;
    
    /** A buszvezető szerepkört kiválasztó rádiógombok tömbje. */
    private JRadioButton[] busRadios;
    
    /** A játékosok beviteli mezőit tartalmazó dinamikus panel. */
    private JPanel playersPanel;
    
    /** Jelzi, hogy a felhasználó a "Játék indítása" gombbal zárta-e be az ablakot. */
    private boolean confirmed = false;

    /**
     * Létrehozza a beállító dialógusablakot.
     * 
     * Inicializálja a fő elrendezést, létrehozza a játékosszámot kiválasztó 
     * legördülő menüt ({@link JComboBox}), valamint a start gombot. Automatikusan 
     * meghívja a {@link #rebuildPlayersPanel()} metódust az alapértelmezett 
     * beviteli mezők legenerálásához.
     *
     * @param parent a szülő ablak ({@link JFrame}), amelyhez a dialógus rögzítve van (lehet null is)
     */
    public PlayerSetupDialog(JFrame parent) {
        super(parent, "Játékosok beállítása", true);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createTitledBorder("Játékosok beállítása"));

        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        countPanel.add(new JLabel("Játékosok száma:"));
        JComboBox<Integer> countCombo = new JComboBox<>(new Integer[]{2, 3, 4});
        countCombo.setSelectedItem(4);
        countCombo.addActionListener(e -> {
            playerCount = (Integer) countCombo.getSelectedItem();
            rebuildPlayersPanel();
        });
        countPanel.add(countCombo);
        mainPanel.add(countPanel);

        playersPanel = new JPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        mainPanel.add(playersPanel);

        add(mainPanel, BorderLayout.CENTER);

        JButton startButton = new JButton("Játék indítása");
        startButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(startButton);
        add(btnPanel, BorderLayout.SOUTH);

        rebuildPlayersPanel();
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Újraépíti a játékosok adatait bekérő panelt a kiválasztott játékosszám alapján.
     * 
     * Ez a metódus törli a panel korábbi tartalmát, majd a {@link #playerCount} 
     * értékének megfelelően új szövegmezőket és rádiógombokat hoz létre. 
     * Kényelmi funkcióként előre kitölti a mezőket alapértelmezett nevekkel 
     * és váltakozó szerepkörökkel. Végül frissíti a felületet.
     * 
     */
    private void rebuildPlayersPanel() {
        playersPanel.removeAll();
        nameFields = new JTextField[playerCount];
        cleanerRadios = new JRadioButton[playerCount];
        busRadios = new JRadioButton[playerCount];

        String[] defaultNames = {"Kovács Péter", "Nagy Imre", "Szabó Béla", "Molnár Andrea"};
        boolean[] defaultCleaner = {true, false, false, true};

        for (int i = 0; i < playerCount; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            row.add(new JLabel("Játékos " + (i + 1) + ":"));

            nameFields[i] = new JTextField(i < defaultNames.length ? defaultNames[i] : "Játékos " + (i + 1), 15);
            row.add(nameFields[i]);

            cleanerRadios[i] = new JRadioButton("Hókotró");
            busRadios[i] = new JRadioButton("Busz");
            ButtonGroup bg = new ButtonGroup();
            bg.add(cleanerRadios[i]);
            bg.add(busRadios[i]);

            boolean isCleaner = i < defaultCleaner.length ? defaultCleaner[i] : (i % 2 == 0);
            cleanerRadios[i].setSelected(isCleaner);
            busRadios[i].setSelected(!isCleaner);

            row.add(cleanerRadios[i]);
            row.add(busRadios[i]);

            playersPanel.add(row);
        }

        playersPanel.revalidate();
        playersPanel.repaint();
        pack();
    }

    /**
     * Lekérdezi, hogy a beállításokat megerősítették-e.
     *
     * @return true, ha a felhasználó a "Játék indítása" gombra kattintott, egyébként false
     */
    public boolean isConfirmed() { 
        return confirmed; 
    }

    /**
     * Kigyűjti és visszaadja a szövegmezőkbe beírt játékosneveket.
     * Ha egy mezőt a felhasználó üresen hagy, automatikusan generál 
     * számára egy azonosítót (pl. "Játékos 1").
     *
     * @return a játékosok neveit tartalmazó tömb
     */
    public String[] getPlayerNames() {
        String[] names = new String[playerCount];
        for (int i = 0; i < playerCount; i++) {
            names[i] = nameFields[i].getText().trim();
            if (names[i].isEmpty()) names[i] = "Játékos " + (i + 1);
        }
        return names;
    }

    /**
     * Kigyűjti a rádiógombok állapotát, megállapítva minden játékos szerepkörét.
     *
     * @return egy logikai tömb, ahol a true érték a takarító (hókotró), a false pedig a buszvezető szerepkört jelzi
     */
    public boolean[] getIsCleaner() {
        boolean[] result = new boolean[playerCount];
        for (int i = 0; i < playerCount; i++) {
            result[i] = cleanerRadios[i].isSelected();
        }
        return result;
    }
}