import java.util.*;
import java.util.concurrent.*;

import java.util.*;
import java.util.concurrent.*;

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

// Thread-safe Inventory Manager
class InventoryManager {
    private final Map<String, Integer> roomInventory;

    public InventoryManager() {
        roomInventory = new HashMap<>();
        roomInventory.put("Single Room", 5);
        roomInventory.put("Double Room", 3);
        roomInventory.put("Suite Room", 2);
    }

    // Synchronized method for safe room allocation
    public synchronized boolean allocateRooms(String roomType, int numOfRooms) {
        int available = roomInventory.getOrDefault(roomType, 0);
        if (numOfRooms <= 0 || numOfRooms > available) return false;
        roomInventory.put(roomType, available - numOfRooms);
        return true;
    }

    public synchronized void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : roomInventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
    }
}

// Shared booking queue
class BookingQueue {
    private final Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addReservation(Reservation res) {
        queue.offer(res);
        System.out.println("Booking request added: " + res.getGuestName());
    }

    public synchronized Reservation pollReservation() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Worker thread to process bookings
class BookingProcessor implements Runnable {
    private final BookingQueue bookingQueue;
    private final InventoryManager inventoryManager;
    private final List<Reservation> confirmedBookings;

    public BookingProcessor(BookingQueue bookingQueue, InventoryManager inventoryManager, List<Reservation> confirmedBookings) {
        this.bookingQueue = bookingQueue;
        this.inventoryManager = inventoryManager;
        this.confirmedBookings = confirmedBookings;
    }

    @Override
    public void run() {
        while (true) {
            Reservation res;
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) break;
                res = bookingQueue.pollReservation();
            }

            if (res != null) {
                boolean allocated = inventoryManager.allocateRooms(res.getRoomType(), res.getNumOfRooms());
                if (allocated) {
                    synchronized (confirmedBookings) {
                        confirmedBookings.add(res);
                    }
                    System.out.println("Reservation confirmed for " + res.getGuestName());
                } else {
                    System.out.println("Failed to allocate rooms for " + res.getGuestName() +
                            " (" + res.getRoomType() + ")");
                }
            }
        }
    }
}

// Main program for UC11
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("   Concurrent Booking Simulation (UC11)");
        System.out.println("=====================================");

        InventoryManager inventoryManager = new InventoryManager();
        BookingQueue bookingQueue = new BookingQueue();
        List<Reservation> confirmedBookings = Collections.synchronizedList(new ArrayList<>());

        // Sample booking requests
        Reservation[] sampleRequests = {
                new Reservation("R001", "Alice", "Single Room", 1),
                new Reservation("R002", "Bob", "Double Room", 2),
                new Reservation("R003", "Charlie", "Suite Room", 1),
                new Reservation("R004", "Diana", "Single Room", 2),
                new Reservation("R005", "Eve", "Double Room", 1)
        };

        // Add requests to queue
        for (Reservation res : sampleRequests) {
            bookingQueue.addReservation(res);
        }

        // Create multiple threads to process bookings concurrently
        int numThreads = 3;
        Thread[] workers = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            workers[i] = new Thread(new BookingProcessor(bookingQueue, inventoryManager, confirmedBookings));
            workers[i].start();
        }

        // Wait for all threads to finish
        for (Thread t : workers) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Display results
        System.out.println("\n--- Confirmed Bookings ---");
        for (Reservation res : confirmedBookings) {
            res.displayReservation();
        }

        inventoryManager.displayInventory();

        System.out.println("\nUC11 Concurrent Booking Simulation Completed!");
    }
}