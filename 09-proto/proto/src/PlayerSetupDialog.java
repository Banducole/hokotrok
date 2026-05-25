import javax.swing.*;
import java.awt.*;

public class PlayerSetupDialog extends JDialog {

    private int playerCount = 4;
    private JTextField[] nameFields;
    private JRadioButton[] cleanerRadios;
    private JRadioButton[] busRadios;
    private JPanel playersPanel;
    private boolean confirmed = false;

    public PlayerSetupDialog(JFrame parent) {
        super(parent, "Jatekosok Beallitasa", true);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createTitledBorder("Jatekosok Beallitasa"));

        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        countPanel.add(new JLabel("Jatekosok szama:"));
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

        JButton startButton = new JButton("Jatek inditasa");
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

    private void rebuildPlayersPanel() {
        playersPanel.removeAll();
        nameFields = new JTextField[playerCount];
        cleanerRadios = new JRadioButton[playerCount];
        busRadios = new JRadioButton[playerCount];

        String[] defaultNames = {"Kovacs Peter", "Nagy Imre", "Szabo Bela", "Molnar Andrea"};
        boolean[] defaultCleaner = {true, false, false, true};

        for (int i = 0; i < playerCount; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            row.add(new JLabel("Jatekos " + (i + 1) + ":"));

            nameFields[i] = new JTextField(i < defaultNames.length ? defaultNames[i] : "Jatekos" + (i + 1), 15);
            row.add(nameFields[i]);

            cleanerRadios[i] = new JRadioButton("Hokotro");
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

    public boolean isConfirmed() { return confirmed; }

    public String[] getPlayerNames() {
        String[] names = new String[playerCount];
        for (int i = 0; i < playerCount; i++) {
            names[i] = nameFields[i].getText().trim();
            if (names[i].isEmpty()) names[i] = "Jatekos" + (i + 1);
        }
        return names;
    }

    public boolean[] getIsCleaner() {
        boolean[] result = new boolean[playerCount];
        for (int i = 0; i < playerCount; i++) {
            result[i] = cleanerRadios[i].isSelected();
        }
        return result;
    }
}
