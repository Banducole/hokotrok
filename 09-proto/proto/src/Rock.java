/**
 * Zúzalék (kavics) üzemanyag-típus, amelyet a {@link RockHead} használ.
 * <p>
 * A zúzalék szórása csúszásmentesítő hatású: a sávon {@code rocky=true}
 * állapotot állít be, amely megszünteti a csúszósságot. Ára a
 * {@link Constants#PRICE_ROCK_UNIT} konstansban van meghatározva.
 * </p>
 */
public class Rock extends FuelType {

    /**
     * Létrehozza a zúzalék üzemanyag-típust és beállítja az egységárát.
     */
    public Rock() { this.price = Constants.PRICE_ROCK_UNIT; }
}
