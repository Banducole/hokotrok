/**
 * Kerozin üzemanyag-típus, amelyet a {@link DragonHead} (sárkányfej) használ.
 * <p>
 * A kerozin eléget minden hót és jeget, ClearState-re állítva a sávot;
 * ára a {@link Constants#PRICE_KEROSENE_UNIT} konstansban van meghatározva.
 * </p>
 */
public class Kerosene extends FuelType {

    /**
     * Létrehozza a kerozin üzemanyag-típust és beállítja az egységárát.
     */
    public Kerosene() { this.price = Constants.PRICE_KEROSENE_UNIT; }
}
