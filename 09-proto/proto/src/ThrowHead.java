/**
 * Hanyofej – egyszerre eltavolitja az osszs tolhato tartalmat a savrol.
 * <p>
 * Ha a sav allapota tolhato ({@link LaneState#canBePushed()}), a fej
 * azonnal ClearState-re allitja a savot es torli a zuzalekot.
 * Nem igenyel uzemanyagot, mindig uzemiképes.
 * Nem hat jeges ({@link IcyState}) savra.
 * </p>
 */
public class ThrowHead extends CleanerHead {

    /**
     * Letrehozza a hanyofejet es beallitja az arat.
     */
    public ThrowHead() { this.price = Constants.PRICE_THROW_HEAD; }

    /**
     * Takaritis: ha a sav tartalma tolhato, azonnal ClearState-re allitja.
     * Jeges vagy mar tiszta savon nincs hatasa.
     *
     * @param lane a takaritando sav
     */
    @Override
    public void clean(Lane lane) {
        SnowPlow plow = lane.getSnowPlow();
        if (lane.getState().canBePushed()) {
            lane.setRocky(false);
            lane.setState(new ClearState());
            Logger.action(plow, "Hanyofej takaritott");
            Logger.action(lane, "allapota ClearState-re valtozott");
        } else {
            Logger.action(plow, "Hanyofej: nincs mit takaritani");
        }
    }

    /**
     * A hanyofej mindig uzemikepés, nem igenyel uzemanyagot.
     *
     * @return {@code true}
     */
    @Override public boolean isOperational() { return true; }
}
