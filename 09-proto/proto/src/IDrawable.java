import java.awt.Graphics2D;

/**
 * Grafikus elemek kozos interfesze.
 * Minden megjelenitheto elem (sav, csomopont, jarmu, hokotro) implementalja.
 */
public interface IDrawable {
    void draw(Graphics2D g);
}
