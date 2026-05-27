import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopPanel extends JPanel {

    private final Game game;
    private GameController controller;
    private boolean active = false;
    private final List<JButton> allButtons = new ArrayList<>();

    private JButton btnSweep, btnThrow, btnIceBreaker, btnSalt, btnDragon, btnRock;
    private JButton btnFuelSalt, btnFuelKerosene, btnFuelRock;
    private JButton btnNewPlow;
    private JButton btnNextPlayer;
    private JButton btnSnowfall;

    private JPanel rowFuelSalt, rowFuelKerosene, rowFuelRock;
    private JPanel lastRow;

    private final Map<String, JLabel> headCheckmarks = new HashMap<>();

    private static final int PANEL_WIDTH = 250;

    private static BufferedImage imgSweep, imgThrow, imgIceBreaker, imgSaltHead, imgDragon, imgRockHead;
    private static BufferedImage imgFuelSalt, imgFuelKerosene, imgFuelRock;
    private static BufferedImage imgPipa;
    private static ImageIcon iconPipa;

    static {
        try {
            imgSweep = ImageIO.read(ShopPanel.class.getResource("/images/soprofej.png"));
            imgThrow = ImageIO.read(ShopPanel.class.getResource("/images/hanyofej.png"));
            imgIceBreaker = ImageIO.read(ShopPanel.class.getResource("/images/jegtotro.png"));
            imgSaltHead = ImageIO.read(ShopPanel.class.getResource("/images/soszorofej.png"));
            imgDragon = ImageIO.read(ShopPanel.class.getResource("/images/sarkanyfej.png"));
            imgRockHead = ImageIO.read(ShopPanel.class.getResource("/images/kavicsszorofej.png"));

            imgFuelSalt = ImageIO.read(ShopPanel.class.getResource("/images/so.png"));
            imgFuelKerosene = ImageIO.read(ShopPanel.class.getResource("/images/kerozin.png"));
            imgFuelRock = ImageIO.read(ShopPanel.class.getResource("/images/kis_kavics.png"));

            imgPipa = ImageIO.read(ShopPanel.class.getResource("/images/pipa.png"));
            if (imgPipa != null) {
                iconPipa = new ImageIcon(imgPipa.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {
            System.err.println("ShopPanel: Egy vagy több kép nem található az /images mappában!");
        }
    }

    public ShopPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(PANEL_WIDTH, 0));
        setBackground(new Color(195, 200, 208));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initComponents();
    }

    private void initComponents() {
        add(Box.createVerticalStrut(8));

        btnFuelSalt = addShopRow("Só", "Vásárlás", "FUEL_SALT", imgFuelSalt);
        rowFuelSalt = lastRow;
        btnFuelKerosene = addShopRow("Kerozin", "Vásárlás", "FUEL_KEROSENE", imgFuelKerosene);
        rowFuelKerosene = lastRow;
        btnFuelRock = addShopRow("Zúzalék", "Vásárlás", "FUEL_ROCK", imgFuelRock);
        rowFuelRock = lastRow;

        add(Box.createVerticalStrut(8));
        addSeparator();
        add(Box.createVerticalStrut(4));

        btnSweep = addHeadRow("Söprőfej", Constants.PRICE_SWEEP_HEAD, "BUY_SWEEP", imgSweep);
        btnThrow = addHeadRow("Hányófej", Constants.PRICE_THROW_HEAD, "BUY_THROW", imgThrow);
        btnIceBreaker = addHeadRow("Jégtörőfej", Constants.PRICE_ICEBREAKER_HEAD, "BUY_ICEBREAKER", imgIceBreaker);
        btnSalt = addHeadRow("Sószórófej", Constants.PRICE_SALT_HEAD, "BUY_SALT_HEAD", imgSaltHead);
        btnDragon = addHeadRow("Sárkányfej", Constants.PRICE_DRAGON_HEAD, "BUY_DRAGON", imgDragon);
        btnRock = addHeadRow("Zúzalékszórófej", Constants.PRICE_ROCK_HEAD, "BUY_ROCK_HEAD", imgRockHead);

        add(Box.createVerticalGlue());

        addSeparator();
        add(Box.createVerticalStrut(6));

        btnNextPlayer = createStyledButton("Következő játékos", "NEXT_PLAYER");
        btnNextPlayer.setBackground(new Color(180, 185, 192));
        btnNextPlayer.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnNextPlayer.setMaximumSize(new Dimension(PANEL_WIDTH - 20, 36));
        addCentered(btnNextPlayer);
        allButtons.remove(btnNextPlayer);

        add(Box.createVerticalStrut(4));

        btnNewPlow = createStyledButton("Új hókotró", "BUY_PLOW");
        btnNewPlow.setBackground(new Color(180, 185, 192));
        btnNewPlow.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnNewPlow.setMaximumSize(new Dimension(PANEL_WIDTH - 20, 32));
        addCentered(btnNewPlow);

        add(Box.createVerticalStrut(4));

        btnSnowfall = createStyledButton("Hóesés", "SNOWFALL");
        btnSnowfall.setBackground(new Color(180, 185, 192));
        btnSnowfall.setFont(new Font("SansSerif", Font.PLAIN, 11));
        btnSnowfall.setMaximumSize(new Dimension(PANEL_WIDTH - 20, 28));
        addCentered(btnSnowfall);
        allButtons.remove(btnSnowfall);

        add(Box.createVerticalStrut(8));
    }

    private JButton addShopRow(String label, String btnText, String cmd, BufferedImage iconImg) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(PANEL_WIDTH - 10, 40));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        JPanel icon = new IconPanel(iconImg, 30, 30);
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

        this.lastRow = row;
        add(row);
        add(Box.createVerticalStrut(2));
        return btn;
    }

    private JButton addHeadRow(String name, int price, String cmd, BufferedImage iconImg) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(PANEL_WIDTH - 10, 50));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        JPanel icon = new IconPanel(iconImg, 36, 36);
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

        JLabel checkmark = new JLabel(iconPipa);
        checkmark.setVisible(false);
        headCheckmarks.put(cmd, checkmark);
        row.add(checkmark);
        row.add(Box.createHorizontalStrut(8));

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

    public void setController(GameController c) { this.controller = c; }

    public void update() {
        Player current = game.getCurrentPlayer();
        boolean isCleanerTurn = current instanceof CleanerPlayer;
        setActive(isCleanerTurn);

        boolean showSalt = false, showKerosene = false, showRock = false;
        String activeHeadCmd = "";

        if (isCleanerTurn && controller != null) {
            SnowPlow sel = controller.getSelectedPlow();
            if (sel != null) {
                CleanerHead head = sel.getHead();
                if (head != null) {
                    if (head instanceof SweepHead) activeHeadCmd = "BUY_SWEEP";
                    else if (head instanceof ThrowHead) activeHeadCmd = "BUY_THROW";
                    else if (head instanceof IceBreakerHead) activeHeadCmd = "BUY_ICEBREAKER";
                    else if (head instanceof SaltHead) {
                        activeHeadCmd = "BUY_SALT_HEAD";
                        showSalt = true;
                    }
                    else if (head instanceof DragonHead) {
                        activeHeadCmd = "BUY_DRAGON";
                        showKerosene = true;
                    }
                    else if (head instanceof RockHead) {
                        activeHeadCmd = "BUY_ROCK_HEAD";
                        showRock = true;
                    }
                }
            }
        }

        if (rowFuelSalt != null) rowFuelSalt.setVisible(showSalt);
        if (rowFuelKerosene != null) rowFuelKerosene.setVisible(showKerosene);
        if (rowFuelRock != null) rowFuelRock.setVisible(showRock);

        for (Map.Entry<String, JLabel> entry : headCheckmarks.entrySet()) {
            entry.getValue().setVisible(entry.getKey().equals(activeHeadCmd));
        }

        revalidate();
        repaint();
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
        private final BufferedImage img;

        IconPanel(BufferedImage img, int w, int h) {
            this.img = img;
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
            
            if (img != null) {
                g2d.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            } else {
                g2d.setColor(Color.GRAY);
                g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 6, 6);
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 6, 6);
            }
        }
    }
}