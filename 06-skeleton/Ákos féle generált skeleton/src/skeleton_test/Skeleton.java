package skeleton_test;

import java.util.Scanner;

/**
 * Skeleton segédosztály – naplózás és felhasználói kérdések kezelése.
 */
public class Skeleton {

    private static final Scanner scanner = new Scanner(System.in);
    private static int depth = 0; // behúzás mélysége

    /** Metódushívás naplózása. */
    public static void logCall(String methodName) {
        System.out.println("  ".repeat(depth) + ">> " + methodName);
    }

    /** Visszatérési érték naplózása. */
    public static void logReturn(String value) {
        System.out.println("  ".repeat(depth) + "<< return: " + value);
    }

    /** Behúzás növelése (metódusba lépés). */
    public static void enter() { depth++; }

    /** Behúzás csökkentése (metódusból kilépés). */
    public static void exit()  { if (depth > 0) depth--; }

    /** Igen/nem kérdés a felhasználónak – boolean visszatérési értékhez. */
    public static boolean askBool(String question) {
        System.out.print("  ".repeat(depth) + "[?] " + question + " (i/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("i") || input.equals("igen");
    }

    /** Egész szám kérése a felhasználótól. */
    public static int askInt(String question) {
        System.out.print("  ".repeat(depth) + "[?] " + question + ": ");
        try { return Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    /** Szöveg kérése a felhasználótól. */
    public static String askString(String question) {
        System.out.print("  ".repeat(depth) + "[?] " + question + ": ");
        return scanner.nextLine().trim();
    }

    /** Teszt eredményének kiírása. */
    public static void testResult(boolean success) {
        System.out.println(success ? "\n=== TESZT SIKERES ===" : "\n=== TESZT SIKERTELEN ===");
    }

    // ── Főmenü ───────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("=== Hókotrók Skeleton ===\n");
        boolean running = true;

        while (running) {
            printMenu();
            int choice = askInt("Válassz tesztesetet");
            depth = 0;
            System.out.println();

            switch (choice) {
                case 0  -> UC00.run();
                case 1  -> UC01.run();
                case 2  -> UC02.run();
                case 3  -> UC03.run();
                case 4  -> UC04.run();
                case 5  -> UC05.run();
                case 6  -> UC06.run();
                case 7  -> UC07.run();
                case 8  -> UC08.run();
                case 9  -> UC09.run();
                case 10 -> UC10.run();
                case 11 -> UC11.run();
                case 12 -> UC12.run();
                case 13 -> UC13.run();
                case 14 -> UC14.run();
                case 15 -> UC15.run();
                case 16 -> UC16.run();
                case 17 -> UC17.run();
                case 18 -> UC18.run();
                case 19 -> UC19.run();
                case 20 -> UC20.run();
                case 21 -> UC21.run();
                case 99 -> running = false;
                default -> System.out.println("Ismeretlen opció.");
            }
        }
        System.out.println("Kilépés.");
    }

    private static void printMenu() {
        System.out.println("\n--- Tesztesetek ---");
        System.out.println(" 0  UC-00: Skeleton inicializáció");
        System.out.println(" 1  UC-01: Autó mozog – normál eset");
        System.out.println(" 2  UC-02: Jármű elakad – nincs járható szomszéd");
        System.out.println(" 3  UC-03: Jármű megcsúszik és ütközik");
        System.out.println(" 4  UC-04: Hókotró takarít – sáv már tiszta");
        System.out.println(" 5  UC-05: Hókotró takarít – fej nem működőképes");
        System.out.println(" 6  UC-06: Hókotró áthalad vastag hóban");
        System.out.println(" 7  UC-07: Havazás – sima sávra esik hó");
        System.out.println(" 8  UC-08: Jégképződés vékony havas sávon");
        System.out.println(" 9  UC-09: Söprő fejjel takarítás");
        System.out.println("10  UC-10: Hányó fejjel takarítás");
        System.out.println("11  UC-11: Jégtörő fejjel takarítás");
        System.out.println("12  UC-12: Sószóróval takarítás");
        System.out.println("13  UC-13: Sárkányfejjel takarítás");
        System.out.println("14  UC-14: Fejcsere");
        System.out.println("15  UC-15: Hajtóanyag vásárlás");
        System.out.println("16  UC-16: Új hókotró vásárlás");
        System.out.println("17  UC-17: Busz végállomásra érkezik");
        System.out.println("18  UC-18: Jármű blokkolt – nem lép");
        System.out.println("19  UC-19: Jármű átvált szomszéd sávra");
        System.out.println("20  UC-20: Havazás – sózott sávra esik hó");
        System.out.println("21  UC-21: Havazás – vékony havas sávra esik hó");
        System.out.println("99  Kilépés");
    }
}
