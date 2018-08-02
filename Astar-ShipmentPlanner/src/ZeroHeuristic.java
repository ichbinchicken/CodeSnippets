import java.util.TreeMap;

/**
 * COMP2511 Assignment 2
 * @author Ziming(Toby) Zheng z5052592
 * This class implements a strategy of getting heuristic.
 * The heuristic is always zero.
 */
public class ZeroHeuristic implements Heuristic {

    /**
     * Constructor of ZeroHeuristic
     * @pre none
     * @post none
     */
    public ZeroHeuristic() {
    }

    /**
     * The implementation of Heuristic interface function for getting a specific heuristic as a strategy
     * @param thisState the info of a State object passed in, but not used in this method
     * @param sailingMap the info of travelling map, but not used in this method
     * @param fulfilled the TreeMap of already fulfilled orders, but not used in this method
     * @param total the TreeMap of total orders, but not used in this method
     * @return an integer zero as the amount of heuristic
     * @pre thisSate != null && sailingMap != null && fulfilled != null && total != null
     * @post none
     */
    public int getEstimate(State thisState, Map sailingMap, TreeMap<Integer, String> fulfilled,
                                                            TreeMap<Integer, String> total) {
        return 0;
    }
}
