package skeleton;

/**
 * A játék fő vezérlő osztálya.
 * Felelős a játék inicializálásáért, a város létrehozásáért és a játékmenet elindításáért.
 */
public class Game {
    
    /**
     * Elindítja a játékot.
     * A valós működésben ez hozza létre a pályát és indítja el a köröket.
     */
    public void startGame() {
        SkeletonHelper.enterMethod("Game.StartGame()");
        System.out.println("  [LOG] Játék elindítva.");
        SkeletonHelper.exitMethod("Game.StartGame()");
    }
}