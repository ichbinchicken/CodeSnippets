import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.TreeMap;

import static org.junit.Assert.*;

    public class SessionTest {

        /*private Integer[] generator(int num) {
            Integer[] s = new Integer[num];
            for(int i = 0;i < s.length;i++) s[i] = 0;
            return s;
        }*/

@Test
        public void getOccupiedSeatsInARow() {
            /*LinkedHashMap<String, Integer[]> seatsMap = new LinkedHashMap<>();
            seatsMap.put("CA", generator(12));
            seatsMap.put("BQ", generator(13));
            seatsMap.put("XXY", generator(23));
            seatsMap.put("UUD", generator(30));
            Session s1 = new Session("m1", "08:00", seatsMap);
            System.out.println(s1.occupySeats(12));
            System.out.println(s1.getOccupiedSeatsInARow("CA"));
            s1.removeOccupation("CA", 1, 2);
            s1.removeOccupation("CA", 7, 9);
            System.out.println(s1.getOccupiedSeatsInARow("CA"));
            s1.removeOccupation("CA", 10, 11);
            System.out.println(s1.getOccupiedSeatsInARow("CA"));*/
        }

@Test
    public void occupySeats() {
        /*LinkedHashMap<String, Integer[]> seatsMap = new LinkedHashMap<>();
        seatsMap.put("CA", generator(12));
        seatsMap.put("BQ", generator(13));
        seatsMap.put("XXY", generator(23));
        seatsMap.put("UUD", generator(30));
        Session s1 = new Session("m1", "08:00", seatsMap);
        System.out.println("Testing Session s1:");
        Assert.assertTrue(s1.occupySeats(10));
        Assert.assertTrue(s1.occupySeats(13));
        Assert.assertTrue(s1.occupySeats(20));
        Assert.assertTrue(s1.occupySeats(25));
        Assert.assertTrue(s1.occupySeats(2));
        Assert.assertTrue(s1.occupySeats(5));
        Assert.assertFalse(s1.occupySeats(4));
        Assert.assertFalse(s1.occupySeats(5));
        Assert.assertFalse(s1.occupySeats(7));
        Assert.assertTrue(s1.occupySeats(1));
        Assert.assertFalse(s1.occupySeats(3));
        Assert.assertFalse(s1.occupySeats(4));
        Assert.assertTrue(s1.occupySeats(2));
        Assert.assertFalse(s1.occupySeats(1));
        System.out.println("Simple Tests succeed!");
        System.out.println("Some seats will deleted and re-occupy...");
        s1.removeOccupation("CA", 0, 3);
        s1.removeOccupation("BQ", 9, 12);
        Assert.assertFalse(s1.occupySeats(5));
        Assert.assertTrue(s1.occupySeats(4));
        Assert.assertTrue(s1.occupySeats(2));
        Assert.assertFalse(s1.occupySeats(4));
        Assert.assertTrue(s1.occupySeats(2));
        Assert.assertFalse(s1.occupySeats(1));
        s1.removeOccupation("UUD", 5, 20);
        s1.removeOccupation("XXY", 3, 12);
        Assert.assertTrue(s1.occupySeats(15));
        Assert.assertFalse(s1.occupySeats(15));
        Assert.assertFalse(s1.occupySeats(11));
        Assert.assertTrue(s1.occupySeats(8));
        System.out.println("Tests passed!");*/
    }

}
