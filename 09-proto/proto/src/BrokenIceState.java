/**
 * Feltort jeges savалlapot – a sav atjarhato es csuszos, a jeg tolhato.
 * <p>
 * Az {@link IceBreakerHead} hozza letre {@link IcyState}-bol.
 * A feltort jeg – ellentetben a szilarddal – attorheto szomszed savba
 * (pl. soprofejjel). So hatasara azonnal {@link ClearState}-re olvaszt.
 * </p>
 */
public class BrokenIceState implements LaneState {

    /** A feltort jeg vastagsaga egysegekben (hoeses eseten no). */
    private int iceThickness;

    /**
     * Letrehozza az allapotot 1 egysegnyi kezdeti vastagssaggal.
     */
    public BrokenIceState() { this.iceThickness = 1; }

    /**
     * Hoeses hatasa: noveli a jegvastagot (a ho rafagy a jegre).
     *
     * @param lane   az erintett sav
     * @param amount az erkező ho mennyisege egysegekben
     */
    @Override
    public void onSnowAdded(Lane lane, int amount) { iceThickness += amount; }

    /**
     * So hatasa: azonnal ClearState-re olvasztja a jegreteg.
     *
     * @param lane az erintett sav
     */
    @Override
    public void onSaltApplied(Lane lane) {
        lane.setState(new ClearState());
        Logger.action(lane, "so hatasara ClearState-re valtozott");
    }

    /**
     * Jegkepzodes hatastalan – a sav mar jeges allapotban van.
     *
     * @param lane az erintett sav
     */
    @Override public void onIceFormed(Lane lane)  { /* mar jeges, nincs tovabbi hatas */ }

    /** @return {@code true} – a feltort jeg meg atjarhato */
    @Override public boolean isPassable()   { return true;  }

    /**
     * @return {@code true} – a jeg csuszossa teszi a savot
     *         (a tenyleges csuszossagot {@link Lane#isSlippery()} hatarozza meg)
     */
    @Override public boolean isSlippery()   { return true;  }

    /** @return {@code true} – a feltort jeg tolhato szomszed savba */
    @Override public boolean canBePushed()  { return true;  }

    /**
     * Visszaadja az aktualis jegvastagot.
     *
     * @return jegvastagsag egysegekben
     */
    @Override public int getSnowThickness() { return iceThickness; }
}
