import java.util.*;

public class Node {
    private int nodeID;
    private Hashtable <Integer, Node> neighbors;
    private Hashtable<Integer, Integer> linkCost; //holds the link costs to the node's direct neighbors
    private Hashtable<Integer, Integer> linkBandwidth;
    private int[][] distanceTable;
    private List<Integer> bottleNeckBandwidthTable;
    private boolean isConverged;
    private NodeGUI nodeGUI;
    private int nonEntry = 0;
    private int nodeListSize;

    public Node(int nodeID, Hashtable<Integer, Integer> linkCost, Hashtable<Integer, Integer> linkBandwidth, int size) {
        this.nodeID = nodeID;
        this.linkCost = linkCost;
        this.linkBandwidth = linkBandwidth;
        this.distanceTable = new int[size][size]; //distanceTable[i][j] gives the cost to reach j from neighbor i
        nodeListSize = size;
        for(int i=0; i<size; i++){
            Arrays.fill(distanceTable[i], 999);
        }
        makeDistanceTable(this.linkCost);
        this.bottleNeckBandwidthTable = new ArrayList<Integer>();
        isConverged = false;
        nodeGUI = new NodeGUI("     Router "+ nodeID + "     ");
    }

    /**
     * <p>"
     *     Creates the initial distance table based on the costs to neighbors. Since initially the costs to other nodes are unknown, they are set to "infinity", 999.
     * </p>
     * @param linkCost hashtable that holds the cost to the direct neighbor i. The keys and values are integers
     */
    private void makeDistanceTable(Hashtable<Integer, Integer> linkCost) {
        distanceTable[this.nodeID][this.nodeID] = 0;
        for(Map.Entry<Integer, Integer> entry : linkCost.entrySet()) {
            int neighborID = entry.getKey();
            distanceTable[this.nodeID][neighborID] = linkCost.get(neighborID);
            distanceTable[neighborID][this.nodeID] = linkCost.get(neighborID);
        }
    }

    /**
     * <p>
     *     The method isI am convergence: " called when the current node receives a distance vector update from one of its neighbors.
     *     Upon receiving the message, the method checks if its own distance table needs to be changed in the case of a lower cost to a target node
     * </p>
     * @param m the message object that holds the distance vector of the node that sent the message
     */
    public void receiveUpdate(Message m){
        int sourceNode = m.getSenderID();
        int[] distanceVectorReceived = m.getDistanceVector();
        for (int distance: distanceVectorReceived) {
            nodeGUI.print(distance + " abc ");
        }
        for(int i =0; i < distanceVectorReceived.length; i++){
            if(distanceVectorReceived[i] < distanceTable[sourceNode][i]){
                nodeGUI.println("Entry changed from: " + distanceTable[sourceNode][i] + " to " + distanceVectorReceived[i]);
            //this.distanceTable[sourceNode][i] = Math.min(distanceVectorReceived[i], distanceTable[sourceNode][i]);
                addToDistanceTable(sourceNode, i, distanceVectorReceived[i]);
            } else {
                nonEntry++;
                //System.out.println("Non-entry for " + nodeID + ": " + nonEntry);
                //System.out.println("isConverged for " + nodeID + ": " + isConverged);distance: distanceTable[nodeID]
            }
        }
        int distanceToSource = distanceTable[nodeID][sourceNode];
        for (int i=0; i<distanceVectorReceived.length; i++){
            //distanceTable[nodeID][i] = Math.min(distanceTable[nodeID][i], distanceToSource + distanceVectorReceived[i]);

            if(distanceToSource + distanceVectorReceived[i] < distanceTable[nodeID][i]){
                addToDistanceTable(nodeID, i, distanceToSource + distanceVectorReceived[i]);
            }

        }
        /*
        System.out.println(nodeID + " _ " + nonEntry);
        if (nonEntry >= 2) {
            isConverged = true;
        }
        */
    }

    /**
     * <p>
     *     This methods is called when the node has an update in its distanceTable and needs to notify its neighbors. The method creates the distance vector for the node
     *     that contains the shortest path values to all other nodes, creates a message object and passes the message object by calling the receiveUpdate method of its direct
     *     neighbors.
     * </p>
     * @return true if the update message is sent correctly, false if the update message is not sent (i.e. node is converged)
     */
    public boolean sendUpdate(){
        if(!isConverged){
            Message message;
            int distanceVector[] = new int[distanceTable[0].length];
            // Make the distance vector based on shortest paths to all nodes
            for(int j=0; j < distanceVector.length; j++){
                /*
                distanceVector[j] = 999;
                for(int i=0; i < distanceTable.length; i++){
                    if(distanceTable[i][j] < distanceVector[j]){
                        distanceVector[j] = distanceTable[i][j];
                    }
                }
                */
                distanceVector[j] = distanceTable[nodeID][j];
            }
            // Traverse all the neighbors and notify them
            for(Map.Entry<Integer, Integer> entry : linkCost.entrySet()) {
                int neighborID = entry.getKey();
                message = new Message(this.nodeID, neighborID, linkBandwidth.get(neighborID), distanceVector);
                neighbors.get(neighborID).receiveUpdate(message);
            }
            return true;
        }
        //System.out.println(nodeID);
        /*
        System.out.println("dst  |   0   1   2   3   4");
        for (int[] table: distanceTable) {
            System.out.print("router  |");
            for (int distance: table) {
                System.out.print("  " + distance);
            }
            System.out.print("\n");
        }

         */
        return false;
    }

    /**
     * <p>
     *     Constructs the forwarding table for the node based on the distanceTable. The key in the forwarding table denotes the destination node and the values are the first two nodes with the
     *     lowest path cost to the destination in ascending order.
     * </p>
     * @return forwardingTable to instruct the node which node it should forward the packet for the given destination
     */
    public Hashtable<String, String> getForwardingTable(){
        Hashtable<String, String> forwardingTable = new Hashtable<String, String>();
        for(int j=0; j<distanceTable[0].length; j++){
            int cost1 = 999;
            int cost2 = 999;
            int node1 = 999;
            int node2 = 999;
            for(int i=0; i<distanceTable.length; i++){
                if(distanceTable[i][j]<cost1){
                    cost1 = distanceTable[i][j];
                    node1 = i;
                }
                else if(distanceTable[i][j]<cost2){
                    distanceTable[i][j]=cost2;
                    node2 = i;
                }
            }
            forwardingTable.put(Integer.toString(j), '(' + Integer.toString(node1) + ',' + Integer.toString(node2) + ')');
        }
        return forwardingTable;
    }

    public void addToLinkCostTable(int neighbor, int cost){
        this.linkCost.put(neighbor, cost);
    }
    public void addToLinkBandwidthTable(int neighbor, int cost){
        this.linkBandwidth.put(neighbor, cost);
    }
    public void addToDistanceTable(int neighbor, int destination, int cost){
        //If the index of the neighbor or the destination exceeds the size of the distanceTable
        //increase its size
        /*
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
        */
        this.distanceTable[neighbor][destination] = cost;
        this.distanceTable[destination][neighbor] = cost;
    }
    public void addToBottleNeckBandwidthTable(int neighbor, int cost){
        this.bottleNeckBandwidthTable.add(neighbor, cost);
    }

    public void printInfo() {
        for (int distance: distanceTable[nodeID]) {
            nodeGUI.print("   " + distance);
        }
        nodeGUI.println();
        Hashtable<String, String> forwardingTable = getForwardingTable();
        Iterator<Map.Entry<String, String>> itr = forwardingTable.entrySet().iterator();
        Map.Entry<String, String> entry = null;
        while(itr.hasNext()){
            entry = itr.next();
            nodeGUI.println(entry.getKey() + "->" + entry.getValue());
        }
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

    public NodeGUI getNodeGUI() { return nodeGUI; }

    public void setNeighbors(Hashtable<Integer, Node> neighbors) {
        this.neighbors = neighbors;
    }

    public Hashtable<Integer, Node> getNeighbors() {
        return neighbors;
    }


}
