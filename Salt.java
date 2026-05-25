/**
 * Só üzemanyag-típus, amelyet a {@link SaltHead} használ.
 * <p>
 * A só felolvasztja a hót és a jeget a sávon; ára a
 * {@link Constants#PRICE_SALT_UNIT} konstansban van meghatározva.
 * </p>
 */
public class Salt extends FuelType {

    /**
     * Létrehozza a só üzemanyag-típust és beállítja az egységárát.
     */
    public Salt() { this.price = Constants.PRICE_SALT_UNIT; }
}
