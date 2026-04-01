import java.util.*;

// Reservation - represents a guest booking request
class Reservation {
    private String guestName;
    private String roomType;
    private int numOfRooms;

    public Reservation(String guestName, String roomType, int numOfRooms) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.numOfRooms = numOfRooms;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNumOfRooms() { return numOfRooms; }

    public void displayRequest() {
        System.out.println("Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Rooms Requested: " + numOfRooms);
    }
}

// BookingRequestQueue - handles booking requests in FIFO order
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add a reservation request
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for guest: " + reservation.getGuestName());
    }

    // Peek next request
    public Reservation peekNextRequest() { return requestQueue.peek(); }

    // Poll next request for future allocation
    public Reservation pollNextRequest() { return requestQueue.poll(); }

    // Display all queued requests
    public void displayQueue() {
        System.out.println("\n--- Current Booking Queue ---");
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in the queue.");
        } else {
            for (Reservation res : requestQueue) {
                res.displayRequest();
            }
        }
    }
}

// Main Application for UC5
public class BookingRequestApp {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("   Booking Request Intake System");
        System.out.println("=====================================");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Sample booking requests
        Reservation r1 = new Reservation("Alice", "Single Room", 1);
        Reservation r2 = new Reservation("Bob", "Double Room", 2);
        Reservation r3 = new Reservation("Charlie", "Suite Room", 1);

        // Add requests to queue
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display current queue
        bookingQueue.displayQueue();

        // Peek next request
        System.out.println("\nNext request to process:");
        Reservation next = bookingQueue.peekNextRequest();
        if (next != null) next.displayRequest();

        System.out.println("\nBooking request intake completed!");
    }
}