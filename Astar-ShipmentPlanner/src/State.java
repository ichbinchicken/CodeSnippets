import java.util.*;

/**
 * COMP2511 Assignment 2
 * @author Ziming(Toby) Zheng z5052592
 * This class stores information for each state in state-space
 */
public class State {
    private String currentPortName;
    private LinkedList<String> path;
    private TreeMap<Integer, String> fulfilled;
    private int numFulfilled;
    private int totalTimeCost;
    private int totalRealTime;

    /**
     * Constructor class of State class
     * @param currentPortName the name of current port
     * @param path the optimal path from Sydney to the current port so far
     * @param fulfilled the covered orders from total orders in this optimal path
     * @param numFulfilled the number of fulfilled orders out of number of total orders in this optimal path
     * @param totalTimeCost the total time cost including heuristic accumulated so far
     * @param totalRealTime the total travelling and refuelling excluding heuristic time accumulated so far
     * @pre none
     * @post path != null && fulfilled != null && numFulfilled >= 0 && totalTimeCost >= 0 && totalRealTime >=0
     */
    public State(String currentPortName, LinkedList<String> path, TreeMap<Integer, String> fulfilled,
                 int numFulfilled, int totalTimeCost, int totalRealTime) {
        this.currentPortName = currentPortName;
        this.path = path;
        this.fulfilled = fulfilled;
        this.numFulfilled = numFulfilled;
        this.totalTimeCost = totalTimeCost;
        this.totalRealTime = totalRealTime;
    }

    /**
     * generate a unique hash StringBuilder to distinguish/match each state
     * @return a unique hash StringBuilder
     * @pre fulfilled != null && currentPortName must exist on the map
     * @post hashStr != null
     */
    public StringBuilder generateStateString() {
        StringBuilder hashStr = new StringBuilder(currentPortName);
        hashStr.append(",");
        for (Integer i : fulfilled.keySet()) {
            hashStr.append(fulfilled.get(i));
            hashStr.append(",");
        }
        return hashStr;
    }

    /**
     * getter method for getting already fulfilled orders
     * @return the TreeMap of fulfilled orders with key=id, value=orderString
     * @pre none
     * @post fulfilled != null
     */
    public TreeMap<Integer, String> getFulfilled() {
        return fulfilled;
    }

    /**
     * getter method for getting the current state's port name
     * @return the port name of current state
     * @pre none
     * @post a string containing the name of destination port (from Sydney) in this state
     */
    public String getCurrentPortName() {
        return currentPortName;
    }

    /**
     * getter method for getting the number of fulfilled orders
     * @return the integer of already fulfilled orders
     * @pre none
     * @post none
     */
    public int getNumFulfilled() {
        return numFulfilled;
    }

    /**
     * getter method for getting the path from Sydney to the current port in this state
     * @return a path consisting of a list of ports names
     * @pre none
     * @post path != null
     */
    public LinkedList<String> getPath() {
        return path;
    }

    /**
     * getter method for getting total real time cost(only having weight and refuelling time)
     * @return the total real time cost
     * @pre none
     * @post none
     */
    public int getTotalRealTime() {
        return totalRealTime;
    }

    /**
     * getter method for getting the total cost(total real time + heuristic)
     * @return the total cost(total real time + heuristic)
     * @pre none
     * @post none
     */
    public int getTotalTimeCost() {
        return totalTimeCost;
    }

    /**
     * copy a path in this state to another
     * @return a copy of path consisting of a list port names
     * @pre none
     * @post copied != null
     */
    public LinkedList<String> copyPath() {
        LinkedList<String> copied = new LinkedList<>();
        copied.addAll(path);
        return copied;
    }

    /**
     * setter method for setting the totalTimeCost(total real time + heuristic)
     * @param totalTimeCost updated totalTimeCost
     * @pre none
     * @post none
     */
    public void setTotalTimeCost(int totalTimeCost) {
        this.totalTimeCost = totalTimeCost;
    }

    /**
     * setter method of setting the totalRealTime(weight + refuelling time)
     * @param totalRealTime updated totalRealTime
     * @pre none
     * @post none
     */
    public void setTotalRealTime(int totalRealTime) {
        this.totalRealTime = totalRealTime;
    }

    /**
     * copy a TreeMap of already fulfilled orders
     * @return copied TreeMap of fulfilled orders
     * @pre none
     * @post copied != null
     */
    public TreeMap<Integer, String> copyFulfilled() {
        TreeMap<Integer, String> copied = new TreeMap<>();
        for(Integer key : fulfilled.keySet()) {
            copied.put(key, fulfilled.get(key));
        }
        return copied;

    }

    /**
     * compare if two states are the same
     * @param other the other object to be compared
     * @return true if two states have the same state string otherwise false
     * @pre none
     * @post none
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other.getClass() != this.getClass()) return false;
        State s = (State) other;
        StringBuilder hash = this.generateStateString();
        StringBuilder sHash = s.generateStateString();
        return hash.toString().equals(sHash.toString());
    }

    /**
     * generate a hash code for this State
     * @return the hash code of this State based on the unique state string
     * @pre none
     * @post none
     */
    @Override
    public int hashCode() {
        StringBuilder hash = this.generateStateString();
        return Objects.hash(hash.toString());
    }
}
