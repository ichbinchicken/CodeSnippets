import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * COMP2511 Assignment 1
 * @author Ziming(Toby) Zheng z5052592
 * This class represents a cinema with funtionality of managing seats in different sessoins
 */
public class Cinema {
    private int cinemaID;
    private HashMap<Integer, BookingTicket> cinemaTicketsMap; //key=bookingID, value=BookingTicket
    private LinkedHashMap<String, Integer[]> seatsMap;  // key=row, value=Array of Seats
    private HashMap<String, Session> sessionsMap; // key=time, value=Session

    /**
     * Constructor of class Cinema
     * @param cinemaID the unique id of cinema
     * @pre cinemaID must be unique in each class Cinema
     * @post seatsMap != null && sessionsMap != null && cinemaTicketsMap != null
     */
    public Cinema(int cinemaID) {
        this.cinemaID = cinemaID;
        this.seatsMap = new LinkedHashMap<>();
        this.sessionsMap = new HashMap<>();
        this.cinemaTicketsMap = new HashMap<>();
    }

    /**
     * Initialise the template seats of a cinema
     * @param row the name of row to be added
     * @param numSeats the number of seats to be added in that row
     * @pre seatsMap != null
     * @post seatsMap.get(row) != null
     */
    public void addRowSeats(String row, Integer numSeats) {
        Integer[] seats = new Integer[numSeats];
        for(int i = 0; i < seats.length; i ++) seats[i] = 0;
        seatsMap.put(row, seats);
    }

    /**
     * Add a new session in a cinema
     * @param movie the name of the movie
     * @param time the time of session
     * @pre sessionsMap != null
     * @post sessionsMap.get(time) != null
     */
    public void addSession(String movie, String time) {
        LinkedHashMap<String, Integer[]> copiedSeatsMap = copySeatsMap(seatsMap);
        Session movieSession = new Session(movie, time, copiedSeatsMap);
        if (sessionsMap.get(time) == null) {
            sessionsMap.put(time, movieSession);
        }
    }

    /**
     * Generate a new booking ticket
     * @param bookingID the id of new booking
     * @param cinemaID the id of cinema
     * @param time the time of the session
     * @param requestSeats the number of seats to be occupied
     * @return a booking ticket with specified cinema, time and seats if successfully, otherwise null
     * @pre sessionsMap.get(time) != null
     * @post cinemaTicketsMap.get(bookingID) != null
     */
    public BookingTicket makeABookingTicket(int bookingID, int cinemaID, String time, int requestSeats) {
        Session s = sessionsMap.get(time);
        String seatString = s.occupySeats(requestSeats);
        if (seatString.equals("")) {
            return null;
        }
        BookingTicket newTicket = new BookingTicket(bookingID, cinemaID, time, seatString, requestSeats);
        collectCinemaTickets(bookingID, newTicket);
        return newTicket;
    }

    /**
     * Cancel a booking in this cinema
     * @param time the time of session
     * @param seatString the booked seats string
     * @param bookingID the id of booking to be cancelled
     * @pre sessionsMap.get(time) != null
     * @post cinemaTicketsMap.get(bookingID) == null
     */
    public void cancelBooking(String time, String seatString, int bookingID) {
        Session s = sessionsMap.get(time);
        String row = "";
        int startIndex = 0;
        int endIndex = 0;
        int count = (seatString.matches(".+-.+") ? 2 : 1);

        // parse seatString
        Pattern p = Pattern.compile("([A-Za-z]+)([0-9]+)");
        Matcher m = p.matcher(seatString);
        if (count == 1) {
            if (m.find()) {
                row = m.group(1);
                startIndex = Integer.valueOf(m.group(2)) - 1;
                endIndex = startIndex;
            }
        } else {
            int i = 0;
            while (m.find()) {
                if (i == 0) {
                    row = m.group(1);
                    startIndex = Integer.valueOf(m.group(2)) - 1;
                } else {
                    endIndex = Integer.valueOf(m.group(2)) - 1;
                }
                i ++;
            }
        }

        // call removeOccupation:
        s.removeOccupation(row, startIndex, endIndex);

        // remove tickets in map:
        cinemaTicketsMap.remove(bookingID);
    }

    /**
     * Recover the original seats
     * This is used when a change is requested and a new booking failed to replace the original one
     * Therefore, this can recover the original seats
     * @param time the time of session
     * @param seatString the booked seats string
     * @pre sessionsMap.get(time) != null
     * @post original seats have been occupied again
     */
    public void recoverOriginalSeats(String time, String seatString) {
        Session s = sessionsMap.get(time);
        String row = "";
        int startIndex = 0;
        int endIndex = 0;
        int count = (seatString.matches(".+-.+") ? 2 : 1);

        // parse seatString
        Pattern p = Pattern.compile("([A-Za-z]+)([0-9]+)");
        Matcher m = p.matcher(seatString);
        if (count == 1) {
            if (m.find()) {
                row = m.group(1);
                startIndex = Integer.valueOf(m.group(2)) - 1;
                endIndex = startIndex;
            }
        } else {
            int i = 0;
            while (m.find()) {
                if (i == 0) {
                    row = m.group(1);
                    startIndex = Integer.valueOf(m.group(2)) - 1;
                } else {
                    endIndex = Integer.valueOf(m.group(2)) - 1;
                }
                i ++;
            }
        }

        // call recoverSeats:
        s.recoverSeats(row, startIndex, endIndex);

    }

    /**
     * get tickets map for a particular cinema
     * @param time the time of session
     * @return the map of tickets for a particular cinema
     * @pre cinemaTicketsMap != null
     * @post none
     */
    public HashMap<Integer, BookingTicket> getCinemaTicketsMapForASession(String time) {
        HashMap<Integer, BookingTicket> copyMap = new HashMap<>();
        for(Integer i : cinemaTicketsMap.keySet()) {
            BookingTicket tmp = cinemaTicketsMap.get(i);
            if (tmp.getTime().equals(time)) {
                copyMap.put(i, tmp);
            }
        }
        return copyMap;
    }

    /**
     * put the booking ticket into cinemaTicketsMap for a particular cinema
     * @param bookingID the id of booking
     * @param t the booking ticket to be added
     * @pre t != null
     * @post cinemaTicketsMap.get(bookingID) != null
     */
    public void collectCinemaTickets(int bookingID, BookingTicket t) {
        cinemaTicketsMap.put(bookingID, t);
    }

    /**
     * get movie name for a particular time
     * @param time the time of session
     * @return the movie name
     * @pre sessionsMap.get(time) != null
     * @post none
     */
    public String getMovieFromTime(String time) {
        return sessionsMap.get(time).getMovie();
    }

    /**
     * get all of names of rows in a cinema
     * @return an array of rows
     * @pre seatsMap != null
     * @post none
     */
    public String[] getRows() {
        Set<String> keySet = seatsMap.keySet();
        String[] rows = new String[keySet.size()];
        int p = 0;
        for(String t : keySet) {
            rows[p] = t;
            p ++;
        }
        return rows;
    }

    /**
     * Copy a seatsMap for each session
     * This is used for selecting seats for each session
     * @param original the original template non-occupied seatsMap
     * @return a deep copy of seatsMap
     * @pre original != null
     * @post returned value is not null
     */
    private LinkedHashMap<String, Integer[]> copySeatsMap (LinkedHashMap<String, Integer[]> original) {
        LinkedHashMap<String, Integer[]> copiedSeatsMap = new LinkedHashMap<>();
        for (String row : original.keySet()) {
            Integer[] old = original.get(row);
            Integer[] copiedArray = new Integer[old.length];
            System.arraycopy(old, 0, copiedArray, 0, old.length);
            copiedSeatsMap.put(row, copiedArray);
        }
        return copiedSeatsMap;
    }

    /**
     * Compares if two cinemas are equal
     * @param obj the other object to be compared
     * @return true if two cinemas have equal cinemaID, otherwise false
     * @pre none
     * @post none
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Cinema)) {
            return false;
        }
        Cinema c = (Cinema) obj;
        return this.cinemaID == c.cinemaID;
    }

    /**
     * Generate a hash code for this Cinema
     * @return the hash code of this Cinema based on its ID
     * @pre none
     * @post none
     */
    @Override
    public int hashCode() {
        return Objects.hash(cinemaID);
    }

}