import javax.swing.*;

public class Main {

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
