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

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for guest: " + reservation.getGuestName());
    }

    public Reservation pollNextRequest() { return requestQueue.poll(); }

    public boolean isEmpty() { return requestQueue.isEmpty(); }
}

// InventoryService - maintains room availability
class InventoryService {
    private Map<String, Integer> availableRooms;

    public InventoryService() {
        availableRooms = new HashMap<>();
        availableRooms.put("Single Room", 5);
        availableRooms.put("Double Room", 3);
        availableRooms.put("Suite Room", 2);
    }

    public synchronized boolean isAvailable(String roomType, int quantity) {
        return availableRooms.getOrDefault(roomType, 0) >= quantity;
    }

    public synchronized void decrement(String roomType, int quantity) {
        availableRooms.put(roomType, availableRooms.get(roomType) - quantity);
    }

    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : availableRooms.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " rooms available");
        }
    }
}

// BookingService - allocates rooms safely and confirms reservations
class BookingService {
    private InventoryService inventoryService;
    private Map<String, Set<String>> allocatedRooms; // roomType -> assigned room IDs
    private int roomCounter = 1;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        this.allocatedRooms = new HashMap<>();
    }

    public synchronized boolean allocateRoom(Reservation reservation) {
        String roomType = reservation.getRoomType();
        int quantity = reservation.getNumOfRooms();

        if (!inventoryService.isAvailable(roomType, quantity)) {
            System.out.println("Cannot allocate " + quantity + " " + roomType + "(s) for " + reservation.getGuestName() + ". Not enough inventory.");
            return false;
        }

        Set<String> allocatedSet = allocatedRooms.computeIfAbsent(roomType, k -> new HashSet<>());
        List<String> assignedRoomIds = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            String roomId;
            do {
                roomId = roomType.replaceAll(" ", "") + "-" + roomCounter++;
            } while (allocatedSet.contains(roomId));
            allocatedSet.add(roomId);
            assignedRoomIds.add(roomId);
        }

        inventoryService.decrement(roomType, quantity);
        System.out.println("Reservation confirmed for " + reservation.getGuestName() + ". Assigned Rooms: " + assignedRoomIds);
        return true;
    }
}

// Main Application for UC6
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("       Room Allocation (UC6)");
        System.out.println("=====================================");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(inventoryService);

        // Sample queued booking requests (UC5)
        bookingQueue.addRequest(new Reservation("Alice", "Single Room", 1));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room", 2));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room", 1));
        bookingQueue.addRequest(new Reservation("Diana", "Single Room", 3)); // May exceed inventory

        System.out.println("\n--- Processing Booking Requests (UC6) ---");
        while (!bookingQueue.isEmpty()) {
            Reservation reservation = bookingQueue.pollNextRequest();
            bookingService.allocateRoom(reservation);
        }

        inventoryService.displayInventory();
        System.out.println("\nUC6 Room Allocation Completed!");
    }
}