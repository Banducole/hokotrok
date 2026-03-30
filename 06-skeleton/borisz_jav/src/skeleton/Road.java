package skeleton;

/**
 * Egy utat (útszakaszt) reprezentáló osztály a városban.
 * Az utak sávokból (Lane) állnak, és feladatuk, hogy a globális eseményeket (pl. havazás) 
 * továbbítsák a hozzájuk tartozó sávok felé.
 */
public class Road {

    /**
     * Havazás alkalmazása az úton.
     * A metódus a lehulló havat elosztja (továbbítja) az úthoz tartozó sávoknak.
     * @param amount A lehulló hó mennyisége.
     */
    public void applySnow(int amount) {
        SkeletonHelper.enterMethod("Road.ApplySnow(" + amount + ")");
        Lane dummyLane = new Lane();
        dummyLane.addSnow(amount);
        SkeletonHelper.exitMethod("Road.ApplySnow(" + amount + ")");
    }
}