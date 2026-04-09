
package icetask4;
import java.util.ArrayList;
import java.time.LocalDateTime;


public class DataStorage {

    // ── Parallel ArrayLists ──────────────────────────────────────────
    public static ArrayList<String>        usernames   = new ArrayList<>();
    public static ArrayList<String>        pins        = new ArrayList<>();
    public static ArrayList<Integer>       roles       = new ArrayList<>();   
    public static ArrayList<LocalDateTime> timestamps  = new ArrayList<>();
    public static ArrayList<Boolean>       locked      = new ArrayList<>();
    public static ArrayList<Integer>       failCounts  = new ArrayList<>();

    // ── Pre-populated users ──────────────────────────────────────────
    static {
        addUser("admin",   "1234", 1);
        addUser("manman",   "2222", 2);
        addUser("taeg",     "3333", 2);
        addUser("me", "4444", 3);
    }

    public static void addUser(String username, String pin, int role) {
        usernames.add(username);
        pins.add(pin);
        roles.add(role);
        timestamps.add(LocalDateTime.now());   
        locked.add(false);                     
        failCounts.add(0);                     
    }


    public static int findUser(String username) {
        return usernames.indexOf(username);     
    }

    
    public static String roleName(int role) {
        switch (role) {
            case 1:  return "Admin";
            case 2:  return "Staff";
            case 3:  return "Visitor";
            default: return "Unknown";
        }
    }
}
