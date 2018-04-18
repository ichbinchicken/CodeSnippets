import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class CinemaBookingSystemTest {

    @org.junit.Test
    public void addCinemasSeats() {
        /*CinemaBookingSystem system = new CinemaBookingSystem();
        system.addCinemasSeats(1, "A", 20);
        system.addCinemasSeats(1, "B", 14);
        system.addCinemasSeats(2, "AA", 30);
        system.addCinemasSeats(2, "ABC", 5);
        system.addCinemasSeats(4, "T", 50);
        system.addCinemasSeats(18, "R", 23);
        system.addCinemasSeats(2, "C", 16);
        HashMap<Integer, Cinema> tmp = system.getCinemas();
        for(int i :tmp.keySet()) {
            System.out.println("CinemaKey = "+i);
            Cinema c = tmp.get(i);
            System.out.println("RealID = "+c.getCinemaID());
            System.out.println("Print seatsHashMap:");
            for(String s : c.getSeatsMap().keySet()) {
                System.out.println("Row: "+s+" Seats Size: "+c.getSeatsMap().get(s).length);
            }
        }*/
    }


    @org.junit.Test
    public void addSession() {
        /*CinemaBookingSystem system = new CinemaBookingSystem();
        system.addCinemasSeats(1, "A", 15);
        system.addCinemasSeats(1, "B", 20);
        system.addCinemasSeats(2, "B", 5);
        system.addCinemasSeats(2, "X", 5);
        system.addSession(1, "Toy Story", "09:00");
        system.addSession(1, "Ratatouille", "14:30");
        system.addSession(2, "Up", "09:00");

        HashMap<Integer, Cinema> tmp = system.getCinemas();
        for(int i :tmp.keySet()) {
            System.out.println("CinemaKey = "+i);
            Cinema c = tmp.get(i);
            System.out.println("RealID = "+c.getCinemaID());
            for(String j: c.getSessionsMap().keySet()) {
                Assert.assertEquals(j, c.getSessionsMap().get(j).getTime());
                System.out.println("time: " + c.getSessionsMap().get(j).getTime());
                System.out.println("movie: " + c.getSessionsMap().get(j).getMovie());
                for (String row : c.getSessionsMap().get(j).getSeatsMap().keySet()) {
                    System.out.println("row "+row+" of "+"seats: " + c.getSessionsMap().get(j).getSeatsMap().get(row).length);
                }
            }

        }*/
    }

    @Test
    public void bookingRequest() {
        /*CinemaBookingSystem system = new CinemaBookingSystem();
        system.addCinemasSeats(1,"Z", 80);
        system.addCinemasSeats(1, "Y", 30);
        system.addCinemasSeats(2, "AbC", 40);
        system.addCinemasSeats(2, "aBc", 100);

        system.addCinemasSeats(3, "Z", 60);
        system.addCinemasSeats(3, "Y", 110);

        system.addSession(1, "m1", "09:00");
        system.addSession(1, "m2", "12:00");
        system.addSession(1, "m3", "15:00");

        system.addSession(2, "m1", "09:00");
        system.addSession(2, "m2", "12:00");
        system.addSession(2, "m3", "15:00");

        system.addSession(3, "m1", "09:00");
        system.addSession(3, "m2", "12:00");
        system.addSession(3, "m3", "15:00");

        system.bookingRequest(1, 1, "09:00", 50); // Z1-Z50
        system.bookingRequest(2, 1, "12:00", 50); // Z1-Z50
        system.bookingRequest(3, 1, "15:00", 50); // Z1-Z50
        system.bookingRequest(4, 1, "15:00", 50); // rejected
        system.bookingRequest(5, 1, "12:00", 50); // rejected
        system.bookingRequest(6, 1, "09:00", 50); // rejected
        system.bookingRequest(7, 1, "09:00", 29); // Z51-Z79
        system.bookingRequest(8, 1, "09:00", 31); // rejected
        system.bookingRequest(9, 1, "09:00", 1); // Z80
        system.bookingRequest(10, 1, "12:00", 1); // Z51
        system.bookingRequest(11, 1, "15:00", 10); // Z51-Z60
        system.bookingRequest(12, 1, "15:00", 30); // Y1-Y30
        system.bookingRequest(13, 1, "15:00", 30); //rejected
        system.bookingRequest(14, 1, "09:00", 1); // Y1

        system.printSessionDetails(1, "09:00");
        system.printSessionDetails(1, "12:00");
        system.printSessionDetails(1, "15:00");

        System.out.println("Cancel some bookings and rebook:");

        system.cancelBooking(1);
        Assert.assertNull(system.getTicketsMap().get(1));
        system.bookingRequest(15, 1, "09:00", 50); // Z1-Z50

        system.cancelBooking(2);
        Assert.assertNull(system.getTicketsMap().get(2));
        system.bookingRequest(16, 1, "12:00",50); // Z1-Z50

        system.cancelBooking(3);
        Assert.assertNull(system.getTicketsMap().get(3));
        system.bookingRequest(17, 1, "15:00",50); // Z1-Z50

        system.cancelBooking(1);
        Assert.assertNull(system.getTicketsMap().get(1)); // Cancel rejected

        system.cancelBooking(12);
        Assert.assertNull(system.getTicketsMap().get(12));
        system.bookingRequest(18, 1, "15:00",20); // Z61-Z80
        system.printSessionDetails(1, "15:00");

        system.cancelBooking(17);
        Assert.assertNull(system.getTicketsMap().get(17));
        system.bookingRequest(19, 1, "15:00", 30); // Z1-Z30
        system.printSessionDetails(1, "15:00");*/
        /*system.cancelBooking(10);
        Assert.assertNull(system.getTicketsMap().get(10));


        system.cancelBooking(7);
        Assert.assertNull(system.getTicketsMap().get(7));*/
    }

    @Test
    public void changeBooking() {
        /*CinemaBookingSystem system = new CinemaBookingSystem();
        system.addCinemasSeats(3,"WOR", 10);
        system.addCinemasSeats(3, "ROW", 75);
        system.addCinemasSeats(4, "Z", 30);
        system.addCinemasSeats(4, "A", 200);

        system.addSession(3, "Darkest Hour", "15:00");
        system.addSession(3, "The Death Of Stalin", "19:15");
        system.addSession(4,"Love", "10:00");

        system.bookingRequest(1, 3,"15:00",10);
        system.bookingRequest(2,3,"15:00",50);
        system.bookingRequest(3,3,"15:00",20);
        system.printSessionDetails(3,"15:00");
        system.bookingRequest(4, 4, "10:00", 100);
        system.printSessionDetails(4, "10:00");
        system.changeBooking(4, 3, "15:00", 1);*/

    }
}