import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private final GamePanel gamePanel;
    private final HUDPanel hudPanel;
    private final ShopPanel shopPanel;
    private final GameController controller;
    private final Game game;

    public GameFrame(Game game) {
        super("Banducole - Varosi Hotakarito Jatek");
        this.game = game;
        this.gamePanel = new GamePanel(game);
        this.hudPanel = new HUDPanel(game);
        this.shopPanel = new ShopPanel(game);
        this.controller = new GameController(game, this);

        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(hudPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        add(shopPanel, BorderLayout.EAST);

        gamePanel.addMouseListener(controller);
        shopPanel.registerActionListener(controller);

        shopPanel.update();

        pack();
        setMinimumSize(new Dimension(1350, 800));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateUI_game() {
        hudPanel.update();
        shopPanel.update();
        gamePanel.repaint();
    }

    public GamePanel getGamePanel() { return gamePanel; }
    public HUDPanel getHudPanel() { return hudPanel; }
    public ShopPanel getShopPanel() { return shopPanel; }
    public Game getGame() { return game; }
}
