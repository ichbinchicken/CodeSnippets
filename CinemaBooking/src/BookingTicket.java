import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * COMP2511 Assignment 1
 * @author Ziming(Toby) Zheng z5052592
 * This class represents a booking ticket containing a booking ID and the cinema, seats and time information
 */
public class BookingTicket {
    private int bookingID;
    private int cinemaID;
    private String time;
    private String seatString;
    private int requestSeats;

    /**
     * Constructor for a booking ticket
     * @param bookingID the unique ID of this booking
     * @param cinemaID the ID of the cinema this ticket is for
     * @param time the time of the movie this ticket is for
     * @param seatString the string representation of the range of seats this ticket is for
     * @param requestSeats the number of seats in this ticket
     * @pre requestSeats > 0
     * @post none
     */
    public BookingTicket(int bookingID, int cinemaID, String time, String seatString, int requestSeats) {
        this.bookingID = bookingID;
        this.cinemaID = cinemaID;
        this.time = time;
        this.seatString = seatString;
        this.requestSeats = requestSeats;
    }

    /**
     * Getter for cinema ID in this ticket
     * @return the cinema ID contained in this ticket
     * @pre none
     * @post none
     */
    public int getCinemaID() {
        return cinemaID;
    }

    /**
     * Getter for the string representation of seats in this ticket
     * @return the string representing the seats in this ticket
     * @pre none
     * @post none
     */
    public String getSeatString() {
        return seatString;

    }

    /**
     * Getter for the time stored in this ticket
     * @return the time of the movie this ticket is for
     * @pre none
     * @post none
     */
    public String getTime() {
        return time;
    }

    /**
     * Get a string representing the seats in the specified row among seats booked in this ticket
     * @param row the name of the row
     * @return the string representation of the seats in that row booked in this ticket;
     * return null if the given row does not exist
     * @pre none
     * @post none
     */
    public String getSeatsInARow(String row) {
        Pattern p = Pattern.compile("([A-Za-z]+)([0-9]+)");
        Matcher m = p.matcher(seatString);
        String targetRow = "";
        String seats = null;
        if (m.find()) {
            targetRow = m.group(1);
        }
        if (targetRow.equals(row)) {
            seats = seatString.replace(targetRow, "");
        }
        return seats;
    }


    /**
     * Compares if two tickets are equal
     * @param obj the other object to be compared
     * @return true if two tickets have the same booking ID, cinema ID, time and seats, false otherwise
     * @pre none
     * @post none
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof BookingTicket)) {
            return false;
        }
        BookingTicket other = (BookingTicket) obj;
        return this.bookingID == other.bookingID && this.cinemaID == other.cinemaID
                                                   && this.time.equals(other.time)
                                                   && this.seatString.equals(other.seatString);
    }

    /**
     * Generate a hash code for this ticket
     * @return the hash code of this ticket based on its booking ID
     * @pre none
     * @post none
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(bookingID);
    }
}
