import java.util.List;

/**
 * Soprofej – attolja a hót vagy zuzalekot a szomszedos savra.
 * <p>
 * Ha van szomszédos sav (a sav indexe + 1), a hó és a zúzalék
 * átkerül oda; egyébként az út széle mellé kerül (eltűnik).
 * Nem igényel üzemanyagot, mindig üzemképes.
 * Tolható állapotokat kezel: {@link ThinSnowState}, {@link ThickSnowState},
 * {@link BrokenIceState} és zúzalék.
 * </p>
 */
public class SweepHead extends CleanerHead {

    /**
     * Letrehozza a soprofejt és beallitja az arat.
     */
    public SweepHead() { this.price = Constants.PRICE_SWEEP_HEAD; }

    /**
     * Takaritis: tolhato ho vagy zuzalek atiranyitasa a szomszed savra,
     * majd a sav allapotanak ClearState-re allitasa.
     *
     * @param lane a takaritando sav
     */
    @Override
    public void clean(Lane lane) {
        LaneState state = lane.getState();
        SnowPlow plow = lane.getSnowPlow();
        boolean wasRocky = lane.isRocky();
        if (state.canBePushed() || wasRocky) {
            Road road = lane.getRoad();
            int amount = state.getSnowThickness();
            if (amount <= 0) amount = 1;
            Lane neighbor = null;
            if (road != null) {
                /* a szomszed sav a lista kovetkezo eleme (magasabb index) */
                List<Lane> lanes = road.getLanes();
                int idx = lanes.indexOf(lane);
                if (idx >= 0 && idx < lanes.size() - 1) neighbor = lanes.get(idx + 1);
            }
            if (neighbor != null) {
                if (state.canBePushed()) neighbor.addSnow(amount);
                if (wasRocky) {
                    neighbor.applyRock();
                    Logger.action(plow, "Soprofej: zuzalek atkerult szomszed savba");
                }
                Logger.action(plow, "Soprofej takaritott, ho atkerult szomszed savba");
            } else {
                Logger.action(plow, "Soprofej takaritott, ho az ut melle kerult (szelsoso sav)");
            }
            lane.setRocky(false);
            if (state.canBePushed()) {
                lane.setState(new ClearState());
                Logger.action(lane, "allapota ClearState-re valtozott");
            }
        } else {
            Logger.action(plow, "Soprofej: nincs mit takaritani");
        }
    }

    /**
     * A soprofej mindig uzemiképes, nem igenyel uzemanyagot.
     *
     * @return {@code true}
     */
    @Override public boolean isOperational() { return true; }
}
