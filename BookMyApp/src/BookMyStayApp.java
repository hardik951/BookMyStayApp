import java.util.*;

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
        roomInventory.put("Single Room", 5);
        roomInventory.put("Double Room", 3);
        roomInventory.put("Suite Room", 2);
    }

    // Increment inventory after cancellation
    public void releaseRooms(String roomType, int numOfRooms) {
        roomInventory.put(roomType, roomInventory.getOrDefault(roomType, 0) + numOfRooms);
        System.out.println(numOfRooms + " " + roomType + "(s) released back to inventory.");
    }

    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
    }
}

// Booking history manager
class BookingHistory {
    private List<Reservation> confirmedBookings;
    private Stack<String> cancelledReservations; // track cancellations for rollback

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
        cancelledReservations = new Stack<>();
    }

    // Add confirmed reservation
    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
        System.out.println("Reservation " + reservation.getReservationId() + " added to booking history.");
    }

    // Cancel reservation
    public boolean cancelBooking(String reservationId, InventoryManager inventoryManager) {
        for (Iterator<Reservation> it = confirmedBookings.iterator(); it.hasNext(); ) {
            Reservation res = it.next();
            if (res.getReservationId().equals(reservationId)) {
                // Rollback inventory
                inventoryManager.releaseRooms(res.getRoomType(), res.getNumOfRooms());

                // Remove from confirmed bookings
                it.remove();

                // Track cancelled reservation
                cancelledReservations.push(reservationId);

                System.out.println("Reservation " + reservationId + " cancelled successfully.");
                return true;
            }
        }
        System.out.println("Cancellation failed: Reservation " + reservationId + " does not exist.");
        return false;
    }

    public void displayAllBookings() {
        System.out.println("\n--- Confirmed Reservations ---");
        if (confirmedBookings.isEmpty()) {
            System.out.println("No confirmed bookings.");
        } else {
            for (Reservation res : confirmedBookings) {
                res.displayReservation();
            }
        }
    }

    public void displayCancelledReservations() {
        System.out.println("\n--- Recently Cancelled Reservations (LIFO) ---");
        if (cancelledReservations.isEmpty()) {
            System.out.println("No cancellations yet.");
        } else {
            for (String id : cancelledReservations) {
                System.out.println("Reservation ID: " + id);
            }
        }
    }
}

// Main program for UC10
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("   Booking Cancellation & Rollback (UC10)");
        System.out.println("=====================================");

        InventoryManager inventoryManager = new InventoryManager();
        BookingHistory bookingHistory = new BookingHistory();

        // Sample reservations
        Reservation res1 = new Reservation("R001", "Alice", "Single Room", 1);
        Reservation res2 = new Reservation("R002", "Bob", "Double Room", 2);
        Reservation res3 = new Reservation("R003", "Charlie", "Suite Room", 1);

        // Add reservations
        bookingHistory.addBooking(res1);
        bookingHistory.addBooking(res2);
        bookingHistory.addBooking(res3);

        // Display bookings and inventory
        bookingHistory.displayAllBookings();
        inventoryManager.displayInventory();

        // Cancel reservations
        System.out.println("\nAttempting cancellations...");
        bookingHistory.cancelBooking("R002", inventoryManager); // valid cancellation
        bookingHistory.cancelBooking("R004", inventoryManager); // invalid: does not exist

        // Display updated bookings and inventory
        bookingHistory.displayAllBookings();
        inventoryManager.displayInventory();

        // Display cancelled reservations stack
        bookingHistory.displayCancelledReservations();

        System.out.println("\nUC10 Booking Cancellation & Inventory Rollback Completed!");
    }
}