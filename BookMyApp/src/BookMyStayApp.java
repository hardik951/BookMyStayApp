/**
 * BookMyStayApp
 *
 * This class demonstrates Use Case 2:
 * Basic Room Types & Static Availability
 * using abstraction, inheritance, and polymorphism.
 *
 * @version 2.1
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

    public void displayDetails() {
        System.out.println("Room Type       : " + roomType);
        System.out.println("Beds            : " + numberOfBeds);
        System.out.println("Price per Night : ₹" + pricePerNight);
    }
}

// Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000.0);
    }
}

// Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500.0);
    }
}

// Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 6000.0);
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v2.1");
        System.out.println("=====================================");

        // Create room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("\n--- Room Details ---\n");

        single.displayDetails();
        System.out.println("Availability    : " + singleAvailable);
        System.out.println("-------------------------------------");

        doubleRoom.displayDetails();
        System.out.println("Availability    : " + doubleAvailable);
        System.out.println("-------------------------------------");

        suite.displayDetails();
        System.out.println("Availability    : " + suiteAvailable);
        System.out.println("-------------------------------------");

        System.out.println("\nApplication terminated successfully!");
    }
}