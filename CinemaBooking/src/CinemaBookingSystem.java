import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * COMP2511 Assignment 1
 * @author Ziming(Toby) Zheng z5052592
 * This class implements entry points of cinema booking system.
 *
 */
public class CinemaBookingSystem {

    private HashMap<Integer, BookingTicket> ticketsMap; //key=bookingID, value=BookingTicket
    private HashMap<Integer, Cinema> cinemas; //key=cinemaID, value=Cinema

    /**
     * Constructor of class CinemaBookingSystem.
     * Initialise a HashMap of tickets with key=bookingID and value=BookingTicket
     * Initialise a HashMap of cinemas with key=cinemaID and value=Cinema
     * @pre none
     * @post ticketsMap != null && cinemas != null
     */
    public CinemaBookingSystem() {
        this.ticketsMap = new HashMap<>();
        this.cinemas = new HashMap<>();
    }

    /**
     * Create a list of seats for a specified "row" in a cinema
     * @param cinemaID the id of cinema
     * @param row the name of row in the cinema
     * @param numSeats the number of seats for the row
     * @pre numSeats > 0
     * @post cinemas.get(cinemaID) != null && the number of seats of "row" in "cinemas.get(cinemaID)" > 0
     */
    public void addCinemasSeats(int cinemaID, String row, int numSeats) {
        Cinema tmp = cinemas.get(cinemaID);
        if (tmp == null) {
            Cinema newCinema = new Cinema(cinemaID);
            newCinema.addRowSeats(row, numSeats);
            cinemas.put(cinemaID, newCinema);
        } else {
            tmp.addRowSeats(row, numSeats);
        }
    }

    /**
     * Create a session of movie with specified time in a cinema
     * @param cinemaID the id of cinema
     * @param movie the name of movie
     * @param time the time of this session
     * @pre the format of time is "HH:MM" && cinemas.get(cinemaID) != null
     * @post the Session in "cinemas.get(cinemaID)" != null
     */
    public void addSession(int cinemaID, String movie, String time) {
        Cinema tmp = cinemas.get(cinemaID);
        if (tmp != null) {
            tmp.addSession(movie, time);
        }
    }

    /**
     * Dealing with a booking request of a movie session
     * @param requestID the id of a request as well as the id of a booking
     * @param cinemaID the id of cinema requested
     * @param time the movie time(session) requested
     * @param requestSeats the number of seats requested for the requested movie time
     * @pre requestSeats > 0
     * @post ticketsMap.get(requestID) != null and either the number of request seats occupied
     * or rejected because of insufficient space
     */
    public void bookingRequest(int requestID, int cinemaID, String time, int requestSeats) {
        Cinema targetCinema = cinemas.get(cinemaID);
        BookingTicket ticket = targetCinema.makeABookingTicket(requestID, cinemaID, time, requestSeats);
        if (ticket == null) {
            System.out.println("Booking rejected");
        } else {
            addBookingTicket(requestID, ticket);
            System.out.println("Booking "+requestID+" "+ticket.getSeatString());
        }

    }

    /**
     * Dealing with a change of booking
     * @param bookingID the id of the booking to be changed
     * @param cinemaID the cinema of the new booking
     * @param time the time of the new booking
     * @param requestSeats the number of seats requested for the new booking
     * @pre requestSeats > 0
     * @post replacedTicket != null || originalTicket != null, and either change rejected because
     * of insufficient space and the original seats remained or the original seats released and
     * the new seats occupied
     */
    public void changeBooking(int bookingID, int cinemaID, String time, int requestSeats) {
        BookingTicket ticketToBeChanged = ticketsMap.get(bookingID); // original ticket
        Cinema originalCinema;
        Cinema replacedCinema;

        // bookingID to be changed doesn't exist
        if (ticketToBeChanged == null) {
            System.out.println("Change rejected");
        } else {
            originalCinema = cinemas.get(ticketToBeChanged.getCinemaID()); // original cinema
            replacedCinema = cinemas.get(cinemaID); // replaced cinema
            String originalTime = ticketToBeChanged.getTime();
            String originalSeatString = ticketToBeChanged.getSeatString();

            // first cancel the original one:
            originalCinema.cancelBooking(originalTime, originalSeatString, bookingID);
            ticketsMap.remove(bookingID);

            // book a new one:
            BookingTicket replacedTicket = replacedCinema.makeABookingTicket(bookingID, cinemaID, time, requestSeats);

            // change rejected only if there is no available seat
            if (replacedTicket == null) {
                System.out.println("Change rejected");
                originalCinema.recoverOriginalSeats(originalTime,originalSeatString);
                addBookingTicket(bookingID, ticketToBeChanged);
                originalCinema.collectCinemaTickets(bookingID, ticketToBeChanged);
            } else {
                System.out.println("Change " + bookingID + " " + replacedTicket.getSeatString());
                addBookingTicket(bookingID, replacedTicket);
            }
        }
    }

    /**
     * Dealing with a request of cancelling booking
     * @param bookingID the id of booking to be cancelled
     * @pre none
     * @post ticketsMap.get(bookingID) == null, and the original occupied seats has been released
     */
    public void cancelBooking(int bookingID) {
        BookingTicket ticketToBeDeleted = ticketsMap.get(bookingID);
        if (ticketToBeDeleted == null) {
            System.out.println("Cancel rejected");
        } else {
            int cid = ticketToBeDeleted.getCinemaID();
            Cinema targetCinema = cinemas.get(cid);
            String time = ticketToBeDeleted.getTime();
            String seats = ticketToBeDeleted.getSeatString();

            // empty seats in cinema:
            targetCinema.cancelBooking(time, seats, bookingID);

            // remove from tickets map:
            ticketsMap.remove(bookingID);

            // output:
            System.out.println("Cancel "+bookingID);
        }
    }

    /**
     * Print the details of session
     * @param cinemaID the id of cinema to be printed
     * @param time the time of the session
     * @pre cinemas.get(cinemaID) != null && the Session of the specified time must exist in that cinema
     * @post the session info would be printed
     */
    public void printSessionDetails(int cinemaID, String time) {
        Cinema c = cinemas.get(cinemaID);
        HashMap<Integer, BookingTicket> manyTickets = c.getCinemaTicketsMapForASession(time);
        String[] rows = c.getRows();

        // print movie name:
        System.out.println(c.getMovieFromTime(time));

        // print seats info:
        for (String row : rows) {
            String[] seats = new String[manyTickets.size()];
            int sum = 0;
            for (Integer i : manyTickets.keySet()) {
                BookingTicket ticket = manyTickets.get(i);
                String tmp = ticket.getSeatsInARow(row);
                if (tmp != null) {
                    seats[sum] = tmp;
                    sum ++;
                }
            }
            if (sum == 0) continue;

            // find correct seat strings matched with current "row"
            String[] filteredSeats = new String[sum];
            Integer[] keys = new Integer[sum];
            HashMap<Integer, String> sortMap = new HashMap<>();
            int i = 0;
            for(int k = 0; k < manyTickets.size(); k++) {
                if (seats[k] != null) {
                    filteredSeats[i++] = seats[k];
                }
            }

            // put every starting number of seat string as key of sortMap:
            for (int v = 0; v < filteredSeats.length; v++) {
                Pattern p = Pattern.compile("(^[0-9]+)");
                Matcher m = p.matcher(filteredSeats[v]);
                if (m.find()) {
                    Integer key = Integer.valueOf(m.group(1));
                    keys[v] = key;
                    sortMap.put(key, filteredSeats[v]);
                }
            }

            // sort the starting number of seat string:
            Arrays.sort(keys);

            // print details:
            System.out.print(row + ": ");
            for(int pos = 0; pos < sum; pos++) {
                if (pos == sum - 1) {
                    System.out.print(sortMap.get(keys[pos]) + "\n");
                } else {
                    System.out.print(sortMap.get(keys[pos]) + ",");
                }
            }
        }
    }

    /**
     * Create an element of ticketsMap
     * @param bookingID the id of booking
     * @param t the ticket of booking
     * @pre t != null
     * @post ticketsMap.get(bookingID) != null
     */
    private void addBookingTicket(int bookingID, BookingTicket t) {
            ticketsMap.put(bookingID, t);
    }

    public static void main(String[] args) {
        String line = "";
        String[] segments;
        String[] segs;
        int cinemaID;
        String row;
        int numSeats;
        String time;
        String movie;
        int requestID;
        int requestSeats;
        CinemaBookingSystem system = new CinemaBookingSystem();

        try (Scanner sc = new Scanner(new File(args[0]))) {
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                line += " #"; // To guarantee there is a "#" at the end for each line.
                if (line.matches("^Cinema.*")) {
                    segments = line.split("[ ]+", 4);
                    cinemaID = Integer.valueOf(segments[1]);
                    row = segments[2];
                    segs = segments[3].split("#");
                    segs[0] = segs[0].trim();
                    numSeats = Integer.valueOf(segs[0]);
                    system.addCinemasSeats(cinemaID, row, numSeats);
                } else if (line.matches("^Session.*")) {
                    segments = line.split("[ ]+", 4);
                    cinemaID = Integer.valueOf(segments[1]);
                    time = segments[2];
                    segs = segments[3].split("#");
                    segs[0] = segs[0].trim();
                    movie = segs[0];
                    system.addSession(cinemaID, movie, time);
                } else if (line.matches("^Request.*")) {
                    segments = line.split("[ ]+", 5);
                    requestID = Integer.valueOf(segments[1]);
                    cinemaID = Integer.valueOf(segments[2]);
                    time = segments[3];
                    segs = segments[4].split("#");
                    segs[0] = segs[0].trim();
                    requestSeats = Integer.valueOf(segs[0]);
                    system.bookingRequest(requestID, cinemaID, time, requestSeats);
                } else if (line.matches("^Cancel.*")) {
                    segments = line.split("[ ]+", 2);
                    segs = segments[1].split("#");
                    segs[0] = segs[0].trim();
                    requestID = Integer.valueOf(segs[0]);
                    system.cancelBooking(requestID);
                } else if (line.matches("^Change.*")) {
                    segments = line.split("[ ]+", 5);
                    requestID = Integer.valueOf(segments[1]);
                    cinemaID = Integer.valueOf(segments[2]);
                    time = segments[3];
                    segs = segments[4].split("#");
                    segs[0] = segs[0].trim();
                    requestSeats = Integer.valueOf(segs[0]);
                    system.changeBooking(requestID, cinemaID, time, requestSeats);
                } else if (line.matches("^Print.*")) {
                    segments = line.split("[ ]+", 3);
                    cinemaID = Integer.valueOf(segments[1]);
                    segs = segments[2].split("#");
                    segs[0] = segs[0].trim();
                    time = segs[0];
                    system.printSessionDetails(cinemaID, time);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
}
