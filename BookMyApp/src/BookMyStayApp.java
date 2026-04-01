import java.util.*;

// Service - represents an optional add-on service
class Service {
    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() { return serviceName; }
    public double getCost() { return cost; }

    public void displayService() {
        System.out.println(serviceName + " ($" + cost + ")");
    }
}

// Reservation - represents a guest booking
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

// AddOnServiceManager - manages mapping of services to reservations
class AddOnServiceManager {
    private Map<String, List<Service>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Attach a service to a reservation
    public void addServiceToReservation(Reservation reservation, Service service) {
        reservationServices.computeIfAbsent(reservation.getReservationId(), k -> new ArrayList<>()).add(service);
        System.out.println("Added service " + service.getServiceName() + " to reservation " + reservation.getReservationId());
    }

    // Get all services for a reservation
    public List<Service> getServicesForReservation(Reservation reservation) {
        return reservationServices.getOrDefault(reservation.getReservationId(), new ArrayList<>());
    }

    // Calculate total additional cost for services
    public double calculateTotalCost(Reservation reservation) {
        double total = 0;
        for (Service service : getServicesForReservation(reservation)) {
            total += service.getCost();
        }
        return total;
    }

    // Display services and cost
    public void displayReservationServices(Reservation reservation) {
        List<Service> services = getServicesForReservation(reservation);
        if (services.isEmpty()) {
            System.out.println("No add-on services for reservation " + reservation.getReservationId());
        } else {
            System.out.println("Add-On Services for reservation " + reservation.getReservationId() + ":");
            for (Service service : services) {
                service.displayService();
            }
            System.out.println("Total Additional Cost: $" + calculateTotalCost(reservation));
        }
    }
}

// Main Program for UC7
public class BookMyStayApp {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("      Add-On Service Selection (UC7)");
        System.out.println("=====================================");

        // Sample reservations (from previous UC6)
        Reservation res1 = new Reservation("R001", "Alice", "Single Room", 1);
        Reservation res2 = new Reservation("R002", "Bob", "Double Room", 2);

        // Sample add-on services
        Service breakfast = new Service("Breakfast", 15.0);
        Service spa = new Service("Spa Access", 50.0);
        Service airportPickup = new Service("Airport Pickup", 30.0);

        // Manage services
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Guests select services
        serviceManager.addServiceToReservation(res1, breakfast);
        serviceManager.addServiceToReservation(res1, airportPickup);
        serviceManager.addServiceToReservation(res2, spa);

        // Display reservation info with add-on services
        System.out.println("\n--- Reservation Details with Add-On Services ---");
        res1.displayReservation();
        serviceManager.displayReservationServices(res1);

        System.out.println();
        res2.displayReservation();
        serviceManager.displayReservationServices(res2);

        System.out.println("\nUC7 Add-On Service Selection Completed!");
    }
}