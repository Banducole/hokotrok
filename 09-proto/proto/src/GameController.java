import javax.swing.*;
import java.awt.event.*;
import java.util.List;

/**
 * A felhasznaloi esemenyek (eger, gomb) ertelmezeseert es
 * modellbeli muveletekre forditasaert felelos controller.
 */
public class GameController implements MouseListener, ActionListener {

    private final Game game;
    private final GameFrame frame;
    private SnowPlowView selectedPlowView;

    public GameController(Game game, GameFrame frame) {
        this.game = game;
        this.frame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Player current = game.getCurrentPlayer();
        if (current == null) return;

        GamePanel panel = frame.getGamePanel();
        int mx = e.getX();
        int my = e.getY();

        if (game.isGameOver()) return;

        if (current instanceof CleanerPlayer) {
            handleCleanerClick(mx, my, (CleanerPlayer) current, panel);
        } else if (current instanceof BusDriver) {
            handleBusDriverClick(mx, my, (BusDriver) current, panel);
        }

        frame.updateUI_game();
    }

    private void handleCleanerClick(int mx, int my, CleanerPlayer cp, GamePanel panel) {
        SnowPlowView clickedPlow = panel.findPlowViewAt(mx, my);

        if (clickedPlow != null && clickedPlow.getSnowPlow().getOwner() == cp) {
            if (selectedPlowView != null) {
                selectedPlowView.setSelected(false);
            }
            if (selectedPlowView == clickedPlow) {
                selectedPlowView = null;
            } else {
                selectedPlowView = clickedPlow;
                selectedPlowView.setSelected(true);
            }
            return;
        }

        if (selectedPlowView != null) {
            LaneView clickedLane = panel.findLaneViewAt(mx, my);
            if (clickedLane != null) {
                SnowPlow plow = selectedPlowView.getSnowPlow();
                Lane targetLane = clickedLane.getLane();

                if (isAdjacentLane(plow.getCurrentLane(), targetLane)) {
                    plow.setTargetLane(targetLane);
                    plow.step();
                    selectedPlowView.setSelected(false);
                    selectedPlowView = null;
                    game.nextPlayer();
                }
            }
        }
    }

    private void handleBusDriverClick(int mx, int my, BusDriver bd, GamePanel panel) {
        LaneView clickedLane = panel.findLaneViewAt(mx, my);
        if (clickedLane == null) return;

        Bus bus = bd.getBus();
        Lane targetLane = clickedLane.getLane();

        if (bus.isBlocked()) return;

        Lane currentBusLane = bus.getCurrentLane();
        if (currentBusLane != null && currentBusLane.getState() instanceof ThickSnowState) return;

        if (!isAdjacentLane(bus.getCurrentLane(), targetLane)) return;

        Lane oldLane = bus.getCurrentLane();
        if (oldLane != null) oldLane.removeVehicle(bus);
        targetLane.accept(bus);
        bus.setCurrentLane(targetLane);

        Road road = targetLane.getRoad();
        if (road != null) {
            Node arrival = road.getTo();
            if (arrival instanceof Terminal) {
                Terminal t = (Terminal) arrival;
                if (t == bus.getTerminalEnd()) {
                    bus.arrivedAtTerminal();
                }
            }
            Node from = road.getFrom();
            if (from instanceof Terminal) {
                Terminal t = (Terminal) from;
                if (t == bus.getTerminalEnd()) {
                    bus.arrivedAtTerminal();
                }
            }
        }

        if (targetLane.isSlippery()) {
            List<Vehicle> vehicles = targetLane.getVehicles();
            for (Vehicle v : vehicles) {
                if (v != bus) {
                    bus.meetVehicle(v);
                    break;
                }
            }
        }

        game.nextPlayer();
    }

    private boolean isAdjacentLane(Lane current, Lane target) {
        if (current == null || target == null) return false;
        if (current == target) return false;

        Road currentRoad = current.getRoad();
        if (currentRoad != null && currentRoad == target.getRoad()) {
            return true;
        }

        if (currentRoad != null) {
            Node toNode = currentRoad.getTo();
            Node fromNode = currentRoad.getFrom();
            Road targetRoad = target.getRoad();
            if (targetRoad != null) {
                if (toNode == targetRoad.getFrom() || toNode == targetRoad.getTo()
                    || fromNode == targetRoad.getFrom() || fromNode == targetRoad.getTo()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if ("NEXT_PLAYER".equals(cmd)) {
            if (selectedPlowView != null) {
                selectedPlowView.setSelected(false);
                selectedPlowView = null;
            }
            game.nextPlayer();
            checkGameOver();
            frame.updateUI_game();
            return;
        }

        if ("SNOWFALL".equals(cmd)) {
            game.getCity().applySnowfall(1);
            frame.updateUI_game();
            return;
        }

        Player current = game.getCurrentPlayer();
        if (!(current instanceof CleanerPlayer)) return;
        CleanerPlayer cp = (CleanerPlayer) current;

        SnowPlow plow = getSelectedPlow();

        switch (cmd) {
            case "BUY_SWEEP":
                if (plow != null) cp.buyHead(plow, new SweepHead());
                break;
            case "BUY_THROW":
                if (plow != null) cp.buyHead(plow, new ThrowHead());
                break;
            case "BUY_ICEBREAKER":
                if (plow != null) cp.buyHead(plow, new IceBreakerHead());
                break;
            case "BUY_SALT_HEAD":
                if (plow != null) cp.buyHead(plow, new SaltHead());
                break;
            case "BUY_DRAGON":
                if (plow != null) cp.buyHead(plow, new DragonHead());
                break;
            case "BUY_ROCK_HEAD":
                if (plow != null) cp.buyHead(plow, new RockHead());
                break;
            case "FUEL_SALT":
                if (plow != null) cp.buyFuel(plow, new Salt(), 5);
                break;
            case "FUEL_KEROSENE":
                if (plow != null) cp.buyFuel(plow, new Kerosene(), 5);
                break;
            case "FUEL_ROCK":
                if (plow != null) cp.buyFuel(plow, new Rock(), 5);
                break;
            case "BUY_PLOW":
                SnowPlow refPlow = plow != null ? plow : getFirstPlow(cp);
                if (refPlow != null) {
                    SnowPlow newPlow = cp.buyNewPlow(refPlow, "hk_new", game.getCity());
                    if (newPlow != null) {
                        addNewPlowView(newPlow);
                    }
                }
                break;
        }

        frame.updateUI_game();
    }

    private SnowPlow getSelectedPlow() {
        if (selectedPlowView != null) {
            return selectedPlowView.getSnowPlow();
        }
        Player current = game.getCurrentPlayer();
        if (current instanceof CleanerPlayer) {
            CleanerPlayer cp = (CleanerPlayer) current;
            if (cp.getPlows().size() == 1) {
                return cp.getPlows().get(0);
            }
        }
        return null;
    }

    private SnowPlow getFirstPlow(CleanerPlayer cp) {
        if (!cp.getPlows().isEmpty()) return cp.getPlows().get(0);
        return null;
    }

    private void addNewPlowView(SnowPlow plow) {
        GamePanel panel = frame.getGamePanel();
        SnowPlowView spv = new SnowPlowView(plow, panel.getLaneViewMap());
        panel.getPlowViews().add(spv);
        panel.addDrawable(spv);
    }

    private void checkGameOver() {
        if (game.isGameOver()) {
            JOptionPane.showMessageDialog(frame,
                "A jatek veget ert!\nMinden jarmu blokkolt allapotba kerult.\nKor: " + game.getRoundNumber(),
                "Jatek vege",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
