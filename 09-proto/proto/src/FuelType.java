/**
 * Hókotró-fejek által fogyasztható üzemanyag-típusok absztrakt ősosztálya.
 * <p>
 * Konkrét altípusok: {@link Salt}, {@link Kerosene}, {@link Rock}.
 * Az üzemanyag árát az altípusok állítják be a konstruktorban,
 * a {@link Constants} osztály megfelelő konstansát használva.
 * </p>
 */
public abstract class FuelType {

    /** Az üzemanyag egységára (pénznemben). */
    protected int price;

    /**
     * Visszaadja az üzemanyag egy egységének árát.
     *
     * @return egységár
     */
    public int getPrice() { return price; }
}
