import java.util.TreeMap;
/**
 * COMP2511 Assignment 2
 * @author Ziming(Toby) Zheng z5052592
 * This is the interface of heuristic strategy.
 */
public interface Heuristic {

    public int getEstimate(State thisState, Map sailingMap, TreeMap<Integer, String> fulfilled,
                                                            TreeMap<Integer, String> total);
}
