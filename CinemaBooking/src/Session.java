import java.util.*;

/**
 * COMP2511 Assignment 1
 * @author Ziming(Toby) Zheng z5052592
 * This class represents a cinema session containing the seats for one particular movie at a particular time
 */
public class Session {
    private String movie;
    private String time;
    private LinkedHashMap<String, Integer[]> seatsMap;

    /**
     * Constructor of Session class
     * @param movie the movie name
     * @param time the time of this session in the format "HH:MM"
     * @param seatsMap the seats map for this session
     * @pre seatsMap != null
     * @post this.seatsMap != null
     */
    public Session(String movie, String time, LinkedHashMap<String, Integer[]> seatsMap) {
        this.movie = movie;
        this.time = time;
        this.seatsMap = seatsMap;
    }

    /**
     * Mark the given number of consecutive seats as occupied
     * @param seatsToBeOccupied the number of seats to be allocated
     * @return a string representing the booked seats; if not successful, return empty string
     * @pre this.seatsMap != null
     * @post if successful, seatsMap will have seatsToBeOccupied number of seats marked as booked
     */
    public String occupySeats (int seatsToBeOccupied) {
        String seatString = "";
        int count;
        boolean firstOne;
        int startIndex;

        for(String row : seatsMap.keySet()) {
            Integer[] thisRow = seatsMap.get(row);
            count = 0;
            firstOne = true;
            startIndex = 0;
            for(int i = 0; i < thisRow.length; i++) {
                if (count == seatsToBeOccupied) {
                    break;
                }
                if (thisRow[i] != 1) {
                    if (firstOne) {
                        firstOne = false;
                        startIndex = i;
                    }
                    count ++;
                } else {
                    firstOne = true;
                    count = 0;
                }
            }
            if (count == seatsToBeOccupied) {
                for(int j = startIndex; j < startIndex + seatsToBeOccupied; j++) thisRow[j] = 1;
                if (seatsToBeOccupied == 1) {
                    seatString = row + (startIndex+1);
                } else {
                    seatString = row + (startIndex+1) + "-" + row + (startIndex+seatsToBeOccupied);
                }
                break;
            }
        }
        return seatString;
    }

    /**
     * Free up given range of seats
     * @param row the name of the row to be freed
     * @param startIndex the starting index of seats to be freed
     * @param endIndex the ending index of seats to be freed
     * @pre this.seatsMap != null
     * @post if row exists in seatsMap, seats from startIndex to endIndex will be freed
     */
    public void removeOccupation(String row, int startIndex, int endIndex) {
        Integer[] thisRow = seatsMap.get(row);
        if (thisRow != null) {
            for(int i = startIndex; i <= endIndex; i++) thisRow[i] = 0;
        }
    }

    /**
     * Restore original booking of seats in given range
     * @param row the row name of the seats in the original booking
     * @param startIndex the starting index of the seats to be restored
     * @param endIndex the ending index of the seats to be restored
     * @pre this.seatsMap != null
     * @post seats from startIndex to endIndex are occupied again
     */
    public void recoverSeats(String row, int startIndex, int endIndex) {
        Integer[] thisRow = seatsMap.get(row);
        if (thisRow != null) {
            for(int i = startIndex; i <= endIndex; i++) thisRow[i] = 1;
        }
    }

    /**
     * Getter for movie name
     * @return movie name
     * @pre none
     * @post none
     */
    public String getMovie() {
        return movie;
    }

    /**
     * Compares if two sessions are equal
     * @param obj the other object to be compared
     * @return true if two sessions have the same time otherwise false
     * @pre none
     * @post none
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Session)) {
            return false;
        }
        Session s = (Session) obj;
        return this.time.equals(s.time);
    }

    /**
     * Generate a hash code for this Session
     * @return the hash code of this Session based on its time
     * @pre none
     * @post none
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(time);
    }
}