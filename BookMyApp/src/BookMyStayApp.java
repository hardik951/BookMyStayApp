import java.io.*;
import java.util.*;

// Entities must implement Serializable to be saved to disk
class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    String guestName;
    String roomType;
    String bookingId;

    public Booking(String guestName, String roomType, String bookingId) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.bookingId = bookingId;
    }

    @Override
    public String toString() {
        return "[" + bookingId + "] " + guestName + " - " + roomType;
    }
}

public class BookMyStayApp implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "system_state.dat";

    // System State
    private Map<String, Integer> inventory = new HashMap<>();
    private List<Booking> bookings = new ArrayList<>();

    public BookMyStayApp() {
        // Default values for a fresh system
        inventory.put("Single", 10);
        inventory.put("Double", 5);
    }

    public void createBooking(String name, String type) {
        if (inventory.getOrDefault(type, 0) > 0) {
            String id = "RES" + (bookings.size() + 500);
            bookings.add(new Booking(name, type, id));
            inventory.put(type, inventory.get(type) - 1);
            System.out.println("Success: Booking confirmed for " + name);
        } else {
            System.out.println("Error: No " + type + " rooms available.");
        }
    }

    public void showStatus() {
        System.out.println("\n--- Current System Snapshot ---");
        System.out.println("Inventory: " + inventory);
        System.out.println("Total Bookings: " + bookings.size());
        bookings.forEach(System.out::println);
        System.out.println("-------------------------------\n");
    }

    // PERSISTENCE LOGIC
    public void saveToDisk() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
            System.out.println(">>> System state persisted to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println(">>> Error saving state: " + e.getMessage());
        }
    }

    public static BookMyStayApp loadFromDisk() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println(">>> No previous state found. Initializing fresh system...");
            return new BookMyStayApp();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            System.out.println(">>> Recovery Successful: Restoring state from disk.");
            return (BookMyStayApp) ois.readObject();
        } catch (Exception e) {
            System.out.println(">>> Recovery Failed: Starting fresh system.");
            return new BookMyStayApp();
        }
    }

    public static void main(String[] args) {
        // 1. Recover System
        BookMyStayApp app = BookMyStayApp.loadFromDisk();

        // 2. Perform some operations
        if (app.bookings.isEmpty()) {
            app.createBooking("John Doe", "Double");
            app.createBooking("Jane Smith", "Single");
        } else {
            System.out.println("Continuing session... Adding new booking.");
            app.createBooking("Guest_" + System.currentTimeMillis() % 1000, "Single");
        }

        app.showStatus();

        // 3. Persist before exiting
        app.saveToDisk();
        System.out.println("Application closing. Data is safe.");
    }
}