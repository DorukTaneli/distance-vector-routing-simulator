import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Node {

    private int nodeID;
    Hashtable<Integer, Integer> linkCost;
    Hashtable<Integer, Integer> linkBandwidth;
    private List<Integer> distanceTable;

    public Node(int nodeID, Hashtable<Integer, Integer> linkCost, Hashtable<Integer, Integer> linkBandwidth) {
        this.nodeID = nodeID;
        this.linkCost = linkCost;
        this.linkBandwidth = linkBandwidth;
        distanceTable = new ArrayList<Integer>();
    }

    //TODO
    public void receiveUpdate(Message m){

    }

    //TODO
    public boolean sendUpdate(){
        return false;
    }

    //TODO
    public Hashtable<String, String> getForwardingTable(){
        Hashtable<String, String> forwardingTable = new Hashtable<String, String>();

        return forwardingTable;
    }
}
