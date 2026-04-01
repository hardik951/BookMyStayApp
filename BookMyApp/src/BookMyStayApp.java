import java.util.HashMap;
import java.util.Map;

/**
 * BookMyStayApp
 *
 * Use Case 3: Centralized Inventory Management using HashMap
 *
 * Demonstrates how to manage room availability using a single
 * data structure instead of scattered variables.
 *
 * @version 3.0
 */

// Abstract Room class
abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double pricePerNight;

    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayDetails() {
        System.out.println("Room Type       : " + roomType);
        System.out.println("Beds            : " + numberOfBeds);
        System.out.println("Price per Night : ₹" + pricePerNight);
    }
}

// Concrete Room Types
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 6000.0);
    }
}

/**
 * RoomInventory - Centralized inventory manager
 */
class RoomInventory {

    private Map<String, Integer> availabilityMap;

    // Constructor initializes inventory
    public RoomInventory() {
        availabilityMap = new HashMap<>();

        availabilityMap.put("Single Room", 5);
        availabilityMap.put("Double Room", 3);
        availabilityMap.put("Suite Room", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    // Update availability (controlled update)
    public void updateAvailability(String roomType, int newCount) {
        availabilityMap.put(roomType, newCount);
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : availabilityMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v3.0");
        System.out.println("=====================================");

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize centralized inventory
        RoomInventory inventory = new RoomInventory();

        System.out.println("\n--- Room Details ---\n");

        single.displayDetails();
        System.out.println("Availability    : " + inventory.getAvailability(single.getRoomType()));
        System.out.println("-------------------------------------");

        doubleRoom.displayDetails();
        System.out.println("Availability    : " + inventory.getAvailability(doubleRoom.getRoomType()));
        System.out.println("-------------------------------------");

        suite.displayDetails();
        System.out.println("Availability    : " + inventory.getAvailability(suite.getRoomType()));
        System.out.println("-------------------------------------");

        // Show full inventory
        inventory.displayInventory();

        // Example update
        System.out.println("\nUpdating Single Room availability to 4...");
        inventory.updateAvailability("Single Room", 4);

        inventory.displayInventory();

        System.out.println("\nApplication terminated successfully!");
    }
}