import java.util.*;

// Reservation - represents a confirmed booking
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

// BookingHistory - maintains confirmed reservations
class BookingHistory {
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    // Add a confirmed reservation
    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
        System.out.println("Reservation " + reservation.getReservationId() + " added to booking history.");
    }

    // Retrieve all bookings
    public List<Reservation> getAllBookings() {
        return new ArrayList<>(confirmedBookings); // return a copy to prevent modification
    }

    // Display all bookings
    public void displayAllBookings() {
        System.out.println("\n--- Booking History ---");
        if (confirmedBookings.isEmpty()) {
            System.out.println("No bookings have been confirmed yet.");
        } else {
            for (Reservation res : confirmedBookings) {
                res.displayReservation();
            }
        }
    }
}

// BookingReportService - generates summary reports
class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    // Generate a summary report: total bookings and counts per room type
    public void generateSummaryReport() {
        List<Reservation> bookings = bookingHistory.getAllBookings();
        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Bookings: " + bookings.size());

        Map<String, Integer> roomTypeCounts = new HashMap<>();
        for (Reservation res : bookings) {
            roomTypeCounts.put(res.getRoomType(), roomTypeCounts.getOrDefault(res.getRoomType(), 0) + res.getNumOfRooms());
        }

        System.out.println("Rooms Booked by Type:");
        for (Map.Entry<String, Integer> entry : roomTypeCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " rooms");
        }
    }
}

// Main Program for UC8
public class UseCase8BookingHistoryReport {
    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("   Booking History & Reporting (UC8)");
        System.out.println("=====================================");

        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService(bookingHistory);

        // Sample confirmed reservations
        Reservation res1 = new Reservation("R001", "Alice", "Single Room", 1);
        Reservation res2 = new Reservation("R002", "Bob", "Double Room", 2);
        Reservation res3 = new Reservation("R003", "Charlie", "Suite Room", 1);

        // Add reservations to history
        bookingHistory.addBooking(res1);
        bookingHistory.addBooking(res2);
        bookingHistory.addBooking(res3);

        // Display all bookings
        bookingHistory.displayAllBookings();

        // Generate summary report
        reportService.generateSummaryReport();

        System.out.println("\nUC8 Booking History & Reporting Completed!");
    }
}