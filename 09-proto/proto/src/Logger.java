import java.util.HashMap;
import java.util.Map;

/** Central logger that prints structured [ACTION], [STAT], [ERROR] lines. */
public class Logger {
    private static final Map<Object, String> NAMES = new HashMap<>();

    public static void register(Object obj, String name) {
        NAMES.put(obj, name);
    }

    public static String name(Object obj) {
        if (obj == null) return "null";
        String n = NAMES.get(obj);
        return n != null ? n : obj.getClass().getSimpleName();
    }

    public static void action(Object obj, String message) {
        System.out.println("[ACTION] " + name(obj) + ": " + message);
    }

    public static void error(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void error(Object obj, String message) {
        System.out.println("[ERROR] " + name(obj) + ": " + message);
    }

    public static void stat(String type, Object obj, String fields) {
        System.out.println("[STAT] " + type + " " + name(obj) + " " + fields);
    }
}
