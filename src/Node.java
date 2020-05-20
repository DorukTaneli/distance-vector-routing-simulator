import java.util.ArrayList;
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
        this.distanceTable = new int[10][10];
        this.bottleNeckBandwidthTable = new ArrayList<Integer>();
        isConverged = false;

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
