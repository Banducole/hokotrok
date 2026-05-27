import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Hókotrók játék boltját és az akciókat tartalmazó oldalsó grafikus panel.
 * * Ez a {@link JPanel} leszármazott felelős a fejlesztések (hókotró fejek), 
 * üzemanyagok (só, kerozin, zúzalék) vásárlásának, valamint az általános 
 * játékakciók (következő játékos, hóesés, új hókotró vásárlása) megjelenítéséért. 
 * A panel dinamikusan frissül az éppen soron lévő játékos típusától (takarító vagy busz) 
 * és a kiválasztott hókotró felszereltségétől függően.
 */
public class ShopPanel extends JPanel {

    /** A játék belső logikáját tároló modell objektum. */
    private final Game game;
    
    /** A felhasználói interakciókat kezelő vezérlő. */
    private GameController controller;
    
    /** Jelzi, hogy a panel aktuálisan aktív-e (általában csak takarító játékos körében az). */
    private boolean active = false;
    
    /** Az összes regisztrált gomb listája az eseménykezelők egyszerűbb kiosztásához. */
    private final List<JButton> allButtons = new ArrayList<>();

    // Felszerelés és akció gombok
    private JButton btnSweep, btnThrow, btnIceBreaker, btnSalt, btnDragon, btnRock;
    private JButton btnFuelSalt, btnFuelKerosene, btnFuelRock;
    private JButton btnNewPlow;
    private JButton btnNextPlayer;
    private JButton btnSnowfall;

    // Üzemanyag vásárló sorok (dinamikusan elrejtésre/megjelenítésre kerülnek)
    private JPanel rowFuelSalt, rowFuelKerosene, rowFuelRock;
    private JPanel lastRow;

    /** A felszerelt fejek melletti pipákat tároló térkép, a parancs (cmd) alapján. */
    private final Map<String, JLabel> headCheckmarks = new HashMap<>();

    /** A panel fix szélessége pixelben. */
    private static final int PANEL_WIDTH = 250;

    // Statikus képek a bolt ikonjaihoz
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

    /**
     * Létrehozza a bolt panelt a megadott játékmodell alapján.
     * * @param game a {@link Game} modellpéldány
     */
    public ShopPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(PANEL_WIDTH, 0));
        setBackground(new Color(195, 200, 208));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initComponents();
    }

    /**
     * Inicializálja és elrendezi a bolt panel összes grafikus komponensét.
     * Létrehozza az üzemanyag- és fejvásárló sorokat, valamint az alsó vezérlőgombokat.
     */
    private void initComponents() {
        add(Box.createVerticalStrut(8));

        btnFuelSalt = addShopRow("Só", "Vásárlás", "FUEL_SALT", imgFuelSalt, Constants.PRICE_SALT_UNIT * 5);
        rowFuelSalt = lastRow;
        btnFuelKerosene = addShopRow("Kerozin", "Vásárlás", "FUEL_KEROSENE", imgFuelKerosene, Constants.PRICE_KEROSENE_UNIT * 5);
        rowFuelKerosene = lastRow;
        btnFuelRock = addShopRow("Zúzalék", "Vásárlás", "FUEL_ROCK", imgFuelRock, Constants.PRICE_ROCK_UNIT * 5);
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

    /**
     * Létrehoz és hozzáad egy általános vásárlói sort (pl. üzemanyaghoz) a panelhez.
     * * @param label   a termék neve (pl. "Só")
     * @param btnText a gomb felirata (pl. "Vásárlás")
     * @param cmd     a gombhoz tartozó egyedi parancs (ActionCommand)
     * @param iconImg a termék ikonja
     * @param price   a termék ára
     * @return a létrehozott vásárlás gomb
     */
    private JButton addShopRow(String label, String btnText, String cmd, BufferedImage iconImg, int price) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(PANEL_WIDTH - 10, 50));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        JPanel icon = new IconPanel(iconImg, 30, 30);
        row.add(icon);
        row.add(Box.createHorizontalStrut(8));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        JLabel nameLabel = new JLabel(label);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        JLabel priceLabel = new JLabel("(" + price + " Ft)");
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        textPanel.add(nameLabel);
        textPanel.add(priceLabel);
        row.add(textPanel);
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

    /**
     * Létrehoz egy speciális sort a hókotró fejek kiválasztásához és vásárlásához.
     * A sor teljes egésze kattintható, és támogatja a "felszerelt" állapotot jelző pipát.
     * * @param name    a fej neve
     * @param price   a fej ára
     * @param cmd     a művelet parancsa (ActionCommand)
     * @param iconImg a fej ikonja
     * @return a sorhoz tartozó rejtett gomb, ami az eseményt kiváltja
     */
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

    /**
     * Stílusozott (szegéllyel és megfelelő margókkal rendelkező) vezérlőgombot hoz létre.
     * * @param text a gomb felirata
     * @param cmd  a gomb eseményparancsa
     * @return az elkészült gomb
     */
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

    /**
     * Középre igazítva ad hozzá egy komponenst a panelhez.
     * * @param comp a hozzáadandó grafikus elem
     */
    private void addCentered(JComponent comp) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(PANEL_WIDTH, 44));
        wrapper.add(comp);
        add(wrapper);
    }

    /**
     * Hozzáad egy esztétikus, vízszintes elválasztó vonalat a panelhez.
     */
    private void addSeparator() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        sep.setForeground(new Color(150, 150, 150));
        add(sep);
    }

    /**
     * Beállítja a panel aktív vagy inaktív állapotát. Inaktív állapotban 
     * a legtöbb gomb letiltásra kerül (kivéve a körpasszolás és hóesés).
     * * @param active true, ha a bolt aktív, false ha nem
     */
    public void setActive(boolean active) {
        this.active = active;
        for (JButton btn : allButtons) {
            btn.setEnabled(active);
        }
        btnNextPlayer.setEnabled(true);
        btnSnowfall.setEnabled(true);
        repaint();
    }

    /**
     * Beregisztrálja a megadott vezérlőt.
     * * @param c a {@link GameController} példány
     */
    public void setController(GameController c) { 
        this.controller = c; 
    }

    /**
     * Frissíti a bolt grafikus felületét a játék aktuális állapota alapján.
     * Ellenőrzi, hogy takarító játékos van-e soron, beállítja az aktív fejet 
     * jelző pipát, valamint csak azt az üzemanyagvásárló sort teszi láthatóvá, 
     * amelyik a felszerelt speciális fejhez (só, kerozin, zúzalék) szükséges.
     */
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

    /**
     * Beregisztrálja a megadott eseménykezelőt a panel összes gombjához.
     * * @param listener a külső {@link ActionListener} (általában a controller)
     */
    public void registerActionListener(ActionListener listener) {
        for (JButton btn : allButtons) {
            btn.addActionListener(listener);
        }
        btnNextPlayer.addActionListener(listener);
        btnSnowfall.addActionListener(listener);
    }

    /**
     * A panel egyedi rajzolásáért felelős metódus. Ha a panel inaktív, 
     * egy áttetsző, sötétítő réteget húz az egész területre.
     * * @param g a {@link Graphics} kontextus
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!active) {
            g.setColor(new Color(0, 0, 0, 60));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * Belső segédosztály a képek méretezett és élsimított megjelenítésére a boltban.
     * Ha a kép hiányzik, egy szürke, fallback (helyettesítő) négyzetet rajzol.
     */
    private static class IconPanel extends JPanel {
        
        /** A kirajzolandó kép. */
        private final BufferedImage img;

        /**
         * Létrehoz egy fix méretű ikonpanelt.
         * * @param img a megjelenítendő kép
         * @param w   a panel szélessége
         * @param h   a panel magassága
         */
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