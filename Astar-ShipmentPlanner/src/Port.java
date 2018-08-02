import java.util.Objects;

/**
 * COMP2511 Assignment 2
 * @author Ziming(Toby) Zheng z5052592
 * This class stores the information of a Port.
 * A port is a node on the graph.
 */
public class Port {
    private String portName;
    private int refuelingTime;

    /**
     * Constructor of Port class
     * @param portName the name of this port
     * @param time the the refuelling time of this port
     */
    public Port(String portName, int time) {
        this.portName = portName;
        this.refuelingTime = time;
    }

    /**
     * getter method for getting the refuelling time of this port
     * @return the refuelling time
     */
    public int getRefuelingTime() {
        return refuelingTime;
    }

    /**
     * getter method for getting the name of this port
     * @return the name of this port
     */
    public String getPortName() {
        return portName;
    }

    /**
     * generate a hash code for this Port
     * @return the hash code of this Port based on the unique port name
     * @pre none
     * @post none
     */
    @Override
    public int hashCode() {
        return Objects.hash(portName);
    }

    /**
     * compare if two Ports are the same
     * @param other the other object to be compared
     * @return true if two ports have the same port name otherwise false
     * @pre none
     * @post none
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other.getClass() != this.getClass()) return false;
        Port p = (Port) other;
        return p.getPortName().equals(this.portName);
    }
}
