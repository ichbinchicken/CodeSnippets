import java.security.PublicKey;
import java.util.*;

/**
 * COMP2511 Assignment 2
 * @author Ziming(Toby) Zheng z5052592
 * This class is to store the entire map and simulate it.
 */
public class Map {
    private TreeMap<String, Port> ports;  // key=portName, value=Port, This is for sorting ports alphabetically
    private TreeMap<String, TreeMap<String, Integer>> sailingMap; //key=every port, value=weight to the city

    /**
     * Constructor of class Map
     * Initialise TreeMap ports to store info of each port
     * with key = portName, value = port object
     * Initialise TreeMap sailingMap to store the connection among ports
     * with key = name of port_i, value = (TreeMap of connected ports with key = portName, value = time cost)
     * @pre none
     * @post posts != null && sailingMap != null
     */
    public Map() {
        ports = new TreeMap<>();
        sailingMap = new TreeMap<>();
    }

    /**
     * Add a port into TreeMap ports to store info of every port
     * @param name the name of port as a key
     * @param p the Port object as a value
     * @pre ports.get(name) == null && p != null
     * @post ports.get(name) = p
     */
    public void addPortInDic(String name, Port p) {
        ports.put(name, p);
    }

    /**
     * Add a connection with the weight between two ports
     * @param portA the one end of the connected port
     * @param portB the another end of connected port
     * @param weight the time cost between two ports
     * @pre weight > 0
     * @post sailingMap.get(portA) != null && sailingMap.get(portA).get(portB) != null
     *  && sailingMap.get(portB) != null && sailingMap.get(portB).get(portA) != null
     */
    public void addConnectionInDic(String portA, String portB, int weight) {
        String[] tmp = new String[2];
        tmp[0] = portA;
        tmp[1] = portB;
        TreeMap<String, Integer> connectedPorts;

        for(int i = 0;i < 2;i++) {
            connectedPorts = sailingMap.get(tmp[i]);
            int j = Math.abs(i-1);

            // if there is no pair, then create a new one:
            if (connectedPorts == null) {
                connectedPorts = new TreeMap<>();
                connectedPorts.put(tmp[j], weight);
                sailingMap.put(tmp[i], connectedPorts);
            } else {
                connectedPorts.put(tmp[j], weight);
            }
        }

    }

    /**
     * Getter method for getting all neighbours of a specific current port
     * @param currPort the name of current port
     * @return a list of ports which are neighbours of this specific current port
     * @pre currPort must exist on the map
     * @post neighbours != null
     */
    public LinkedList<Port> getNeighbours(String currPort) {
        LinkedList<Port> neighbours = new LinkedList<>();
        TreeMap<String, Integer> targets = sailingMap.get(currPort);
        for (String s : targets.keySet()) {
            neighbours.add(ports.get(s));
        }
        return neighbours;
    }

    /**
     * Getter method for getting neighbour's weights of a specific current port
     * @param currPort the name of current port
     * @return a TreeMap of weights where key is the name of a neighbour and value is the cost
     */
    public TreeMap<String, Integer> getWeights(String currPort) {
        return sailingMap.get(currPort);
    }

    /**
     * Getter method for getting a specific neighbour of a specific port
     * @param from the specific name of port where a trip starts
     * @param to the specific name of port where a trip ends
     * @return an integer weight between the starting port and the ending port
     */
    public Integer getWeightOfAPort(String from, String to) {
        return sailingMap.get(from).get(to);
    }

    /**
     * Getter method for getting all the ports on the map
     * @return the TreeMap that contains all the info of Ports on the map
     */
    public TreeMap<String, Port> getPorts() {
        return ports;

    }
}
