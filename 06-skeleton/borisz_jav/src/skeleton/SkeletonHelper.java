package skeleton;
import java.util.Scanner;

/**
 * A szkeleton működését, a naplózást és a tesztelővel való kommunikációt
 * támogató segédosztály. A skeleton szöveges konzolon kommunikál a tesztelővel.
 */
public class SkeletonHelper {
    private static int depth = 0;
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Naplózza egy metódusba való belépést. A hívási mélységnek megfelelően behúzza a szöveget.
     * @param methodName A metódus neve, amibe beléptünk.
     */
    public static void enterMethod(String methodName) {
        printIndent();
        System.out.println("Beléptünk a(z) " + methodName + " metódusba.");
        depth++;
    }

    /**
     * Naplózza egy metódusból való kilépést.
     * @param methodName A metódus neve, amiből kiléptünk.
     */
    public static void exitMethod(String methodName) {
        depth--;
        printIndent();
        System.out.println("Kiléptünk a(z) " + methodName + " metódusból.");
    }

    /**
     * Feltesz egy eldöntendő kérdést a tesztelőnek a konzolon, és beolvassa a választ.
     * A kérdések formátuma egyértelműen jelzi, milyen választ vár a program[cite: 210].
     * @param question A kérdés szövege.
     * @return Igaz, ha a felhasználó igennel ('y') válaszol, egyébként hamis.
     */
    public static boolean askQuestion(String question) {
        printIndent();
        System.out.print("? KÉRDÉS: " + question + " (y/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("y");
    }

    /**
     * Feltesz egy kérdést, amire szöveges választ (pl. rövidítést) vár a tesztelőtől.
     *
     * @param question A kérdés szövege.
     * @return A tesztelő által beírt szöveg, kisbetűsítve.
     */
    public static String askString(String question) {
        printIndent();
        System.out.print("? KÉRDÉS: " + question + " ");
        return scanner.nextLine().trim().toLowerCase();
    }

    /**
     * Kiírja a megfelelő mennyiségű behúzást a vizuális hierarchia érdekében.
     */
    private static void printIndent() {
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }
    }
}
