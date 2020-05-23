import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class Node {

    private int nodeID;
    private Hashtable<Integer, Integer> linkCost;
    private Hashtable<Integer, Integer> linkBandwidth;
    private int[][] distanceTable;
    private List<Integer> bottleNeckBandwidthTable;
    private boolean isConverged;

    public Node(int nodeID, Hashtable<Integer, Integer> linkCost, Hashtable<Integer, Integer> linkBandwidth) {
        this.nodeID = nodeID;
        this.linkCost = linkCost;
        this.linkBandwidth = linkBandwidth;
        this.distanceTable = new int[10][10]; //distanceTable[i][j] gives the cost to reach j from neighbor i
        Arrays.fill(distanceTable, 999);
        makeDistanceTable(linkCost);
        this.bottleNeckBandwidthTable = new ArrayList<Integer>();
        isConverged = false;

    }

    /**
     * <p>"
     *     Creates the initial distance table based on the costs to neighbors. Since initially the costs to other nodes are unknown, they are set to "infinity", 999.
     * </p>
     * @param linkCost hashtable that holds the cost to the direct neighbor i. The keys and values are integers
     */
    private void makeDistanceTable(Hashtable<Integer, Integer> linkCost) {
        for(int i=0; i<linkCost.size(); i++){
            distanceTable[i][i] = linkCost.get(i);
        }
    }

    /**
     * <p>
     *     The method is called when the current node receives a distance vector update from one of its neighbors.
     *     Upon receiving the message, the method checks if its own distance table needs to be changed in the case of a lower cost to a target node
     * </p>
     * @param m the message object that holds the distance vector of the node that sent the message
     */
    public void receiveUpdate(Message m){
        int sourceNode = m.getSenderID();
        int[] distanceVectorReceived = m.getDistanceVector();
        for(int i =0; i < distanceVectorReceived.length; i++){
            if(distanceVectorReceived[i] < distanceTable[sourceNode][i]){
               addToDistanceTable(sourceNode, i,distanceVectorReceived[i]);
            }
        }
    }
    //TODO
    public boolean sendUpdate(){
        if(!isConverged){
            Message message;
            int distanceVector[] = new int[distanceTable[0].length];
            // Make the distance vector based on shortest paths to all nodes
            for(int j=0; j<distanceTable[0].length; j++){
                distanceVector[j] = 999;
                for(int i=0; i<distanceTable.length; i++){
                    if(distanceTable[i][j]<distanceVector[j]){
                        distanceVector[j] = distanceTable[i][j];
                    }
                }
            }
            // Traverse all the neighbors and notify them
            for(int i = 0; i<linkCost.size(); i++){
                if(linkCost.containsKey(i)){
                    int neighborID = i;
                    message = new Message(this.nodeID, neighborID, linkBandwidth.get(i), distanceVector);
                }

            }
            return true;
        }
        return false;
    }

    //TODO
    public Hashtable<String, String> getForwardingTable(){
        Hashtable<String, String> forwardingTable = new Hashtable<String, String>();

        return forwardingTable;
    }

    public void addToLinkCostTable(int neighbor, int cost){
        this.linkCost.put(neighbor, cost);
    }
    public void addToLinkBandwidthTable(int neighbor, int cost){
        this.linkBandwidth.put(neighbor, cost);
    }
    public void addToDistanceTable(int neighbor, int destination, int cost){
        //If the index of the neigbor or the destination exceeds the size of the distanceTable
        //increase its size
        if(distanceTable.length < neighbor) {
            int[][] temp = new int[neighbor + 10][neighbor + 10];
            for (int i = 0; i < distanceTable.length; i++) {
                for (int j = 0; j < distanceTable.length; j++) {
                    temp[i][j] = distanceTable[i][j];
                }
            }
            setDistanceTable(temp);
        }
        if(distanceTable.length < destination) {
            int[][] temp = new int[destination + 10][destination + 10];
            for (int i = 0; i < distanceTable.length; i++) {
                for (int j = 0; j < distanceTable.length; j++) {
                    temp[i][j] = distanceTable[i][j];
                }
            }
            setDistanceTable(temp);
        }
        this.distanceTable[destination][neighbor] = cost;
    }
    public void addToBottleNeckBandwidthTable(int neighbor, int cost){
        this.bottleNeckBandwidthTable.add(neighbor, cost);
    }

    //Getter and setter methods

    public boolean isConverged() {
        return isConverged;
    }

    public void setConverged(boolean converged) {
        isConverged = converged;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public void setLinkCost(Hashtable<Integer, Integer> linkCost) {
        this.linkCost = linkCost;
    }

    public void setLinkBandwidth(Hashtable<Integer, Integer> linkBandwidth) {
        this.linkBandwidth = linkBandwidth;
    }

    public void setDistanceTable(int[][] distanceTable) {
        this.distanceTable = distanceTable;
    }

    public int getNodeID() {
        return nodeID;
    }

    public Hashtable<Integer, Integer> getLinkCost() {
        return linkCost;
    }

    public Hashtable<Integer, Integer> getLinkBandwidth() {
        return linkBandwidth;
    }

    public int[][] getDistanceTable() {
        return distanceTable;
    }


}
