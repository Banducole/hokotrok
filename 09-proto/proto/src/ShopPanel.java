import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Jobb oldali vasarlasi panel. Fej, uzemanyag es hokotro vasarlasi gombok.
 * Inaktiv (szurke), ha nem CleanerPlayer van soron.
 */
public class ShopPanel extends JPanel {

    private final Game game;
    private boolean active = false;
    private final List<JButton> allButtons = new ArrayList<>();

    private JButton btnSweep, btnThrow, btnIceBreaker, btnSalt, btnDragon, btnRock;
    private JButton btnFuelSalt, btnFuelKerosene, btnFuelRock;
    private JButton btnNewPlow;
    private JButton btnNextPlayer;
    private JButton btnSnowfall;

    private static final int PANEL_WIDTH = 200;

    public ShopPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(PANEL_WIDTH, 0));
        setBackground(new Color(35, 40, 48));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initComponents();
    }

    private void initComponents() {
        addSectionLabel("Fej vasarlas");
        btnSweep = addButton("Sopro ($" + Constants.PRICE_SWEEP_HEAD + ")", "BUY_SWEEP");
        btnThrow = addButton("Hanyo ($" + Constants.PRICE_THROW_HEAD + ")", "BUY_THROW");
        btnIceBreaker = addButton("Jegtoro ($" + Constants.PRICE_ICEBREAKER_HEAD + ")", "BUY_ICEBREAKER");
        btnSalt = addButton("Soszoro ($" + Constants.PRICE_SALT_HEAD + ")", "BUY_SALT_HEAD");
        btnDragon = addButton("Sarkany ($" + Constants.PRICE_DRAGON_HEAD + ")", "BUY_DRAGON");
        btnRock = addButton("Zuzalek ($" + Constants.PRICE_ROCK_HEAD + ")", "BUY_ROCK_HEAD");

        add(Box.createVerticalStrut(10));
        addSectionLabel("Uzemanyag toltes");
        btnFuelSalt = addButton("So toltes x5 ($" + Constants.PRICE_SALT_UNIT * 5 + ")", "FUEL_SALT");
        btnFuelKerosene = addButton("Kerozin x5 ($" + Constants.PRICE_KEROSENE_UNIT * 5 + ")", "FUEL_KEROSENE");
        btnFuelRock = addButton("Zuzalek x5 ($" + Constants.PRICE_ROCK_UNIT * 5 + ")", "FUEL_ROCK");

        add(Box.createVerticalStrut(10));
        addSectionLabel("Egyeb");
        btnNewPlow = addButton("Uj hokotro ($" + Constants.PRICE_SNOWPLOW + ")", "BUY_PLOW");

        add(Box.createVerticalStrut(20));
        addSeparator();
        btnSnowfall = addButton("Hoeses inditasa", "SNOWFALL");
        allButtons.remove(btnSnowfall);

        add(Box.createVerticalStrut(10));
        btnNextPlayer = addButton("Kovetkezo jatekos >>", "NEXT_PLAYER");
        allButtons.remove(btnNextPlayer);
        btnNextPlayer.setBackground(new Color(0, 130, 0));
        btnNextPlayer.setForeground(Color.WHITE);

        add(Box.createVerticalGlue());
    }

    private void addSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(200, 200, 200));
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(8, 10, 4, 0));
        add(label);
    }

    private void addSeparator() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        sep.setForeground(new Color(80, 80, 80));
        add(sep);
    }

    private JButton addButton(String text, String actionCommand) {
        JButton btn = new JButton(text);
        btn.setActionCommand(actionCommand);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(PANEL_WIDTH - 20, 30));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        add(btn);
        add(Box.createVerticalStrut(3));
        allButtons.add(btn);
        return btn;
    }

    public void setActive(boolean active) {
        this.active = active;
        for (JButton btn : allButtons) {
            btn.setEnabled(active);
        }
        repaint();
    }

    public void update() {
        Player current = game.getCurrentPlayer();
        setActive(current instanceof CleanerPlayer);
    }

    public void registerActionListener(ActionListener listener) {
        for (JButton btn : allButtons) {
            btn.addActionListener(listener);
        }
        btnNextPlayer.addActionListener(listener);
        btnSnowfall.addActionListener(listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!active) {
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
