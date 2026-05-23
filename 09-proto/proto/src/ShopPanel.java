import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ShopPanel extends JPanel {

    private final Game game;
    private boolean active = false;
    private final List<JButton> allButtons = new ArrayList<>();

    private JButton btnSweep, btnThrow, btnIceBreaker, btnSalt, btnDragon, btnRock;
    private JButton btnFuelSalt, btnFuelKerosene, btnFuelRock;
    private JButton btnNewPlow;
    private JButton btnNextPlayer;
    private JButton btnSnowfall;

    private static final int PANEL_WIDTH = 250;

    public ShopPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(PANEL_WIDTH, 0));
        setBackground(new Color(195, 200, 208));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initComponents();
    }

    private void initComponents() {
        add(Box.createVerticalStrut(8));

        btnFuelSalt = addShopRow("So", "Vasarlas", "FUEL_SALT", new Color(240, 240, 240));
        btnFuelKerosene = addShopRow("Kerozin", "Vasarlas", "FUEL_KEROSENE", new Color(200, 50, 50));
        btnFuelRock = addShopRow("Ko", "Vasarlas", "FUEL_ROCK", new Color(150, 150, 150));

        add(Box.createVerticalStrut(8));
        addSeparator();
        add(Box.createVerticalStrut(4));

        btnSweep = addHeadRow("Soprofej", Constants.PRICE_SWEEP_HEAD, "BUY_SWEEP", new Color(100, 180, 80));
        btnThrow = addHeadRow("Hanyofej", Constants.PRICE_THROW_HEAD, "BUY_THROW", new Color(220, 150, 50));
        btnIceBreaker = addHeadRow("Jegtorofej", Constants.PRICE_ICEBREAKER_HEAD, "BUY_ICEBREAKER", new Color(140, 180, 210));
        btnSalt = addHeadRow("Soszorofej", Constants.PRICE_SALT_HEAD, "BUY_SALT_HEAD", new Color(80, 130, 200));
        btnDragon = addHeadRow("Sarkanyfej", Constants.PRICE_DRAGON_HEAD, "BUY_DRAGON", new Color(210, 50, 50));
        btnRock = addHeadRow("Koszorofej", Constants.PRICE_ROCK_HEAD, "BUY_ROCK_HEAD", new Color(160, 130, 90));

        add(Box.createVerticalGlue());

        addSeparator();
        add(Box.createVerticalStrut(6));

        btnNextPlayer = createStyledButton("Kovetkezo jatekos", "NEXT_PLAYER");
        btnNextPlayer.setBackground(new Color(180, 185, 192));
        btnNextPlayer.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnNextPlayer.setMaximumSize(new Dimension(PANEL_WIDTH - 20, 36));
        addCentered(btnNextPlayer);
        allButtons.remove(btnNextPlayer);

        add(Box.createVerticalStrut(4));

        btnNewPlow = createStyledButton("Uj hokotro", "BUY_PLOW");
        btnNewPlow.setBackground(new Color(180, 185, 192));
        btnNewPlow.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnNewPlow.setMaximumSize(new Dimension(PANEL_WIDTH - 20, 32));
        addCentered(btnNewPlow);

        add(Box.createVerticalStrut(4));

        btnSnowfall = createStyledButton("Hoeses", "SNOWFALL");
        btnSnowfall.setBackground(new Color(180, 185, 192));
        btnSnowfall.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnSnowfall.setMaximumSize(new Dimension(PANEL_WIDTH - 20, 28));
        addCentered(btnSnowfall);
        allButtons.remove(btnSnowfall);

        add(Box.createVerticalStrut(8));
    }

    private JButton addShopRow(String label, String btnText, String cmd, Color iconColor) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(PANEL_WIDTH - 10, 40));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        JPanel icon = new IconPanel(iconColor, 30, 30);
        row.add(icon);
        row.add(Box.createHorizontalStrut(8));

        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        row.add(nameLabel);
        row.add(Box.createHorizontalGlue());

        JButton btn = new JButton(btnText);
        btn.setActionCommand(cmd);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btn.setFocusPainted(false);
        btn.setMargin(new Insets(2, 8, 2, 8));
        row.add(btn);
        allButtons.add(btn);

        add(row);
        add(Box.createVerticalStrut(2));
        return btn;
    }

    private JButton addHeadRow(String name, int price, String cmd, Color iconColor) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(PANEL_WIDTH - 10, 50));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        JPanel icon = new IconPanel(iconColor, 36, 36);
        row.add(icon);
        row.add(Box.createHorizontalStrut(6));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel priceLabel = new JLabel("(" + price + " Ft)");
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        textPanel.add(nameLabel);
        textPanel.add(priceLabel);
        row.add(textPanel);

        row.add(Box.createHorizontalGlue());

        JButton btn = new JButton(name);
        btn.setActionCommand(cmd);
        btn.setVisible(false);
        allButtons.add(btn);

        add(row);
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        row.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (active) btn.doClick();
            }
        });

        add(Box.createVerticalStrut(2));
        return btn;
    }

    private JButton createStyledButton(String text, String cmd) {
        JButton btn = new JButton(text);
        btn.setActionCommand(cmd);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(140, 140, 140)),
            BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        allButtons.add(btn);
        return btn;
    }

    private void addCentered(JComponent comp) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(PANEL_WIDTH, 44));
        wrapper.add(comp);
        add(wrapper);
    }

    private void addSeparator() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        sep.setForeground(new Color(150, 150, 150));
        add(sep);
    }

    public void setActive(boolean active) {
        this.active = active;
        for (JButton btn : allButtons) {
            btn.setEnabled(active);
        }
        btnNextPlayer.setEnabled(true);
        btnSnowfall.setEnabled(true);
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
            g.setColor(new Color(0, 0, 0, 60));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private static class IconPanel extends JPanel {
        private final Color color;

        IconPanel(Color color, int w, int h) {
            this.color = color;
            setPreferredSize(new Dimension(w, h));
            setMaximumSize(new Dimension(w, h));
            setMinimumSize(new Dimension(w, h));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 6, 6);
            g2d.setColor(color.darker());
            g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 6, 6);
        }
    }
}
