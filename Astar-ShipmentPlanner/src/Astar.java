import java.util.*;

/**
 * COMP2511 Assignment 2
 * @author Ziming(Toby) Zheng z5052592
 * This class implements the A star algorithm
 */
public class Astar {
    private Heuristic heuristicStrategy;
    private TreeMap<Integer, String> totalOrder;
    private int numberOfTotalNumber;
    private PriorityQueue<State> open; // open states priority queue
    private int expanded; // number of expanded nodes

    /**
     * Constructor of Astar class
     * Doing Astar search state by state
     * @param strategy the heuristic strategy chosen
     * @pre strategy != null
     * @post totalOrder != null && heuristicStrategy != null && expanded >= 0 && open != null
     */
    public Astar(Heuristic strategy) {
        heuristicStrategy = strategy;
        totalOrder = new TreeMap<>();
        numberOfTotalNumber = 0;
        open = new PriorityQueue<>(new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                int t1 = o1.getTotalTimeCost();
                int t2 = o2.getTotalTimeCost();
                if (t1 != t2) {
                    return t1 - t2;
                } else {
                    return o1.getCurrentPortName().compareTo(o2.getCurrentPortName());
                }
            }
        });
        expanded = 0;
    }

    /**
     * Add an order into totalOrder
     * @param start the starting port
     * @param end the ending port
     * @pre start and end must exist on the map
     * @post totalOrder != null
     */
    public void addOrder(String start, String end) {
       String order = start + "," + end;
       totalOrder.put(numberOfTotalNumber, order);
       numberOfTotalNumber ++;
    }

    /**
     * A star search to find a path with min time cost covering all required orders
     * @param sailingMap a sailing map showing connection of all ports
     * @return a path with min time cost covering all required orders
     * @pre sailingMap != null
     * @post the returned path is the optimal solution
     */
    public LinkedList<String> searchAlgorithm(Map sailingMap) {
        State resultState = null;
        TreeMap<String, Port> ports = sailingMap.getPorts();
        HashMap<String, State> openStateDict = new HashMap<>();
        HashMap<String, State> closedStateDict = new HashMap<>();
        StringBuilder hash;

        /* initial state: */
        State init = setInitState(ports);
        hash = init.generateStateString();
        open.add(init);
        openStateDict.put(hash.toString(), init); //key=currentLocation, val=State
        State current = null;
        // closed is empty now

        /* loop for expanding the graph */
        boolean found = false;

        while (!open.isEmpty()) {

            /* remove the lowest rank item from OPEN state: */
            current = open.poll();

            // if there is no record in openSateDict, skip this loop
            hash = current.generateStateString();
            if (openStateDict.get(hash.toString()) == null) continue;

            String currPortName = current.getCurrentPortName();

            /* check whether current is the goal state */
            if (current.getNumFulfilled() == numberOfTotalNumber) {
                found = true;
                resultState = current;
                break;
            }

            // if not the goal state, current state will be in CLOSE
            openStateDict.remove(hash.toString());
            closedStateDict.put(hash.toString(), current);
            expanded ++;

            /* get neighbours of currentPort: */
            LinkedList<Port> neighbours = sailingMap.getNeighbours(currPortName);
            TreeMap<String, Integer> neighbourWeights = sailingMap.getWeights(currPortName);

            for (Port neighbourPort : neighbours) {
                String currNeighbourName = neighbourPort.getPortName();
                State currNeighbourState = setState(current, currNeighbourName);
                hash = currNeighbourState.generateStateString();
                String hashStr = hash.toString();
                int weight = neighbourWeights.get(currNeighbourName);
                Port p = ports.get(currPortName);

                /* cost = g(current) + movementCost */
                int totalRealTime = current.getTotalRealTime() + p.getRefuelingTime() + weight;

                /* if 's' in OPEN and new totalRealTime less than s.getTotalRealTime()
                 * where 's' has the same current location as neighbourPort
                 * remove 's' from openStateDict
                 */
                boolean inOpen = false;
                State tmpState = openStateDict.get(hashStr);
                if (tmpState != null) inOpen = true;
                if (inOpen && totalRealTime < tmpState.getTotalRealTime()) {
                    openStateDict.remove(hashStr);
                    inOpen = false;
                }

                /* if 's' in CLOSE and new totalRealTime less than s.getTotalRealTime()
                 * where 's' has the same current location as neighbourPort
                 * remove 's' from closedStateDict
                 */
                boolean inClosed = false;
                tmpState = closedStateDict.get(hashStr);
                if (tmpState != null) inClosed = true;
                if (inClosed && totalRealTime < tmpState.getTotalRealTime()) {
                    closedStateDict.remove(hashStr);
                    inClosed = false;
                }


                /* add in open state */
                if (!inOpen && !inClosed) {
                    // have to set realTimeCost and realTime before adding into OPEN state
                    TreeMap<Integer, String> fulfilled = currNeighbourState.getFulfilled();

                    /* heuristic = getEstimate() + neighbourPort refuelling time */
                    int heuristic = heuristicStrategy.getEstimate(currNeighbourState,
                                                                  sailingMap, fulfilled, totalOrder);

                    heuristic += ports.get(currNeighbourName).getRefuelingTime();

                    currNeighbourState.setTotalTimeCost(totalRealTime + heuristic);
                    currNeighbourState.setTotalRealTime(totalRealTime);

                    open.add(currNeighbourState);
                    openStateDict.put(hashStr, currNeighbourState);
                }
            }
        }

        if (found) {
            System.out.println(expanded + " nodes expanded");
            System.out.println("cost = " + resultState.getTotalRealTime());
            return resultState.getPath();
        } else {
            return null;
        }
    }

    /**
     * Set initial state in state-space search
     * @param ports all the Port objects storing info of each port
     * @return an initial State object
     * @pre ports != null
     * @post new State != null
     */
    private State setInitState (TreeMap<String, Port> ports) {
        // path:
        LinkedList<String> path = new LinkedList<>();
        path.add("Sydney");

        // fulfilled order:
        TreeMap<Integer, String> fulfilled = new TreeMap<>();

        // num of fulfilled order
        int numFulfilled = 0;

        State newState = new State("Sydney", path, fulfilled, numFulfilled, 0, 0);
        // realTime and totalTimeCost
        Port p = ports.get("Sydney");

        return newState;
    }

    /**
     * Set a neighbour state when neighbour nodes have been expanded by the current node
     * @param current current State object storing all the information of a state
     * @param neighbourName the port name of current's neighbour
     * @return a state corresponding to the neighbour port
     * @pre current != null && neighbourName must exist on the map
     * @post new state != null
     */
    private State setState(State current, String neighbourName) {
        // path:
        LinkedList<String> path = current.copyPath();
        path.add(neighbourName);

        // fulfilled order:
        String currPortName = current.getCurrentPortName();

        TreeMap<Integer, String> fulfilled = current.copyFulfilled();

        // check whether fulfilled has already added this order:
        boolean alreadyAdded = false;
        String newOrder = currPortName + "," + neighbourName;

        for(Integer i : fulfilled.keySet()) {
            if (newOrder.equals(fulfilled.get(i))) {
                alreadyAdded = true;
                break;
            }
        }

        boolean newAdded = false;
        if (!alreadyAdded) {
            for (Integer i: totalOrder.keySet()) {
                if (newOrder.equals(totalOrder.get(i))) {
                    fulfilled.put(i, newOrder);
                    newAdded = true;
                    break;
                }
            }
        }

        // number of fulfilled
        int numFulfilled = current.getNumFulfilled();
        if (newAdded) {
            numFulfilled += 1;
        }

        return new State(neighbourName, path, fulfilled, numFulfilled, 0, 0);
    }
}
