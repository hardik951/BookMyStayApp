import java.util.*;

// Custom exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int numOfRooms;

    public Reservation(String reservationId, String guestName, String roomType, int numOfRooms) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.numOfRooms = numOfRooms;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNumOfRooms() { return numOfRooms; }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Rooms: " + numOfRooms);
    }
}

// Inventory manager
class InventoryManager {
    private Map<String, Integer> roomInventory;

    public InventoryManager() {
        roomInventory = new HashMap<>();
        // Sample room types and counts
        roomInventory.put("Single Room", 5);
        roomInventory.put("Double Room", 3);
        roomInventory.put("Suite Room", 2);
    }

    // Validate room type
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!roomInventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    // Check inventory availability
    public void checkAvailability(String roomType, int requestedRooms) throws InvalidBookingException {
        int available = roomInventory.getOrDefault(roomType, 0);
        if (requestedRooms <= 0) {
            throw new InvalidBookingException("Number of rooms must be greater than 0.");
        }
        if (requestedRooms > available) {
            throw new InvalidBookingException("Not enough rooms available for " + roomType +
                    ". Requested: " + requestedRooms + ", Available: " + available);
        }
    }

    // Reserve rooms
    public void reserveRooms(String roomType, int numOfRooms) throws InvalidBookingException {
        checkAvailability(roomType, numOfRooms);
        roomInventory.put(roomType, roomInventory.get(roomType) - numOfRooms);
        System.out.println(numOfRooms + " " + roomType + "(s) reserved successfully.");
    }

    // Display current inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
    }
}

// Main program for UC9
public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("      Booking System – UC9 Validation");
        System.out.println("=====================================");

        InventoryManager inventoryManager = new InventoryManager();
        List<Reservation> confirmedReservations = new ArrayList<>();

        // Sample bookings (some valid, some invalid)
        Object[][] sampleBookings = {
                {"R001", "Alice", "Single Room", 1},
                {"R002", "Bob", "Double Room", 4},   // Invalid: more than available
                {"R003", "Charlie", "Penthouse", 1}, // Invalid: room type does not exist
                {"R004", "Diana", "Suite Room", -1}, // Invalid: negative rooms
                {"R005", "Eve", "Double Room", 2}    // Valid
        };

        for (Object[] bookingData : sampleBookings) {
            try {
                String id = (String) bookingData[0];
                String guest = (String) bookingData[1];
                String roomType = (String) bookingData[2];
                int rooms = (Integer) bookingData[3];

                // Validation
                inventoryManager.validateRoomType(roomType);
                inventoryManager.checkAvailability(roomType, rooms);

                // Reserve rooms
                inventoryManager.reserveRooms(roomType, rooms);

                // Add to confirmed reservations
                confirmedReservations.add(new Reservation(id, guest, roomType, rooms));

            } catch (InvalidBookingException e) {
                System.out.println("Booking failed: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }

        // Display confirmed bookings
        System.out.println("\n--- Confirmed Reservations ---");
        for (Reservation res : confirmedReservations) {
            res.displayReservation();
        }

        // Display remaining inventory
        inventoryManager.displayInventory();

        System.out.println("\nUC9 Error Handling & Validation Completed!");
    }
}