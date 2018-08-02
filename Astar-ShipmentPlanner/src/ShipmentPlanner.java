import java.util.*;
import java.io.*;

/**
 * COMP2511 Assignment 2
 * @author Ziming(Toby) Zheng z5052592
 * This class implements the entry point of shipment planner system.
 */

/**
 * The javadoc in CostHeuristic class contains the description of my strategy.
 * Experimental Analysis of Heuristic:
 * I first ran some experiments to show efficiency of my heuristic:
 * For sample input on the spec, There are 72 nodes expanded with zero heuristic,
 * but only 37 nodes expanded with my heuristic(in "CostHeuristic class").
 * For my submitted input1.txt, there are 754 nodes expanded with zero heuristic,
 * but only 524 nodes expanded with my CostHeuristic.
 * For my submitted input2.txtThere are 244433 nodes expanded with zero heuristic,
 * but only 204335 nodes expanded with CostHeuristic, reducing 40098 nodes.
 * For my submitted input3.txt, there are 1310578 nodes expanded with zero heuristic,
 * but only 1282660 nodes expanded with CostHeuristic, reducing 27918 nodes.
 * Time Complexity Analysis:
 * N = number of total orders
 * best case: O(N)
 * The first loop in the heuristic method will check whether the chosen order is fulfilled or not
 * through all total orders. Ideally, if there is no unfinished orders, the program will skip the second
 * loop in the heuristic method. In total, it's O(N).
 * worst case: O(N)
 * The first loop's time complexity is explained as above. The second loop will calculate each weight between two ports
 * for each order. Note that getting weight from Map is constant time because I used TreeMap to store the map information.
 * Therefore O(N) + O(N) = O(N)
 */
public class ShipmentPlanner {
    private Map sailingMap;
    private Astar astarSearch;

    /**
     * Constructor of ShipmentPlanner class
     * Create a Map object to represent the sailing map.
     * @pre none
     * @post sailingMap != null
     */
    public ShipmentPlanner(Heuristic strategy) {
        sailingMap = new Map();
        astarSearch = new Astar(strategy);
    }

    /**
     * Store the info of port into TreeMap ports in sailing map object
     * @param portName the name of port
     * @param time the refuelling time in that port
     * @pre portName != null && time > 0
     * @post sailingMap.getPorts().get(portName) != null
     */
    public void addPort(String portName, int time) {
        Port portCity = new Port(portName, time);
        sailingMap.addPortInDic(portName, portCity);
    }

    /**
     * Add a connection between two ports with travel time
     * @param portA the name of one end port
     * @param portB the name of another end port
     * @param travelTime the time cost of travelling
     * @pre travelTime > 0
     * @post sailingMap.getSailingMap.get(portA) != null
     *  && sailingMap.getSailingMap.get(portB) != null
     */
    public void addConnection(String portA, String portB, int travelTime) {
        sailingMap.addConnectionInDic(portA, portB, travelTime);
    }

    /**
     * Add an order into total order(the goal state of Astar search)
     * @param start the starting port
     * @param end the ending port
     * @pre starting port and ending port must exist on the map
     * @post totalOrder in astarSearch is not null.
     */
    public void addOrder(String start, String end) {
        astarSearch.addOrder(start, end);
    }

    /**
     * Doing the Astar search to find the shortest path
     * @pre none
     * @post the final path has the minimum time cost
     */
    public void planning() {
        LinkedList<String> path = astarSearch.searchAlgorithm(sailingMap);
        if (path == null) {
            System.out.println("No Path: There should always have a path. A* search may have problems");
        } else {
            Iterator<String> it = path.iterator();
            String before = "";
            String curr;
            while (it.hasNext()) {
                curr = it.next();
                if (!before.equals("")) System.out.println("Ship " + before + " to " + curr);
                before = curr;
            }
        }
    }

    public static void main(String[] args) {
        String line;
        String[] segments;
        int portRefuelTime;
        String port;
        String portA;
        String portB;
        int travelTime;

        // Change the heuristic strategy between ZeroHeuristic and CostHeuristic:
        // Heuristic h = new ZeroHeuristic();
        Heuristic h = new CostHeuristic();
        ShipmentPlanner sys = new ShipmentPlanner(h);

        try (Scanner sc = new Scanner(new File(args[0]))) {
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                if (line.matches("^Refuelling.*")) {
                    segments = line.split("[ ]+");
                    portRefuelTime = Integer.valueOf(segments[1]);
                    port = segments[2];
                    sys.addPort(port, portRefuelTime);
                } else if (line.matches("^Time.*")) {
                    segments = line.split("[ ]+");
                    travelTime = Integer.valueOf(segments[1]);
                    portA = segments[2];
                    portB = segments[3];
                    sys.addConnection(portA, portB, travelTime);
                } else if (line.matches("^Shipment.*")) {
                    segments = line.split("[ ]+");
                    portA = segments[1];
                    portB = segments[2];
                    sys.addOrder(portA, portB);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        sys.planning();
    }
}
