package skeleton;

/**
 * A jégtörő fej a jeget töri fel, de nem takarítja el azt.
 * Használata után a jeges sáv feltört jég (BrokenIceState) állapotba kerül.
 */
public class IceBreakerHead implements CleanerHead {
    
    /**
     * Visszaadja, hogy a jégtörő fej működőképes-e.
     * @return Igaz, mivel a jégtörő fej alapértelmezetten mindig működik (nem igényel üzemanyagot).
     */
    @Override
    public boolean isOperational() {
        SkeletonHelper.enterMethod("IceBreakerHead.IsOperational()");
        SkeletonHelper.exitMethod("IceBreakerHead.IsOperational()");
        return true;
    }

    /**
     * Megpróbálja feltörni a jeget az adott sávon.
     * Ha a sáv állapota jeges (IcyState) és nem tolható el a jég, feltöri azt, 
     * így az állapot BrokenIceState-re változik.
     * @param lane A takarítandó (jégmentesítendő) sáv.
     */
    @Override
    public void clean(Lane lane) {
        SkeletonHelper.enterMethod("IceBreakerHead.Clean(Lane)");
        
        LaneState currentState = lane.getState();
        
        if(currentState.getClass().getSimpleName().equals("IcyState")) {
            boolean canBePushed = SkeletonHelper.askQuestion("Tolható a tartalom?"); 
            if(!canBePushed) {
                lane.setState(new BrokenIceState());
            }
        }
        
        SkeletonHelper.exitMethod("IceBreakerHead.Clean(Lane)");
    }
}