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
import java.util.*;

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

    public RoomInventory() {
        availabilityMap = new HashMap<>();
        availabilityMap.put("Single Room", 5);
        availabilityMap.put("Double Room", 3);
        availabilityMap.put("Suite Room", 2);
    }

    // Get availability (read-only)
    public int getAvailability(String roomType) {
        return availabilityMap.getOrDefault(roomType, 0);
    }

    // Optional controlled update (used in booking, not search)
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

/**
 * SearchService - Read-only search functionality
 */
class SearchService {
    private RoomInventory inventory;
    private List<Room> rooms;

    public SearchService(RoomInventory inventory, List<Room> rooms) {
        this.inventory = inventory;
        this.rooms = rooms;
    }

    public void searchAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getRoomType());
            if (available > 0) { // Only show available rooms
                room.displayDetails();
                System.out.println("Availability    : " + available);
                System.out.println("-------------------------------------");
            }
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

        // List of rooms for search
        List<Room> allRooms = Arrays.asList(single, doubleRoom, suite);

        // Use SearchService to display available rooms (read-only)
        SearchService searchService = new SearchService(inventory, allRooms);
        searchService.searchAvailableRooms();

        // Show full inventory for reference
        inventory.displayInventory();

        System.out.println("\nApplication terminated!");
    }
}