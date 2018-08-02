import java.util.TreeMap;
import java.util.ArrayList;

/**
 * COMP2511 Assignment 2
 * @author Ziming(Toby) Zheng z5052592
 * This class implements a strategy of getting heuristic,
 * The heuristic is the sum of real time cost of all unfinished orders
 * plus the real time cost of shortest path between the port in current state
 * and one of the starting port in the list of unfinished orders.
 */
public class CostHeuristic implements Heuristic {
    /**
     * Constructor of CostHeuristic
     * @pre none
     * @post none
     */
    public CostHeuristic() {
    }

    /**
     * The implementation of Heuristic interface function for getting a specific heuristic as a strategy
     * @param thisState the info of a State object passed in
     * @param sailingMap the info of travelling map
     * @param fulfilled the TreeMap of already fulfilled orders
     * @param total the TreeMap of total orders
     * @return an integer representing the sum of real time cost of all unfinished orders
     *         plus the real time cost of shortest path between the port in current state
     *         and one of the starting port in the list of unfinished orders.
     * @pre thisSate != null && sailingMap != null && fulfilled != null && total != null
     * @post estimate + shortest > 0
     */
    public int getEstimate(State thisState, Map sailingMap, TreeMap<Integer, String> fulfilled,
                                                            TreeMap<Integer, String> total) {
        int estimate = 0;
        ArrayList<Integer> remaining = new ArrayList<>();
        int shortest = -1;
        String currPort = thisState.getCurrentPortName();
        Integer pathCost;

        for (Integer i : total.keySet()) {
            // If the order is not in fulfilled, it will be put in remaining.
            if (fulfilled.get(i) == null) {
                remaining.add(i);
            }
        }

        for(Integer orderID : remaining) {
            String orderStr = total.get(orderID);
            String[] orders = orderStr.split(",");
            String startPort = orders[0];
            String endPort = orders[1];
            Integer cost = sailingMap.getWeightOfAPort(startPort, endPort);
            estimate += cost;

            if (shortest == 0) continue;

            // compare the shortest path from currPort to startPort of each remaining order
            if (!currPort.equals(startPort)) {
                pathCost = sailingMap.getWeightOfAPort(currPort, startPort);
            } else {
                pathCost = 0;
            }

            if (pathCost != 0) {
                if (shortest < 0) {
                    shortest = pathCost;
                } else {
                    if (pathCost < shortest) {
                        shortest = pathCost;
                    }
                }
            } else {
                shortest = 0;
            }
        }

        return estimate + shortest;
    }
}