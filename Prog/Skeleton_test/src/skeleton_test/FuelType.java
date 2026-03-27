package skeleton_test;
/**
 * Üzemanyag típust reprezentál (pl. Só, Kerozin).
 */
public class FuelType {

    private String name;
    private int price;

    public FuelType(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }

    // Előre definiált típusok
    public static final FuelType SALT    = new FuelType("Salt",     100);
    public static final FuelType KEROSENE = new FuelType("Kerosene", 150);
}
