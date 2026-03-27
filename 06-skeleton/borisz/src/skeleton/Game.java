package skeleton;

/**
 * A játék objektum tárolja a várost, a játékosokat és az aktuális kör számot. 
 * Felelőssége a játék elindítása, valamint annak megállapítása, hogy véget ért-e a játék.
 */
public class Game {
    private City city;
    
    public void startGame() {
        SkeletonHelper.enterMethod("Game.StartGame()");
        System.out.println("  [LOG] Játék elindítva.");
        SkeletonHelper.exitMethod("Game.StartGame()");
    }
}